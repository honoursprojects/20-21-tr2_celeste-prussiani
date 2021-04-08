package com.example.prototypes.CovidTracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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

import com.example.prototypes.R;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class CovidTrackerChat extends AppCompatActivity {
    Intent intent;
    Doctor doctor;
    LinearLayout chatBox;
    EditText inputBox;

    final int DOC = 1;
    final int USER = 2;
    final String SYMPTOM_FLAG = "symptom";
    final String STORE_FLAG = "store";

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

        if(output.equals(SYMPTOM_FLAG)) {
            output = "Keeping track of your health is a great way to keep yourself safe and prevent the spread. Here is where to find the symptom tracker:";
            generateBubble(output, DOC);
            generateButton(output);
        } else if (output.equals(STORE_FLAG)) {
            output = "We have collected the best medically certified PPE in our store. Here's where to find it:";
            generateBubble(output, DOC);
            generateButton(output);
        } else {
            //Display doctor message
            generateBubble(output, DOC);
        }

        //Clear input box after message from user has been sent
        inputBox.setText("");
        //Close keyboard
        collapseKeyboard();
    }

    public void generateButton(String activity) {

        Button btn = new Button(CovidTrackerChat.this);
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


        if(activity.equals(SYMPTOM_FLAG)) {
            btn.setText("symptom tracker");
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openSymptomTracker(v);
                }
            });
        } else {
            btn.setText("PPE store");
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openStore(v);
                }
            });
        }

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
        params.setMargins(10,10,10, 0);
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
        intent = new Intent(this, CovidTrackerSymptomTracker.class);
        startActivity(intent);
    }

    public void openStore(View view) {
        intent = new Intent(this, CovidTrackerStore.class);
        startActivity(intent);
    }
}