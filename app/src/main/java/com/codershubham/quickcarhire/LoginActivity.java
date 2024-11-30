package com.codershubham.quickcarhire;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codershubham.quickcarhire.ClassFile.InternetConnection;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    SharedPreferences sharedPreferences;
    private static final String BASE_URL = "https://quickcarshire.000webhostapp.com/API/Login.php";
    EditText edLoginemail, edLoginpass;
    TextView loginbtn, clickSignUp,forgotpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        if (!InternetConnection.isInternetAvailable(this)) {
            InternetConnection.showInternetDialog(this);
        }

        requestQueue = Volley.newRequestQueue(this);

        edLoginemail = findViewById(R.id.edLoginemail);
        edLoginpass = findViewById(R.id.edLoginpass);
        loginbtn = findViewById(R.id.loginbtn);
        clickSignUp = findViewById(R.id.clickSignUp);
        forgotpassword = findViewById(R.id.forgotpassword);

        sharedPreferences = getSharedPreferences("LoginData", MODE_PRIVATE);

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, ForggotPassword.class);
                startActivity(i);
                finish();
            }
        });
        clickSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(i);
                finish();
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edLoginemail.getText().toString().trim();
                String password = edLoginpass.getText().toString().trim();

                String url = BASE_URL + "?email=" + email + "&password=" + password;

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");
                            String message = jsonResponse.getString("message");

                            if ("success".equals(status)) {
                                String user_email = jsonResponse.getString("user_email");

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("fullName", user_email);
                                editor.putString("password", password);
                                editor.putString("email", email);
                                editor.commit();

                                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LoginActivity", "Error: " + error.toString());
                        Toast.makeText(LoginActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
                    }
                });
                requestQueue.add(stringRequest);
            }
        });
    }
}
