package com.example.prototypes.stopCovid;

import androidx.appcompat.app.AppCompatActivity;
import com.example.prototypes.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SymptomTracker extends AppCompatActivity {
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_tracker);
    }

    public void goHome(View view) {
        intent = new Intent(this, StopCovidHome.class);
        startActivity(intent);
    }
}