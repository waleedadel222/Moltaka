package com.university.moltaka.Splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.Toast;

import com.university.moltaka.Home.HomeActivity;
import com.university.moltaka.InternetConnection.ConnectionDetector;
import com.university.moltaka.Login.MainActivity;
import com.university.moltaka.R;


public class SplashActivity extends Activity {

    String[] numbers;
    Integer[] imgResources;
    private static int SPLASH_TIME_OUT = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        numbers= new String[] { "A", "B", "C","D", "E","F", "G", "H", "I", "J","K", "L",  "M", "N", "O","P", "Q", "R" };

        imgResources=new Integer []{
                R.drawable.android,R.drawable.apple,R.drawable.chrome,
                R.drawable.dell,R.drawable.ubuntu,R.drawable.vanamo,
                R.drawable.android,R.drawable.apple,R.drawable.chrome,
                R.drawable.dell,R.drawable.ubuntu,R.drawable.vanamo,
                R.drawable.android,R.drawable.apple,R.drawable.chrome,
                R.drawable.dell,R.drawable.ubuntu,R.drawable.vanamo,
        };

        GridView gView = (GridView)findViewById(R.id.gridView);
        SplashGridViewAdapter adapter = new SplashGridViewAdapter(SplashActivity.this, R.layout.splah_row,numbers,imgResources);

        gView.setAdapter(adapter);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fly_in_from_center);
        gView.setAnimation(anim);
        anim.start();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                ConnectionDetector detector = new ConnectionDetector(SplashActivity.this);
                if (detector.isConnectingToInternet() == true){

                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }
                else {
                    Toast.makeText(SplashActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                }





            }
        }, SPLASH_TIME_OUT);



    }




}
