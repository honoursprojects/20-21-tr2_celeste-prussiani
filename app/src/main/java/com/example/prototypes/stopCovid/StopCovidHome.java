package com.example.prototypes.stopCovid;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
        //Change colour accordingly
        Boolean state = true;
        if(!bluetooth || !symptoms) {
            state = false;
            intent = new Intent(this, WarningMessage.class);
            startActivity(intent);
        }
        changeColour(state);
        //Create a barchart
        createChart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Subscribe to bluetooth listener in Application class
        EventBus.getDefault().register(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Subscribe
    public void onMessageEvent(Boolean bluetooth) {
        //Change colour when bluetooth changes
        if(!bluetooth){
            intent = new Intent(this, WarningMessage.class);
            startActivity(intent);
        }
        changeColour(bluetooth);
    }


    //Handle view based on Bluetooth activation
    //Displays yellow view if bluetooth is active, red view if bluetooth is disabled
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceType")
    public void changeColour(Boolean bluetooth) {
        ConstraintLayout background = (ConstraintLayout) findViewById(R.id.background);
        ScrollView wrapperView = (ScrollView) findViewById(R.id.wrapperView);
        LinearLayout innerView = (LinearLayout) findViewById(R.id.innerView);
        TextView title = (TextView) findViewById(R.id.appLogo);
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

            //Add a new textView with a warning. needs to be changed to box_shadow
            TextView warning = new TextView(StopCovidHome.this);
            //Set an Id for warning title so it can be deleted on Bluetooth activation
            warning.setId(150);
            warning.setText("WARNING");
            warning.setBackgroundResource(R.drawable.box_shadow);
            warning.setTextColor(Color.WHITE);
            warning.setTypeface(Typeface.DEFAULT_BOLD);
            warning.setGravity(Gravity.CENTER);
            warning.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
            warning.setBackgroundTintList(getResources().getColorStateList(R.color.colorDanger));
            warning.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (btAdapter != null) {
                        btAdapter.enable();
                    }
                }
            });
            innerView.addView(warning, 0);

        } else {
                background.setBackgroundColor(darkYellow);
                wrapperView.setBackgroundColor(lightYellow);
                title.setTextColor(textColour);
                if(innerView.getChildAt(0).getId() == 150) {
                    innerView.getChildAt(0).setVisibility(View.GONE);
                }
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