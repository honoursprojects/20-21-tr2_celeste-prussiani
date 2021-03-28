package com.example.prototypes.stopCovid;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.prototypes.Application;
import com.example.prototypes.R;
import com.example.prototypes.Warning;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class StopCovidContactTracing extends AppCompatActivity {
    Intent intent;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_covid_report);
        Boolean bluetooth = ((Application) getApplicationContext()).getBluetoothState();
        Boolean symptoms = ((Application) getApplicationContext()).getSymptomsState();
        Boolean test = ((Application) getApplicationContext()).getTestState();
        Boolean state = true;
        if(!bluetooth || !symptoms || !test) {
            state = false;
        }
        changeColour(state);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Subscribe to bluetooth listener in Application class
        EventBus.getDefault().register(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Subscribe
    public void onMessageEvent(Warning warning) {
        boolean flag = warning.getWarning();
        if(!flag){
            intent = new Intent(this, WarningMessage.class);
            startActivity(intent);
        }
        changeColour(flag);
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void changeColour(Boolean flag) {
        ConstraintLayout background = findViewById(R.id.background);
        ScrollView wrapperView = findViewById(R.id.wrapperView);
        TextView appLogo = findViewById(R.id.appLogo);
        TextView title = findViewById(R.id.reportTitle);
        TextView subtitle = findViewById(R.id.reportSubtitle);
        ImageView appIcon = findViewById(R.id.appIcon);
        Button reportBtn = findViewById(R.id.reportBtn);
        int darkRed = getResources().getColor(R.color.colorDangerDark);
        int darkYellow = getResources().getColor(R.color.backgroundColor);
        int lightYellow = getResources().getColor(R.color.lightBackgroundColor);
        int textColor = getResources().getColor(R.color.textColour);
        if(!flag) {
            //Change background to red
            background.setBackgroundColor(darkRed);
            wrapperView.setBackgroundColor(darkRed);
            //Logo and text to white
            appLogo.setTextColor(Color.WHITE);
            appIcon.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            title.setTextColor(Color.WHITE);
            subtitle.setTextColor(Color.WHITE);
        } else {
            background.setBackgroundColor(darkYellow);
            wrapperView.setBackgroundColor(lightYellow);
            appLogo.setTextColor(Color.BLACK);
            appIcon.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
            title.setTextColor(textColor);
            subtitle.setTextColor(textColor);
        }
    }

    //Method to report a negative test on click. Can be used to report a positive too
    public void reportTest(View view) {
        boolean test = false;
        ((Application) getApplicationContext()).checkTest(test);
    }

    @Override
    public void onBackPressed() {
        intent = new Intent(this, StopCovidHome.class);
        startActivity(intent);
        finish();
    }

    /**
     * Handle activity close
     */
    @Override
    protected void onStop() {
        super.onStop();
        //Unsubscribe to bluetooth listener
        EventBus.getDefault().unregister(this);
    }
}