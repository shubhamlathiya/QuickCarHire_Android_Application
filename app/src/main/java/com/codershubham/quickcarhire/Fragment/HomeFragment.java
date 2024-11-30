package com.codershubham.quickcarhire.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.codershubham.quickcarhire.Adapter.SliderAdapter;
import com.codershubham.quickcarhire.ClassFile.City;
import com.codershubham.quickcarhire.ClassFile.InternetConnection;
import com.codershubham.quickcarhire.ClassFile.SliderData;
import com.codershubham.quickcarhire.HomeActivity;
import com.codershubham.quickcarhire.Notification;
import com.codershubham.quickcarhire.OfferActivity;
import com.codershubham.quickcarhire.R;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {
    final ArrayList<City> cities = new ArrayList<>();
    TextView pickDateRangeButton;
    private String webServiceUrl = "https://quickcarshire.000webhostapp.com/API/offer.php";
    String url1 = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQwdAxlG1jKtbNOIxwaV3w4OeNqHMbvDOHkbjwvdaQffVWyt_ZE5dQpIPhynKu13rMrWgs&usqp=CAU";
    String url2 = "https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?auto=format&fit=crop&q=60&w=500&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxleHBsb3JlLWZlZWR8Mnx8fGVufDB8fHx8fA%3D%3D";
    String url3 = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS4mT-mQVboQIXVHbTReXlqo76rp3Ww4DCS4d0uoEaR8FCjvG5QmMdKCs6YjV08Xpz7u_8&usqp=CAU";
    TextView btnSearch, offerviewAll,notification;
    SliderView sliderView, offerSliderView;
    Spinner spinner;
    String selectedCityId;
    String startText;
    String endText;
//    ProgressBar progressBar;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        if (!InternetConnection.isInternetAvailable(requireContext())) {
            InternetConnection.showInternetDialog(requireContext());
        }

        offerviewAll = view.findViewById(R.id.offerviewAll);
        ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();
        sliderView = view.findViewById(R.id.slider);
        btnSearch = view.findViewById(R.id.btnSearch);
        spinner = view.findViewById(R.id.spinner);
        offerSliderView = view.findViewById(R.id.offerSlider);
        pickDateRangeButton = view.findViewById(R.id.dateRangePickerButton);
        notification = view.findViewById(R.id.notification);

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(requireContext() , Notification.class);
                startActivity(i);
            }
        });
//        progressBar = view.findViewById(R.id.progressBar);

//        progressBar.setVisibility(View.VISIBLE);

        fetchCity();
        fetchOffer();

        CalendarConstraints.Builder calendarConstraintBuilder = new CalendarConstraints.Builder();
        calendarConstraintBuilder.setValidator(DateValidatorPointForward.now());

        MaterialDatePicker.Builder<Pair<Long, Long>> materialDateRangePickerBuilder = MaterialDatePicker.Builder.dateRangePicker();
        materialDateRangePickerBuilder.setTitleText("SELECT A DATE RANGE");
        materialDateRangePickerBuilder.setCalendarConstraints(calendarConstraintBuilder.build());

        final MaterialDatePicker<Pair<Long, Long>> materialDateRangePicker = materialDateRangePickerBuilder.build();

        pickDateRangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDateRangePicker.show(getChildFragmentManager(), "MATERIAL_DATE_RANGE_PICKER");
            }
        });

        materialDateRangePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                Long startTimestamp = selection.first;
                Long endTimestamp = selection.second;

                Date startDate = new Date(startTimestamp);
                Date endDate = new Date(endTimestamp);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                startText = dateFormat.format(startDate);
                endText = dateFormat.format(endDate);
                pickDateRangeButton.setText(startText + " to " + endText);
            }
        });

        offerviewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(requireContext(), OfferActivity.class);
                startActivity(i);
            }
        });

        sliderDataArrayList.add(new SliderData(url1));
        sliderDataArrayList.add(new SliderData(url2));
        sliderDataArrayList.add(new SliderData(url3));

        SliderAdapter adapter = new SliderAdapter(requireContext(), sliderDataArrayList);
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(adapter);
        sliderView.setScrollTimeInSec(2);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (isAdded()) {
                    City selectedCity = cities.get(position);
                    selectedCityId = selectedCity.getCityId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here if needed
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAdded()) {
                    CarFregment newFragment = new CarFregment();

                    Bundle bundle = new Bundle();
                    bundle.putString("City", selectedCityId);
                    bundle.putString("startDate", startText);
                    bundle.putString("endDate", endText);

                    newFragment.setArguments(bundle);
                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.containerFregment, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });

        return view;
    }

    private void fetchCity() {
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());

        String url = "https://quickcarshire.000webhostapp.com/API/City.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (isAdded()) {
                    try {
                        JSONArray citiesArray = response.getJSONArray("cities");
                        cities.clear();
                        for (int i = 0; i < citiesArray.length(); i++) {
                            JSONObject cityObject = citiesArray.getJSONObject(i);

                            City city = new City();
                            city.setCityId(cityObject.getString("City_Id"));
                            city.setCityName(cityObject.getString("City"));

                            cities.add(city);
                        }

                        ArrayAdapter<City> adapter = new ArrayAdapter<>(requireContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, cities);
                        spinner.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    public void fetchOffer() {
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, webServiceUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (isAdded()) {
                    try {
                        ArrayList<SliderData> offerSliderData = new ArrayList<>();

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject offer = response.getJSONObject(i);
                            String imageUrl = offer.getString("Image");
                            offerSliderData.add(new SliderData(imageUrl));
                        }

                        SliderAdapter offerSliderAdapter = new SliderAdapter(requireContext(), offerSliderData);

                        offerSliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
                        offerSliderView.setSliderAdapter(offerSliderAdapter);
                        offerSliderView.setScrollTimeInSec(2);
                        offerSliderView.setAutoCycle(true);
                        offerSliderView.startAutoCycle();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error", error.toString());
            }
        });

        requestQueue.add(jsonArrayRequest);
    }
}
