package com.example.finalproject;

import java.io.IOException;

/**
 * This class is responsible for running a Python script that calculates the
 * areas under the curves of the supernovae from sn_time_association and
 * sn_band_association tables and updates the table sn_curves accordingly.
 */
public class PythonScript {

    // Command to be run.
    private String command;

    /**
     * Class constructor.
     * 
     * @param command the command used.
     */
    public PythonScript(String command) {
        this.command = command;
    }

    /**
     * This method returns the command used.
     * 
     * @return the command.
     */
    public String getCommand() {
        return this.command;
    }

    /**
     * This method allows the user to set the command to be used.
     * 
     * @param command the command to be used.
     */
    public void setCommand(String command) {
        this.command = command;
    }

    /**
     * This method runs the command.
     * 
     * @throws IOException
     */
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
