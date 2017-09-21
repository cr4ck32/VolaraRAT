package com.Volara;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.awt.*;

/**
 * Created by Kevin on 3/31/2017.
 */
public class sysHook implements NativeKeyListener{
    private static boolean run = true;

    public void nativeKeyPressed(NativeKeyEvent e) {
        debug.addLine("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()) + "\n", Color.lightGray, 12, false);

        if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
            try {
                GlobalScreen.unregisterNativeHook();
                debug.addLine("KeyHook Released!" + "\n", Color.YELLOW, 12, false);
            } catch (NativeHookException e1) {
                debug.addLine("HOOK: Problem unregistering the native hook:" + e1.getMessage() + "\n", Color.RED, 12, false);
            }
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
        debug.addLine("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()) + "\n", Color.lightGray, 12, false);
    }

    public void nativeKeyTyped(NativeKeyEvent e) {

    }

    public static void run() {
        try {
            GlobalScreen.registerNativeHook();
            debug.addLine("HOOK: Registered Hook: " + "\n", Color.GREEN, 12, false);
        }
        catch (NativeHookException ex) {
            debug.addLine("HOOK: Problem registering the native hook:" + ex.getMessage() + "\n", Color.RED, 12, false);
        }

        GlobalScreen.addNativeKeyListener(new sysHook());
    }
}
