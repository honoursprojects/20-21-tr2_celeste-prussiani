package com.example.prototypes.stopCovid;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;

import com.example.prototypes.Application;
import com.example.prototypes.R;
import com.example.prototypes.SymptomTracker;
import com.example.prototypes.Warning;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class StopCovidSymptomTracker extends AppCompatActivity {
    Intent intent;
    final static String FILE_NAME = "tracker.txt";
    BroadcastReceiver mBroadcastReceiver;
    AlarmManager alarmManager;
    ArrayList<String> symptoms;
    ArrayList<String> reportedSymptoms;
    MutableLiveData<String> listen;

    CheckBox coughCheck;
    CheckBox feverCheck;
    CheckBox tasteCheck;
    CheckBox breathingCheck;
    CheckBox appetiteCheck;
    CheckBox noCheck;
    SymptomTracker symptomTracker;

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_tracker);
        coughCheck = findViewById(R.id.coughCheck);
        feverCheck = findViewById(R.id.feverCheck);
        tasteCheck = findViewById(R.id.tasteCheck);
        breathingCheck = findViewById(R.id.breathingCheck);
        appetiteCheck = findViewById(R.id.appetiteCheck);
        noCheck = findViewById(R.id.noCheck);
        symptoms = new ArrayList<String>();

       symptomTracker = new SymptomTracker();


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
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onMessageEvent(Warning warning) {
        boolean flag = warning.getWarning();
        if(!flag) {
            intent = new Intent(this, WarningMessage.class);
            startActivity(intent);
        }
        changeColour(flag);
    }

    public void goHome(View view) {
        intent = new Intent(this, StopCovidHome.class);
        startActivity(intent);
    }

    public void saveSymptoms(View view) {
        if(coughCheck.isChecked()) {
            symptomTracker.addSymptom(1);
        }
        if(feverCheck.isChecked()) {
            symptomTracker.addSymptom(2);
        }
        if(tasteCheck.isChecked()) {
            symptomTracker.addSymptom(3);
        }
        if(breathingCheck.isChecked()) {
            symptomTracker.addSymptom(4);
        }
        if(appetiteCheck.isChecked()) {
            symptomTracker.addSymptom(5);
        }

        ((Application) getApplicationContext()).checkSymptoms(symptomTracker.checkSymptoms());
        Button logBtn = findViewById(R.id.logBtn);
        logBtn.setClickable(false);
        logBtn.setBackgroundTintList(getResources().getColorStateList(R.color.grey));
        logBtn.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
        saveHistory();
    }

    public void saveHistory() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("stopCovid_preferences", MODE_PRIVATE);
        symptomTracker.saveToHistory(pref);
    }

    //Bluetooth
    //Handle view based on Bluetooth activation
    //Displays yellow view if bluetooth is active, red view if bluetooth is disabled
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"ResourceType", "NewApi"})
    public void changeColour(Boolean bluetooth) {
        ConstraintLayout background = findViewById(R.id.background);
        ScrollView wrapperView = findViewById(R.id.wrapperView);
        TextView appLogo = findViewById(R.id.appLogo);
        TextView title = findViewById(R.id.symptomTitle);
        TextView subtitle = findViewById(R.id.symptomSubtitle);
        Button saveBtn = findViewById(R.id.saveBtn);
        ImageView appIcon = findViewById(R.id.appIcon);
        ImageView contactTracingIcon = findViewById(R.id.contactTracingIcon);
        BottomNavigationView navBar = findViewById(R.id.bottomNav);
        int darkRed = getResources().getColor(R.color.colorDangerDark);
        int darkYellow = getResources().getColor(R.color.backgroundColor);
        int lightYellow = getResources().getColor(R.color.lightBackgroundColor);
        int textColour = getResources().getColor(R.color.textColour);

        if(!bluetooth) {
            //Change background to red
            background.setBackgroundColor(darkRed);
            wrapperView.setBackgroundColor(darkRed);
            //Change logo colour to white
            appLogo.setTextColor(Color.WHITE);
            appIcon.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
            contactTracingIcon.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
            title.setTextColor(Color.WHITE);
            subtitle.setTextColor(Color.WHITE);
            navBar.setBackgroundColor(darkRed);
        } else {
            background.setBackgroundColor(darkYellow);
            wrapperView.setBackgroundColor(lightYellow);
            appLogo.setTextColor(Color.BLACK);
            appIcon.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
            contactTracingIcon.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
            title.setTextColor(textColour);

            navBar.setBackgroundColor(darkYellow);
        }
    }

    @Override
    public void onBackPressed() {
        intent = new Intent(this, StopCovidHome.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void openContactTracing(View view) {
        intent = new Intent(this, StopCovidContactTracing.class);
        startActivity(intent);
        finish();
    }


}