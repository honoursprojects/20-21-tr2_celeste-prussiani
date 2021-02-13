package com.example.prototypes.stopCovid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.prototypes.R;

public class StopCovid extends AppCompatActivity {

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_covid);
    }

    public void goHome(View view) {
        intent = new Intent(this, StopCovidHome.class);
        startActivity(intent);
    }


}