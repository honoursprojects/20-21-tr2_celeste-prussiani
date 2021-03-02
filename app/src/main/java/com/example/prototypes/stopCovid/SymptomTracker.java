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
    Boolean bluetooth;

    final int STATE_ON = 12;
    final int STATE_OFF = 10;
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

     //   bluetooth = ((Application) getApplicationContext()).checkBluetooth(mBroadcastReceiver);

        checkBluetooth();
        symptoms = new ArrayList<String>();
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

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void checkBluetooth() {

        final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        //Check if bluetooth is on on startup
        if (btAdapter == null) {
            //bluetooth does notn exist
        } else {
            if (!btAdapter.isEnabled()) {
                changeColour(STATE_OFF);
            }
        }

        //Activate broadcast receiver; detect when bluetooth is switched off
        final BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();

                // It means the user has changed his bluetooth state.
                if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {

                    if (btAdapter.getState() == BluetoothAdapter.STATE_TURNING_OFF) {
                        // The user bluetooth is turning off yet, but it is not disabled yet.
                        return;
                    }

                    if (btAdapter.getState() == BluetoothAdapter.STATE_OFF) {
                        changeColour(STATE_OFF);
                    }
                    if(btAdapter.getState() == BluetoothAdapter.STATE_ON) {
                        changeColour(STATE_ON);
                    }

                }
            }
        };

        this.registerReceiver(mReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    //Bluetooth
    //Handle view based on Bluetooth activation
    //Displays yellow view if bluetooth is active, red view if bluetooth is disabled
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"ResourceType", "NewApi"})
    public void changeColour(int state) {
        ConstraintLayout background = (ConstraintLayout) findViewById(R.id.background);
        ScrollView wrapperView = (ScrollView) findViewById(R.id.wrapperView);
        LinearLayout innerView = (LinearLayout) findViewById(R.id.innerView);
        TextView appLogo = (TextView) findViewById(R.id.appLogo);
        TextView title = (TextView) findViewById(R.id.title);
        TextView subtitle = (TextView) findViewById(R.id.subtitle);
        TextView history = (TextView) findViewById(R.id.history);
        TextView historyTitle = (TextView) findViewById(R.id.historyTitle);


        switch(state) {
            case STATE_OFF:
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
                break;
            case STATE_ON:
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
                break;
        }
    }
}