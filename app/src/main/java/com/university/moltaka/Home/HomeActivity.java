package com.university.moltaka.Home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.university.moltaka.Drawer.FragmentActivity;
import com.university.moltaka.R;


public class HomeActivity extends Activity{

    String gridRowContent[];
    Intent intent ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

         gridRowContent= new String[]{ "Profile", "Job offers", "Feed back","Help", "About", "Log out"};
         intent = new Intent(HomeActivity.this , FragmentActivity.class);


        GridView homeGView = (GridView)findViewById(R.id.homeGridView);

        HomeGridViewAdapter homeAdapter=new HomeGridViewAdapter(HomeActivity.this, R.layout.home_row,gridRowContent);

        homeGView.setAdapter(homeAdapter);

        homeGView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

              public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                  if (position == 5)
                  {
                      AlertDialog.Builder dialog =new AlertDialog.Builder(HomeActivity.this);
                      dialog.setTitle("Logout").setMessage("   Are You Sure ?");

                     dialog.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {

                              //close the app
                              dialog.dismiss();
                              Intent intent = new Intent(Intent.ACTION_MAIN);
                              intent.addCategory(Intent.CATEGORY_HOME);
                              intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                              startActivity(intent);


                          } });
                     dialog.setNegativeButton("No" , new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {
                              dialog.dismiss();  } });
                      dialog.create().show();
                  }
                  else {
                  intent.putExtra("position",position);
                  startActivity(intent);
                  }

            } });

    } }
