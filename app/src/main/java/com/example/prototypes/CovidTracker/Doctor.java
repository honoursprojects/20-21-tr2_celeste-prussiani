package com.example.prototypes.CovidTracker;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.prototypes.R;
import com.example.prototypes.stopCovid.StopCovidSymptomTracker;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import static androidx.core.content.ContextCompat.startActivity;

public class Doctor {

    private HashMap<String, String> library;
    public Intent intent;

    public Doctor() {
        library = new HashMap<String, String>();
        generateLibrary(library);
    }

    public void generateLibrary(HashMap<String, String> library) {
        String welcome = "Hello, I am your virtual doctor. How can I help you today?";
        String regulations = "The current COVID-19 regulations in the UK are: do not leave home except for essential purposes, adhere to social distancing" +
                ", wear face masks where possible.";
        String friends = "You can meet with up to one person outside, but you have to adhere to social distancing. There are a few excpetions regarding affirmed couples," +
                " children of divorced parents and people needing care.";
        String masks = "You must wear face masks indoors whenever possible. The correct face masks for your own health and safety and others' are surgical masks" +
                "or FP2 masks. You can find them and other PPE here";
        String gloves = "Gloves are recommended, but did you know thorough hand washing is much better? If you need them, you can get some gloves from the store; for more information about PPE, go here:";
        String not_found = "Sorry, I'm afraid I' don't understand the question. Could you try and rephrase?";
        String cases = "There are currently " + R.string.cases_no + " cases in the UK. The current R number is";
        String symptomTracker = "symptom";
        String store = "store";

        library.put("regulations", regulations);
        library.put("rules", regulations);
        library.put("restrictions", regulations);
        library.put("meet", friends);
        library.put("friends", friends);
        library.put("gloves", gloves);
        library.put("mask", masks);
        library.put("masks", masks);
        library.put("ppe", masks);
        library.put("error", not_found);
        library.put("welcome", welcome);
        library.put("cases", cases);
        library.put("deaths", cases);
        library.put("symptom", symptomTracker);
        library.put("symptoms", symptomTracker);
        library.put("store", store);
        library.put("buy", store);
        library.put("unwell", symptomTracker);
    }

    public String greetUser() {
        return library.get("welcome").toString();
    }

    public String thinkOfAnswer(String msg) {
        String result = "";
        String output = "";
        String[] words = msg.split(" ");
        for(String w : words) {
            if(w.equals("buy")) {
                result = library.get(w).toString();
                break;
            }
            if(library.containsKey(w)) {
                result = library.get(w).toString();
            }
        }
        if(result.equals("")) {
            output = library.get("error");
        } else {
            output = result;
        }
        return output;
    }


}
