package com.example.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.core.app.NotificationCompat;

public class MyFirebaseMessageService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("VinhLT","FirebaseMessageToken:"+s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("VinhLT","onReceiverTheirMessage:");

        if (remoteMessage.getData().size() > 0) {
            Log.d("VinhLT","onReceiverTheirMessage:000000001");
            showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"));
        }

        if (remoteMessage.getNotification() != null) {
            Log.d("VinhLT","onReceiverTheirMessage:00000000222222");
            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }

    }

    private void showNotification(String title, String content){

        String channel_id ="home_noti";
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        //sound
        Uri uri =  RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuider = new NotificationCompat.Builder(this);
        notificationBuider.setContentTitle(title);
        notificationBuider.setContentText(content);
        notificationBuider.setAutoCancel(true);
        notificationBuider.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuider.setSound(uri);
        notificationBuider.setPriority(Notification.PRIORITY_MAX);

        notificationBuider.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channel_id,
                    getResources().getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_HIGH);
//            channel.setShowBadge(false);
            channel.setSound(uri,null);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
                notificationBuider.setChannelId(channel_id);
            }
        }
        notificationManager.notify(0,notificationBuider.build());
    }
}
