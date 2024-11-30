package com.codershubham.quickcarhire;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.codershubham.quickcarhire.Adapter.DataAdapter;
import com.codershubham.quickcarhire.ClassFile.DateUtils;
import com.codershubham.quickcarhire.ClassFile.InternetConnection;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BookingActivity extends AppCompatActivity implements PaymentResultWithDataListener {
//public class BookingActivity extends AppCompatActivity implements OnDataPassedListener{

    private static final int MY_SOCKET_TIMEOUT_MS = 5000;
    TextView registrationNoTextView, textViewCarCategory, textViewCarName, StartDate, EndDate, booknow, btnapplyoffer, FinalTotalAmount, Discount, txtsecurityD;
    ImageView carImageView;
    EditText offerCodeEditText;
    String email, registrationNo;
    String startDate, endDate, city;
    TextView btn1, btn2, btn3, TotalAmount, totalSeletedKMS, ExtraCharg;
    int totalkm8, totalkm15, totalkm20, securityD, discount = 0, KMS = 0, total = 0, Ftotal = 0;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        if (!InternetConnection.isInternetAvailable(this)) {
            InternetConnection.showInternetDialog(this);
        }

        sharedPreferences = getSharedPreferences("LoginData", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("email", null);

        carImageView = findViewById(R.id.carImageView);
        registrationNoTextView = findViewById(R.id.registrationNoTextView);
        textViewCarCategory = findViewById(R.id.textViewCarCategory);
        textViewCarName = findViewById(R.id.textViewCarName);
        StartDate = findViewById(R.id.StartDate);
        EndDate = findViewById(R.id.EndDate);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        TotalAmount = findViewById(R.id.TotalAmount);
        totalSeletedKMS = findViewById(R.id.totalSeletedKMS);
        ExtraCharg = findViewById(R.id.ExtraCharg);
        booknow = findViewById(R.id.booknow);
        offerCodeEditText = findViewById(R.id.offerCodeEditText);
        btnapplyoffer = findViewById(R.id.btnapplyoffer);
        FinalTotalAmount = findViewById(R.id.FinalTotalAmount);
        Discount = findViewById(R.id.Discount);
        txtsecurityD = findViewById(R.id.securityD);

        Bundle data = DataAdapter.getInstance().getData();

        if (data != null) {
            city = data.getString("City");
            startDate = data.getString("startDate");
            endDate = data.getString("endDate");
        }

        String startDateStr = startDate;
        String endDateStr = endDate;
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            Date startDate = format.parse(startDateStr);
            Date endDate = format.parse(endDateStr);
            long timeDifference = Math.abs(endDate.getTime() - startDate.getTime());

            int hoursDifference = (int) (timeDifference / (60 * 60 * 1000));

            System.out.println("TextView" + hoursDifference);
            totalkm8 = hoursDifference * 8;
            totalkm15 = hoursDifference * 11;
            totalkm20 = hoursDifference * 15;

            btn1.setText(String.valueOf(totalkm8) + "KMS ");
            btn2.setText(String.valueOf(totalkm15) + "KMS ");
            btn3.setText(String.valueOf(totalkm20) + "KMS ");
            KMS = totalkm8;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        StartDate.setText(startDate + " | ");
        EndDate.setText(endDate + " | ");


        Intent intent = getIntent();
        if (intent != null) {

            registrationNo = intent.getStringExtra("registrationNo");
            String carName = intent.getStringExtra("carName");
            String brand = intent.getStringExtra("brand");
            String imageUrl = intent.getStringExtra("imageUrl");
            double carHireCost = intent.getDoubleExtra("carHireCost", 0.0);
            int totalBooked = intent.getIntExtra("totalBooked", 0);
            String seats = intent.getStringExtra("seats");
            String fuel = intent.getStringExtra("fuel");
            String transmission = intent.getStringExtra("transmission");

            if (carHireCost > 20) {
                securityD = 3000;
            } else {
                securityD = 2000;
            }

            registrationNoTextView.setText(registrationNo);
            textViewCarCategory.setText(brand);
            textViewCarName.setText(carName);
            Picasso.get().load(imageUrl).into(carImageView);

            ExtraCharg.setText("₹ " + carHireCost + "(per KMS)");
            totalSeletedKMS.setText(String.valueOf(totalkm8) + " KMS");
            total = (int) (carHireCost * totalkm8);

            TotalAmount.setText("₹ " + String.valueOf(total));
            Discount.setText("₹ " + 0);
            txtsecurityD.setText("₹ " + securityD);

            Ftotal = total + securityD;

            FinalTotalAmount.setText("₹ " + String.valueOf(total + securityD));


            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    total = (int) (carHireCost * totalkm8);
                    TotalAmount.setText("₹ " + String.valueOf(total));
                    KMS = totalkm8;
                    totalSeletedKMS.setText(String.valueOf(totalkm8) + " KMS");
                    Ftotal = total + securityD;
                    FinalTotalAmount.setText("₹ " + String.valueOf(total + securityD));
                }
            });

            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    total = (int) (carHireCost * totalkm15);
                    TotalAmount.setText("₹ " + String.valueOf(total));
                    KMS = totalkm15;
                    totalSeletedKMS.setText(String.valueOf(totalkm15) + " KMS");
                    Ftotal = total + securityD;
                    FinalTotalAmount.setText("₹ " + String.valueOf(total + securityD));
                }
            });

            btn3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    total = (int) (carHireCost * totalkm20);
                    TotalAmount.setText("₹ " + String.valueOf(total));
                    KMS = totalkm20;
                    totalSeletedKMS.setText(String.valueOf(totalkm20) + " KMS");
                    Ftotal = total + securityD;
                    FinalTotalAmount.setText("₹ " + String.valueOf(total + securityD));
                }
            });

        }

        booknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String samount = FinalTotalAmount.getText().toString();
//                int amount = Math.round(Float.parseFloat(samount) * 100);

                samount = samount.replaceAll("[^\\d.]", "");

                if (!samount.isEmpty()) {

                    int amount = Math.round(Float.parseFloat(samount) * 100);

                    Checkout checkout = new Checkout();
                    checkout.setKeyID("rzp_test_Wab8jHTFKFvPS6");
                    JSONObject object = new JSONObject();
                    try {
                        // to put name
                        object.put("name", "Quick Car Hire");

                        // put description
                        object.put("description", "Test payment");

                        // to set theme color
                        object.put("theme.color", "");

                        // put the currency
                        object.put("currency", "INR");

                        // put amount
                        object.put("amount", amount);

                        // put mobile number
                        object.put("prefill.contact", "7041138931");

                        // put email
                        object.put("prefill.email", "shubhamlathiya2004@gmail.com");

                        // open razorpay to checkout activity
                        checkout.open(BookingActivity.this, object);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Handle the case where samount is empty or doesn't contain valid numeric characters
                    // You might want to show an error message or take appropriate action
                }
            }
        });


        btnapplyoffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyOffer();
            }
        });
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentId, PaymentData paymentData) {
        // Handle the successful payment
        String transactionId = razorpayPaymentId;
        Toast.makeText(this, transactionId, Toast.LENGTH_SHORT).show();

        try {
            makeGetRequest(transactionId);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

//        Intent i = new Intent(BookingActivity.this, HomeActivity.class);
//        startActivity(i);
//        finish();

    }


    // ... (previous imports and code)


    private void makeGetRequest(String transactionId) throws ParseException {
        String SERVER_URL = "https://quickcarshire.000webhostapp.com/API/booking.php";

        Uri.Builder builder = Uri.parse(SERVER_URL).buildUpon();

        String insertEmail = email;
        Log.d("insert", insertEmail);
        String insertR_no = registrationNo;
        Log.d("insert", insertR_no);
        String insertCity = city;
        Log.d("insert", insertCity);

        String insertSDate = startDate;
        Log.d("insert", insertSDate);
        String insertEDate = endDate;
        Log.d("insert", insertEDate);
        String insertKMS = String.valueOf(KMS);
        Log.d("insert", insertKMS);
        String insertDiscount = String.valueOf(discount);
        Log.d("insert", insertDiscount);
        String insertTotalAmount = String.valueOf(total);
        Log.d("insert", insertTotalAmount);
        String insertsecurityD = String.valueOf(securityD);
        Log.d("insert", insertsecurityD);

        String insertFinalTotalAmount = String.valueOf(Ftotal);
        Log.d("insert", insertFinalTotalAmount);

        String inserttransactionId = transactionId;
        Log.d("insert", inserttransactionId);

        String Sdate = DateUtils.convertToDatabaseFormat(startDate);
        String Edate = DateUtils.convertToDatabaseFormat(endDate);

        builder.appendQueryParameter("email", insertEmail).appendQueryParameter("R_no", insertR_no).appendQueryParameter("city", insertCity).appendQueryParameter("Start_Date", Sdate).appendQueryParameter("End_Date", Edate).appendQueryParameter("kms", insertKMS).appendQueryParameter("Start_Time", "10:00:00").appendQueryParameter("End_Time", "10:00:00").appendQueryParameter("discount1", insertDiscount).appendQueryParameter("price", insertTotalAmount).appendQueryParameter("securityD", insertsecurityD).appendQueryParameter("TotalAmount", insertFinalTotalAmount).appendQueryParameter("transactionId", inserttransactionId);

        // Instantiate the RequestQueue.
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(this);

        // Request a JSON response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, builder.toString(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonResponse) {
                try {
                    String status = jsonResponse.getString("status");
                    String message = jsonResponse.getString("message");

                    if ("success".equals(status)) {
                        String bookingId = jsonResponse.getString("bookingId");
                        Log.d("Booking ID", bookingId);
                        showsuccessful();
                        Toast.makeText(BookingActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        // Handle error
                        Toast.makeText(BookingActivity.this, "hy", Toast.LENGTH_SHORT).show();
                        Log.e("Error", message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                Log.e("Error", "Volley Error: " + error.toString());
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }


    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        showCustomDialog();
        Toast.makeText(this, "Payment Failed due to error : " + s, Toast.LENGTH_SHORT).show();
    }

    private void applyOffer() {
        String offerCode = offerCodeEditText.getText().toString();
        String samount = TotalAmount.getText().toString();
        samount = samount.replaceAll("[^\\d.]", "");


        String url = "https://quickcarshire.000webhostapp.com/API/apply_offer.php" + "?offerCode=" + offerCode + "&price=" + samount + "&securityD=" + securityD;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("success")) {
                        discount = response.getInt("discount");
                        int totalAmount = response.getInt("totalAmount");

                        Discount.setText("₹ " + discount);

                        FinalTotalAmount.setText("₹ " + totalAmount);
                    } else {
                        String message = response.getString("message");
                        Toast.makeText(BookingActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error", "Error: " + error.toString());
                Toast.makeText(BookingActivity.this, "Error applying offer", Toast.LENGTH_SHORT).show();
            }
        });


        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(jsonObjectRequest);
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

    public void showsuccessful() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.successful_alert_dialog, null);

        builder.setView(dialogView);

        final AlertDialog dialog = builder.create();
        TextView negativeButton = dialogView.findViewById(R.id.tryagain);
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BookingActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
            }
        });


        dialog.show();
    }

}