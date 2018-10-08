package net.poquesoft.appio.push;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public abstract class AppioFCMService extends FirebaseMessagingService {

    private static final String TAG = "AppioFCMService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "[FCM] Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        String from = remoteMessage.getFrom();
        Log.d(TAG, "[FCM] From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        Map<String, String> data = null;
        if (remoteMessage.getData().size() > 0) {
            data = remoteMessage.getData();
            Log.d(TAG, "[FCM] Message data payload: " + data);
        }

        // Check if message contains a notification payload.
        String notificationBody = null;
        if (remoteMessage.getNotification() != null) {
            notificationBody = remoteMessage.getNotification().getBody();
            Log.d(TAG, "[FCM] Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        Map<String,String> extras = remoteMessage.getData();

        String message = extras.get("message");
        String type = extras.get("type");
        String exchangeid = extras.get("exchangeid");

        if (extras != null) {
            processMessage(from, data, notificationBody, extras);
        }
    }


    protected abstract void sendRegistrationToServer(String token);
    protected abstract void processMessage(String from, Map<String, String> data, String notificationBody, Map<String,String> extras);

}
