package com.example.finalproject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;

public class RetrieveData {

    private Connection con;

    public RetrieveData(String dbName, String user, String pwd) throws SQLException {
        this.con = (new DatabaseConnectionManager(dbName, user, pwd)).getConnection();
        System.out.println("\n\nSuccessfully connected to the database :)");
    }

    public Connection getCon() {
        return this.con;
    }

    public void setCon(Connection con) {
        this.con = con;
    }

    public void loadDatabase() throws SQLException, IOException {
        // Read text file with supernovae names.
        File file = new ClassPathResource("static/supernova_data.txt").getFile();
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        String SQL = "INSERT INTO sn_names(name) VALUES (?);";
        PreparedStatement stmt = con.prepareStatement(SQL);
        ArrayList<String> list = new ArrayList<>();
        ResultSet rs;
        int id = 0;
        double counter = 0;
        URL url;

        // Add names to table in database.
        while ((line = br.readLine()) != null) {
            list.add(line);
            stmt.setString(1, line);
            stmt.addBatch();
        }

        stmt.executeBatch();

        // For each supernova name, get band data, if > 0, add data to tables
        // (SN_RESULTS, SN_BAND and SN_TIME).
        for (String sn : list) {
            counter += 1;
            System.out.println("Loading -> " + String.valueOf(Math.round((counter / list.size()) * 100)) + "%");

            SQL = "SELECT id, name FROM sn_names WHERE name = ?;";
            stmt = con.prepareStatement(SQL);
            stmt.setString(1, sn);
            rs = stmt.executeQuery();

            while (rs.next()) {
                id = rs.getInt(1);
                break;
            }

            url = new URL(String.format("https://api.astrocats.space/%s/photometry/band+time+magnitude", sn));

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            conn.setRequestMethod("GET");
            conn.connect();

            int responsecode = conn.getResponseCode();

            if (responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            } else {

                // Read JSON object
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                StringBuilder builder = new StringBuilder();

                for (String l = null; (l = in.readLine()) != null;) {
                    builder.append(l).append("\n");
                }
                JSONObject response = new JSONObject(builder.toString());
                JSONObject data = response.getJSONObject(sn);
                JSONArray photometry = data.getJSONArray("photometry");

                // Prepare Statements
                String SQLBand = "INSERT INTO sn_band_association (magnitude, luminosity, sn_id) VALUES (?, ?, ?);";
                PreparedStatement stmt_band = con.prepareStatement(SQLBand);

                String SQLTime = "INSERT INTO sn_time_association (time_value, sn_id) VALUES (?, ?);";
                PreparedStatement stmt_time = con.prepareStatement(SQLTime);

                String SQLDelta = "INSERT INTO sn_deltas (sn_id, delta_value) VALUES (?, ?);";
                PreparedStatement stmt_delta = con.prepareStatement(SQLDelta);

                String SQLCurve = "INSERT INTO sn_curves (sn_id, mag_curve, lum_curve) VALUES (?, ?, ?);";
                PreparedStatement stmt_curve = con.prepareStatement(SQLCurve);

                String SQLCalc = "INSERT INTO sn_calc (sn_id, calc_res_1, calc_res_2) VALUES (?, ?, ?);";
                PreparedStatement stmt_calc = con.prepareStatement(SQLCalc);

                String SQLResult = "INSERT INTO sn_results (sn_id, band_max, band_min, band_delta_id, curve_id, calc_res_id) VALUES (?, ?, ?, ?, ?, ?);";
                PreparedStatement stmt_result = con.prepareStatement(SQLResult);

                // Variables to add to sn_results
                double band_max = 99999.;
                double band_min = -99999.;
                double band_delta = 0.;
                double aum_curve = 0.;
                double aul_curve = 0.;
                double calc_result_1 = 0.;
                double calc_result_2 = 0.;
                double magnitudes = 0.;
                int size = 0;

                for (int i = 0; i < photometry.length(); i++) {
                    JSONArray phtObj = (JSONArray) photometry.get(i);
                    
                        if (phtObj.get(0).toString().equals("B")) {
                            double magnitude = Double.valueOf(phtObj.get(2).toString());
                            double timeString = Double.valueOf(phtObj.get(1).toString());
                            double luminosity = (-2.5) * Math.log10(magnitude / (3 * Math.pow(10, 28)));

                            magnitudes += magnitude;
                            size += 1;

                            if (magnitude < band_max) {
                                band_max = magnitude;
                            }

                            if (magnitude > band_min) {
                                band_min = magnitude;
                            }

                            stmt_band.setDouble(1, magnitude);
                            stmt_band.setDouble(2, luminosity);
                            stmt_band.setInt(3, id);
                            stmt_band.addBatch();

                            stmt_time.setDouble(1, timeString);
                            stmt_time.setInt(2, id);
                            stmt_time.addBatch();
                        }
                    
                }

                // Add data to the sn_delta table
                band_delta = Math.abs(band_max - band_min);
                stmt_delta.setInt(1, id);
                stmt_delta.setDouble(2, band_delta);
                stmt_delta.executeUpdate();

                String SQLDeltaID = "SELECT MAX(id) FROM sn_deltas;";
                Statement SQLDeltaStmt = con.createStatement();
                rs = SQLDeltaStmt.executeQuery(SQLDeltaID);
                int deltaID = 0;

                while (rs.next()) {
                    deltaID = rs.getInt(1);
                    break;
                }

                // Add data to the sn_curves
                stmt_curve.setInt(1, id);
                stmt_curve.setDouble(2, aum_curve);
                stmt_curve.setDouble(3, aul_curve);
                stmt_curve.executeUpdate();

                String SQLCurveID = "SELECT MAX(id) FROM sn_curves;";
                Statement SQLCurveStmt = con.createStatement();
                rs = SQLCurveStmt.executeQuery(SQLCurveID);
                int curveID = 0;

                while (rs.next()) {
                    curveID = rs.getInt(1);
                    break;
                }

                // Add data to the sn_calc
                calc_result_1 = band_delta / band_max;
                calc_result_2 = magnitudes / size;
                stmt_calc.setInt(1, id);
                stmt_calc.setDouble(2, calc_result_1);
                stmt_calc.setDouble(3, calc_result_2);
                stmt_calc.executeUpdate();

                String SQLCalcID = "SELECT MAX(id) FROM sn_calc;";
                Statement SQLCalcStmt = con.createStatement();
                rs = SQLCalcStmt.executeQuery(SQLCalcID);
                int calcID = 0;

                while (rs.next()) {
                    calcID = rs.getInt(1);
                    break;
                }

                // Add data to sn_band_association and sn_time_association tables
                stmt_band.executeBatch();
                stmt_time.executeBatch();

                // Add data to sn_results table
                stmt_result.setInt(1, id);
                stmt_result.setDouble(2, band_max);
                stmt_result.setDouble(3, band_min);
                stmt_result.setInt(4, deltaID);
                stmt_result.setInt(5, curveID);
                stmt_result.setInt(6, calcID);
                stmt_result.executeUpdate();
            }
        }

    }

}