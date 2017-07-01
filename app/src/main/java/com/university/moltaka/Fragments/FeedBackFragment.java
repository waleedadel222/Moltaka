package com.university.moltaka.Fragments;


import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.university.moltaka.JSON.JsonReader;
import com.university.moltaka.Login.Login_ResponseHandler;
import com.university.moltaka.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;


public class FeedBackFragment extends Fragment {

    String id = Login_ResponseHandler.national_id;
    String name = Login_ResponseHandler.name;

    String feed_back_url ="http://10.0.2.2:80/moltaka/feed_back.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_feed_back, container, false);

        final EditText feed_back = (EditText) view.findViewById(R.id.feed_back);

        Button send  = (Button) view.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String data = feed_back.getText().toString();
                if (data.equals("")){

                    Toast.makeText(getActivity() ,"Write Some thing ...", Toast.LENGTH_SHORT).show();

                }else {
                       new FeedBack().execute(id , name , data);
                }

            } });

        return view;
    }

    public class FeedBack extends AsyncTask<String,Void,Void>{

        int success;
        String  message ,result;
        JSONObject jsonObject;

        private android.app.ProgressDialog ProgressDialog;

         @Override
         protected void onPreExecute() {
            super.onPreExecute();

            ProgressDialog = ProgressDialog.show(getActivity() ," Processing..."
                             ,"Sending Your Feed Back", false, false);
        }

        @Override
        protected Void doInBackground(String... params) {

            ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("national_id", params[0]));
            pairs.add(new BasicNameValuePair("name", params[1]));
            pairs.add(new BasicNameValuePair("feed_back", params[2]));

            JsonReader j = new JsonReader(feed_back_url, pairs);
            result = j.sendRequest();

            try {
                jsonObject = new JSONObject(result);
                success = jsonObject.getInt("success");
                message = jsonObject.getString("message");
            }catch (Exception e) {e.printStackTrace(); }

            switch (success){

                case 0: if (message == null) { message = "error";  }
                        break;

                case 1:  break;

                default: message = "error in connection ";
            }

               return null;
        }

         @Override
         protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ProgressDialog.dismiss();
            Toast.makeText(getActivity() , message, Toast.LENGTH_SHORT).show();
        }


    }
}