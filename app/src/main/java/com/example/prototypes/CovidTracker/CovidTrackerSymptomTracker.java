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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        symptoms = new ArrayList<Integer>();
        symptomTracker = new SymptomTracker();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_tracker_symptom_tracker);
    }

    public Integer printSymptom(String id) {
        int symptom = 1;
        switch(id) {
            case "2131230820":
                symptom = 2;
                break;
            case "2131230821":
                symptom = 1;
                break;
            case "2131230822":
                symptom = 5;
                break;
            case "2131230823":
                symptom = 4;
                break;
            case "2131230824":
                symptom = 6;
                break;
            case "2131230825":
                symptom = 3;
                break;
        }
        return symptom;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void saveSymptom(View view) {
        LinearLayout layout = findViewById(view.getId());
        layout.setBackgroundTintList(getResources().getColorStateList(R.color.darkPinkHighlight));
        layout.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
        String symptom = Integer.toString(view.getId());
        symptomTracker.addSymptom(printSymptom(symptom));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void appendHistory(View view) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("covidTracker_preferences", MODE_PRIVATE);
        symptomTracker.saveToHistory(pref);
        revertBg();
    }

    public void revertBg() {
        LinearLayout s1 = findViewById(R.id.btn1);
        LinearLayout s2 = findViewById(R.id.btn2);
        LinearLayout s3 = findViewById(R.id.btn3);
        LinearLayout s4 = findViewById(R.id.btn4);
        LinearLayout s5 = findViewById(R.id.btn5);
        LinearLayout s6 = findViewById(R.id.btn6);

        s1.setBackground(ContextCompat.getDrawable(this, R.drawable.box_shadow));
        s2.setBackground(ContextCompat.getDrawable(this, R.drawable.box_shadow));
        s3.setBackground(ContextCompat.getDrawable(this, R.drawable.box_shadow));
        s4.setBackground(ContextCompat.getDrawable(this, R.drawable.box_shadow));
        s5.setBackground(ContextCompat.getDrawable(this, R.drawable.box_shadow));
        s6.setBackground(ContextCompat.getDrawable(this, R.drawable.box_shadow));
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