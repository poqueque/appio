package net.poquesoft.appio;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AppioApp extends Application {

    private static final String TAG = "AppioApp";
    protected static Context context;

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getAppContext() {
        return context;
    }

    public static void restart(Context context) {
        Log.d(TAG,"[DATA] Restart app");
        Intent i = context.getPackageManager().getLaunchIntentForPackage( context.getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);
    }
}
