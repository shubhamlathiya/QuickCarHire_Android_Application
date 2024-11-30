package com.codershubham.quickcarhire;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Menu extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

      if (id == R.id.menu_setting) {
//            Intent dashboard = new Intent(this, Setting.class);
//            startActivity(dashboard);
            return true;
        }else if (id == R.id.menu_logout) {
            if (sharedPreferences == null) {
                sharedPreferences = getSharedPreferences("LoginData", Context.MODE_PRIVATE);
            }
            showConfirmationDialog();
            return true;
        } else if (id == R.id.menu_profile) {
          Intent dashboard = new Intent(this, Profile.class);
            startActivity(dashboard);

      }
        return super.onOptionsItemSelected(item);
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to exit?");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                Intent dashboard = new Intent(getApplicationContext(), LoginActivity.class);
                dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(dashboard);
                finish();
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

