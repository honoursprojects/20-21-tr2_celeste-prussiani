package com.example.prototypes.CovidTracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.prototypes.CovidTracker.Doctor;

import com.example.prototypes.R;

public class CovidTrackerHome extends AppCompatActivity {
    Intent intent;
    Doctor doctor;
    LinearLayout chatBox;
    EditText inputBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_tracker_home);
        chatBox = (LinearLayout) findViewById(R.id.innerView);
        inputBox = (EditText) findViewById(R.id.inputMessage);
        doctor = new Doctor();
        generateBubble(doctor.greetUser());
    }

    public void askDoctor(View view) {
        String input = inputBox.getText().toString();
        String output = doctor.thinkOfAnswer(input);
        generateBubble(output);

    }

    public void generateBubble(String output) {
        TextView outputBox = new TextView(CovidTrackerHome.this);
        outputBox.setBackgroundColor(getResources().getColor(R.color.pinkHighlight));
        outputBox.setText(output);
        chatBox.addView(outputBox);

    }

}