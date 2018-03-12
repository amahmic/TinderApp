package com.example.azramahmic.tinderapp.services;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.example.azramahmic.tinderapp.R;
import com.example.azramahmic.tinderapp.data.local.model.ForegroundChatUser;
import com.example.azramahmic.tinderapp.presentation.chat.ChatActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import io.realm.Realm;

public class FcmMessagingService extends FirebaseMessagingService {

    private static final String TAG = FcmMessagingService.class.getName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("FCM", "Message received");
        // Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Map<String, String> notificationData = remoteMessage.getData();
            // Create an Intent for the activity you want to start
            String senderId = notificationData.get("senderId");
            if (senderId != null && !senderId.isEmpty()) {
                ForegroundChatUser foregroundChatUser = getCurrentChat();
                String receiverId = foregroundChatUser != null ? foregroundChatUser.getReceiverId() : null;
                if (receiverId != null && receiverId.equals(senderId)) {
                    Log.d("FCM", "User is talking to this user, don't send notification!");
                }
                else {
                    String title = notificationData.get("title");
                    String body = notificationData.get("body");
                    sendNotification(senderId, title, body);
                }
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Log.d(TAG, "Message Notification title: " + remoteMessage.getNotification().getTitle());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void sendNotification(String senderId, String title, String body) {
        Intent chatActivity = ChatActivity.getCallingIntent(this, senderId);
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(chatActivity);
        // Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri notificationSoundUri = Uri.parse("android.resource://"+getPackageName()+"/raw/notification_sound");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setContentIntent(resultPendingIntent)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setColor(getResources().getColor(R.color.colorAccent))
                .setContentTitle(title)
                .setContentText(body)
                .setSound(notificationSoundUri)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat.from(this).notify(0, builder.build());
    }

    private ForegroundChatUser getCurrentChat() {
        try (Realm realm = Realm.getDefaultInstance()) {
            ForegroundChatUser foregroundChatUser = realm.where(ForegroundChatUser.class).findFirst();
            if (foregroundChatUser != null) {
                return realm.copyFromRealm(foregroundChatUser);
            } else {
                return null;
            }
        }
    }
}
