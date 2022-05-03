package com.example.finalproject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.core.io.ClassPathResource;

public class Credentials {
    private String dbName;
    private String user;
    private String pwd;

    public Credentials() {
    }

    public HashMap<String, String> getCredentials() throws IOException {
        HashMap<String, String> map = new HashMap<>();
        File file = new ClassPathResource("application.properties").getFile();
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
        dbName = dbName.substring(dbName.lastIndexOf("/") + 1);
        user = user.substring(user.lastIndexOf("=") + 1);
        pwd = pwd.substring(pwd.lastIndexOf("=") + 1);

        map.put("dbName", dbName);
        map.put("user", user);
        map.put("pwd", pwd);

        return map;
    }
}
