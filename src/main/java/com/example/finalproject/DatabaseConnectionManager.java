package com.example.finalproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionManager {
    private Connection con;

    public DatabaseConnectionManager(String dbName, String user, String pwd) throws SQLException {
        this.con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName, user, pwd);
    }

    public Connection getConnection() {
        return this.con;
    }

    
}
