package com.codershubham.quickcarhire;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.codershubham.quickcarhire.launcher.Luncher;
import com.codershubham.quickcarhire.launcher.LuncherManager;

public class Splash extends AppCompatActivity {

    LuncherManager luncherManager;
    SharedPreferences sharedPreferences;

    @SuppressLint("AppCompatMethod")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        luncherManager = new LuncherManager(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (luncherManager.isFirstTime()) {
                    luncherManager.setFirstLunch(false);
                    startActivity(new Intent(getApplicationContext(), Luncher.class));
                    finish();
                } else {

                    sharedPreferences = getSharedPreferences("LoginData", MODE_PRIVATE);
                    if (sharedPreferences.contains("email") && sharedPreferences.contains("password")) {
//                        startActivity(new Intent(Splash.this, MainActivity.class));
                        startActivity(new Intent(Splash.this, HomeActivity.class));
                        finish();
                    } else {
                        Intent a = new Intent(Splash.this, LoginActivity.class);
                        startActivity(a);
                        finish();
                    }
                }
            }
        }, 3000);
    }
}