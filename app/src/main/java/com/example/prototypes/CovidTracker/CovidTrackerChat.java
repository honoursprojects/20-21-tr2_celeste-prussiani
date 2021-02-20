package com.example.prototypes.CovidTracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototypes.R;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class CovidTrackerChat extends AppCompatActivity {
    private static final String FILE_NAME = "history.txt";

    Intent intent;
    Doctor doctor;
    LinearLayout chatBox;
    EditText inputBox;
    final int DOC = 1;
    final int USER = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_tracker_chat);
        chatBox = (LinearLayout) findViewById(R.id.innerView);
        inputBox = (EditText) findViewById(R.id.inputMessage);
        doctor = new Doctor();
        generateBubble(doctor.greetUser(), DOC);
    }

    public void chatToDoctor(View view) {
        //Get message from user
        String input = inputBox.getText().toString();
        //Display user message in a chat bubble
        generateBubble(input, USER);
        //Get response from doctor library
        String output = doctor.thinkOfAnswer(input);
        //Display doctor message
        generateBubble(output, DOC);
        //Clear input box after message from user has been sent
        inputBox.setText("");
        //Close keyboard
        collapseKeyboard();
    }

    public void generateBubble(String output, int user) {
        //Create and style text bubble
        String id = "bubble";
        TextView outputBox = new TextView(CovidTrackerChat.this);
        outputBox.setBackgroundResource(R.drawable.box_no_shadow);
        outputBox.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
        outputBox.setTextColor(getResources().getColor(R.color.textColour));
        outputBox.setTextSize(18);
        outputBox.setId(Integer.parseInt(id));
        outputBox.setGravity(Gravity.CENTER_VERTICAL);
        outputBox.setPadding(20, 20, 20, 20);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,10,10,10);
        outputBox.setLayoutParams(params);
        outputBox.setText(output);

        //Display different colour and alignment depending on who sends the message
        switch(user) {
            case DOC:
                outputBox.setBackgroundTintList(getResources().getColorStateList(R.color.pinkHighlight));
                break;
            case USER:
                //Align bubble to the right
                outputBox.setBackgroundTintList(getResources().getColorStateList(R.color.darkPinkHighlight));
                params.gravity = Gravity.RIGHT;
                break;
        }
        chatBox.addView(outputBox);
    }

    public void collapseKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

}