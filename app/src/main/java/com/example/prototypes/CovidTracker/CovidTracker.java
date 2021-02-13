package com.example.prototypes.CovidTracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.prototypes.R;

public class CovidTracker extends AppCompatActivity {
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_tracker);
    }

    public void goHome(View view) {
        intent = new Intent(this, CovidTrackerHome.class);
        startActivity(intent);
    }
}