package com.example.finalproject;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class.
 */
@SpringBootApplication
public class FinalProjectApplication {

	/**
	 * Main method.
	 * 
	 * @param args arguments from the CL.
	 * @throws SQLException
	 * @throws IOException
	 */
	public static void main(String[] args) throws SQLException, IOException {
		// Run Spring app.
		SpringApplication.run(FinalProjectApplication.class, args);

		// Read application.properties file from the /resources folder to get database
		// credentials.
		Credentials credentials = new Credentials();
		HashMap<String, String> credentialsMap = credentials.getCredentials();
		String dbName = credentialsMap.get("dbName");
		String user = credentialsMap.get("user");
		String pwd = credentialsMap.get("pwd");

		// Check if database needs to be populated from API.
		RetrieveData rd = new RetrieveData(dbName, user, pwd);

		if (isInitialExecution(rd)) {
			// Let user know of procedure to follow.
			System.out.println("Running data retrieval routine...");
			System.out.println("Wait until the data is correctly inserted into the database...");
			System.out.println("This may take a few minutes...");

			// Load the database.
			rd.loadDatabase();

			// Let user know of the success of the operation.
			System.out.println("Successfully loaded database!");
		} else {

			// Let user know that the data retrieval is not necessary.
			System.out.println("No need to insert data into the database.");
		}
	}

	/**
	 * This method checks if the MySQL database is empty or not.
	 * If it is, then the program needs to collect data from the API and insert it
	 * in the database.
	 * 
	 * @param rd The RetrieveData object.
	 * @return True if database is empty; false otherwise.
	 * @throws SQLException
	 */
	public static boolean isInitialExecution(RetrieveData rd) throws SQLException {
		// Get number of items in sn_names table.
		Connection conn = rd.getCon();
		String SQL = "{CALL countNames()};";
		CallableStatement stmt = conn.prepareCall(SQL);
		ResultSet rs = stmt.executeQuery();

		rs.next();

		int count = rs.getInt(1);

		// Return true if database is empty; false otherwise.
		return count == 0;
	}



}
