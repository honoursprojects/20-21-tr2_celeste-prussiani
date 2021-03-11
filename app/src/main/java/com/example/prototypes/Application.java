package com.example.prototypes;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.example.prototypes.stopCovid.WarningMessage;

import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;

public class Application extends android.app.Application {

    public static final String WARNING_CHANNEL_NAME = "warningChannel";
    public final int WARNING_CHANNEL_ID = 1;
    private NotificationManagerCompat notificationManager;

    BroadcastReceiver mReceiver;
    public Boolean bluetooth = true;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate() {
        super.onCreate();
        checkBluetooth();
        notificationManager = NotificationManagerCompat.from(this);
        createNotificationChannels();
    }

    public void checkSymptoms(ArrayList<String> newSymptoms) {
        Boolean warning = true;
        if(newSymptoms.contains("Cough") && newSymptoms.contains("Fever")
                && newSymptoms.contains("Difficulty breathing")) {
            warning = false;
        } else {
            warning = true;
        }
        EventBus.getDefault().post(warning);
    }


    public Boolean getBluetoothState() {
        return bluetooth;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Boolean checkBluetooth() {
        final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        //Check if bluetooth is on on startup
        if (btAdapter == null) {
            //bluetooth does not exist
        } else {
            if (!btAdapter.isEnabled()) {
                bluetooth = false;
            }
        }

        //Activate broadcast receiver; detect when bluetooth is switched off
        mReceiver = new BroadcastReceiver() {
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
                      //  displayToast("Bluetooth off");
                        bluetooth = false;
                        showNotification();
                        EventBus.getDefault().post(bluetooth);
                    }
                    if(btAdapter.getState() == BluetoothAdapter.STATE_ON) {
                      //  displayToast("Bluetooth on");
                        bluetooth = true;
                        cancelNotification(1);
                        EventBus.getDefault().post(bluetooth);
                    }

                }
            }
        };
        this.registerReceiver(mReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        return bluetooth;
    }

    public void displayToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void createNotificationChannels() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel warning = new NotificationChannel(
                    WARNING_CHANNEL_NAME,
                    "WarningNotification",
                    NotificationManager.IMPORTANCE_HIGH
            );
            warning.setDescription("Warning no bluetooth on");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(warning);
        }
    }

    public void showNotification() {
        String message = "Please activate bluetooth to activate contact tracing";
        //Define which activity to open when tapping on notification
        Intent activityIntent = new Intent(this, WarningMessage.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, activityIntent, 0);

        Notification warning = new NotificationCompat.Builder(this, WARNING_CHANNEL_NAME)
                .setSmallIcon(R.drawable.ic_baseline_bluetooth_disabled_24)
                .setContentTitle("Contact tracing disabled")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(Color.WHITE)
                .setColor(getResources().getColor(R.color.colorDanger))
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(false)
                .setOngoing(true)
                .setContentIntent(contentIntent)
                .build();

        //Display notification
        notificationManager.notify(WARNING_CHANNEL_ID, warning);
    }

    public void cancelNotification(int id) {
        notificationManager.cancel(id);
    }
}
