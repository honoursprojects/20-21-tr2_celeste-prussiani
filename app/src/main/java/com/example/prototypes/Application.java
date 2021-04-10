package com.example.prototypes;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.prototypes.stopCovid.StopCovidHome;

import org.greenrobot.eventbus.EventBus;

import static androidx.core.app.NotificationCompat.*;

public class Application extends android.app.Application {


    public static final String BLUETOOTH_CHANNEL_NAME = "warningChannel";
    public final int BLUETOOTH_CHANNEL_ID = 1;
    public static final String CONTACT_CHANNEL_NAME = "contactChannel";
    public final int CONTACT_CHANNEL_ID = 2;
    public static final String TEST_CHANNEL_NAME = "testChannel";
    public final int TEST_CHANNEL_ID = 3;
    public static final String SYMPTOM_CHANNEL_NAME = "symptomChannel";
    public final int SYMPTOM_CHANNEL_ID = 4;
    public final String BLUETOOTH_WARNING = "bluetooth";
    public final String SYMPTOMS_WARNING = "symptoms";
    public final String CONTACT_WARNING = "contact";
    public final String TEST_WARNING = "test";
    private NotificationManagerCompat notificationManager;
    private AlarmManager alarmManager;
    BroadcastReceiver mReceiver;

    public Boolean bluetooth = true;
    public Boolean symptoms = true;
    public Boolean contact = true;
    public Boolean testState = true;
    public String reason = "";
    public Cart cart;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate() {
        super.onCreate();
        checkBluetooth();
        notificationManager = NotificationManagerCompat.from(this);
        createNotificationChannels();
        //fakeContactAlarm();
    }
    /** Getters **/
    public Boolean getBluetoothState() {return bluetooth;}
    public Boolean getSymptomsState() {return symptoms;}
    public Boolean getContactState() {return contact;}
    public Boolean getTestState() {return testState;}

    public String getReason() {
        return this.reason;
    }
    /** Checkers **/
    public void checkSymptoms(Boolean symptoms) {
        if(!symptoms) {
            showNotification(SYMPTOMS_WARNING);
            this.reason = SYMPTOMS_WARNING;
            this.symptoms = false;
            Warning post = new Warning(symptoms, SYMPTOMS_WARNING);
            EventBus.getDefault().post(post);
        }
    }

    public void checkTest(Boolean test) {
        if(!test) {
            showNotification(TEST_WARNING);
            this.reason = TEST_WARNING;
            testState = false;
            Warning post = new Warning(test, TEST_WARNING);
            EventBus.getDefault().post(post);
        }
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
                        reason = BLUETOOTH_WARNING;
                        Warning post = new Warning(bluetooth, BLUETOOTH_WARNING);
                        EventBus.getDefault().post(post);
                    }
                    if(btAdapter.getState() == BluetoothAdapter.STATE_ON) {
                      //  displayToast("Bluetooth on");
                        bluetooth = true;
                        cancelNotification(BLUETOOTH_CHANNEL_ID);
                        Warning post = new Warning(bluetooth, BLUETOOTH_WARNING);
                        EventBus.getDefault().post(post);

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

            NotificationChannel symptom = new NotificationChannel(
                    SYMPTOM_CHANNEL_NAME,
                    "SymptomNotification",
                    NotificationManager.IMPORTANCE_HIGH
            );
            symptom.setDescription("Warning dangerous symptoms");
            manager.createNotificationChannel(symptom);

            NotificationChannel test = new NotificationChannel(
                    TEST_CHANNEL_NAME,
                    "TestNotification",
                    NotificationManager.IMPORTANCE_HIGH
            );
            test.setDescription("Warning test");
            manager.createNotificationChannel(test);
        }
    }

    /**
     * Persistent warning notification
     */
    public void showNotification(String notificationType) {
        String bluetoothMessage = "Please activate bluetooth to activate contact tracing";
        String contactMessage = "Dangerous contact alert";
        String symptomMessage = "You have reported dangerous symptoms. You must self isolate.";
        String testMessage = "You have reported a positive test. You must self isolate.";

        //Define which activity to open when tapping on notification
        Intent activityIntent = new Intent(this, StopCovidHome.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, activityIntent, 0);

        switch(notificationType) {
            case BLUETOOTH_WARNING:
                Notification bluetooth = new NotificationCompat.Builder(this, BLUETOOTH_CHANNEL_NAME)
                        .setSmallIcon(R.drawable.ic_baseline_bluetooth_disabled_24)
                        .setContentTitle("Contact tracing disabled")
                        .setContentText(bluetoothMessage)
                        .setPriority(PRIORITY_HIGH)
                        .setColor(getResources().getColor(R.color.colorDanger))
                        .setCategory(CATEGORY_MESSAGE)
                        .setAutoCancel(false)
                        .setOngoing(true)
                        .setContentIntent(contentIntent)
                        .build();
                notificationManager.notify(BLUETOOTH_CHANNEL_ID, bluetooth);
                break;
            case CONTACT_WARNING:
                Notification contact = new NotificationCompat.Builder(this, CONTACT_CHANNEL_NAME)
                        .setSmallIcon(R.drawable.ic_covid)
                        .setContentTitle("Dangerous contact")
                        .setContentText(contactMessage)
                        .setPriority(PRIORITY_HIGH)
                        .setColor(getResources().getColor(R.color.colorDanger))
                        .setCategory(CATEGORY_MESSAGE)
                        .setAutoCancel(false)
                        .setOngoing(true)
                        .setContentIntent(contentIntent)
                        .build();

                contact.defaults = 0;
                notificationManager.notify(CONTACT_CHANNEL_ID, contact);
                break;
            case TEST_WARNING:
                Notification test = new NotificationCompat.Builder(this, TEST_CHANNEL_NAME)
                        .setSmallIcon(R.drawable.ic_covid)
                        .setContentTitle("Positive test reported")
                        .setContentText(testMessage)
                        .setPriority(PRIORITY_HIGH)
                        .setColor(getResources().getColor(R.color.colorDanger))
                        .setCategory(CATEGORY_MESSAGE)
                        .setAutoCancel(false)
                        .setOngoing(true)
                        .setContentIntent(contentIntent)
                        .build();
                notificationManager.notify(TEST_CHANNEL_ID, test);
                break;
            case SYMPTOMS_WARNING:
                Notification symptoms = new NotificationCompat.Builder(this, SYMPTOM_CHANNEL_NAME)
                     .setSmallIcon(R.drawable.ic_covid)
                    .setContentTitle("Dangerous symptoms")
                    .setContentText(symptomMessage)
                    .setPriority(PRIORITY_HIGH)
                    .setColor(getResources().getColor(R.color.colorDanger))
                    .setCategory(CATEGORY_MESSAGE)
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .setContentIntent(contentIntent)
                    .build();
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

    /**
     * Create a fake notification at a random time

    public void fakeContactAlarm() {
        Random random = new Random();
        int time = random.nextInt();
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() +
                        time*1000, pendingIntent);
    }
*/
}
