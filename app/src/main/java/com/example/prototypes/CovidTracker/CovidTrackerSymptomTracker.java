package com.example.prototypes.CovidTracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.prototypes.R;
import com.example.prototypes.SymptomTracker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CovidTrackerSymptomTracker extends AppCompatActivity {
    Intent intent;
    ArrayList<Integer> symptoms;
    SymptomTracker symptomTracker;
    Boolean saved;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        symptoms = new ArrayList<Integer>();
        symptomTracker = new SymptomTracker();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_tracker_symptom_tracker);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(getSavedStatus()) {
                makeUnclickable();
            }
        }
    }

    public Integer printSymptom(String id) {
        int symptom = 1;
        switch(id) {
            case "2131230824":
                symptom = 1;
                break;
            case "2131230825":
                symptom = 2;
                break;
            case "2131230826":
                symptom = 4;
                break;
            case "2131230827":
                symptom = 5;
                break;
            case "2131230828":
                symptom = 6;
                break;
            case "2131230829":
                symptom = 3;
                break;
        }
        return symptom;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void saveSymptom(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(!getSavedStatus()) {
                System.out.println(view.getId());
                LinearLayout layout = findViewById(view.getId());
                layout.setBackgroundTintList(getResources().getColorStateList(R.color.darkPinkHighlight));
                layout.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
                String symptom = Integer.toString(view.getId());
                symptomTracker.addSymptom(printSymptom(symptom));
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void appendHistory(View view) {
        if(!getSavedStatus()) {
            SharedPreferences pref = getApplicationContext().getSharedPreferences("covidTracker_preferences", MODE_PRIVATE);
            symptomTracker.saveToHistory(pref);
            revertBg();
            makeUnclickable();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void revertBg() {
        LinearLayout s1 = findViewById(R.id.btn1);
        LinearLayout s2 = findViewById(R.id.btn2);
        LinearLayout s3 = findViewById(R.id.btn3);
        LinearLayout s4 = findViewById(R.id.btn4);
        LinearLayout s5 = findViewById(R.id.btn5);
        LinearLayout s6 = findViewById(R.id.btn6);

        s1.setBackgroundTintList(getResources().getColorStateList(R.color.white));
        s1.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
        s2.setBackgroundTintList(getResources().getColorStateList(R.color.white));
        s2.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
        s3.setBackgroundTintList(getResources().getColorStateList(R.color.white));
        s3.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
        s4.setBackgroundTintList(getResources().getColorStateList(R.color.white));
        s4.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
        s5.setBackgroundTintList(getResources().getColorStateList(R.color.white));
        s5.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
        s6.setBackgroundTintList(getResources().getColorStateList(R.color.white));
        s6.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Boolean getSavedStatus() {
        Boolean flag = false;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime dateTime = LocalDateTime.now();
        String date = dateTimeFormatter.format(dateTime);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("covidTracker_preferences", MODE_PRIVATE);
        if(pref.contains(date)) {
           flag = true;
        }
        return flag;

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void makeUnclickable() {
        Button saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setClickable(false);
        saveBtn.setBackgroundTintList(getResources().getColorStateList(R.color.grey));
        saveBtn.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
    }

    public void openChat(View view) {
        intent = new Intent(this, CovidTrackerChat.class);
        startActivity(intent);
    }

    public void openDiary(View view) {
        intent = new Intent(this, CalendarActivity.class);
        startActivity(intent);
    }

}