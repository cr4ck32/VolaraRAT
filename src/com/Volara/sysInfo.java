package com.Volara;
import oshi.SystemInfo;
import oshi.hardware.ComputerSystem;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HWPartition;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.hardware.UsbDevice;
import oshi.software.os.OSFileStore;
import oshi.util.FormatUtil;
import java.util.Arrays;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Kevin on 3/30/2017.
 */
public class sysInfo {
    //System CORE-INFO Vars
    //-]CORE-VARS
    public static SystemInfo si = null;
    public static HardwareAbstractionLayer hal = null;
    public static ComputerSystem cs = null;
    public static Boolean siLoaded = false;
    //-]SUB-VARS
    //Motherboard
    public static String Baseboard_Manufacturer = "Unknown";
    public static String Baseboard_Model = "Unknown";
    public static String Baseboard_Version = "Unknown";
    public static String Baseboard_SerialNumber = "Unknown";
    //CPU
    public static String CPU_Name = "Unknown";
    public static String CPU_Model = "Unknown";
    public static String CPU_Vendor = "Unknown";
    public static String CPU_Family = "Unknown";
    public static String CPU_UpTime = "Unknown";
    public static String CPU_Identifier = "Unknown";
    public static String CPU_ID = "Unknown";
    public static String CPU_Step = "Unknown";
    public static String CPU_Freq = "Unknown";
    public static String CPU_Load = "Unknown";
    public static String CPU_PhyCores = "Unknown";
    public static String CPU_LogCores = "Unknown";
    public static String CPU_Temp = "Unknown";
    public static String FAN_Speed = "Unknown";
    public static String CPU_Volt = "Unknown";
    //Computer System
    public static String CS_Manufacturer = "Unknown";
    public static String CS_Model = "Unknown";
    public static String CS_SerialNumber = "Unknown";
    //Memory
    public static String MEMORY_Available = "Unknown";
    public static String MEMORY_Swap = "Unknown";
    public static String MEMORY_Threads= "Unknown";
    public static String MEMORY_Processes = "Unknown";
    //Disk
    public static Integer numDisks = 0;
    public static Boolean disksLoaded = false;
    public static ArrayList<String> DISK_Name = new ArrayList<String>();
    public static ArrayList<String> DISK_Model = new ArrayList<String>();
    public static ArrayList<String> DISK_Size = new ArrayList<String>();
    public static ArrayList<String> DISK_SerialNumber = new ArrayList<String>();
    public static ArrayList<String> DISK_Write = new ArrayList<String>();
    public static ArrayList<String> DISK_Read = new ArrayList<String>();
    public static ArrayList<String> DISK_Transfer = new ArrayList<String>();
    //Partitions
    public static Integer numParts = 0;
    public static Boolean partsLoaded = false;
    public static ArrayList<String> PART_DiskName = new ArrayList<String>();
    public static ArrayList<String> PART_Name = new ArrayList<String>();
    public static ArrayList<String> PART_ID = new ArrayList<String>();
    public static ArrayList<String> PART_Size = new ArrayList<String>();
    public static ArrayList<String> PART_Type = new ArrayList<String>();
    public static ArrayList<String> PART_Minor = new ArrayList<String>();
    public static ArrayList<String> PART_Major = new ArrayList<String>();
    public static ArrayList<String> PART_MountPoint = new ArrayList<String>();
    //FileSystem
    public static Integer numFileSys = 0;
    public static Boolean fsLoaded = false;
    public static String fsOFD = "Unknown";
    public static String fsMFD = "Unknown";
    public static ArrayList<String> FS_Name = new ArrayList<String>();
    public static ArrayList<String> FS_FreeSpace = new ArrayList<String>();
    public static ArrayList<String> FS_TotalSpace = new ArrayList<String>();
    public static ArrayList<String> FS_Desc = new ArrayList<String>();
    public static ArrayList<String> FS_Type = new ArrayList<String>();
    public static ArrayList<String> FS_Volume = new ArrayList<String>();
    public static ArrayList<String> FS_Mount = new ArrayList<String>();
    //Network
    public static Integer numNICS = 0;
    public static Boolean nicsLoaded = false;
    public static ArrayList<String> NIC_Name = new ArrayList<String>();
    public static ArrayList<String> NIC_Mac = new ArrayList<String>();
    public static ArrayList<String> NIC_MTU = new ArrayList<String>();
    public static ArrayList<String> NIC_IPv6 = new ArrayList<String>();
    public static ArrayList<String> NIC_IPv4 = new ArrayList<String>();
    public static ArrayList<String> NIC_pRev = new ArrayList<String>();
    public static ArrayList<String> NIC_pSent = new ArrayList<String>();
    public static ArrayList<String> NIC_Errors = new ArrayList<String>();
    public static ArrayList<String> NIC_Speed = new ArrayList<String>();
    public static String hostName = "Unknown";
    public static String domainName = "Unknown";
    public static String dnsServer = "Unknown";
    public static String IPv4 = "Unknown";
    public static String IPv6 = "Unknown";
    //USB Devices
    public static Integer numDevices = 0;
    public static Boolean usbsLoaded = false;
    public static ArrayList<String> USB_Devices = new ArrayList<String>();
    //System INFO Initialize
    public static Boolean Init(){
        Boolean var = false;
        debug.addLine("Init sysInfo: Starting.\n", Color.lightGray, 10, false);
        try{
            si = new SystemInfo();
            hal = si.getHardware();
            cs = hal.getComputerSystem();
            siLoaded = true;
            debug.addLine("Init sysInfo: SUCCESS.\n", Color.GREEN, 10, false);
            var = true;
        }catch(Exception e){
            siLoaded = false;
            debug.addLine("Init sysInfo: FAILED: " + e.toString() + ".\n", Color.RED, 10, false);
            return var;
        }
        debug.addLine("Init sysInfo: Finished.\n", Color.lightGray, 10, false);
        return var;
    }

    //System INFO MAIN
    public static void Run(){
        if (!siLoaded){
            Init();
        }
        //Baseboard
        Baseboard_Manufacturer = grabBaseboard_MANUFACTURER();
        Baseboard_Model = grabBaseboard_MODEL();
        Baseboard_Version = grabBaseboard_VERSION();
        Baseboard_SerialNumber = grabBaseboard_SERIALNUMBER();
        //CPU
        CPU_Family = grabCPU_FAMILY();
        CPU_Freq = grabCPU_DEFAULTFREQ();
        CPU_ID = grabCPU_PROCESSORID();
        CPU_Name =grabCPU_NAME();
        CPU_Vendor = grabCPU_VENDOR();
        CPU_Identifier = grabCPU_IDENTIFIER();
        CPU_Load = grabCPU_LOAD();
        CPU_LogCores = grabCPU_LOGCORES();
        CPU_PhyCores = grabCPU_PHYCORES();
        CPU_Model = grabCPU_MODEL();
        CPU_Step = grabCPU_STEPPING();
        CPU_UpTime = grabCPU_UPTIME();
        CPU_Temp = grabCPU_TEMP();
        CPU_Volt = grabCPU_VOLT();
        //Fan
        FAN_Speed = grabCPU_FANS();
        //Computer System
        CS_Manufacturer = grabCS_MANUFACTURER();
        CS_Model = grabCS_MODEL();
        CS_SerialNumber = grabCS_SERIALNUMBER();
        //Memory
        MEMORY_Available = grabMemory_AVAILABLE();
        MEMORY_Processes = grabMemory_PROCESSES();
        MEMORY_Swap = grabMemory_SWAP();
        MEMORY_Threads = grabMemory_THREADS();
        //Disks & Parts
        grabDisks();
        grabParts();
        grabFileSys();
        //Network
        grabNICS();
        hostName = grabNet_HOST();
        domainName = grabNet_DOMAIN();
        dnsServer = grabNet_DNS();
        IPv4 = grabNet_IPv4Gateway();
        IPv6 = grabNet_IPv6Gateway();
        //Usb Devices
        grabUSB_Devices();
    }

    //System INFO Funcs: BASEBOARD
    public static String grabBaseboard_MANUFACTURER(){
        String var = "Unknown";
        debug.addLine("GRAB sysInfo: Baseboard Manufacturer.\n", Color.lightGray, 10, false);
        if (siLoaded == true){
            try{
                var = cs.getBaseboard().getManufacturer();
                debug.addLine("GRAB sysInfo: Baseboard Manufacturer | SUCCESS.\n", Color.GREEN, 10, false);
                return var;
            }catch(Exception e){
                debug.addLine("GRAB sysInfo: Baseboard Manufacturer | FAILED: " + e.toString() + ".\n", Color.RED, 10, false);
                return var;
            }
        }else{
            debug.addLine("GRAB sysInfo: System Info is not Init.\n", Color.YELLOW, 10, false);
            return var;
        }
    }
    public static String grabBaseboard_MODEL(){
        String var = "Unknown";
        debug.addLine("GRAB sysInfo: Baseboard Model.\n", Color.lightGray, 10, false);
        if (siLoaded == true){
            try{
                var = cs.getBaseboard().getModel();
                debug.addLine("GRAB sysInfo: Baseboard Model | SUCCESS.\n", Color.GREEN, 10, false);
                return var;
            }catch(Exception e){
                debug.addLine("GRAB sysInfo: Baseboard Model | FAILED: " + e.toString() + ".\n", Color.RED, 10, false);
                return var;
            }
        }else{
            debug.addLine("GRAB sysInfo: System Info is not Init.\n", Color.YELLOW, 10, false);
            return var;
        }
    }
    public static String grabBaseboard_VERSION(){
        String var = "Unknown";
        debug.addLine("GRAB sysInfo: Baseboard Version.\n", Color.lightGray, 10, false);
        if (siLoaded == true){
            try{
                var = cs.getBaseboard().getVersion();
                debug.addLine("GRAB sysInfo: Baseboard Version | SUCCESS.\n", Color.GREEN, 10, false);
                return var;
            }catch(Exception e){
                debug.addLine("GRAB sysInfo: Baseboard Version | FAILED: " + e.toString() + ".\n", Color.RED, 10, false);
                return var;
            }
        }else{
            debug.addLine("GRAB sysInfo: System Info is not Init.\n", Color.YELLOW, 10, false);
            return var;
        }
    }
    public static String grabBaseboard_SERIALNUMBER(){
        String var = "Unknown";
        debug.addLine("GRAB sysInfo: Baseboard Serial Number.\n", Color.lightGray, 10, false);
        if (siLoaded == true){
            try{
                var = cs.getBaseboard().getSerialNumber();
                debug.addLine("GRAB sysInfo: Baseboard Serial Number | SUCCESS.\n", Color.GREEN, 10, false);
                return var;
            }catch(Exception e){
                debug.addLine("GRAB sysInfo: Baseboard Serial Number | FAILED: " + e.toString() + ".\n", Color.RED, 10, false);
                return var;
            }
        }else{
            debug.addLine("GRAB sysInfo: System Info is not Init.\n", Color.YELLOW, 10, false);
            return var;
        }
    }

    //System INFO Funcs: CPU
    public static String grabCPU_MODEL(){
        String var = "Unknown";
        debug.addLine("GRAB sysInfo: CPU Model.\n", Color.lightGray, 10, false);
        if (siLoaded == true){
            try{
                var = hal.getProcessor().getModel();
                debug.addLine("GRAB sysInfo: CPU Model | SUCCESS.\n", Color.GREEN, 10, false);
                return var;
            }catch(Exception e){
                debug.addLine("GRAB sysInfo: CPU Model  | FAILED: " + e.toString() + ".\n", Color.RED, 10, false);
                return var;
            }
        }else{
            debug.addLine("GRAB sysInfo: System Info is not Init.\n", Color.YELLOW, 10, false);
            return var;
        }
    }
    public static String grabCPU_NAME(){
        String var = "Unknown";
        debug.addLine("GRAB sysInfo: CPU Name.\n", Color.lightGray, 10, false);
        if (siLoaded == true){
            try{
                var = hal.getProcessor().getName();
                debug.addLine("GRAB sysInfo: CPU Name | SUCCESS.\n", Color.GREEN, 10, false);
                return var;
            }catch(Exception e){
                debug.addLine("GRAB sysInfo: CPU Name | FAILED: " + e.toString() + ".\n", Color.RED, 10, false);
                return var;
            }
        }else{
            debug.addLine("GRAB sysInfo: System Info is not Init.\n", Color.YELLOW, 10, false);
            return var;
        }
    }
    public static String grabCPU_VENDOR(){
        String var = "Unknown";
        debug.addLine("GRAB sysInfo: CPU Vendor.\n", Color.lightGray, 10, false);
        if (siLoaded == true){
            try{
                var = hal.getProcessor().getVendor();
                debug.addLine("GRAB sysInfo: CPU Vendor | SUCCESS.\n", Color.GREEN, 10, false);
                return var;
            }catch(Exception e){
                debug.addLine("GRAB sysInfo: CPU Vendor | FAILED: " + e.toString() + ".\n", Color.RED, 10, false);
                return var;
            }
        }else{
            debug.addLine("GRAB sysInfo: System Info is not Init.\n", Color.YELLOW, 10, false);
            return var;
        }
    }
    public static String grabCPU_FAMILY(){
        String var = "Unknown";
        debug.addLine("GRAB sysInfo: CPU Family.\n", Color.lightGray, 10, false);
        if (siLoaded == true){
            try{
                var = hal.getProcessor().getFamily();
                debug.addLine("GRAB sysInfo: CPU Family | SUCCESS.\n", Color.GREEN, 10, false);
                return var;
            }catch(Exception e){
                debug.addLine("GRAB sysInfo: CPU Family | FAILED: " + e.toString() + ".\n", Color.RED, 10, false);
                return var;
            }
        }else{
            debug.addLine("GRAB sysInfo: System Info is not Init.\n", Color.YELLOW, 10, false);
            return var;
        }
    }
    public static String grabCPU_UPTIME(){
        String var = "Unknown";
        debug.addLine("GRAB sysInfo: CPU Up-Time in Minutes.\n", Color.lightGray, 10, false);
        if (siLoaded == true){
            try{
                var = FormatUtil.formatElapsedSecs(hal.getProcessor().getSystemUptime());
                debug.addLine("GRAB sysInfo: CPU Up-Time | SUCCESS.\n", Color.GREEN, 10, false);
                return var;
            }catch(Exception e){
                debug.addLine("GRAB sysInfo: CPU Up-Time | FAILED: " + e.toString() + ".\n", Color.RED, 10, false);
                return var;
            }
        }else{
            debug.addLine("GRAB sysInfo: System Info is not Init.\n", Color.YELLOW, 10, false);
            return var;
        }
    }
    public static String grabCPU_IDENTIFIER(){
        String var = "Unknown";
        debug.addLine("GRAB sysInfo: CPU Identifier.\n", Color.lightGray, 10, false);
        if (siLoaded == true){
            try{
                var = hal.getProcessor().getIdentifier();
                debug.addLine("GRAB sysInfo: CPU Identifier | SUCCESS.\n", Color.GREEN, 10, false);
                return var;
            }catch(Exception e){
                debug.addLine("GRAB sysInfo: CPU Identifier | FAILED: " + e.toString() + ".\n", Color.RED, 10, false);
                return var;
            }
        }else{
            debug.addLine("GRAB sysInfo: System Info is not Init.\n", Color.YELLOW, 10, false);
            return var;
        }
    }
    public static String grabCPU_PROCESSORID(){
        String var = "Unknown";
        debug.addLine("GRAB sysInfo: CPU ID.\n", Color.lightGray, 10, false);
        if (siLoaded == true){
            try{
                var = hal.getProcessor().getProcessorID();
                debug.addLine("GRAB sysInfo: CPU ID | SUCCESS.\n", Color.GREEN, 10, false);
                return var;
            }catch(Exception e){
                debug.addLine("GRAB sysInfo: CPU ID | FAILED: " + e.toString() + ".\n", Color.RED, 10, false);
                return var;
            }
        }else{
            debug.addLine("GRAB sysInfo: System Info is not Init.\n", Color.YELLOW, 10, false);
            return var;
        }
    }
    public static String grabCPU_STEPPING(){
        String var = "Unknown";
        debug.addLine("GRAB sysInfo: CPU Stepping.\n", Color.lightGray, 10, false);
        if (siLoaded == true){
            try{
                var = hal.getProcessor().getStepping();
                debug.addLine("GRAB sysInfo: CPU Stepping | SUCCESS.\n", Color.GREEN, 10, false);
                return var;
            }catch(Exception e){
                debug.addLine("GRAB sysInfo: CPU Stepping | FAILED: " + e.toString() + ".\n", Color.RED, 10, false);
                return var;
            }
        }else{
            debug.addLine("GRAB sysInfo: System Info is not Init.\n", Color.YELLOW, 10, false);
            return var;
        }
    }
    public static String grabCPU_DEFAULTFREQ(){
        String var = "Unknown";
        debug.addLine("GRAB sysInfo: CPU Default Freq.\n", Color.lightGray, 10, false);
        if (siLoaded == true){
            try{
                var = FormatUtil.formatHertz(hal.getProcessor().getVendorFreq());
                debug.addLine("GRAB sysInfo: CPU Default Freq | SUCCESS.\n", Color.GREEN, 10, false);
                return var;
            }catch(Exception e){
                debug.addLine("GRAB sysInfo: CPU Default Freq | FAILED: " + e.toString() + ".\n", Color.RED, 10, false);
                return var;
            }
        }else{
            debug.addLine("GRAB sysInfo: System Info is not Init.\n", Color.YELLOW, 10, false);
            return var;
        }
    }
    public static String grabCPU_LOAD(){
        String var = "Unknown";
        debug.addLine("GRAB sysInfo: CPU Load.\n", Color.lightGray, 10, false);
        if (siLoaded == true){
            try{
                DecimalFormat df = new DecimalFormat("0.00##");
                var = df.format(hal.getProcessor().getSystemCpuLoad());
                debug.addLine("GRAB sysInfo: CPU Load | SUCCESS.\n", Color.GREEN, 10, false);
                return var;
            }catch(Exception e){
                debug.addLine("GRAB sysInfo: CPU Load | FAILED: " + e.toString() + ".\n", Color.RED, 10, false);
                return var;
            }
        }else{
            debug.addLine("GRAB sysInfo: System Info is not Init.\n", Color.YELLOW, 10, false);
            return var;
        }
    }
    public static String grabCPU_PHYCORES(){
        String var = "Unknown";
        debug.addLine("GRAB sysInfo: CPU Physical Cores.\n", Color.lightGray, 10, false);
        if (siLoaded == true){
            try{
                var = Integer.toString(hal.getProcessor().getPhysicalProcessorCount());
                debug.addLine("GRAB sysInfo: CPU Physical Cores | SUCCESS.\n", Color.GREEN, 10, false);
                return var;
            }catch(Exception e){
                debug.addLine("GRAB sysInfo: CPU Physical Cores | FAILED: " + e.toString() + ".\n", Color.RED, 10, false);
                return var;
            }
        }else{
            debug.addLine("GRAB sysInfo: System Info is not Init.\n", Color.YELLOW, 10, false);
            return var;
        }
    }
    public static String grabCPU_LOGCORES(){
        String var = "Unknown";
        debug.addLine("GRAB sysInfo: CPU Logical Cores.\n", Color.lightGray, 10, false);
        if (siLoaded == true){
            try{
                var = Integer.toString(hal.getProcessor().getPhysicalProcessorCount());
                debug.addLine("GRAB sysInfo: CPU Logical Cores | SUCCESS.\n", Color.GREEN, 10, false);
                return var;
            }catch(Exception e){
                debug.addLine("GRAB sysInfo: CPU Logical Cores | FAILED: " + e.toString() + ".\n", Color.RED, 10, false);
                return var;
            }
        }else{
            debug.addLine("GRAB sysInfo: System Info is not Init.\n", Color.YELLOW, 10, false);
            return var;
        }
    }
    public static String grabCPU_VOLT(){
        String var = "Unknown";
        debug.addLine("GRAB sysInfo: CPU Voltage.\n", Color.lightGray, 10, false);
        if (siLoaded == true){
            try{
                var = Double.toString(hal.getSensors().getCpuVoltage());
                debug.addLine("GRAB sysInfo: CPU Logical Cores | SUCCESS.\n", Color.GREEN, 10, false);
                return var;
            }catch(Exception e){
                debug.addLine("GRAB sysInfo: CPU Voltage | FAILED: " + e.toString() + ".\n", Color.RED, 10, false);
                return var;
            }
        }else{
            debug.addLine("GRAB sysInfo: System Info is not Init.\n", Color.YELLOW, 10, false);
            return var;
        }
    }
    public static String grabCPU_TEMP(){
        String var = "Unknown";
        debug.addLine("GRAB sysInfo: CPU Temperature.\n", Color.lightGray, 10, false);
        if (siLoaded == true){
            try{
                var = Double.toString(hal.getSensors().getCpuTemperature());
                debug.addLine("GRAB sysInfo: CPU Temperature | SUCCESS.\n", Color.GREEN, 10, false);
                return var;
            }catch(Exception e){
                debug.addLine("GRAB sysInfo: CPU Temperature | FAILED: " + e.toString() + ".\n", Color.RED, 10, false);
                return var;
            }
        }else{
            debug.addLine("GRAB sysInfo: System Info is not Init.\n", Color.YELLOW, 10, false);
            return var;
        }
    }
    public static String grabCPU_FANS(){
        String var = "Unknown";
        debug.addLine("GRAB sysInfo: CPU Fans.\n", Color.lightGray, 10, false);
        if (siLoaded == true){
            try{
                int fss = 0;
                int fs = 0;
                int[] fans = hal.getSensors().getFanSpeeds();
                for(int i =0; i < fans.length; i++){
                    fss += fans[i];
                    fs++;
                }
                var = Integer.toString(fss / fs);
                debug.addLine("GRAB sysInfo: CPU Fans | SUCCESS.\n", Color.GREEN, 10, false);
                return var;
            }catch(Exception e){
                debug.addLine("GRAB sysInfo: CPU Fans | FAILED: " + e.toString() + ".\n", Color.RED, 10, false);
                return var;
            }
        }else{
            debug.addLine("GRAB sysInfo: System Info is not Init.\n", Color.YELLOW, 10, false);
            return var;
        }
    }

    //System INFO Funcs: COMPUTER
    public static String grabCS_MANUFACTURER(){
        String var = "Unknown";
        debug.addLine("GRAB sysInfo: Computer Manufacturer.\n", Color.lightGray, 10, false);
        if (siLoaded == true){
            try{
                var = cs.getManufacturer();
                debug.addLine("GRAB sysInfo: Computer Manufacturer | SUCCESS.\n", Color.GREEN, 10, false);
                return var;
            }catch(Exception e){
                debug.addLine("GRAB sysInfo: Computer Manufacturer  | FAILED: " + e.toString() + ".\n", Color.RED, 10, false);
                return var;
            }
        }else{
            debug.addLine("GRAB sysInfo: System Info is not Init.\n", Color.YELLOW, 10, false);
            return var;
        }
    }
    public static String grabCS_MODEL(){
        String var = "Unknown";
        debug.addLine("GRAB sysInfo: Computer Model.\n", Color.lightGray, 10, false);
        if (siLoaded == true){
            try{
                var = cs.getModel();
                debug.addLine("GRAB sysInfo: Computer Model | SUCCESS.\n", Color.GREEN, 10, false);
                return var;
            }catch(Exception e){
                debug.addLine("GRAB sysInfo: Computer Model  | FAILED: " + e.toString() + ".\n", Color.RED, 10, false);
                return var;
            }
        }else{
            debug.addLine("GRAB sysInfo: System Info is not Init.\n", Color.YELLOW, 10, false);
            return var;
        }
    }
    public static String grabCS_SERIALNUMBER(){
        String var = "Unknown";
        debug.addLine("GRAB sysInfo: Computer Serial Number.\n", Color.lightGray, 10, false);
        if (siLoaded == true){
            try{
                var = cs.getSerialNumber();
                debug.addLine("GRAB sysInfo: Computer Serial Number | SUCCESS.\n", Color.GREEN, 10, false);
                return var;
            }catch(Exception e){
                debug.addLine("GRAB sysInfo: Computer Serial Number  | FAILED: " + e.toString() + ".\n", Color.RED, 10, false);
                return var;
            }
        }else{
            debug.addLine("GRAB sysInfo: System Info is not Init.\n", Color.YELLOW, 10, false);
            return var;
        }
    }

    //System INFO Funcs: Memory
    public static String grabMemory_AVAILABLE(){
        String var = "Unknown";
        debug.addLine("GRAB sysInfo: Memory Available.\n", Color.lightGray, 10, false);
        if (siLoaded == true){
            try{
                var = FormatUtil.formatBytes(hal.getMemory().getAvailable()) + "/"
                        + FormatUtil.formatBytes(hal.getMemory().getTotal());
                debug.addLine("GRAB sysInfo: Memory Available | SUCCESS.\n", Color.GREEN, 10, false);
                return var;
            }catch(Exception e){
                debug.addLine("GRAB sysInfo: Memory Available | FAILED: " + e.toString() + ".\n", Color.RED, 10, false);
                return var;
            }
        }else{
            debug.addLine("GRAB sysInfo: System Info is not Init.\n", Color.YELLOW, 10, false);
            return var;
        }
    }
    public static String grabMemory_SWAP(){
        String var = "Unknown";
        debug.addLine("GRAB sysInfo: Memory Swap.\n", Color.lightGray, 10, false);
        if (siLoaded == true){
            try{
                var = FormatUtil.formatBytes(hal.getMemory().getSwapUsed()) + "/"
                        + FormatUtil.formatBytes(hal.getMemory().getSwapTotal());
                debug.addLine("GRAB sysInfo: Memory Swap | SUCCESS.\n", Color.GREEN, 10, false);
                return var;
            }catch(Exception e){
                debug.addLine("GRAB sysInfo: Memory Swap | FAILED: " + e.toString() + ".\n", Color.RED, 10, false);
                return var;
            }
        }else{
            debug.addLine("GRAB sysInfo: System Info is not Init.\n", Color.YELLOW, 10, false);
            return var;
        }
    }
    public static String grabMemory_THREADS(){
        String var = "Unknown";
        debug.addLine("GRAB sysInfo: Memory Threads.\n", Color.lightGray, 10, false);
        if (siLoaded == true){
            try{
                var = Integer.toString(si.getOperatingSystem().getThreadCount());
                debug.addLine("GRAB sysInfo: Memory Threads | SUCCESS.\n", Color.GREEN, 10, false);
                return var;
            }catch(Exception e){
                debug.addLine("GRAB sysInfo: Memory Threads | FAILED: " + e.toString() + ".\n", Color.RED, 10, false);
                return var;
            }
        }else{
            debug.addLine("GRAB sysInfo: System Info is not Init.\n", Color.YELLOW, 10, false);
            return var;
        }
    }
    public static String grabMemory_PROCESSES(){
        String var = "Unknown";
        debug.addLine("GRAB sysInfo: Memory Processes.\n", Color.lightGray, 10, false);
        if (siLoaded == true){
            try{
                var = Integer.toString(si.getOperatingSystem().getProcessCount());
                debug.addLine("GRAB sysInfo: Memory Processes | SUCCESS.\n", Color.GREEN, 10, false);
                return var;
            }catch(Exception e){
                debug.addLine("GRAB sysInfo: Memory Processes | FAILED: " + e.toString() + ".\n", Color.RED, 10, false);
                return var;
            }
        }else{
            debug.addLine("GRAB sysInfo: System Info is not Init.\n", Color.YELLOW, 10, false);
            return var;
        }
    }

    //System INFO Funcs: Disk/Partitions
    public static void grabDisks(){
        String var = "Unknown";
        debug.addLine("GRAB sysInfo: Disks.\n", Color.lightGray, 10, false);
        if (siLoaded == true){
            try{
                int i = 0;
                if (disksLoaded == true){
                    DISK_Write.clear();
                    DISK_Read.clear();
                    DISK_SerialNumber.clear();
                    DISK_Model.clear();
                    DISK_Size.clear();
                    DISK_Transfer.clear();
                    DISK_Name.clear();
                }
                for (HWDiskStore disk : hal.getDiskStores()) {
                    DISK_Name.add(disk.getName());
                    DISK_Model.add(disk.getModel());
                    DISK_SerialNumber.add(disk.getSerial());
                    DISK_Size.add(FormatUtil.formatBytesDecimal(disk.getSize()));
                    DISK_Read.add(FormatUtil.formatBytesDecimal(disk.getReadBytes()));
                    DISK_Write.add(FormatUtil.formatBytesDecimal(disk.getWriteBytes()));
                    DISK_Transfer.add(FormatUtil.formatBytesDecimal(disk.getTransferTime()));
                    i++;
                }
                numDisks = i;
                disksLoaded = true;
                debug.addLine("GRAB sysInfo: Disks | SUCCESS.\n", Color.GREEN, 10, false);
            }catch(Exception e){
                debug.addLine("GRAB sysInfo: Disks | FAILED: " + e.toString() + ".\n", Color.RED, 10, false);

            }
        }else{
            debug.addLine("GRAB sysInfo: System Info is not Init.\n", Color.YELLOW, 10, false);

        }
    }
    public static void grabParts(){
        String var = "Unknown";
        debug.addLine("GRAB sysInfo: Parts.\n", Color.lightGray, 10, false);
        if (siLoaded == true){
            try{
                int i = 0;
                if (partsLoaded == true){
                    PART_DiskName.clear();
                    PART_ID.clear();
                    PART_Major.clear();
                    PART_Minor.clear();
                    PART_MountPoint.clear();
                    PART_Name.clear();
                    PART_Size.clear();
                    PART_Type.clear();
                }
                for (HWDiskStore disk : hal.getDiskStores()) {
                    HWPartition[] partitions = disk.getPartitions();
                    for (HWPartition part : partitions) {
                        PART_DiskName.add(disk.getName());
                        PART_ID.add(part.getIdentification());
                        PART_Name.add(part.getName());
                        PART_MountPoint.add(part.getMountPoint());
                        PART_Size.add(FormatUtil.formatBytesDecimal(part.getSize()));
                        PART_Minor.add(Integer.toString(part.getMinor()));
                        PART_Major.add(Integer.toString(part.getMajor()));
                        PART_Type.add(part.getType());
                        i++;
                    }
                }
                numParts = i;
                partsLoaded = true;
                debug.addLine("GRAB sysInfo: Parts | SUCCESS.\n", Color.GREEN, 10, false);
            }catch(Exception e){
                debug.addLine("GRAB sysInfo: Parts | FAILED: " + e.toString() + ".\n", Color.RED, 10, false);
            }
        }else{
            debug.addLine("GRAB sysInfo: System Info is not Init.\n", Color.YELLOW, 10, false);

        }
    }
    public static void grabFileSys(){
        String var = "Unknown";
        debug.addLine("GRAB sysInfo: FileSystem.\n", Color.lightGray, 10, false);
        if (siLoaded == true){
            try{
                int i = 0;
                if (fsLoaded == true){
                    FS_Name.clear();
                    FS_Desc.clear();
                    FS_FreeSpace.clear();
                    FS_Mount.clear();
                    FS_TotalSpace.clear();
                    FS_Volume.clear();
                    FS_Type.clear();
                    fsMFD = "Unknown";
                    fsOFD = "Unknown";
                }
                fsOFD = FormatUtil.formatBytes(si.getOperatingSystem().getFileSystem().getOpenFileDescriptors());
                fsMFD = FormatUtil.formatBytes(si.getOperatingSystem().getFileSystem().getMaxFileDescriptors());
                for (OSFileStore fs : si.getOperatingSystem().getFileSystem().getFileStores()) {
                    FS_Name.add(fs.getName());
                    FS_Type.add(fs.getType());
                    FS_Desc.add(fs.getDescription());
                    FS_Mount.add(fs.getMount());
                    FS_Volume.add(fs.getVolume());
                    FS_TotalSpace.add(FormatUtil.formatBytes(fs.getTotalSpace()));
                    FS_FreeSpace.add(FormatUtil.formatBytes(fs.getUsableSpace()));
                    i++;
                }
                numFileSys = i;
                fsLoaded = true;
                debug.addLine("GRAB sysInfo: FileSystem | SUCCESS.\n", Color.GREEN, 10, false);
            }catch(Exception e){
                debug.addLine("GRAB sysInfo: FileSystem | FAILED: " + e.toString() + ".\n", Color.RED, 10, false);
            }
        }else{
            debug.addLine("GRAB sysInfo: System Info is not Init.\n", Color.YELLOW, 10, false);

        }
    }
    //System INFO Funcs: Network
    public static void grabNICS(){
        String var = "Unknown";
        debug.addLine("GRAB sysInfo: Network Info.\n", Color.lightGray, 10, false);
        if (siLoaded == true){
            try{
                int i = 0;
                if (nicsLoaded == true){
                    NIC_IPv4.clear();
                    NIC_Errors.clear();
                    NIC_Mac.clear();
                    NIC_IPv6.clear();
                    NIC_MTU.clear();
                    NIC_Name.clear();
                    NIC_pRev.clear();
                    NIC_pSent.clear();
                    NIC_Speed.clear();
                }
                for (NetworkIF net : hal.getNetworkIFs()) {
                    NIC_IPv4.add(Arrays.toString(net.getIPv4addr()));
                    NIC_IPv6.add(Arrays.toString(net.getIPv4addr()));
                    NIC_MTU.add(Integer.toString(net.getMTU()));
                    NIC_Speed.add(FormatUtil.formatValue(net.getSpeed(), "bps"));
                    NIC_Name.add(net.getName());
                    NIC_Mac.add(net.getMacaddr());
                    NIC_pSent.add(FormatUtil.formatBytes(net.getBytesSent()));
                    NIC_pRev.add(FormatUtil.formatBytes(net.getBytesRecv()));
                    NIC_Errors.add(Long.toString(net.getInErrors() + net.getOutErrors()));
                    i++;
                }
                numNICS = i;
                nicsLoaded = true;
                debug.addLine("GRAB sysInfo: Network Info | SUCCESS.\n", Color.GREEN, 10, false);
            }catch(Exception e){
                debug.addLine("GRAB sysInfo: Network Info | FAILED: " + e.toString() + ".\n", Color.RED, 10, false);
            }
        }else{
            debug.addLine("GRAB sysInfo: System Info is not Init.\n", Color.YELLOW, 10, false);

        }
    }
    public static String grabNet_HOST(){
        String var = "Unknown";
        debug.addLine("GRAB sysInfo: Host name.\n", Color.lightGray, 10, false);
        if (siLoaded == true){
            try{
                var = si.getOperatingSystem().getNetworkParams().getHostName();
                debug.addLine("GRAB sysInfo: Host name | SUCCESS.\n", Color.GREEN, 10, false);
                return var;
            }catch(Exception e){
                debug.addLine("GRAB sysInfo: Host name  | FAILED: " + e.toString() + ".\n", Color.RED, 10, false);
                return var;
            }
        }else{
            debug.addLine("GRAB sysInfo: System Info is not Init.\n", Color.YELLOW, 10, false);
            return var;
        }
    }
    public static String grabNet_DOMAIN(){
        String var = "Unknown";
        debug.addLine("GRAB sysInfo: Domain name.\n", Color.lightGray, 10, false);
        if (siLoaded == true){
            try{
                var = si.getOperatingSystem().getNetworkParams().getDomainName();
                debug.addLine("GRAB sysInfo: Domain name | SUCCESS.\n", Color.GREEN, 10, false);
                return var;
            }catch(Exception e){
                debug.addLine("GRAB sysInfo: Domain name  | FAILED: " + e.toString() + ".\n", Color.RED, 10, false);
                return var;
            }
        }else{
            debug.addLine("GRAB sysInfo: System Info is not Init.\n", Color.YELLOW, 10, false);
            return var;
        }
    }
    public static String grabNet_DNS(){
        String var = "Unknown";
        debug.addLine("GRAB sysInfo: DNS.\n", Color.lightGray, 10, false);
        if (siLoaded == true){
            try{
                var = Arrays.toString(si.getOperatingSystem().getNetworkParams().getDnsServers());
                debug.addLine("GRAB sysInfo: DNS | SUCCESS.\n", Color.GREEN, 10, false);
                return var;
            }catch(Exception e){
                debug.addLine("GRAB sysInfo: DNS  | FAILED: " + e.toString() + ".\n", Color.RED, 10, false);
                return var;
            }
        }else{
            debug.addLine("GRAB sysInfo: System Info is not Init.\n", Color.YELLOW, 10, false);
            return var;
        }
    }
    public static String grabNet_IPv4Gateway(){
        String var = "Unknown";
        debug.addLine("GRAB sysInfo: IPv4.\n", Color.lightGray, 10, false);
        if (siLoaded == true){
            try{
                var = si.getOperatingSystem().getNetworkParams().getIpv4DefaultGateway();
                debug.addLine("GRAB sysInfo: IPv4 | SUCCESS.\n", Color.GREEN, 10, false);
                return var;
            }catch(Exception e){
                debug.addLine("GRAB sysInfo: IPv4  | FAILED: " + e.toString() + ".\n", Color.RED, 10, false);
                return var;
            }
        }else{
            debug.addLine("GRAB sysInfo: System Info is not Init.\n", Color.YELLOW, 10, false);
            return var;
        }
    }
    public static String grabNet_IPv6Gateway(){
        String var = "Unknown";
        debug.addLine("GRAB sysInfo: IPv6.\n", Color.lightGray, 10, false);
        if (siLoaded == true){
            try{
                var = si.getOperatingSystem().getNetworkParams().getIpv6DefaultGateway();
                debug.addLine("GRAB sysInfo: IPv6 | SUCCESS.\n", Color.GREEN, 10, false);
                return var;
            }catch(Exception e){
                debug.addLine("GRAB sysInfo: IPv6  | FAILED: " + e.toString() + ".\n", Color.RED, 10, false);
                return var;
            }
        }else{
            debug.addLine("GRAB sysInfo: System Info is not Init.\n", Color.YELLOW, 10, false);
            return var;
        }
    }
    //System INFO Funs: Usb Devices
    public static void grabUSB_Devices(){
        String var = "Unknown";
        debug.addLine("GRAB sysInfo: Usb Devices.\n", Color.lightGray, 10, false);
        if (siLoaded == true){
            try{
                int i = 0;
                if (usbsLoaded == true){
                    USB_Devices.clear();
                }
                for (UsbDevice usbDevice : hal.getUsbDevices(true)) {
                    USB_Devices.add(usbDevice.toString());
                    i++;
                }
                numDevices = i;
                usbsLoaded = true;
                debug.addLine("GRAB sysInfo: Usb Devices | SUCCESS.\n", Color.GREEN, 10, false);
            }catch(Exception e){
                debug.addLine("GRAB sysInfo: Usb Devices | FAILED: " + e.toString() + ".\n", Color.RED, 10, false);
            }
        }else{
            debug.addLine("GRAB sysInfo: System Info is not Init.\n", Color.YELLOW, 10, false);

        }
    }
}
