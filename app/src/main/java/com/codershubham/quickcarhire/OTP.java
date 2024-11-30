package com.codershubham.quickcarhire;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.codershubham.quickcarhire.ClassFile.InternetConnection;

import org.json.JSONObject;

public class OTP extends AppCompatActivity {

    private EditText[] otpEditTexts = new EditText[6];
    private TextView resendButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);


        if (!InternetConnection.isInternetAvailable(this)) {
            InternetConnection.showInternetDialog(this);
        }

        otpEditTexts[0] = findViewById(R.id.editText1);
        otpEditTexts[1] = findViewById(R.id.editText2);
        otpEditTexts[2] = findViewById(R.id.editText3);
        otpEditTexts[3] = findViewById(R.id.editText4);
        otpEditTexts[4] = findViewById(R.id.editText5);
        otpEditTexts[5] = findViewById(R.id.editText6);

        resendButton = findViewById(R.id.resendBtn);
        sharedPreferences = getSharedPreferences("LoginData", Context.MODE_PRIVATE);

        setOtpTextListeners();
        setResendButtonListener();

    }

    private void setOtpTextListeners() {
        for (int i = 0; i < otpEditTexts.length; i++) {
            final int currentIndex = i;

            otpEditTexts[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() == 1) {
                        if (currentIndex < otpEditTexts.length - 1) {
                            otpEditTexts[currentIndex + 1].requestFocus();
                        } else {

                            String enteredOtp = getEnteredOTP();
                            String email = sharedPreferences.getString("email", null);

                            String url = "https://quickcarshire.000webhostapp.com/API/OTP.php";

                            String fullUrl = url + "?otp=" + enteredOtp + "&email=" + email;

                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, fullUrl, null,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            // Handle the successful response here
                                            try {
                                                String status = response.getString("status");
                                                String message = response.getString("message");
                                                if(status.equals("success")){
                                                    Toast.makeText(OTP.this, message, Toast.LENGTH_SHORT).show();
                                                    Intent i=new Intent(OTP.this, HomeActivity.class);
                                                    startActivity(i);
                                                    finish();
                                                }else {
                                                    Toast.makeText(OTP.this, "invalid", Toast.LENGTH_SHORT).show();
                                                }

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            // Handle any errors that occur during the request
                                            error.printStackTrace();
                                        }
                                    }
                            );

                            RequestQueue requestQueue = Volley.newRequestQueue(OTP.this);
                            requestQueue.add(jsonObjectRequest);

                        }
                    } else if (charSequence.length() == 0 && currentIndex > 0) {
                        otpEditTexts[currentIndex - 1].requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });

            otpEditTexts[i].setOnKeyListener((view, keyCode, keyEvent) -> {
                if (keyCode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == KeyEvent.ACTION_DOWN && otpEditTexts[currentIndex].getText().toString().isEmpty() && currentIndex > 0) {
                    otpEditTexts[currentIndex - 1].requestFocus();
                }
                return false;
            });
        }
    }

    private String getEnteredOTP() {
        StringBuilder otpBuilder = new StringBuilder();
        for (EditText editText : otpEditTexts) {
            otpBuilder.append(editText.getText().toString());
        }
        return otpBuilder.toString();
    }

    private boolean yourVerificationFunction(String enteredOtp) {
        String correctOtp = "123456";

        return enteredOtp.equals(correctOtp);
    }

    private void setResendButtonListener() {
        resendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearEnteredOTP();
            }
        });
    }

    private void clearEnteredOTP() {
        for (EditText editText : otpEditTexts) {
            editText.setText("");
        }
    }
}