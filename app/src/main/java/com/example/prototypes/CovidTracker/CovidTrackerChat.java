package com.example.prototypes.CovidTracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.prototypes.R;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class CovidTrackerChat extends AppCompatActivity {
    Intent intent;
    Doctor doctor;
    LinearLayout chatBox;
    EditText inputBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_tracker_chat);
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
        //Create and style text bubble
        TextView outputBox = new TextView(CovidTrackerChat.this);
        outputBox.setBackgroundResource(R.drawable.box_no_shadow);
        outputBox.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
        outputBox.setBackgroundTintList(getResources().getColorStateList(R.color.pinkHighlight));
        outputBox.setTextColor(getResources().getColor(R.color.textColour));
        outputBox.setPadding(20, 20, 20, 20);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,10,10,10);
        outputBox.setLayoutParams(params);
        outputBox.setText(output);
        chatBox.addView(outputBox);
    }

}