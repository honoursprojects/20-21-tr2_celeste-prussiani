package com.example.prototypes.stopCovid;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.MutableLiveData;

import com.example.prototypes.Application;
import com.example.prototypes.R;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.O)
public class SymptomTracker extends AppCompatActivity {
    Intent intent;
    final static String FILE_NAME = "tracker.txt";
    BroadcastReceiver mBroadcastReceiver;
    ArrayList<String> symptoms;
    ArrayList<String> reportedSymptoms;
    MutableLiveData<String> listen;
    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_tracker);
       // int state, ConstraintLayout background, ScrollView wrapperView, LinearLayout innerView, TextView title, Context
      //  context
        ConstraintLayout background = (ConstraintLayout) findViewById(R.id.background);
        ScrollView wrapperView = (ScrollView) findViewById(R.id.wrapperView);
        LinearLayout innerView = (LinearLayout) findViewById(R.id.innerView);
        TextView title = (TextView) findViewById(R.id.appLogo);

        symptoms = new ArrayList<String>();
        EventBus.getDefault().register(this);
        Boolean bluetooth = ((Application) getApplicationContext()).getBluetoothState();
        changeColour(bluetooth);
    }

    @Subscribe
    public void onMessageEvent(Boolean bluetooth) {
        changeColour(bluetooth);
    }

    public void goHome(View view) {
        intent = new Intent(this, StopCovidHome.class);
        startActivity(intent);
    }

    public void saveSymptom(View view) {
        String symptom = Integer.toString(view.getId());
        System.out.println(symptom);
        if(!symptoms.contains(symptom)) {
            symptoms.add(symptom);
        }
    }

    public void appendHistory(View view) {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime dateTime = LocalDateTime.now();
        String date = dateTimeFormatter.format(dateTime);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        StringBuilder stringBuilder = new StringBuilder();
        TextView box = findViewById(R.id.history);

        String text = "";
        for(String s : symptoms) {
            editor.putString(date, s);
            stringBuilder.append(text).append("\n").append(s);
        }
        editor.commit();
        box.setText(stringBuilder.toString());
    }

    public void clearHistory(View view) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        symptoms.clear();
        editor.clear();
        editor.commit();
    }


    //Bluetooth
    //Handle view based on Bluetooth activation
    //Displays yellow view if bluetooth is active, red view if bluetooth is disabled
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"ResourceType", "NewApi"})
    public void changeColour(Boolean bluetooth) {
        ConstraintLayout background = (ConstraintLayout) findViewById(R.id.background);
        ScrollView wrapperView = (ScrollView) findViewById(R.id.wrapperView);
        LinearLayout innerView = (LinearLayout) findViewById(R.id.innerView);
        TextView appLogo = (TextView) findViewById(R.id.appLogo);
        TextView title = (TextView) findViewById(R.id.title);
        TextView subtitle = (TextView) findViewById(R.id.subtitle);
        TextView history = (TextView) findViewById(R.id.history);
        TextView historyTitle = (TextView) findViewById(R.id.historyTitle);


        if(!bluetooth) {
            int darkRed = getResources().getColor(R.color.colorDangerDark);
            //Change background to red
            background.setBackgroundColor(darkRed);
            wrapperView.setBackgroundColor(darkRed);
            //Change logo colour to white
            appLogo.setTextColor(Color.WHITE);
            title.setTextColor(Color.WHITE);
            subtitle.setTextColor(Color.WHITE);
            history.setTextColor(Color.WHITE);
            historyTitle.setTextColor(Color.WHITE);
        } else {
            int darkYellow = getResources().getColor(R.color.backgroundColor);
            int lightYellow = getResources().getColor(R.color.lightBackgroundColor);
            int textColour = getResources().getColor(R.color.textColour);
            background.setBackgroundColor(darkYellow);
            wrapperView.setBackgroundColor(lightYellow);
            appLogo.setTextColor(textColour);
            title.setTextColor(textColour);
            subtitle.setTextColor(textColour);
            history.setTextColor(textColour);
            historyTitle.setTextColor(textColour);
        }
    }
}