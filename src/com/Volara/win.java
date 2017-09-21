package com.Volara;

import java.awt.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
/**
 * Created by Kevin on 3/23/2017.
 */
public class win {
    public static void initWin(){
        if (isAdmin() == true){
            debug.addLine("Init: User is admin!\n", Color.WHITE, 12, true);
        }else {
            debug.addLine("Init: User is not admin!\n", Color.WHITE, 12, true);
        }

    }
    public static Boolean isAdmin(){
        try {
            String command = "reg query \"HKU\\S-1-5-19\"";
            Process p = Runtime.getRuntime().exec(command);
            p.waitFor();                            // Wait for for command to finish
            int exitValue = p.exitValue();          // If exit value 0, then admin user.

            if (0 == exitValue) {
                global.userAdmin_GINFO = true;
                return true;
            } else {
                global.userAdmin_GINFO = false;
                return false;
            }

        } catch (Exception e) {
            debug.addLine("Error Trying to detect if user was admin: " + e.toString() +"\n", Color.RED, 12, true);
        }
        global.userAdmin_GINFO = false;
        return false;
    }
}
