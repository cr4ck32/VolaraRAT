package com.Volara;
import java.awt.*;
import java.io.*;
import javax.net.ssl.HttpsURLConnection;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;

/**
 * Created by Kevin on 3/23/2017.
 */
public class webRequest {

    // HTTP GET request
    public static ArrayList<String> sendGet(String URL, int timeOut) throws Exception {

        String url = URL;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");
        con.setConnectTimeout(timeOut);
        //add request header
        con.setRequestProperty("User-Agent", "Volara API Request");

        int responseCode = con.getResponseCode();
        debug.addLine("Sending 'GET' request to URL : " + URL + "\n", Color.LIGHT_GRAY, 10, false);
        debug.addLine("Response Code : " + responseCode + "\n", Color.LIGHT_GRAY, 10, false);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        debug.addLine("Response was acquired : " + global.cutString(response.toString(), 100) + "\n", Color.LIGHT_GRAY, 10, false);
        ArrayList<String> varReturn = new ArrayList<String>();
        varReturn.add(0, Integer.toString(responseCode));
        varReturn.add(1, response.toString());
        return varReturn;

    }
    // HTTP POST request
    public static ArrayList<String> sendPost(String URL, int timeOut, String Params) throws Exception {

        String url = URL;
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
        con.setConnectTimeout(timeOut);
        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Volara API Request");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        String urlParameters = Params;

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        debug.addLine("Sending 'POST' request to URL : " + URL + "\n", Color.LIGHT_GRAY, 10, false);
        debug.addLine("Post parameters : " + urlParameters + "\n", Color.LIGHT_GRAY, 10, false);
        debug.addLine("Response Code : " + responseCode + "\n", Color.LIGHT_GRAY, 10, false);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        debug.addLine("Response was acquired : " + global.cutString(response.toString(), 100) + "\n", Color.LIGHT_GRAY, 10, false);
        ArrayList<String> varReturn = new ArrayList<String>();
        varReturn.add(0, Integer.toString(responseCode));
        varReturn.add(1, response.toString());
        return varReturn;

    }
    //Download File
    public static Boolean downloadFileFromURL(String urlString, File destination) {
        try {
            URL website = new URL(urlString);
            ReadableByteChannel rbc;
            rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(destination);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
            debug.addLine("DOWNLOAD SUCCESS: " + urlString + "\n", Color.green, 12, false);
            return true;
        } catch (IOException e) {
            debug.addLine("ERROR: DOWNLOAD FAIL: " + e.toString() + "\n", Color.red, 12, false);
            return false;
        }
    }
}
