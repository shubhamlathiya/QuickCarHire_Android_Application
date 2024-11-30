package com.codershubham.quickcarhire.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
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
import com.codershubham.quickcarhire.ClassFile.InternetConnection;
import com.codershubham.quickcarhire.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private List<Booking> bookingList;
    private HistoryAdapter bookingAdapter;
    private RecyclerView recyclerView;
    private RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        if (!InternetConnection.isInternetAvailable(requireContext())) {
            InternetConnection.showInternetDialog(requireContext());
        }

        sharedPreferences = requireActivity().getSharedPreferences("LoginData", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);

        recyclerView = view.findViewById(R.id.recyclerView123);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bookingList = new ArrayList<>();
        bookingAdapter = new HistoryAdapter(requireContext(), bookingList);
        recyclerView.setAdapter(bookingAdapter);

        // Initialize Volley request queue
        requestQueue = Volley.newRequestQueue(requireContext());

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
                                booking.setCity(bookingJson.getString("City_Id"));
                                booking.setSecurity_Deposit(bookingJson.getString("Security_Deposit"));
                                booking.setSelected_Kms(bookingJson.getString("Selected_Kms"));
                                booking.setAmount(bookingJson.getInt("Total_Amount"));

                                bookingList.add(booking);
                            }

                            bookingAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(requireContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(request);


//        generatePDFbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // calling method to
//                // generate our PDF file.
//                generatePDF();
//            }
//        });
//
        return view;
    }

}
