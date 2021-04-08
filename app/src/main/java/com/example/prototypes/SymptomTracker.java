package com.example.prototypes;

import android.app.SharedElementCallback;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;

import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static android.content.Context.MODE_APPEND;

public class SymptomTracker {
   final String FILE_NAME = "history.txt";

    ArrayList<Integer> reportedSymptoms;

    String[] symptoms = {"Cough", "Fever", "Loss of appetite",
            "Loss of smell", "Loss of taste", "Difficulty breathing"};

    public SymptomTracker() {
        reportedSymptoms = new ArrayList<Integer>();
    }

    public void addSymptom(int s) {
        reportedSymptoms.add(s);
    }
    public ArrayList<Integer> getSymptoms() {
        return this.reportedSymptoms;
    }

    public String printSymptoms(int s) {
        return symptoms[s-1];
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveToHistory(SharedPreferences pref) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime dateTime = LocalDateTime.now();
        String date = dateTimeFormatter.format(dateTime);
        SharedPreferences.Editor editor = pref.edit();
        StringBuilder stringBuilder = new StringBuilder();
        String text = "";
        String space = " ";
        for(int s : reportedSymptoms) {
            stringBuilder.append(text).append(space).append(printSymptoms(s)).toString();
        }
        editor.putString(date, stringBuilder.toString());
        editor.commit();
    }

    public void clearHistory(SharedPreferences pref) {
        SharedPreferences.Editor editor = pref.edit();
        reportedSymptoms.clear();
        editor.clear();
        editor.commit();
    }



}
