package com.codershubham.quickcarhire;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.codershubham.quickcarhire.ClassFile.InternetConnection;

public class CustomerCareSupport extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_care_support);


        if (!InternetConnection.isInternetAvailable(this)) {
            InternetConnection.showInternetDialog(this);
        }

    }
}