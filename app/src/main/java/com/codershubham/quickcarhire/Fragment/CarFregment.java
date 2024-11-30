package com.codershubham.quickcarhire.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import com.codershubham.quickcarhire.Adapter.CarAdapter;
import com.codershubham.quickcarhire.Adapter.DataAdapter;
import com.codershubham.quickcarhire.ClassFile.Car;
import com.codershubham.quickcarhire.ClassFile.InternetConnection;
import com.codershubham.quickcarhire.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CarFregment extends Fragment {

    private List<Car> carList;
    private CarAdapter carAdapter;
    private RecyclerView recyclerView;
    private RequestQueue requestQueue;
    String city, startDate, endDate;


    public CarFregment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_fregment, container, false);

        if (!InternetConnection.isInternetAvailable(requireContext())) {
            InternetConnection.showInternetDialog(requireContext());
        }

        recyclerView = view.findViewById(R.id.recyclerView);


        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        carList = new ArrayList<>();
        carAdapter = new CarAdapter(requireContext(), carList);
        recyclerView.setAdapter(carAdapter);

        // Initialize Volley request queue
        requestQueue = Volley.newRequestQueue(requireContext());

        String baseUrl = "https://quickcarshire.000webhostapp.com/API/car.php";

        Bundle bundle = getArguments();
        if (bundle == null) {
            Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show();
        } else {
            city = bundle.getString("City");
            startDate = bundle.getString("startDate");
            endDate = bundle.getString("endDate");

            Bundle data = new Bundle();
            data.putString("City", city);
            data.putString("startDate", startDate);
            data.putString("endDate", endDate);
            DataAdapter.getInstance().setData(bundle);


        }


        String url = baseUrl + "?City=" + city + "&Start_Date=" + startDate + "&End_Date=" + endDate;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject carJson = response.getJSONObject(i);
                        Car car = new Car();
                        car.setRegistrationNo(carJson.getString("Registration_No"));
                        car.setCarName(carJson.getString("Car_name"));
                        car.setBrand(carJson.getString("Brand"));

                        String imageFileName = carJson.getString("Image");
                        String imageUrl = "https://quickcarshire.000webhostapp.com/images/carimg/" + imageFileName;
                        car.setImage(imageUrl);

                        car.setCarHireCost(carJson.getDouble("Car_hire_cost"));
                        car.setTotalBooked(carJson.getInt("TotalBooked"));
                        JSONObject categoryInfo = carJson.getJSONObject("CategoryInfo");
                        car.setSeats(categoryInfo.getString("Seats"));
                        car.setFuel(categoryInfo.getString("Fuel"));
                        car.setTransmission(categoryInfo.getString("Transmission"));

                        carList.add(car);
                    }
                    carAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(requireContext(), "Error fetching car data", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(request);

        return view;
    }
}
