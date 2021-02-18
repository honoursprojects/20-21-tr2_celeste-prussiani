package com.example.prototypes;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

public class Application extends android.app.Application {

    public Boolean bluetooth = true;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Boolean checkBluetooth() {
        final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        //Check if bluetooth is on on startup
        if (btAdapter == null) {
            //bluetooth does notn exist
        } else {
            if (!btAdapter.isEnabled()) {
                bluetooth = false;
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
                        bluetooth = false;
                    }
                    if(btAdapter.getState() == BluetoothAdapter.STATE_ON) {
                        bluetooth = true;
                    }

                }
            }
        };
        this.registerReceiver(mReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        return bluetooth;
    }

    public void bluetoothChecker(int state, ConstraintLayout bg) {
        int darkYellow = getResources().getColor(R.color.backgroundColor);
        int darkRed = getResources().getColor(R.color.colorDangerDark);
        if(!bluetooth) {
            //Change background to red
            bg.setBackgroundColor(darkRed);
        } else {
            bg.setBackgroundColor(darkYellow);
        }
    }

}
