package com.example.prototypes;

import android.content.SharedPreferences;
import android.view.View;

import java.io.FileOutputStream;
import java.util.ArrayList;

import static android.content.Context.MODE_APPEND;

public class SymptomTracker {
   final String FILE_NAME = "history.txt";

    ArrayList<String> reportedSymptoms;

    String[] symptoms = {"Cough", "Fever", "Loss of appetite",
            "Loss of smell", "Loss of taste", "Difficulty breathing"};

    public SymptomTracker() {
        reportedSymptoms = new ArrayList<String>();
    }

    public ArrayList<String> getSymptoms() {
        return this.reportedSymptoms;
    }

    public void addSymptom(String[] ids, String symptom) {
        if(!reportedSymptoms.contains(symptom)) {
            reportedSymptoms.add(printSymptom(ids, symptom));
        }
    }

    public void clearSymptoms() {
        this.reportedSymptoms.clear();
    }

    public String printSymptom(String[] ids, String id) {
        String symptom = "";
        if(ids[0].equals(id)) {
            symptom = symptoms[0];
        } else if(ids[1].equals(id)) {
            symptom = symptoms[1];
        } else if(ids[2].equals(id)) {
            symptom = symptoms[2];
        } else if(ids[3].equals(id)) {
            symptom = symptoms[3];
        } else if(ids[4].equals(id)) {
            symptom = symptoms[4];
        } else if(ids[5].equals(id)) {
            symptom = symptoms[5];
        } else {
            symptom = "undefined";
        }
        return symptom;
    }

    public Boolean checkSymptoms(ArrayList<String> newSymptoms) {
        Boolean warning = true;
        if(newSymptoms.contains(symptoms[0]) && newSymptoms.contains(symptoms[1])
                && newSymptoms.contains(symptoms[5])) {
            warning = false;
        } else {
            warning = true;
        }
        return warning;
    }

}
