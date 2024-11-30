package com.codershubham.quickcarhire;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.codershubham.quickcarhire.Adapter.HistoryAdapter;
import com.codershubham.quickcarhire.ClassFile.Booking;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private List<Booking> bookingList;
    private HistoryAdapter bookingAdapter;
    private RecyclerView recyclerView;
    private RequestQueue requestQueue;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        email = "drupenEYE@gmail.com";

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookingList = new ArrayList<>();
        bookingAdapter = new HistoryAdapter(this, bookingList);
        recyclerView.setAdapter(bookingAdapter);

        // Initialize Volley request queue
        requestQueue = Volley.newRequestQueue(this);

        // Construct the URL with the email parameter
        String url = "https://quickcarshire.000webhostapp.com/API/history.php?email=" + email;

        // Create a JSON array request to fetch the data.
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject bookingJson = response.getJSONObject(i);
                                Booking booking = new Booking();

                                booking.setId(bookingJson.getInt("id"));
                                booking.setRegistrationNo(bookingJson.getString("registration_no"));
                                booking.setName(bookingJson.getString("name"));
                                booking.setImage(bookingJson.getString("image"));
                                booking.setStartDate(bookingJson.getString("start_date"));
                                booking.setEndDate(bookingJson.getString("end_date"));
                                booking.setAddress(bookingJson.getString("Address"));
                                booking.setAmount(bookingJson.getInt("Total_Amount"));
                                bookingList.add(booking);
                            }

                            // Notify the adapter that the data has changed.
                            bookingAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error, e.g., show an error message.
                        Toast.makeText(HistoryActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(request);
    }
}