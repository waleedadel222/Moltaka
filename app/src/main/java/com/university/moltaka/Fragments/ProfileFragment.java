package com.university.moltaka.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
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

import com.university.moltaka.ExpandableListAdapter;
import com.university.moltaka.JSON.JsonReader;
import com.university.moltaka.Login.Login_ResponseHandler;
import com.university.moltaka.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProfileFragment extends Fragment implements View.OnClickListener{

    String profile_url = "http://10.0.2.2:80/moltaka/profile.php";
    String update_url = "http://10.0.2.2:80/moltaka/update.php";
    String national_id = Login_ResponseHandler.national_id;
    TextView name  ,faculty ,graduation_year ,language , skills;
    EditText  mobile  ;
    Button edit,update,cancel;
    String s  ,l , m ;
    String s1 ,l1 ,m1 ;
    Dialog lang_dialog ;
    CheckBox english , france, german,spanish,rocha;
    ExpandableListView expandableList;
    ArrayList<String> lang ;
    String languages = "";
    String skill ="";
    View view ;
    Button dialog_ok , dialog_cancel;

	public ProfileFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        name = (TextView) rootView.findViewById(R.id.name_tv);
        faculty = (TextView) rootView.findViewById(R.id.faculty_tv);
        graduation_year =(TextView) rootView.findViewById(R.id.graduation_tv);
        language =(TextView) rootView.findViewById(R.id.lang_tv);
        skills = (TextView) rootView.findViewById(R.id.skills_tv);
        mobile =(EditText) rootView.findViewById(R.id.mobile_tv);

        edit = (Button) rootView.findViewById(R.id.edit);
        update = (Button) rootView.findViewById(R.id.update);
        cancel = (Button) rootView.findViewById(R.id.cancel);
        edit.setOnClickListener(this);
        update.setOnClickListener(this);
        cancel.setOnClickListener(this);

        new GetProfile().execute();

       return rootView;
    }

    // Listener

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.edit :
                s  = skills.getText().toString();
                l = language.getText().toString();
                m = mobile.getText().toString();

                update.setVisibility(v.VISIBLE);
                cancel.setVisibility(v.VISIBLE);
                edit.setVisibility(v.INVISIBLE);

                language.setClickable(true);
                skills.setClickable(true);
                mobile.setFocusableInTouchMode(true);

                // languages dialog
               LayoutInflater dialog_inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                ViewGroup root = (ViewGroup) getView().findViewById(R.id.linear_layout);
                view = dialog_inflater.inflate(R.layout.languages ,root , false);
                dialog_ok = (Button) view.findViewById(R.id.ok);
                dialog_cancel = (Button) view.findViewById(R.id.cancel);
                // languages
                english = (CheckBox) view.findViewById(R.id.english);
                france = (CheckBox) view.findViewById(R.id.france);
                german = (CheckBox) view.findViewById(R.id.german);
                spanish = (CheckBox) view.findViewById(R.id.spanish);
                rocha = (CheckBox) view.findViewById(R.id.rocha);
                lang_dialog = new Dialog(getActivity());
                lang_dialog.setTitle("Languages");
                lang_dialog.setCancelable(false);
                lang_dialog.setContentView(view);
                //end  of  dialog

                //language tv listener
                language.setOnClickListener(new View.OnClickListener() {
                    @Override
                   public void onClick(View v) {
                        lang_dialog.show();
                        lang = new ArrayList<>();
                  dialog_ok.setOnClickListener(new View.OnClickListener() {

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
                                        } }
                                }
                                lang_dialog.dismiss();
                                lang.clear();
                                language.setText(languages);
                            }   });
                        dialog_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                lang_dialog.dismiss();
                                lang.clear();
                                language.setText(l);
                            }  });
                       }  });


           skills.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {

                       // Skills  dialog
                       AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                       LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                       view = inflater.inflate(R.layout.skills, null);
                       expandableList = (ExpandableListView) view.findViewById(R.id.skillsExpandableListView);
                       ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter(getActivity());
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
                                       skills.setText(skill);
                                   }})
                               .setNegativeButton("No",new DialogInterface.OnClickListener() {
                                   public void onClick (DialogInterface dialog,int id){
                                       dialog.cancel();
                                   }   } );

                       alertDialogBuilder.setView(view);
                       // create alert dialog
                       AlertDialog alertDialog = alertDialogBuilder.create();
                       // show it
                       alertDialog.show();

                   }
               });

                break;

          case R.id.cancel:
                update.setVisibility(v.INVISIBLE);
                cancel.setVisibility(v.INVISIBLE);
                edit.setVisibility(v.VISIBLE);
                language.setText(l);
                language.setClickable(false);
                skills.setText(s);
                skills.setClickable(false);
                mobile.setText(m);
                mobile.setFocusable(false);
                break;

            case R.id.update :
                l1 = language.getText().toString();
                m1 = mobile.getText().toString();
                s1 = skills.getText().toString();

              if ( !l.equals(l1) || !m1.equals(m) ||!s1.equals(s) ){
                  new Update().execute(national_id ,m1 ,s1 , languages);
              }
                break;
        } }

     // GET Profile Data
     class GetProfile extends AsyncTask<Void, Boolean, Boolean> {

        String user_name  ,user_faculty ,user_skills ,user_language ,user_graduation_year ,user_mobile;

        String result, message;
        int success;
        JSONObject jsonObject;
        JSONArray Profile ;

        // ProgressDialog
        private android.app.ProgressDialog ProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ProgressDialog = ProgressDialog.show(getActivity(),
                    "Processing...", "Getting Profile", false, false);
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("national_id", national_id));

            JsonReader j = new JsonReader(profile_url, pairs);
            result = j.sendRequest();

            try {
                jsonObject = new JSONObject(result);
                success = jsonObject.getInt("success");
                message = jsonObject.getString("message");
            } catch (Exception e) {
                e.printStackTrace();
            }

            switch (success) {
                case 0:
                    if (message == null) {
                        message = "error in connection ";
                    }
                    return false;

                case 1:

                    try {
                        Profile = jsonObject.getJSONArray("Data");

                        for (int i = 0; i < Profile.length(); i++) {
                            JSONObject c = Profile.getJSONObject(i);

                           user_name    = c.getString("name");
                           user_faculty = c.getString("faculty");
                           user_graduation_year = c.getString("graduation_year");
                           user_mobile  = c.getString("mobile");
                           user_skills  = c.getString("skills");
                           user_language= c.getString("language");

                           }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            ProgressDialog.dismiss();

            if (aBoolean) {
                name.setText(user_name);
                faculty.setText(user_faculty);
                graduation_year.setText(user_graduation_year);
                mobile.setText(user_mobile);
                skills.setText(user_skills);
                language.setText(user_language);

            }
            else {Toast.makeText(getActivity() , message , Toast.LENGTH_LONG).show();}
        }
    }


    // UpDate
    class   Update extends AsyncTask<String, Boolean, Boolean> {

        String result, message;
        int success;
        JSONObject jsonObject;

        // ProgressDialog
        private android.app.ProgressDialog ProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ProgressDialog = ProgressDialog.show(getActivity(),
                    "Processing...", "Updating Profile", false, false);
        }

        @Override
        protected Boolean doInBackground(String... params) {

            ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();

            pairs.add(new BasicNameValuePair("national_id", params[0]));
            pairs.add(new BasicNameValuePair("mobile", params[1]));
            pairs.add(new BasicNameValuePair("skills", params[2]));
            pairs.add(new BasicNameValuePair("language", params[3]));


            JsonReader j = new JsonReader(update_url, pairs);
            result = j.sendRequest();

            try {
                jsonObject = new JSONObject(result);
                success = jsonObject.getInt("success");
                message = jsonObject.getString("message");
            } catch (Exception e) {
                e.printStackTrace();
            }

            switch (success)
            {
                case 0:
                    if (message == null) {
                        message = "error in connection ";
                    }
                    return false;

                case 1:   return true;

                default:   message = "error in connection ";
             }
            return false;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            ProgressDialog.dismiss();

            if (success==1){
                Toast.makeText(getActivity() , message ,Toast.LENGTH_SHORT).show();

                update.setVisibility(view.INVISIBLE);
                cancel.setVisibility(view.INVISIBLE);
                edit.setVisibility(view.VISIBLE);
                language.setClickable(false);
                skills.setClickable(false);
                mobile.setFocusable(false);
              }
            else {Toast.makeText(getActivity() , message ,Toast.LENGTH_SHORT).show();}
        }
    }
 }
