package com.example.prototypes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.prototypes.CovidTracker.CovidTracker;
import com.example.prototypes.stopCovid.StopCovid;

public class MainActivity extends AppCompatActivity {
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openStopCovid(View view) {
        intent = new Intent(this, StopCovid.class);
        startActivity(intent);
    }

    public void openCovidTracker(View view) {
        intent = new Intent(this, CovidTracker.class);
        startActivity(intent);
    }

}