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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CovidTrackerSymptomTracker extends AppCompatActivity {
    Intent intent;
    ArrayList<String> symptoms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        symptoms = new ArrayList<String>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_tracker_symptom_tracker);
    }

    public void openChat(View view) {
        intent = new Intent(this, CovidTrackerChat.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void saveSymptom(View view) {
        LinearLayout layout = findViewById(view.getId());
        layout.setBackgroundTintList(getResources().getColorStateList(R.color.darkPinkHighlight));
        layout.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
        String symptom = Integer.toString(view.getId());
        System.out.println(symptom);
        if(!symptoms.contains(symptom)) {
            symptoms.add(printSymptom(symptom));
        }
    }

    public String printSymptom(String id) {
        String symptom = "";
        switch(id) {
            case "2131230820":
                symptom = "Cough";
                break;
            case "2131230821":
                symptom = "Fever";
                break;
            case "2131230822":
                symptom = "Loss of appetite";
                break;
            case "2131230823":
                symptom = "Loss of smell";
                break;
            case "2131230824":
                symptom = "Loss of taste";
                break;
            case "2131230825":
                symptom = "Difficulty breathing";
                break;
        }
        return symptom;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void appendHistory(View view) {
        LinearLayout s1 = findViewById(R.id.btn1);
        LinearLayout s2 = findViewById(R.id.btn2);
        LinearLayout s3 = findViewById(R.id.btn3);
        LinearLayout s4 = findViewById(R.id.btn4);
        LinearLayout s5 = findViewById(R.id.btn5);
        LinearLayout s6 = findViewById(R.id.btn6);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime dateTime = LocalDateTime.now();
        String date = dateTimeFormatter.format(dateTime);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        StringBuilder stringBuilder = new StringBuilder();
       // TextView box = findViewById(R.id.history);
        String text = "";
        for(String s : symptoms) {
            stringBuilder.append(text).append("\n").append(s);
        }
        editor.putString(date, text);
        editor.commit();
       // box.setText(stringBuilder.toString());

        s1.setBackground(ContextCompat.getDrawable(this, R.drawable.box_shadow));
        s2.setBackground(ContextCompat.getDrawable(this, R.drawable.box_shadow));
        s3.setBackground(ContextCompat.getDrawable(this, R.drawable.box_shadow));
        s4.setBackground(ContextCompat.getDrawable(this, R.drawable.box_shadow));
        s5.setBackground(ContextCompat.getDrawable(this, R.drawable.box_shadow));
        s6.setBackground(ContextCompat.getDrawable(this, R.drawable.box_shadow));
    }



}