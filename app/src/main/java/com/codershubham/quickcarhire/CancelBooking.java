package com.codershubham.quickcarhire;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.codershubham.quickcarhire.ClassFile.InternetConnection;

import org.json.JSONException;
import org.json.JSONObject;

public class CancelBooking extends AppCompatActivity {


    RadioGroup radioGroup;
    EditText editText;
    TextView btnCancel, textView11;
    String selectedValue,bookingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_booking);

        if (!InternetConnection.isInternetAvailable(this)) {
            InternetConnection.showInternetDialog(this);
        }

        Intent intent = getIntent();
        bookingId = intent.getStringExtra("bookingID");
//
//        if (bookingId != null) {
//            Toast.makeText(this, bookingId, Toast.LENGTH_SHORT).show();
//        } else {
//            // Handle the case where the bookingId is null
//            Toast.makeText(this, "Booking ID is null", Toast.LENGTH_SHORT).show();
//        }

        radioGroup = findViewById(R.id.radioGroup);
        editText = findViewById(R.id.edit_text);
        btnCancel = findViewById(R.id.btnCancel);
        textView11 = findViewById(R.id.textView11);


        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            selectedValue = getSelectedRadioButtonValue(checkedId);
            toggleEditTextVisibility(checkedId);

        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelBooking(bookingId , selectedValue);
            }
        });

    }

    private void toggleEditTextVisibility(int checkedId) {
        if (checkedId == R.id.radioButton5) {
            textView11.setVisibility(View.VISIBLE);
            editText.setVisibility(View.VISIBLE);
        } else {
            textView11.setVisibility(View.GONE);
            editText.setVisibility(View.GONE);
        }
    }


    private void cancelBooking(String bookingId  , String reason) {
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("booking_id", bookingId);
            requestData.put("Cancellation_Reason", reason);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://quickcarshire.000webhostapp.com/API/cancel_booking.php", requestData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            String message = response.getString("message");

                            if (success) {
                                Toast.makeText(CancelBooking.this, message, Toast.LENGTH_SHORT).show();

                                Intent i=new Intent(CancelBooking.this , HomeActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                showCustomDialog();
                                Toast.makeText(CancelBooking.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(CancelBooking.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the Volley request queue
        Volley.newRequestQueue(this).add(request);
    }

    private String getSelectedRadioButtonValue(int checkedId) {
        RadioButton selectedRadioButton = findViewById(checkedId);
        return (selectedRadioButton != null) ? selectedRadioButton.getText().toString() : "";
    }

    public void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.worng_alert_dialog, null);

        builder.setView(dialogView);

        final AlertDialog dialog = builder.create();
        TextView negativeButton = dialogView.findViewById(R.id.tryagain);
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}