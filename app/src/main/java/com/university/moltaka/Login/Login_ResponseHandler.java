package com.university.moltaka.Login;

import org.json.JSONObject;

/**
 * Created by waleed on 2015-06-29.
 */
public class Login_ResponseHandler {
    private String response;
    String message;
    int success;
    public  static String national_id ,name;


    public  int getSuccess() {
        return success;
    }


    public Login_ResponseHandler(String response) {
        this.response = response;
    }


 public String Handler(){

        try {
            JSONObject jsonObject = new JSONObject(response);
            success = jsonObject.getInt("success");
            message =jsonObject.getString("message");
            if (success == 1) {
                national_id =jsonObject.getString("national_id");
                name = jsonObject.getString("name");
            }

            return message;
        }

        catch (Exception e) {e.printStackTrace(); }

        return message;
   } }

