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
    ArrayList<Integer> reportedSymptoms;
    Boolean alert;

    String[] symptoms = {"Cough", "Fever", "Loss of appetite",
            "Loss of smell", "Loss of taste", "Difficulty breathing"};

    public SymptomTracker() {
        reportedSymptoms = new ArrayList<Integer>();
        alert = true;
    }

    public void addSymptom(int s) {
        if(!reportedSymptoms.contains(s)) {
            reportedSymptoms.add(s);
        }
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
        editor.putString(date, composeString());
        editor.commit();
    }

    public void clearHistory(SharedPreferences pref) {
        SharedPreferences.Editor editor = pref.edit();
        reportedSymptoms.clear();
        editor.clear();
        editor.commit();
    }

    public String composeString() {
        StringBuilder stringBuilder = new StringBuilder();
        String text = "";
        String space = " ";
        String comma = ",";
        String dot = ".";
        for(int s : reportedSymptoms) {
            stringBuilder.append(text).append(printSymptoms(s)).append(comma).append(space).toString();
        }
        stringBuilder.delete(stringBuilder.length()-2, stringBuilder.length());
        stringBuilder.append(dot);
        return stringBuilder.toString();
    }

    public Boolean checkSymptoms() {
        if (reportedSymptoms.contains(1) && reportedSymptoms.contains(2)
                && reportedSymptoms.contains(3) && reportedSymptoms.contains(4)) {
            this.alert = false;
        }
        return alert;
    }

}
