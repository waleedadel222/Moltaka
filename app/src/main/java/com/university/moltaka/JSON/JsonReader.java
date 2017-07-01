package com.university.moltaka.JSON;

import org.apache.http.NameValuePair;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Scanner;


public class JsonReader {

   private String url;
    private List<NameValuePair> pairs;

    public JsonReader(String url, List<NameValuePair> pairs) {
        this.url = url;
        this.pairs = pairs;
    }

    public JsonReader(String url) {
        this.url = url;
    }

    public String sendRequest(){
        String response ;


        try {
           // create HTTPURLConnection
             URL url = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

              // set connection properties
            connection.setReadTimeout(15 * 1000);
            connection.setConnectTimeout(5 * 1000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            // set value
           OutputStream outputStream = connection.getOutputStream();
           BufferedWriter writer =new BufferedWriter(new OutputStreamWriter( outputStream , "UTF-8"));
           writer.write(getQuery(pairs));
           writer.flush();
           writer.close();
           outputStream.close();

            // then connect
            connection.connect();

            // get response from connection
             InputStream is = connection.getInputStream();

            // convert input stream to string response
            Scanner s = new Scanner(is).useDelimiter("\\A");
            response = s.hasNext() ? s.next() : "";


        }
        catch (Exception e) {
            e.printStackTrace();

            response = null;
        }

       return response;

    }

     // method for  converting  the  form of the data  to request form

    private String getQuery(List<NameValuePair> params)
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        try {

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

    }catch (Exception e){}

        return result.toString();
    }


}
