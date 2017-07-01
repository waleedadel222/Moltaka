package com.university.moltaka.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.university.moltaka.DateFormActivity;
import com.university.moltaka.Home.HomeActivity;
import com.university.moltaka.JSON.JsonReader;
import com.university.moltaka.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends Activity {

    private EditText username_et, pw_ed;
    private CheckBox new_user_cb;
    private Button login_btn;

    final String login_url ="http://10.0.2.2:80/moltaka/login.php";
    final String first_time_url = "http://10.0.2.2:80/university/first_time_login.php";
    final String check_url = "http://10.0.2.2:80/moltaka/checkusers.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username_et = (EditText) findViewById(R.id.username_et);
        pw_ed = (EditText) findViewById(R.id.pw_et);
        new_user_cb = (CheckBox) findViewById(R.id.new_user_cb);

        //Login button
        login_btn = (Button) findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String u = username_et.getText().toString();
                String p = pw_ed.getText().toString();
                Boolean checked = new_user_cb.isChecked();

                if (u.equals("")||p.equals("")){

                   Toast.makeText(MainActivity.this, "ادخل رقمك القومى و رقم شهادة التخرج اولا", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (checked)
                       {
                          new First_Login().execute(u,p);
                       }
                     else { new Login().execute(u,p);  }
                }   } });
    }


    // Login class
    public  class Login extends AsyncTask<String, Void, Void> {

        String national_id, graduation_id, result;

        //ProgressDialog
        private android.app.ProgressDialog ProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ProgressDialog = ProgressDialog.show(MainActivity.this, "Processing...",
                    "Check Your Data", false, false);
        }

        @Override
        protected Void doInBackground(String... params) {

            national_id = params[0];
            graduation_id = params[1];
            ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("national_id", national_id));
            pairs.add(new BasicNameValuePair("graduation_id", graduation_id));

            JsonReader j = new JsonReader(login_url, pairs);
            result = j.sendRequest();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            String message ;

            try
            {
                Login_ResponseHandler handler = new Login_ResponseHandler(result);
                message = handler.Handler();
                int success = handler.getSuccess();
                ProgressDialog.dismiss();

                switch (success)
                {
                    case  0 :
                        if(message == null)
                        { Toast.makeText(MainActivity.this ,"error in internet connection" , Toast.LENGTH_SHORT).show();}
                        else {Toast.makeText(MainActivity.this ,message , Toast.LENGTH_SHORT).show();}
                        break;

                    case  1 : Toast.makeText(MainActivity.this , message , Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this , HomeActivity.class);
                        startActivity(intent);
                        break;

                    default: Toast.makeText(MainActivity.this ,"Internet error connection ", Toast.LENGTH_LONG).show();
                }
            }
            catch ( Exception e1) {  e1.printStackTrace();}
        }
    }


    //first login class
    public  class First_Login extends AsyncTask<String, Void, Void> {

        String national_id, graduation_id, message  , check_message, check_result ,result;
        int success, check_success;
        //ProgressDialog
        private android.app.ProgressDialog ProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ProgressDialog = ProgressDialog.show(MainActivity.this, "Processing...",
                              "Check Your Data", false, false);
        }

        @Override
        protected Void doInBackground(String... params) {

            national_id = params[0];
            graduation_id = params[1];
            ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("national_id", national_id));
            pairs.add(new BasicNameValuePair("graduation_id", graduation_id));

            JsonReader j = new JsonReader(check_url, pairs);
            check_result = j.sendRequest();

            try {
                JSONObject jsonObject = new JSONObject(check_result);
                check_success = jsonObject.getInt("success");
                check_message = jsonObject.getString("message");

                if (check_success == 2) {
                    JsonReader jsonReader = new JsonReader(first_time_url, pairs);
                    result = jsonReader.sendRequest();
                }
            } catch ( Exception e1) {  e1.printStackTrace();}

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

             if (check_success == 2 ){
                try
                 {
                     First_Login_ResponseHandler first_login_responseHandler = new First_Login_ResponseHandler(result);
                     message = first_login_responseHandler.Handler();
                     success = first_login_responseHandler.getSuccess();
                     ProgressDialog.dismiss();

                     switch (success)
                     {
                         case  0 :
                             if(message == null)
                             { Toast.makeText(MainActivity.this,"error in internet connection",Toast.LENGTH_SHORT).show();}

                             else {Toast.makeText(MainActivity.this ,message , Toast.LENGTH_SHORT).show();}
                             break;

                         case  1 : Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                             Intent intent = new Intent(MainActivity.this , DateFormActivity.class);
                             startActivity(intent);
                             break;

                         default: Toast.makeText(MainActivity.this, " error in internet connection ", Toast.LENGTH_LONG).show();
                     }
                 }
                 catch ( Exception e1) {  e1.printStackTrace();}
             }
            else {
                 ProgressDialog.dismiss();
                 Toast.makeText(MainActivity.this,check_message, Toast.LENGTH_LONG).show();
               }

        }
    }
}


