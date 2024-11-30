package com.codershubham.quickcarhire.ClassFile;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.TextView;

import com.codershubham.quickcarhire.R;

public class InternetConnection {

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    public static void showInternetDialog(Context context) {
        Dialog customDialog = new Dialog(context);
        customDialog.setContentView(R.layout.internet_dialog);

        TextView btnOK = customDialog.findViewById(R.id.retrybtn);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
            }
        });
        customDialog.show();
    }
}
