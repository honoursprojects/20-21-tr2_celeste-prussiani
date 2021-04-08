package com.example.prototypes.stopCovid;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.BlendMode;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.prototypes.Application;
import com.example.prototypes.R;
import com.example.prototypes.Warning;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class StopCovidHome extends AppCompatActivity {
    Intent intent;
    BarChart barchart;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_covid_home);
        //Check bluetooth state from application
        Boolean bluetooth = ((Application) getApplicationContext()).getBluetoothState();
        Boolean symptoms = ((Application) getApplicationContext()).getSymptomsState();
        Boolean test = ((Application) getApplicationContext()).getTestState();
        //Change colour accordingly
        Boolean state = true;
        if(!bluetooth || !symptoms || !test) {
            state = false;
        }
        changeColour(state);

        //Create a barchart
        createChart();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStart() {
        super.onStart();
        //Subscribe to bluetooth listener in Application class
        EventBus.getDefault().register(this);
        checkSymptomsLogged();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Subscribe
    public void onMessageEvent(Warning warning) {
        boolean flag = warning.getWarning();
        String msg = warning.getMessage();
        //Change colour when bluetooth changes
        if(msg.equals("bluetooth") && !flag) {
            intent = new Intent(this, WarningMessage.class);
            startActivity(intent);
        }
        changeColour(flag);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void checkSymptomsLogged() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime dateTime = LocalDateTime.now();
        String date = dateTimeFormatter.format(dateTime);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("stopCovid_preferences", MODE_PRIVATE);
        TextView msg = findViewById(R.id.symptomsMsg);
        LinearLayout symptomsView = findViewById(R.id.symptomsView);
        ImageView icon = findViewById(R.id.symptomsStatusIcon);
        if(pref.contains(date)) {
            msg.setText("Logged symptoms for today");
            icon.setColorFilter(ContextCompat.getColor(this, R.color.green), PorterDuff.Mode.SRC_IN);
            //symptomsView.setClickable(false);
        } else {
            msg.setText("Symptoms not logged. Tap here to open Symptom Tracker");
            icon.setMinimumWidth(20);
            icon.setColorFilter(ContextCompat.getColor(this, R.color.colorDanger), PorterDuff.Mode.SRC_IN);
        }
    }

    //Handle view based on Bluetooth activation
    //Displays yellow view if bluetooth is active, red view if bluetooth is disabled
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceType")
    public void changeColour(Boolean bluetooth) {
        String reason = ((Application) getApplicationContext()).getReason();
        ConstraintLayout background = findViewById(R.id.background);
        ScrollView wrapperView = findViewById(R.id.wrapperView);
        LinearLayout contactTracingView = findViewById(R.id.contactTracingView);
        TextView contactTracingMsg = findViewById(R.id.contactTracingMsg);
        TextView title = findViewById(R.id.appLogo);
        ImageView appIcon = findViewById(R.id.appIcon);
        ImageView statusIcon = findViewById(R.id.contactTracingStatusIcon);
        ImageView contactTracingIcon = findViewById(R.id.contactTracingIcon);
        BottomNavigationView navBar = findViewById(R.id.bottomNav);
        int darkRed = getResources().getColor(R.color.colorDangerDark);
        int darkYellow = getResources().getColor(R.color.backgroundColor);
        int lightYellow = getResources().getColor(R.color.lightBackgroundColor);
        int textColour = getResources().getColor(R.color.textColour);

        if(!bluetooth) {
            //Change background to red
            background.setBackgroundColor(darkRed);
            wrapperView.setBackgroundColor(darkRed);
            //Change logo colour to white
            title.setTextColor(Color.WHITE);
            appIcon.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
            contactTracingIcon.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
            navBar.setBackgroundColor(darkRed);
            if(reason.equals("bluetooth")) {
                contactTracingMsg.setText("Contact Tracing: NOT ACTIVE");
                statusIcon.setColorFilter(ContextCompat.getColor(this, R.color.colorDanger), PorterDuff.Mode.SRC_IN);
                contactTracingView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
                        if (btAdapter != null) {
                            btAdapter.enable();
                        }
                    }
                });

           }
        } else {
            background.setBackgroundColor(darkYellow);
            wrapperView.setBackgroundColor(lightYellow);
            title.setTextColor(textColour);
            appIcon.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
            contactTracingIcon.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
            contactTracingMsg.setTextColor(textColour);
            contactTracingMsg.setText("Contact tracing: active");
            statusIcon.setColorFilter(ContextCompat.getColor(this, R.color.green), PorterDuff.Mode.SRC_IN);
            navBar.setBackgroundColor(darkYellow);
        }
    }

    /**
     * Displays a barchart created with MPAndroid Chart library.
     */
    public void displayChart() {
        //Get a barchart box
        barchart = findViewById(R.id.barChart_view);
        ArrayList<Double> valueList = new ArrayList<Double>();
        ArrayList<BarEntry> entries = new ArrayList<>();
        String title = "Cases";

        //Input data
        for(int i = 0; i < 6; i++) {
            valueList.add(i*100.1);
        }

        //fit data into a bar
        for(int i = 0; i<valueList.size(); i++) {
            BarEntry barEntry = new BarEntry(i, valueList.get(i).floatValue());
            entries.add(barEntry);
        }

        BarDataSet barDataSet = new BarDataSet(entries, title);

        BarData data = new BarData(barDataSet);
        barchart.setData(data);
        barchart.invalidate();
        styleChart(barDataSet);
    }

    /**
     * Styles a barchart created with MPAndroid Chart library.
     */
    public void styleChart(BarDataSet barDataSet) {
        //Change chart colour
        barDataSet.setColor(ContextCompat.getColor(this, R.color.colorDanger));
        //Set size
        barDataSet.setFormSize(15);
        //Set line appearance
        barDataSet.setDrawValues(false);
        //Set text size
        barDataSet.setValueTextSize(12f);
    }

    /**
     * Creates a barchart with MPAndroid Chart library.
     */
    public void createChart() {
        //Find graph box
        barchart = findViewById(R.id.barChart_view);
        //Style chart
        barchart.setDrawGridBackground(false);
        barchart.setDrawBarShadow(false);
        barchart.setDrawBorders(false);
        //Hide description
        Description description = new Description();
        description.setEnabled(false);
        barchart.setDescription(description);
        //Hide grid lines
        XAxis xAxis = barchart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        YAxis yAxisRight = barchart.getAxisRight();
        yAxisRight.setEnabled(false);
        //Styl legend
        Legend legend = barchart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(11f);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);

        displayChart();
    }

    /** ROUTE TO DIFFERENT APP SECTION **/

    /**
     * Allows navigation to SymptomTracker.
     */
    public void openSymptomTracker(View view) {
        intent = new Intent(this, StopCovidSymptomTracker.class);
        startActivity(intent);
        finish();
    }

    public void openContactTracing(View view) {
        intent = new Intent(this, StopCovidContactTracing.class);
        startActivity(intent);
        finish();
    }

    /**
     * Handle activity close
     */
    @Override
    protected void onStop() {
        super.onStop();
        //Unsubscribe to bluetooth listener
        EventBus.getDefault().unregister(this);
    }
}