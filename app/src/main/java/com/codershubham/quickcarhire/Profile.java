package com.codershubham.quickcarhire;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.codershubham.quickcarhire.ClassFile.InternetConnection;

import org.json.JSONObject;

public class Profile extends AppCompatActivity {

    EditText nameTextView,emailTextView,mobileTextView,dobTextView,dlTextView,aadharTextView;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        if (!InternetConnection.isInternetAvailable(this)) {
            InternetConnection.showInternetDialog(this);
        }

        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        mobileTextView = findViewById(R.id.mobileTextView);
        dobTextView = findViewById(R.id.dobTextView);
        dlTextView = findViewById(R.id.dlTextView);
        aadharTextView = findViewById(R.id.aadharTextView);

        fetchData();
    }

    private void fetchData() {

        sharedPreferences = getSharedPreferences("LoginData", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);

        String url = "https://quickcarshire.000webhostapp.com/API/Profile.php?Email=" +email;

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    private void parseResponse(JSONObject response) {
        try {
            String name = response.getString("Name");
            String email = response.getString("Email");
            String mobile = response.getString("Mobile");
            String dob = response.getString("Date_Of_Birth");
            String dl = response.getString("Driving_Licence");
            String aadhar = response.getString("AadharCard");

            nameTextView.setText(name);
            emailTextView.setText(email);
            mobileTextView.setText(mobile);
            dobTextView.setText(dob);
            dlTextView.setText(dl);
            aadharTextView.setText(aadhar);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}