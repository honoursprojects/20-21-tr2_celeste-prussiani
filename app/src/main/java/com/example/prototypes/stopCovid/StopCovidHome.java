package com.example.prototypes.stopCovid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.prototypes.R;

public class StopCovidHome extends AppCompatActivity {
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_covid_home);
    }

    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }

    public void changeColour(View view) {
        intent = new Intent(this, WarningMessage.class);
        startActivity(intent);

        ConstraintLayout background = (ConstraintLayout) findViewById(R.id.background);
        ScrollView wrapperView = (ScrollView) findViewById(R.id.wrapperView);
        LinearLayout innerView = (LinearLayout) findViewById(R.id.innerView);
        TextView title = (TextView) findViewById(R.id.appLogo);

        int darkRed = getResources().getColor(R.color.colorDangerDark);
        //Change background to red
        background.setBackgroundColor(darkRed);
        wrapperView.setBackgroundColor(darkRed);
        //Change logo colour to white
        title.setTextColor(Color.WHITE);

        //Add a new textView with a warning. needs to be changed to card
        TextView warning = new TextView(StopCovidHome.this);
        warning.setText("WARNING");

        innerView.addView(warning, 0);
    }
}