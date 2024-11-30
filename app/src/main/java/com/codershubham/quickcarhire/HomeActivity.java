package com.codershubham.quickcarhire;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.codershubham.quickcarhire.ClassFile.InternetConnection;
import com.codershubham.quickcarhire.Fragment.CarFregment;
import com.codershubham.quickcarhire.Fragment.HistoryFragment;
import com.codershubham.quickcarhire.Fragment.HomeFragment;
import com.codershubham.quickcarhire.Fragment.LocationFragment;
import com.codershubham.quickcarhire.Fragment.ProfileFragment;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class HomeActivity extends Menu {

    private MeowBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (!InternetConnection.isInternetAvailable(this)) {
            InternetConnection.showInternetDialog(this);
        }

        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.show(2, true);

        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.baseline_location_on_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.baseline_car_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.baseline_history_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.baseline_person_24));

        bottomNavigation.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                Fragment fragment;

                if (model.getId() == 1) {
                    fragment = new LocationFragment();
                } else if (model.getId() == 2) {
                    fragment = new HomeFragment();
                } else if (model.getId() == 3) {
                    fragment = new HistoryFragment();
                } else {
                    fragment = new ProfileFragment();
                }

                LoadAndReplace(fragment);
                return null;
            }

        });


    }

    private void LoadAndReplace(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.containerFregment, fragment, null)
                .commit();
    }

}