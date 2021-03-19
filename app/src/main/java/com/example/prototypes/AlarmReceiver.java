package com.example.prototypes;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.prototypes.stopCovid.WarningMessage;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String CONTACT_CHANNEL_NAME = "contactChannel";
    public final int CONTACT_CHANNEL_ID = 2;
    public NotificationManagerCompat notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        notificationManager = NotificationManagerCompat.from(context);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel warning = new NotificationChannel(
                    CONTACT_CHANNEL_NAME,
                    "WarningNotification",
                    NotificationManager.IMPORTANCE_HIGH
            );
            warning.setDescription("Warning no bluetooth on");
            NotificationManager manager = ContextCompat.getSystemService(context, NotificationManager.class);
            manager.createNotificationChannel(warning);
        }
        String message = "You have been in contact with someone with Covid. You must self isolate now";
        Intent activityIntent = new Intent(context, WarningMessage.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 1, activityIntent, 0);
        Notification notification = new NotificationCompat.Builder(context, CONTACT_CHANNEL_NAME)
                .setSmallIcon(R.drawable.ic_baseline_bluetooth_disabled_24)
                .setContentTitle("Dangerous contact")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(Color.RED)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(false)
                .setOngoing(true)
                .setContentIntent(contentIntent)
                .build();

        notificationManager.notify(CONTACT_CHANNEL_ID, notification);

    }
}
