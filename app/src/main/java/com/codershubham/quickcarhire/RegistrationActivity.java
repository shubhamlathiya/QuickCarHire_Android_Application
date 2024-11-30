package com.codershubham.quickcarhire;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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

public class RegistrationActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, confirmPasswordEditText;
    private TextView registerButton;
    TextView clickSignIn;
    private static final String BASE_URL = "https://quickcarshire.000webhostapp.com/API/registration.php";
    private RequestQueue requestQueue;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        if (!InternetConnection.isInternetAvailable(this)) {
            InternetConnection.showInternetDialog(this);
        }

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.cpasswordEditText);
        registerButton = findViewById(R.id.registerButton);
        clickSignIn = findViewById(R.id.clickSignIn);

        sharedPreferences = getSharedPreferences("LoginData", MODE_PRIVATE);

        clickSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        requestQueue = Volley.newRequestQueue(this);

        registerButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(RegistrationActivity.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(RegistrationActivity.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            } else {
                String url = BASE_URL + "?email=" + email + "&password=" + password + "&cpassword=" + confirmPassword;

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");
                            String message = jsonResponse.getString("message");

                            if ("success".equals(status)) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("password", password);
                                editor.putString("email", email);
                                editor.commit();

                                Intent i = new Intent(RegistrationActivity.this, OTP.class);
                                startActivity(i);
                                Toast.makeText(RegistrationActivity.this, "Registration successful.", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(RegistrationActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("RegistrationActivity", "Error: " + error.toString());
                        Toast.makeText(RegistrationActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
                    }
                });


                requestQueue.add(stringRequest);
            }
        });
    }
}
