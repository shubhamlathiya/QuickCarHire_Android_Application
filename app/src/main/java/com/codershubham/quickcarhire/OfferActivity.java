package com.codershubham.quickcarhire;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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
import com.codershubham.quickcarhire.Adapter.OfferAdapter;
import com.codershubham.quickcarhire.ClassFile.InternetConnection;
import com.codershubham.quickcarhire.ClassFile.Offer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OfferActivity extends AppCompatActivity {

    private String webServiceUrl = "https://quickcarshire.000webhostapp.com/API/offer.php";
    private ArrayList<Offer> offerList;
    private RecyclerView recyclerView;
    private OfferAdapter offerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);


        if (!InternetConnection.isInternetAvailable(this)) {
            InternetConnection.showInternetDialog(this);
        }
        recyclerView = findViewById(R.id.offerRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        offerList = new ArrayList<>();
        offerAdapter = new OfferAdapter(this, offerList);
        recyclerView.setAdapter(offerAdapter);

        // Initialize a RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Create a request to fetch JSON data
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, webServiceUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject offer = response.getJSONObject(i);
                        String imageUrl = offer.getString("Image");
                        String name = offer.getString("Name");
                        String code = offer.getString("Code");
                        String percentage = offer.getString("Percentage");
                        String startDate = offer.getString("Start_Date");
                        String endDate = offer.getString("End_Date");
                        offerList.add(new Offer(imageUrl, name, code, percentage, startDate, endDate));
                    }
                    offerAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle errors
                Log.e("Volley Error", error.toString());
            }
        });

        // Add the request to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }

    public void copyText(View view) {
        TextView textView = (TextView) view;
        String textToCopy = textView.getText().toString();

        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Copied Text", textToCopy);
        clipboardManager.setPrimaryClip(clipData);

        Toast.makeText(this, "Text copied " + textToCopy, Toast.LENGTH_SHORT).show();
    }
}
