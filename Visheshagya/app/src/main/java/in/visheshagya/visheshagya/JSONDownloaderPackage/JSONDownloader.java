/*
    @Auther MOHIT KUMAR
    Created on 07/09/2016
 * Copyright (c) Visheshagya Pvt Ltd.
 */
package in.visheshagya.visheshagya.JSONDownloaderPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;

public class JSONDownloader {

    public String jsonData(String link) {

        URL url = null;
        try {
            url = new URL(link);
            //System.out.println("url in json class is "+url);
            // url = URI.create(URLEncoder.encode(url, "UTF-8"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(10000);
            conn.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStream is = null;
        String data = "";
        StringBuffer sb = new StringBuffer("");

        try {
            is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            while ((data = reader.readLine()) != null) {
                sb.append(data);
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
