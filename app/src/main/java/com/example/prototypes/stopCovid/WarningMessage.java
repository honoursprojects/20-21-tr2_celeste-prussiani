package com.example.prototypes.stopCovid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.prototypes.Application;
import com.example.prototypes.R;

public class WarningMessage extends AppCompatActivity {

    public final String BLUETOOTH_WARNING = "bluetooth";
    public final String SYMPTOMS_WARNING = "symptoms";
    public final String CONTACT_WARNING = "contact";
    public final String TEST_WARNING = "test";

    public final String TEST_MESSAGE = "You have reported a positive test. You MUST self-isolate for at least 10 days. All your contacts have been notified.";
    public final String SYMPTOMS_MESSAGE = "The symptoms you have reported suggest you might have Coronavirus. A test has been booked for you and you'll receive details within 48 hours. You MUST self-isolate until your test comes back negative.";
    public final String BLUETOOTH_MESSAGE = "You have deactivated Contact Tracing. Leaving your home might be dangerous for you and others around you. Please re-activate the feature now.";
    public final String CONTACT_MESSAGE = "You have come in contact with someone with Covid. A test has been booked for you and you'll receive details within 48 hours. You MUST self-isolate until your test comes back negative.";

    @Override
    public void onBackPressed() {
        //Leave empty so user can't escape!
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning_message);

    }

    protected void onStart() {
        super.onStart();

        String reason = ((Application) getApplicationContext()).getReason();
        if(reason == null) {
            reason = "bluetooth";
        }
        checkReason(reason);
    }

    public void checkReason(String reason) {
        Button bluetoothButton = findViewById(R.id.contactTracingBtn);
        TextView messageBox = findViewById(R.id.warningMessage);
        switch(reason) {
            case BLUETOOTH_WARNING:
                messageBox.setText(BLUETOOTH_MESSAGE);
                bluetoothButton.setVisibility(View.VISIBLE);
                break;
            case SYMPTOMS_WARNING:
                messageBox.setText(SYMPTOMS_MESSAGE);
                bluetoothButton.setVisibility(View.GONE);
                break;
            case CONTACT_WARNING:
                messageBox.setText(CONTACT_MESSAGE);
                bluetoothButton.setVisibility(View.GONE);
                break;
            case TEST_WARNING:
                messageBox.setText(TEST_MESSAGE);
                bluetoothButton.setVisibility(View.GONE);
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
        getIntent().putExtra("warning", "not-active");
        WarningMessage.this.finish();
    }

    public void closeActivity(View view) {
        getIntent().putExtra("warning", "not-active");
        WarningMessage.this.finish();
    }
 }