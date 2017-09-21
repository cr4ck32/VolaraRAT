package com.Volara;

import java.awt.*;
import java.io.InputStream;

/**
 * Created by Kevin on 5/18/2017.
 */
class execute implements Runnable{
    public void run(){
        // Run a java app in a separate system process
        Process proc = null;
        try {
            proc = Runtime.getRuntime().exec(global.currentCommand);
            ProcessBuilder p = new ProcessBuilder();
            p.command(global.currentCommand);
            p.start();
        } catch (Exception e) {
            debug.addLine("ERROR running command: " + e.toString() + " | END\n", Color.RED, 12, true);
        }
        // Then retreive the process output
        InputStream in = proc.getInputStream();
        InputStream err = proc.getErrorStream();
    }
}
