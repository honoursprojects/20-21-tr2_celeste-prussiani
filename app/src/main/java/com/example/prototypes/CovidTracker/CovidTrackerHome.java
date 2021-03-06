package com.example.prototypes.CovidTracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.prototypes.Application;
import com.example.prototypes.DoctorFragment;
import com.example.prototypes.R;
import com.example.prototypes.SectionStatePagerAdapter;
import com.example.prototypes.Warning;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class CovidTrackerHome extends AppCompatActivity {
    Intent intent;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_tracker_home);
        Boolean bluetooth = ((Application) getApplicationContext()).getBluetoothState();
        changeColour(bluetooth);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Subscribe
    public void onMessageEvent(Warning warning) {
        if (warning.getMessage() == "bluetooth") {
            changeColour(warning.getWarning());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void changeColour(Boolean bluetooth) {

        Button tracingBtn = findViewById(R.id.tracingBtn);
        if(!bluetooth) {
            tracingBtn.setText("Contact tracing: OFF");
            tracingBtn.setBackgroundTintList(getResources().getColorStateList(R.color.colorDanger));
            tracingBtn.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
            tracingBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (btAdapter != null) {
                        btAdapter.enable();
                    }
                }
            });
        } else {
            tracingBtn.setText("Bluetooth: ON");
            tracingBtn.setBackgroundTintList(getResources().getColorStateList(R.color.btnColor));
            tracingBtn.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
            tracingBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (btAdapter != null) {
                        btAdapter.disable();
                    }
                }
            });
        }

    }

    public void openSymptomTracker(View view) {
        intent = new Intent(this, CovidTrackerSymptomTracker.class);
        startActivity(intent);
    }

    public void openReportTest(View view) {
        intent = new Intent(this, CovidTrackerReport.class);
        startActivity(intent);
    }

    public void openStore(View view) {
        intent = new Intent(this, CovidTrackerStore.class);
        startActivity(intent);
    }
    public void openChat(View view) {
        intent = new Intent(this, CovidTrackerChat.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}