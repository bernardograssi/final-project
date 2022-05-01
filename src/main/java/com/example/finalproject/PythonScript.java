package com.example.finalproject;

import java.io.IOException;

public class PythonScript {

    private String command;

    public PythonScript(String command) {
        this.command = command;
    }

    public String getCommand() {
        return this.command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void run() throws IOException {
        Process p = Runtime.getRuntime().exec(this.command);
        try {
            p.waitFor();
            System.out.println("Success area update!!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
