package com.example.finalproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class represents the connection manager to the database engine.
 */
public class DatabaseConnectionManager {
    // The Connection object used to connect to the database engine.
    private Connection con;

    /**
     * This is the constructor the class.
     * @param dbName the name of the database.
     * @param user the username used to log into the database.
     * @param pwd the password used to lof into the database.
     * @throws SQLException
     */
    public DatabaseConnectionManager(String dbName, String user, String pwd) throws SQLException {
        this.con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName, user, pwd);
    }

    /**
     * This method returns the Connection object.
     * @return the Connection object.
     */
    public Connection getConnection() {
        return this.con;
    }
}
