package com.example.prototypes.CovidTracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.usage.UsageEvents;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.prototypes.R;

import java.util.Map;
import java.util.Set;

public class CalendarActivity<view> extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        readDiary();
    }

    public void readDiary() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("covidTracker_preferences", MODE_PRIVATE);
        Map<String, String> diary = (Map<String, String>) pref.getAll();
        Set<String> dates = diary.keySet();
        LinearLayout container = findViewById(R.id.diaryContainer);
        if(dates.isEmpty()) {
            TextView empty = new TextView(this);
            empty.setText("No entries found! Have you tried the symptom tracker yet?");
            container.addView(empty);
        } else {
            for(String s : dates) {
                TextView title = new TextView(this);
                title.setTextSize(20);
                title.setText(s);
                TextView entry = new TextView(this);
                entry.setText(pref.getString(s, "DEFAULT"));
                container.addView(title);
                container.addView(entry);
            }
        }
    }

}