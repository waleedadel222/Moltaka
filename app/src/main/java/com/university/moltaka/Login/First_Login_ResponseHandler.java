package com.university.moltaka.Login;

import org.json.JSONArray;
import org.json.JSONObject;


public class First_Login_ResponseHandler {

    private String response;
    String message;
    int success;
    public  static String name,national_id,graduation_id, faculty, graduation_year;

    public  int getSuccess() {
        return success;
    }

    public First_Login_ResponseHandler(String response) {
        this.response = response;
    }

    public String Handler(){

        try {
            JSONObject jsonObject = new JSONObject(response);

            success = jsonObject.getInt("success");
            message =jsonObject.getString("message");

            if (success == 1)
            {
                JSONArray data  =jsonObject.getJSONArray("user");
                for (int i=0 ; i<data.length();i++){

                    JSONObject c = data.getJSONObject(i);
                    name=c.getString("name");
                    national_id= c.getString("national_id");
                    graduation_id = c.getString("graduation_id");
                    faculty=c.getString("faculty");
                    graduation_year =c.getString("graduation_year");

                }
            }

            return message;
        }

        catch (Exception e) {


            e.printStackTrace();
        }

        return message;
    }
}