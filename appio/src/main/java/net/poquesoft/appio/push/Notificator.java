package net.poquesoft.appio.push;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

public class Notificator {

    public static final String CHANNEL_ID = "APPIO_CHANNEL_ID";
    public static final String CHANNEL_NAME = "Appio Lib Channel";
    public static final String CHANNEL_DESCRIPTION = "Channel used by Appio Lib";

    private static final String TAG = "Notificator";
    private static Notificator INSTANCE = null;
    private boolean init = false;

    public synchronized static Notificator getInstance() {
        if (INSTANCE == null) {
            Log.d(TAG,"[DATA] Data is null. Creating new one.");
            INSTANCE = new Notificator();
        }
        return(INSTANCE);
    }

    public void showNotification(Context c, int notificationId, int icon, String textTitle, String textContent, PendingIntent pendingIntent){
        if (!init) init(c);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(c, CHANNEL_ID)
                .setSmallIcon(icon)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (textContent.length() > 40)
            mBuilder.setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(textContent));

        if (pendingIntent != null)
                mBuilder.setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(c);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, mBuilder.build());

    }

    private void init(Context c) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = CHANNEL_NAME;
            String description = CHANNEL_DESCRIPTION;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = c.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        init = true;
    }
}
