package com.example.prototypes.CovidTracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.usage.UsageEvents;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CalendarView;

import com.example.prototypes.R;

import java.util.Map;
import java.util.Set;

public class CalendarActivity extends AppCompatActivity {
    CalendarView calendar;
    Map<String, String> dates;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
    }

    public void highlightEvent() {
        String color = "";
        calendar = findViewById(R.id.calendar);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("covidTracker_preferences", MODE_PRIVATE);
        dates = (Map<String, String>) pref.getAll();
        Set<String> keys = dates.keySet();
        for(String s : keys) {
            if(dates.get(s).length() > 3) {
            } else if(dates.get(s).length() > 5) {

            } else {

            }
        }
    }

}