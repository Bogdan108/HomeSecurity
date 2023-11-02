package com.example.homesecurity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.homesecurity.MainActivity;
import com.example.homesecurity.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FireMsgService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("Msg", "Message received ["+remoteMessage+"]");
        // Create Notification
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("Image", "Intruder detected");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1410,
                intent,
                PendingIntent.FLAG_MUTABLE);
        String info = null;
        if (remoteMessage.getData().size() > 0) {info = remoteMessage.getData().get("message");
        }
        if (remoteMessage.getNotification() != null) {
            info = remoteMessage.getNotification().getBody();
        }
        NotificationCompat.Builder notificationBuilder = new
                NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Message")
                .setContentText(info)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1410, notificationBuilder.build());
    }
}