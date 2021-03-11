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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototypes.R;
import com.example.prototypes.stopCovid.StopCovidSymptomTracker;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class CovidTrackerChat extends AppCompatActivity {
    private static final String FILE_NAME = "chat.txt";

    Intent intent;
    Doctor doctor;
    LinearLayout chatBox;
    EditText inputBox;
    final int DOC = 1;
    final int USER = 2;
    final String FLAG = "activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_tracker_chat);
        chatBox = (LinearLayout) findViewById(R.id.innerView);
        inputBox = (EditText) findViewById(R.id.inputMessage);
        doctor = new Doctor();
      //  generateChat();
        generateBubble(doctor.greetUser(), DOC);
    }

    public void chatToDoctor(View view) {
        StringBuilder sb = new StringBuilder();
        //Get message from user
        String input = inputBox.getText().toString();
        //Display user message in a chat bubble
        generateBubble(input, USER);
        //Get response from doctor library
        String output = doctor.thinkOfAnswer(input);
        //Display doctor message
        generateBubble(output, DOC);
        if(output == FLAG) {
            generateButton();
        }
        //Clear input box after message from user has been sent
        inputBox.setText("");
        //Close keyboard
        collapseKeyboard();
    }

    public void generateButton() {
        Button btn = new Button(CovidTrackerChat.this);
        btn.setText("Open symptom tracker");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSymptomTracker(v);
            }
        });
        btn.setBackgroundResource(R.drawable.box_shadow);
        btn.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
        btn.setTextColor(getResources().getColor(R.color.lightBackground));
        btn.setBackgroundTintList(getResources().getColorStateList(R.color.btnColor));
        btn.setTextSize(18);
        btn.setGravity(Gravity.CENTER_VERTICAL);
        btn.setPadding(20, 10, 20, 20);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,10,10,10);
        btn.setLayoutParams(params);
        chatBox.addView(btn);
    }

    public void generateBubble(String output, int user) {
        //Create and style text bubble
        TextView outputBox = new TextView(CovidTrackerChat.this);
        outputBox.setBackgroundResource(R.drawable.box_no_shadow);
        outputBox.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
        outputBox.setTextColor(getResources().getColor(R.color.textColour));
        outputBox.setTextSize(18);
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

    /**
     * Allows navigation to SymptomTracker.
     */
    public void openSymptomTracker(View view) {
        intent = new Intent(this, StopCovidSymptomTracker.class);
        startActivity(intent);
    }
/*
    public void appendChat(String message, int user) {
        StringBuilder sb = new StringBuilder();
        try {
            FileOutputStream fos = openFileOutput(FILE_NAME, MODE_APPEND);
            String text = "";
            String newline = "\n";
            String docSignature = "@doc";
            String userSignature = "@user";

            switch(user) {
                case DOC:
                    sb.append(text).append(newline).append(message).append(docSignature);
                    fos.write(text.getBytes());
                    break;
                case USER:
                    sb.append(text).append(newline).append(message).append(userSignature);
                    fos.write(text.getBytes());
                    break;
            }
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> loadChatFile() {
        ArrayList<String> sentMessages = new ArrayList<String>();
        FileInputStream fis = null;
        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sentMessages;
    }

    public void generateChat() {
        ArrayList<String> messages = loadChatFile();
        for(String m : messages) {
            String[] split = m.split("@");
            if(split[1].equals("doc")) {
                generateBubble(split[0], DOC);
            } else if(split[1].equals("user")) {
                generateBubble(split[0], USER);
            }
        }
    }
*/
}