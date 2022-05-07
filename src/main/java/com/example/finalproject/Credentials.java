package com.example.finalproject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.core.io.ClassPathResource;

/**
 * This class contains the credentials used to log into the database.
 * It is supposed to be a central point that can be accessed from any part of
 * the program.
 * 
 * The credentials are obtained from the application.properties file that is
 * automatically used and parsed by Spring Boot.
 */
public class Credentials {
    // The name of the database.
    private String dbName;

    // The username.
    private String user;

    // The password.
    private String pwd;

    /**
     * The class constructor, which is empty
     */
    public Credentials() {
    }

    /**
     * This method reads the application.properties file, parses it correctly and
     * returns the credentials back to the caller.
     * 
     * @return a HashMap object containing the credentials of the database.
     * @throws IOException
     */
    public HashMap<String, String> getCredentials() throws IOException {
        // HashMap object used to store the credentials.
        HashMap<String, String> map = new HashMap<>();

        // File containing the credentials.
        File file = new ClassPathResource("application.properties").getFile();
        
        // Objects used to read the file and store its content in memory.
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line = "";
        ArrayList<String> list = new ArrayList<>();

        // Add lines to the array of lines.
        while ((line = br.readLine()) != null) {
            list.add(line);
        }

        // Get credentials from parsing the file.
        String dbName = list.get(1);
        String user = list.get(2);
        String pwd = list.get(3);

        // Parse file.
        this.dbName = dbName.substring(dbName.lastIndexOf("/") + 1);
        this.user = user.substring(user.lastIndexOf("=") + 1);
        this.pwd = pwd.substring(pwd.lastIndexOf("=") + 1);

        // Put data into the HashMap object.
        map.put("dbName", this.dbName);
        map.put("user", this.user);
        map.put("pwd", this.pwd);

        // Return the HashMap object to the caller.
        return map;
    }

    /**
     * This method returns the database name.
     * @return the database name.
     */
    public String getDbName() {
        return this.dbName;
    }

    /**
     * This method returns the username used to log into the database.
     * @return the username to log into the database.
     */
    public String getUser() {
        return this.user;
    }

    /**
     * This method returns the password used to log into the database.
     * @return the password used to log into the database.
     */
    public String getPwd() {
        return this.pwd;
    }


}
