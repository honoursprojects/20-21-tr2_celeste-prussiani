package com.example.prototypes.stopCovid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.prototypes.R;

public class WarningMessage extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning_message);
    }

    public void closeActivity(View view) {
        WarningMessage.this.finish();
    }
}