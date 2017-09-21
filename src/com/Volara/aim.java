package com.Volara;

import com.sun.javafx.binding.StringFormatter;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Kevin on 4/4/2017.
 */
public class aim {

    private static ApplicationInstanceListener subListener;

    /** Randomly chosen, but static, high socket number */
    public static final int SINGLE_INSTANCE_NETWORK_SOCKET = 44331;

    /** Must end with newline */
    public static final String SINGLE_INSTANCE_SHARED_KEY = "$$NewInstance$$\n";

    /**
     * Registers this instance of the application.
     *
     * @return true if first instance, false if not.
     */
    public static boolean registerInstance() {
        // returnValueOnError should be true if lenient (allows app to run on network error) or false if strict.
        boolean returnValueOnError = true;
        // try to open network socket
        // if success, listen to socket for new instance message, return true
        // if unable to open, connect to existing and send new instance message, return false
        try {
            final ServerSocket socket = new ServerSocket(SINGLE_INSTANCE_NETWORK_SOCKET, 10, InetAddress
                    .getLocalHost());
            //log.debug("Listening for application instances on socket " + SINGLE_INSTANCE_NETWORK_SOCKET);
            Thread instanceListenerThread = new Thread(new Runnable() {
                public void run() {
                    boolean socketClosed = false;
                    while (!socketClosed) {
                        if (socket.isClosed()) {
                            socketClosed = true;
                        } else {
                            try {
                                Socket client = socket.accept();
                                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                                String message = in.readLine();
                                if (SINGLE_INSTANCE_SHARED_KEY.trim().equals(message.trim())) {
                                    // log.debug("Shared key matched - new application instance found");
                                    fireNewInstance();
                                }
                                in.close();
                                client.close();
                            } catch (IOException e) {
                                socketClosed = true;
                            }
                        }
                    }
                }
            });
            instanceListenerThread.start();
            // listen
        } catch (UnknownHostException e) {
            //log.error(e.getMessage(), e);
            return returnValueOnError;
        } catch (IOException e) {
            //log.debug("Port is already taken. Notifying first instance.");
            try {
                Socket clientSocket = new Socket(InetAddress.getLocalHost(), SINGLE_INSTANCE_NETWORK_SOCKET);
                OutputStream out = clientSocket.getOutputStream();
                out.write(SINGLE_INSTANCE_SHARED_KEY.getBytes());
                out.close();
                clientSocket.close();
                //log.debug("Successfully notified first instance.");
                return false;
            } catch (UnknownHostException e1) {
                //log.error(e.getMessage(), e);
                return returnValueOnError;
            } catch (IOException e1) {
                // log.error("Error connecting to local port for single instance notification");
                //log.error(e1.getMessage(), e1);
                return returnValueOnError;
            }

        }
        return true;
    }

    public static void setApplicationInstanceListener(ApplicationInstanceListener listener) {
        subListener = listener;
    }
    public interface ApplicationInstanceListener {
        public void newInstanceCreated();
    }
    public static void createRun(){

        FileOutputStream fos = null;
        File file = new File(global.prepareLocation + File.separator + "lib" + File.separator + "sav.ini");
        if (file.exists()){
            file.delete();
        }
        Path path = Paths.get("lib");
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {

            String content = null;
            try {
                content = global.getPid();
            } catch (InterruptedException e) {

            }
            byte[] ci = null;
            try {
                ci =  global.cryptString(content, "1234567812345678");
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }
            String decrypted = "";
            try {
               decrypted = global.decryptString(ci, "1234567812345678");
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }
            fos = new FileOutputStream(global.prepareLocation + File.separator + "lib" + File.separator + "sav.ini");
            fos.write(ci);

            System.out.println("Done");

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (fos != null)
                    fos.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }
                global.hideFile(file);
                global.hideFolder(global.prepareLocation + File.separator + "lib");
        }

    }
    public static void readRunFile(){
        File file = new File(global.prepareLocation + File.separator + "lib" + File.separator + "sav.ini");
        if (file.exists()){
            try {
                byte[] array = Files.readAllBytes(file.toPath());
                try {
                    debug.addLine(global.decryptString(array, profile.appid), Color.cyan, 12, false);
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private static void fireNewInstance() {
        if (subListener != null) {
            subListener.newInstanceCreated();
        }
    }
}
