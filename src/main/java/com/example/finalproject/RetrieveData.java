package com.example.finalproject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;

/**
 * This class represents an object that has a database connection and loads the
 * database from an open-source API.
 */
public class RetrieveData {

    // Attribute connection.
    private Connection con;

    /**
     * This is the constructor of the class.
     * 
     * @param dbName the database name.
     * @param user   the username from the database.
     * @param pwd    the password from the database.
     * @throws SQLException
     */
    public RetrieveData(String dbName, String user, String pwd) throws SQLException {
        this.con = (new DatabaseConnectionManager(dbName, user, pwd)).getConnection();
        System.out.println("\n\nSuccessfully connected to the database :)");
    }

    /**
     * This method returns the Connection object.
     * 
     * @return the Connection object.
     */
    public Connection getCon() {
        return this.con;
    }

    /**
     * This method allows the user to set the Connection object.
     * 
     * @param con the Connection object.
     */
    public void setCon(Connection con) {
        this.con = con;
    }

    /**
     * This method loads the database with data from both an open-source API and
     * some names from a text file.
     * 
     * @throws SQLException
     * @throws IOException
     */
    public void loadDatabase() throws SQLException, IOException {
        // File with supernovae names.
        File file = new ClassPathResource("static/supernova_data.txt").getFile();

        // Objects used to read the file.
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        // Define variables used to parse the file.
        ArrayList<String> list = new ArrayList<>();
        int id = 0;
        double counter = 0;
        URL url;

        list = insertNames(br);

        // For each supernova name, get band data, if > 0, add data to tables
        // (SN_RESULTS, SN_BAND and SN_TIME).
        for (String sn : list) {
            // Increment counter and output percentage to user.
            counter += 1;
            System.out.println("Loading -> " + String.valueOf(Math.round((counter / list.size()) * 100)) + "%");

            // Get id and names from the sn_names table.
            id = getIdByName(sn);

            // URL object.
            url = new URL(String.format("https://api.astrocats.space/%s/photometry/band+time+magnitude", sn));

            // HTTP connection object.
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            conn.setRequestMethod("GET");
            conn.connect();

            int responsecode = conn.getResponseCode();

            // If response code of HTTP code is not 200, throw exception.
            // Otherwise, move forward to process the response.
            if (responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            } else {

                // Read JSON object
                JSONArray photometry = getJsonArray(conn, sn);

                // Variables to add to sn_results
                double band_max = 99999.;
                double band_min = -99999.;
                double band_delta = 0.;
                double aum_curve = 0.;
                double aul_curve = 0.;
                double calc_result_1 = 0.;
                double calc_result_2 = 0.;
                double x_min_max = 0.;
                int limit = 0;
                JSONArray phtObj = null;

                // For each item in the photometry list.
                for (int i = 0; i < photometry.length(); i++) {
                    phtObj = (JSONArray) photometry.get(i); // Get array of values.

                    // If the current object represents a value that has a "B" band, grab the data
                    // from it.
                    if (phtObj.get(0).toString().equals("B")) {
                        double magnitude = Double.valueOf(phtObj.get(2).toString()); // Magnitude value.
                        double timeString = Double.valueOf(phtObj.get(1).toString()); // Time value.
                        double luminosity = (-2.5) * Math.log10(magnitude / (3 * Math.pow(10, 28))); // Luminosity
                                                                                                     // value.

                        // Check for maximum and update.
                        if (magnitude < band_max) {
                            band_max = magnitude;
                            limit = i;
                        }

                        // Check for minimum and update.
                        if (magnitude > band_min) {
                            band_min = magnitude;
                        }

                        // Insert band and time values.
                        insertBand(magnitude, luminosity, id);
                        insertTime(timeString, id);
                    }

                }

                // Perform calculation to get calc_res_1.
                for (int k = 0; k <= limit; k++) {
                    phtObj = (JSONArray) photometry.get(k);
                    if (phtObj.get(0).toString().equals("B")) {
                        x_min_max += Math.abs(((Double.valueOf(phtObj.get(2).toString())) - band_min) / band_max);
                    }
                }

                // Add data to the sn_delta table.
                band_delta = Math.abs(band_max - band_min);
                insertDeltas(id, band_delta);

                // Get last ID from sn_deltas.
                int deltaID = getMaxDeltaID();

                // Add data to the sn_curves
                insertCurves(id, aum_curve, aul_curve);

                // Get last ID from sn_curves.
                int curveID = getMaxCurveID();

                // Add data to the sn_calc
                calc_result_1 = band_delta / band_max;
                calc_result_2 = x_min_max;
                insertCalc(id, calc_result_1, calc_result_2);

                // Get last ID from sn_calc.
                int calcID = getMaxCalcID();

                // Add data to sn_results table
                insertResults(id, band_max, band_min, deltaID, curveID, calcID);
            }
        }

        // Run the Python script to update the sn_curves database.
        PythonScript ps = new PythonScript("cmd /c py script.py");
        try {
            ps.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @return
     * @throws SQLException
     */
    private int getMaxCalcID() throws SQLException {
        String SQL = "{CALL getLastCalcId()};"; // Stored procedure call.
        CallableStatement stmt = con.prepareCall(SQL);
        ResultSet rs = stmt.executeQuery();
        rs.next();

        return rs.getInt("maxId");
    }

    /**
     * 
     * @return
     * @throws SQLException
     */
    private int getMaxCurveID() throws SQLException {
        String SQL = "{CALL getLastCurveId()};"; // Stored procedure call.
        CallableStatement stmt = con.prepareCall(SQL);
        ResultSet rs = stmt.executeQuery();
        rs.next();

        return rs.getInt("maxId");
    }

    /**
     * 
     * @return
     * @throws SQLException
     */
    private int getMaxDeltaID() throws SQLException {
        String SQL = "{CALL getLastBandId()};"; // Stored procedure call.
        CallableStatement stmt = con.prepareCall(SQL);
        ResultSet rs = stmt.executeQuery();
        rs.next();

        return rs.getInt("maxId");
    }

    /**
     * 
     * @param id
     * @param band_max
     * @param band_min
     * @param deltaID
     * @param curveID
     * @param calcID
     * @throws SQLException
     */
    private boolean insertResults(int id, double band_max, double band_min, int deltaID, int curveID, int calcID) throws SQLException {
        String SQL = "{CALL insertResults(?, ?, ?, ?, ?, ?)};"; // Stored procedure call.
        CallableStatement stmt = con.prepareCall(SQL);
        stmt.setInt(1, id);
        stmt.setDouble(2, band_max);
        stmt.setDouble(3, band_min);
        stmt.setInt(4, deltaID);
        stmt.setInt(5, curveID);
        stmt.setInt(6, calcID);
        stmt.execute();

        return true;
    }

    /**
     * 
     * @param id
     * @param calc_result_1
     * @param calc_result_2
     * @throws SQLException
     */
    private boolean insertCalc(int id, double calc_result_1, double calc_result_2) throws SQLException {
        String SQL = "{CALL insertCalc(?, ?, ?)};"; // Stored procedure call.
        CallableStatement stmt = con.prepareCall(SQL);
        stmt.setInt(1, id);
        stmt.setDouble(2, calc_result_1);
        stmt.setDouble(3, calc_result_2);
        stmt.execute();

        return true;
    }

    /**
     * 
     * @param id
     * @param aum_curve
     * @param aul_curve
     * @return
     * @throws SQLException
     */
    private boolean insertCurves(int id, double aum_curve, double aul_curve) throws SQLException {
        String SQL = "{CALL insertCurves(?, ?, ?)};"; // Stored procedure call.
        CallableStatement stmt = con.prepareCall(SQL);
        stmt.setInt(1, id);
        stmt.setDouble(2, aum_curve);
        stmt.setDouble(3, aul_curve);
        stmt.execute();

        return true;
    }

    /**
     * 
     * @param id
     * @param band_delta
     * @return
     * @throws SQLException
     */
    private boolean insertDeltas(int id, double band_delta) throws SQLException {
        String SQL = "{CALL insertDelta(?, ?)};"; // Stored procedure call.
        CallableStatement stmt = con.prepareCall(SQL);
        stmt.setInt(1, id);
        stmt.setDouble(2, band_delta);
        stmt.execute();

        return true;
    }

    /**
     * 
     * @param timeString
     * @param id
     * @return
     * @throws SQLException
     */
    private boolean insertTime(double timeString, int id) throws SQLException {
        String SQL = "{CALL insertTime(?, ?)};"; // Stored procedure call.
        CallableStatement stmt = con.prepareCall(SQL);
        stmt.setDouble(1, timeString);
        stmt.setInt(2, id);
        stmt.execute();

        return true;
    }

    /**
     * 
     * @param magnitude
     * @param luminosity
     * @param id
     * @return
     * @throws SQLException
     */
    private boolean insertBand(double magnitude, double luminosity, int id) throws SQLException {
        String SQL = "{CALL insertValues(?, ?, ?)};"; // Stored procedure call.
        CallableStatement stmt = con.prepareCall(SQL);
        stmt.setDouble(1, magnitude);
        stmt.setDouble(2, luminosity);
        stmt.setInt(3, id);
        stmt.execute();

        return true;
    }

    /**
     * This method calls the insertName stored procedure, which inserts a supernova
     * name in the sn_names table.
     * The method then returns all the names as an Array of Strings.
     * 
     * @param br the BufferedReader object.
     * @return an Array of Strings containing the names.
     * @throws SQLException
     * @throws IOException
     */
    public ArrayList<String> insertNames(BufferedReader br) throws SQLException, IOException {
        ArrayList<String> list = new ArrayList<>();
        String line;
        String SQL = "{CALL insertName(?)};"; // Stored procedure call.
        CallableStatement stmt = null;

        // For each line in the text file, add it to the sn_names table.
        while ((line = br.readLine()) != null) {
            list.add(line);
            stmt = con.prepareCall(SQL);
            stmt.setString(1, line);
            stmt.executeUpdate();
        }

        // Return the list of names.
        return list;
    }

    /**
     * This method gets the id and name of a given supernova.
     * 
     * @param supernova the supernova name.
     * @return true if operation is successful, false otherwise.
     * @throws SQLException
     */
    public int getIdByName(String supernova) throws SQLException {
        String SQL = "{CALL getIdByName(?)};";
        CallableStatement stmt = con.prepareCall(SQL);
        stmt.setString(1, supernova);

        ResultSet rs = stmt.executeQuery();
        rs.next();

        int count = rs.getInt(1);

        return count;
    }

    /**
     * 
     * @param conn
     * @param sn
     * @return
     * @throws IOException
     */
    public JSONArray getJsonArray(HttpURLConnection conn, String sn) throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        StringBuilder builder = new StringBuilder();

        for (String l = null; (l = in.readLine()) != null;) {
            builder.append(l).append("\n");
        }

        JSONObject response = new JSONObject(builder.toString());
        JSONObject data = response.getJSONObject(sn);
        return data.getJSONArray("photometry");
    }

}