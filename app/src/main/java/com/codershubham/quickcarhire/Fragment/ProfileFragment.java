package com.codershubham.quickcarhire.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.codershubham.quickcarhire.AboutUs;
import com.codershubham.quickcarhire.ClassFile.InternetConnection;
import com.codershubham.quickcarhire.CustomerCareSupport;
import com.codershubham.quickcarhire.LoginActivity;
import com.codershubham.quickcarhire.Profile;
import com.codershubham.quickcarhire.R;

public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    RelativeLayout user, logout, aboutus,careSupport;
    private SharedPreferences sharedPreferences;
    TextView userEmail, username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        if (!InternetConnection.isInternetAvailable(requireContext())) {
            InternetConnection.showInternetDialog(requireContext());
        }

        sharedPreferences = requireActivity().getSharedPreferences("LoginData", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);
        String fullName = sharedPreferences.getString("fullName", null);


        user = view.findViewById(R.id.user);
        username = view.findViewById(R.id.username);
        logout = view.findViewById(R.id.logout);
        userEmail = view.findViewById(R.id.userEmail);
        aboutus = view.findViewById(R.id.aboutus);
        careSupport = view.findViewById(R.id.careSupport);

        userEmail.setText(email);
        username.setText(fullName);

        careSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(requireContext(), CustomerCareSupport.class);
                startActivity(i);
            }
        });
        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(requireContext(), AboutUs.class);
                startActivity(i);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPreferences == null) {
                    sharedPreferences = requireActivity().getSharedPreferences("LoginData", Context.MODE_PRIVATE);
                }
                showConfirmationDialog();
            }
        });
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(requireContext(), Profile.class);
                startActivity(i);
            }
        });

        return view;
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to view your profile?");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                Intent dashboard = new Intent(requireContext(), LoginActivity.class);
                dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(dashboard);

            }
        });


        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
