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

    public static final String BLUETOOTH_CHANNEL_NAME = "warningChannel";
    public final int BLUETOOTH_CHANNEL_ID = 1;
    public static final String CONTACT_CHANNEL_NAME = "contactChannel";
    public final int CONTACT_CHANNEL_ID = 2;
    public static final String SYMPTOM_CHANNEL_NAME = "symptomChannel";
    public final int SYMPTOM_CHANNEL_ID = 3;
    public final String BLUETOOTH_WARNING = "bluetooth";
    public final String SYMPTOMS_WARNING = "symptoms";
    public final String CONTACT_WARNING = "contact";

    private NotificationManagerCompat notificationManager;
    BroadcastReceiver mReceiver;

    public Boolean bluetooth = true;
    public Boolean symptoms = true;
    public Boolean contact = true;
    public Boolean test = true;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate() {
        super.onCreate();
        checkBluetooth();
        notificationManager = NotificationManagerCompat.from(this);
        createNotificationChannels();
    }
    /** Getters **/
    public Boolean getBluetoothState() {return bluetooth;}
    public Boolean getSymptomsState() {return symptoms;}
    public Boolean getContactState() {return contact;}
    public Boolean getTestState() {return test;}

    /** Checkers **/
    public void checkSymptoms(ArrayList<String> newSymptoms) {
        if(newSymptoms.contains("Cough") && newSymptoms.contains("Fever")
                && newSymptoms.contains("Breathing") && newSymptoms.contains("Taste")) {
            symptoms = false;
        } else {
            symptoms = true;
        }
        Warning post = new Warning(symptoms, SYMPTOMS_WARNING);
        EventBus.getDefault().post(symptoms);
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
                        System.out.println("BLUETOOTH OFF");
                        showNotification(BLUETOOTH_WARNING);
                       // Warning post = new Warning(bluetooth, BLUETOOTH_WARNING);
                        EventBus.getDefault().post(bluetooth);
                    }
                    if(btAdapter.getState() == BluetoothAdapter.STATE_ON) {
                      //  displayToast("Bluetooth on");
                        bluetooth = true;
                        cancelNotification(BLUETOOTH_CHANNEL_ID);
                      //  Warning post = new Warning(bluetooth, BLUETOOTH_WARNING);
                        EventBus.getDefault().post(bluetooth);

                        System.out.println("BLUETOOTH ON");
                    }

                }
            }
        };
        this.registerReceiver(mReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        return bluetooth;
    }

    /** Notifications **/

    /**
     * Create notification channels
     */
    private void createNotificationChannels() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel warning = new NotificationChannel(
                    BLUETOOTH_CHANNEL_NAME,
                    "WarningNotification",
                    NotificationManager.IMPORTANCE_HIGH
            );
            warning.setDescription("Warning no bluetooth on");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(warning);

            NotificationChannel contact = new NotificationChannel(
                    CONTACT_CHANNEL_NAME,
                    "ContactNotification",
                    NotificationManager.IMPORTANCE_HIGH
            );
            contact.setDescription("Warning dangerous contact");
            manager.createNotificationChannel(contact);
        }
    }

    /**
     * Persistent warning notification
     */
    public void showNotification(String notificationType) {
        String bluetoothMessage = "Please activate bluetooth to activate contact tracing";
        String contactMessage = "You have been in contact with someone with Covid. You must self isolate now";
        String symptomMessage = "You have reported dangerous symptoms. We advice self isolation";

        //Define which activity to open when tapping on notification
        Intent activityIntent = new Intent(this, WarningMessage.class); //probably post a message to warning activity here
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, activityIntent, 0);

        Notification warning = new NotificationCompat.Builder(this, BLUETOOTH_CHANNEL_NAME)
                .setSmallIcon(R.drawable.ic_baseline_bluetooth_disabled_24)
                .setContentTitle("Contact tracing disabled")
                .setContentText(bluetoothMessage)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(Color.WHITE)
                .setColor(getResources().getColor(R.color.colorDanger))
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(false)
                .setOngoing(true)
                .setContentIntent(contentIntent)
                .build();

        Notification contact = new NotificationCompat.Builder(this, CONTACT_CHANNEL_NAME)
                .setSmallIcon(R.drawable.ic_baseline_bluetooth_disabled_24)
                .setContentTitle("Dangerous contact")
                .setContentText(symptomMessage)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(getResources().getColor(R.color.colorDanger))
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(false)
                .setOngoing(true)
                .setContentIntent(contentIntent)
                .build();

        Notification symptoms = new NotificationCompat.Builder(this, SYMPTOM_CHANNEL_NAME)
                .setSmallIcon(R.drawable.ic_baseline_bluetooth_disabled_24)
                .setContentTitle("Dangerous symptoms")
                .setContentText(contactMessage)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(getResources().getColor(R.color.colorDanger))
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(false)
                .setOngoing(true)
                .setContentIntent(contentIntent)
                .build();

        //Display notification
        switch(notificationType) {
            case BLUETOOTH_WARNING:
                notificationManager.notify(BLUETOOTH_CHANNEL_ID, warning);
                break;
            case CONTACT_WARNING:
                notificationManager.notify(CONTACT_CHANNEL_ID, contact);
                break;
            case SYMPTOMS_WARNING:
                notificationManager.notify(SYMPTOM_CHANNEL_ID, symptoms);
                break;

        }
    }

    /**
     * Cancel notifications
     * @param id - channel id
     */
    public void cancelNotification(int id) {
        notificationManager.cancel(id);
    }
}
