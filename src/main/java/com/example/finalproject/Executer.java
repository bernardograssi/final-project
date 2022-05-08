package com.example.finalproject;

import java.io.File;
import java.io.IOException;

/**
 * This class is responsible for running an executable that was built from a
 * Python script that calculates the
 * areas under the curves of the supernovae from sn_time_association and
 * sn_band_association tables and updates the table sn_curves accordingly.
 */
public class Executer {

    /**
     * Class constructor.
     * 
     * 
     */
    public Executer() {
    }

    /**
     * This method runs the command.
     * 
     * @throws IOException
     */
    public void run() {
        ProcessBuilder pb = new ProcessBuilder(
                "cmd",
                "/c",
                "script.exe");
        pb.directory(new File(
                "src\\main\\java\\com\\example\\finalproject"));
        pb.redirectErrorStream(true);

        try {
            Process p = pb.start();
            p.waitFor();
            System.out.println("Successful area update!");
        } catch (IOException | InterruptedException e) {
            System.out.println("Something went wrong when updating the area!");
        }
    }
}
