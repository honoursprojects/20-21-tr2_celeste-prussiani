package com.example.prototypes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.prototypes.CovidTracker.CovidTracker;
import com.example.prototypes.stopCovid.StopCovid;

public class Home extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void openStopCovid(View view) {
        intent = new Intent(this, StopCovid.class);
        startActivity(intent);
    }

    public void openCovidTracker(View view) {
        intent = new Intent(this, CovidTracker.class);
        startActivity(intent);
    }

    public void onBackPressed() {
        new AlertDialog.Builder(Home.this)
                .setTitle("Close the app")
                .setMessage("Do you really want to exit?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.finishAffinity(Home.this);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

}