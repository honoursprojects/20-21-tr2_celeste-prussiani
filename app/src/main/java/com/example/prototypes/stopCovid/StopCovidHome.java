package com.example.prototypes.stopCovid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.prototypes.R;

public class StopCovidHome extends AppCompatActivity {
    Intent intent;
    final int STATE_ON = 12;
    final int STATE_OFF = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_covid_home);
        checkBluetooth();
    }


    public void checkBluetooth() {

        final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        //Check if bluetooth is on on startup
        if (btAdapter == null) {
            //bluetooth does notn exist
        } else {
            if (!btAdapter.isEnabled()) {
                changeColour(STATE_OFF);
            }
        }

        //Activate broadcast receiver; detect when bluetooth is switched off
        final BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();

                // It means the user has changed his bluetooth state.
                if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {

                    if (btAdapter.getState() == BluetoothAdapter.STATE_TURNING_OFF) {
                        // The user bluetooth is turning off yet, but it is not disabled yet.
                        return;
                    }

                    if (btAdapter.getState() == BluetoothAdapter.STATE_OFF) {
                        changeColour(STATE_OFF);
                    }
                    if(btAdapter.getState() == BluetoothAdapter.STATE_ON) {
                        changeColour(STATE_ON);
                    }

                }
            }
        };

        this.registerReceiver(mReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }

    //Handle view based on Bluetooth activation
    //Displays yellow view if bluetooth is active, red view if bluetooth is disabled
    @SuppressLint("ResourceType")
    public void changeColour(int state) {
        ConstraintLayout background = (ConstraintLayout) findViewById(R.id.background);
        ScrollView wrapperView = (ScrollView) findViewById(R.id.wrapperView);
        LinearLayout innerView = (LinearLayout) findViewById(R.id.innerView);
        TextView title = (TextView) findViewById(R.id.appLogo);

        switch(state) {
            case STATE_OFF:
                intent = new Intent(this, WarningMessage.class);
                startActivity(intent);
                int darkRed = getResources().getColor(R.color.colorDangerDark);
                //Change background to red
                background.setBackgroundColor(darkRed);
                wrapperView.setBackgroundColor(darkRed);
                //Change logo colour to white
                title.setTextColor(Color.WHITE);

                //Add a new textView with a warning. needs to be changed to card
                TextView warning = new TextView(StopCovidHome.this);
                //Set an Id for warning title so it can be deleted on Bluetooth activation
                warning.setId(150);
                warning.setText("WARNING");
                innerView.addView(warning, 0);
                break;
            case STATE_ON:
                int darkYellow = getResources().getColor(R.color.backgroundColor);
                int lightYellow = getResources().getColor(R.color.lightBackgroundColor);
                int textColour = getResources().getColor(R.color.textColour);
                background.setBackgroundColor(darkYellow);
                wrapperView.setBackgroundColor(lightYellow);
                title.setTextColor(textColour);
                if(innerView.getChildAt(0).getId() == 150) {
                    innerView.getChildAt(0).setVisibility(View.GONE);
                }
                break;
        }

    }
}