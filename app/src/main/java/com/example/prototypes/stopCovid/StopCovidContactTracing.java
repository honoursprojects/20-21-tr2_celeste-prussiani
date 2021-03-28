package com.example.prototypes.stopCovid;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;

import com.example.prototypes.Application;
import com.example.prototypes.R;
import com.example.prototypes.Warning;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class StopCovidContactTracing extends AppCompatActivity {
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_covid_report);
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
        //Change colour when bluetooth changes
        if(!flag){
            intent = new Intent(this, WarningMessage.class);
            intent.putExtra("reason", warning.getMessage());
            startActivity(intent);
        }
        changeColour(flag);
    }

    public void changeColour(Boolean flag) {
        ConstraintLayout background = (ConstraintLayout) findViewById(R.id.background);
        ScrollView wrapperView = (ScrollView) findViewById(R.id.wrapperView);
        int darkRed = getResources().getColor(R.color.colorDangerDark);
        int darkYellow = getResources().getColor(R.color.backgroundColor);
        int lightYellow = getResources().getColor(R.color.lightBackgroundColor);
        if(!flag) {
            //Change background to red
            background.setBackgroundColor(darkRed);
            wrapperView.setBackgroundColor(darkRed);
        } else {
            background.setBackgroundColor(darkYellow);
            wrapperView.setBackgroundColor(lightYellow);
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

    public void goBack(View view) {
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