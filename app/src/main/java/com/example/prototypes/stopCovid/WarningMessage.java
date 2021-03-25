package com.example.prototypes.stopCovid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.prototypes.R;

public class WarningMessage extends AppCompatActivity {

    public final String BLUETOOTH_WARNING = "bluetooth";
    public final String SYMPTOMS_WARNING = "symptoms";
    public final String CONTACT_WARNING = "contact";
    public final String TEST_WARNING = "test";


    public final String BLUETOOTH_MESSAGE = "bluetooth";
    public final String SYMPTOMS_MESSAGE = "symptoms";
    public final String CONTACT_MESSAGE = "contact";
    public final String TEST_MESSAGE = "test";
    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning_message);
        Bundle bundle = getIntent().getExtras();
        String reason = bundle.getString("reason");
        checkReason(reason);
    }

    public void checkReason(String reason) {
        TextView messageBox = findViewById(R.id.warningMessage);
        switch(reason) {
            case BLUETOOTH_WARNING:
                messageBox.setText(BLUETOOTH_MESSAGE);
                break;
            case SYMPTOMS_WARNING:
                messageBox.setText(SYMPTOMS_MESSAGE);
                break;
            case CONTACT_WARNING:
                messageBox.setText(CONTACT_MESSAGE);
                break;
            case TEST_WARNING:
                messageBox.setText(TEST_MESSAGE);
                break;
        }
    }
    //Turn on bluetooth on button click; close Warning Message activity when completed
    public void turnOnBluetooth(View view) {
        final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter != null)
        {
            btAdapter.enable();
        }
        WarningMessage.this.finish();
    }

    public void closeActivity(View view) {
        WarningMessage.this.finish();
    }
 }