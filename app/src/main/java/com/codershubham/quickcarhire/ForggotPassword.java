package com.codershubham.quickcarhire;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.codershubham.quickcarhire.ClassFile.InternetConnection;

public class ForggotPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forggot_password);


        if (!InternetConnection.isInternetAvailable(this)) {
            InternetConnection.showInternetDialog(this);
        }

    }
}