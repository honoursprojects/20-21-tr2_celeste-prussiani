package com.example.prototypes.stopCovid;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.prototypes.Application;
import com.example.prototypes.R;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class SymptomTracker extends AppCompatActivity {
    Intent intent;
    final static String FILE_NAME = "tracker.txt";
    ArrayList<String> symptoms;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_tracker);
        ((Application) getApplicationContext()).checkBluetooth();
        symptoms = new ArrayList<String>();
        loadHistory();
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
            appendHistory(symptom);
        }
    }

    public void appendHistory(String symptom) {

        try {
           FileOutputStream fos = openFileOutput(FILE_NAME, MODE_APPEND);
           String newline = "\n";
           fos.write(newline.getBytes());
           fos.write(symptom.getBytes());
           fos.close();
        } catch (Exception e) {
           e.printStackTrace();
        }
        loadHistory();
    }

    public void clearHistory(View view) {
        try {
            FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            String empty = " ";
            fos.write(empty.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadHistory();
    }


    public void loadHistory() {
        TextView historyBox = (TextView) findViewById(R.id.history);
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
            historyBox.setText(sb.toString());

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                if(fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}