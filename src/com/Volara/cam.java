package com.Volara;
import com.github.sarxos.webcam.Webcam;
import java.awt.*;
/**
 * Created by Kevin on 3/25/2017.
 */
public class cam {


    public static Image runDefault() {
        debug.addLine("Cam Init: Starting."+ "\n", Color.PINK, 12, true);
        try{
            Webcam webcam = Webcam.getDefault(3000);
            webcam.close();
            webcam.open();
            Image passImg = webcam.getImage();
            webcam.close();
            global.noWebcam = false;
            debug.addLine("Cam Init: Finished."+ "\n", Color.PINK, 12, true);
            return passImg;
        }catch(NullPointerException e){
            global.noWebcam = true;
            debug.addLine("Cam ERROR: "+ "No Web Camera was found: " + e.toString() + "\n", Color.YELLOW, 12, true);
            debug.addLine("Cam Init: Finished."+ "\n", Color.PINK, 12, true);
            return null;
        }catch(Exception e){
            debug.addLine("Cam ERROR: "+  e.toString() + "\n", Color.RED, 12, true);
            debug.addLine("Cam Init: Finished."+ "\n", Color.PINK, 12, true);
            return null;
        }
    }

    public static void getWebcams(){
        debug.addLine("Init: Grabbing webcams started.\n", Color.LIGHT_GRAY, 12, false);
        int i = 0;
        try{
            global.lstWebcams.clear();
            for(Webcam s: Webcam.getWebcams()){
                debug.addLine("Cam LIST: "+ s.getName() + "\n", Color.MAGENTA, 12, true);
                global.lstWebcams.add(i, s.getName());
                i++;
            }
        }catch(Exception e){
            debug.addLine("Cam LIST ERROR: "+ e.toString() + "\n", Color.MAGENTA, 12, true);
        }
        if (i == 0){
            global.noWebcam = true;
        }else{
            global.noWebcam = false;
        }
        debug.addLine("Init: Grabbing webcams finished.\n", Color.LIGHT_GRAY, 12, false);
    }
    public static Image runCustom(String deviceName){
        debug.addLine("Cam Init: Starting."+ "\n", Color.PINK, 12, true);
        try{
            Webcam webcam = null;
            for(Webcam s: Webcam.getWebcams()){
                if (s.getName() == deviceName){
                    webcam = s;
                }
            }
            if (webcam == null){
                return null;
            }else{
                webcam.close();
                webcam.open();
                Image passImg = webcam.getImage();
                webcam.close();
                global.noWebcam = false;
                debug.addLine("Cam Init: Finished."+ "\n", Color.PINK, 12, true);
                return passImg;
            }
        }catch(NullPointerException e){
            global.noWebcam = true;
            debug.addLine("Cam ERROR: "+ "No Web Camera was found: " + e.toString() + "\n", Color.YELLOW, 12, true);
            debug.addLine("Cam Init: Finished."+ "\n", Color.PINK, 12, true);
            return null;
        }catch(Exception e){
            debug.addLine("Cam ERROR: "+  e.toString() + "\n", Color.RED, 12, true);
            debug.addLine("Cam Init: Finished."+ "\n", Color.PINK, 12, true);
            return null;
        }
    }
}
