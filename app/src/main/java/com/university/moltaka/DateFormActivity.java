package com.university.moltaka;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.university.moltaka.JSON.JsonReader;
import com.university.moltaka.Login.First_Login_ResponseHandler;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;


public class DateFormActivity extends Activity {

    String name,national_id,graduation_id, faculty, graduation_year;
    String insert_url = "http://10.0.2.2:80/moltaka/insert.php";

    ExpandableListView expandableList;
    String skill = "";

    TextView languageTV , skillsTY;
    EditText mobile   ;
    Button submit ;
    String m ;

    CheckBox english , france, german,spanish,rocha;
    ArrayList<String> lang ;
    String languages = "";
    View lang_view , skill_view ;
    Button ok , cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_form);

        name = First_Login_ResponseHandler.name;
        national_id = First_Login_ResponseHandler.national_id;
        graduation_id = First_Login_ResponseHandler.graduation_id;
        faculty = First_Login_ResponseHandler.faculty;
        graduation_year = First_Login_ResponseHandler.graduation_year;

        mobile = (EditText) findViewById(R.id.mobile);

        // languages dialog
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup root = (ViewGroup) findViewById(R.id.linear_layout);
        lang_view =inflater.inflate(R.layout.languages ,root , false);
        // languages
        english = (CheckBox) lang_view.findViewById(R.id.english);
        france  = (CheckBox) lang_view.findViewById(R.id.france);
        german  = (CheckBox) lang_view.findViewById(R.id.german);
        spanish = (CheckBox) lang_view.findViewById(R.id.spanish);
        rocha   = (CheckBox) lang_view.findViewById(R.id.rocha);
        final Dialog dialog = new Dialog(DateFormActivity.this);
        dialog.setTitle("Languages");
        dialog.setContentView(lang_view);
        dialog.setCancelable(false);
        ok = (Button) lang_view.findViewById(R.id.ok);
        cancel = (Button) lang_view.findViewById(R.id.cancel);
        //end  of  dialog

        languageTV = (TextView) findViewById(R.id.language_select);
        languageTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                dialog.show();
                lang = new ArrayList<>();

                ok.setOnClickListener(new View.OnClickListener() {

                     @Override
                     public void onClick(View v) {
                         languages = "";

                         if ((english.isChecked()) == true) {
                            lang.add("english");
                        }
                        if ((france.isChecked()) == true) {
                            lang.add("french");
                        }
                        if ((german.isChecked()) == true) {
                            lang.add("german");
                        }
                        if ((spanish.isChecked()) == true) {
                            lang.add("spanish");
                        }
                        if ((rocha.isChecked()) == true) {
                            lang.add("russian");
                        }
                        if (lang.size() == 0) {
                            languages = "";
                        } else if (lang.size() == 1) {
                            languages = lang.get(0);
                        } else {
                             for (int i = 0; i < lang.size(); i++) {
                                 if (i == (lang.size() - 1)) {
                                    languages += lang.get(i);
                                } else {
                                    languages += lang.get(i) + "-";
                                } }  }
                       dialog.dismiss();
                       lang.clear();
                       languageTV.setText(languages);
                       }   });
               cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                }  });
                }  });


        skillsTY = (TextView) findViewById(R.id.skills);
        skillsTY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Skills  dialog
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DateFormActivity.this);
                skill_view = getLayoutInflater().inflate(R.layout.skills, null);
                expandableList = (ExpandableListView) skill_view.findViewById(R.id.skillsExpandableListView);
                ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter(DateFormActivity.this);
                expandableList.setAdapter(expandableListAdapter);
                // set title
                alertDialogBuilder.setTitle("Select your skills");
                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                skill="";
                                if (ExpandableListAdapter.skillsArray.size() == 0) {
                                    skill = "";
                                } else if (ExpandableListAdapter.skillsArray.size() == 1) {
                                    skill = ExpandableListAdapter.skillsArray.get(0);
                                } else {
                                    for (int i = 0; i < ExpandableListAdapter.skillsArray.size(); i++) {
                                        if (i == (ExpandableListAdapter.skillsArray.size() - 1)) {
                                            skill += ExpandableListAdapter.skillsArray.get(i);
                                        } else {
                                            skill += ExpandableListAdapter.skillsArray.get(i) + "-";
                                        } } }
                                dialog.dismiss();
                                ExpandableListAdapter.skillsArray.clear();
                                skillsTY.setText(skill);
                            }})
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                           public void onClick (DialogInterface dialog,int id){
                                  dialog.cancel();
                         }   } );

                alertDialogBuilder.setView(skill_view);
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
           }   });

            submit = (Button) findViewById(R.id.submit_btn);
            submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                m = mobile.getText().toString();

               if (m.equals("")&& skill.equals("") && languages.equals("")){
                    Toast.makeText(DateFormActivity.this," اكتب بياناتك  اولا", Toast.LENGTH_LONG).show();
                } else {
                new SetData().execute(national_id, graduation_id, name, faculty,graduation_year, m , skill , languages);
                } } } );
    }

    public class SetData extends AsyncTask<String,Boolean,Boolean> {

        int success;
        String  message ,result;
        JSONObject jsonObject;

        private android.app.ProgressDialog ProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ProgressDialog = ProgressDialog.show(DateFormActivity.this," Processing..."
                             ,"Adding Your Data", false, false);
        }

        @Override
        protected Boolean doInBackground(String... params) {

            ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("national_id", params[0]));
            pairs.add(new BasicNameValuePair("graduation_id", params[1]));
            pairs.add(new BasicNameValuePair("name", params[2]));
            pairs.add(new BasicNameValuePair("faculty", params[3]));
            pairs.add(new BasicNameValuePair("graduation_year", params[4]));
            pairs.add(new BasicNameValuePair("mobile", params[5]));
            pairs.add(new BasicNameValuePair("skills", params[6]));
            pairs.add(new BasicNameValuePair("language", params[7]));

            JsonReader j = new JsonReader(insert_url, pairs);
            result = j.sendRequest();

            try {
                jsonObject = new JSONObject(result);
                success = jsonObject.getInt("success");
                message = jsonObject.getString("message");
               } catch (Exception e) {
                e.printStackTrace();
            }

            switch (success){

                case 0:  if (message == null) { message = "Error In internet Connection";  }
                      return false;

                case 1:
                    return true;

               default:   message = "Error In internet Connection";
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

           ProgressDialog.dismiss();

           if (aBoolean){

               AlertDialog.Builder dialog =new AlertDialog.Builder(DateFormActivity.this);
               dialog.setTitle("Login Successfully").setMessage(" Now Login Again Without Checking New User");
               dialog.setCancelable(false);
               dialog.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which) {
                   dialog.dismiss();
                   DateFormActivity.this.finish();
                    } });
               dialog.create().show();
              }
            else {
               Toast.makeText(DateFormActivity.this , message ,Toast.LENGTH_SHORT).show();
           }
    } } }
