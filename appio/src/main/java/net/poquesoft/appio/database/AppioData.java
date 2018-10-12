package net.poquesoft.appio.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import net.poquesoft.appio.authentication.Authentication;
import net.poquesoft.appio.authentication.User;
import net.poquesoft.appio.view.listeners.SimpleListener;

import java.util.ArrayList;
import java.util.Date;

public class AppioData {

    private static final String TAG = "AppioData";

    protected static DatabaseReference mDatabase;

    private static ArrayList<User> users;
    private static Date firstDate;
    private static Date lastDate;
    protected static ArrayList<DataListener> listeners;

    protected AppioData() {
        init();
    }

    private static void init() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        users = new ArrayList<>();
        firstDate = new Date();
        lastDate = new Date();
        listeners = new ArrayList<>();
    }

    /**
     * Initializes user data, reading the profile and grabing all the workouts from database
     *
     */
    public static void initUserData(SimpleListener simpleListener){
        getUserProfile(simpleListener);
    }


    /**
     * Registers a listener that will listen for changes on Firebase data
     *
     * @param d Listener to register
     */
    public void registerListener (DataListener d){
        listeners.add(d);
    }

    /**
     * Unregisters a listener listening for changes on Firebase data
     *
     * @param d Listener to unregister
     */

    public void unregisterListener (DataListener d){
        listeners.remove(d);
    }


    /**
     * Notify all registered listeners that there were changes on the data
     *
     */
    protected static void notifyListeners() {
        Log.d(TAG,"[DB] notifyListeners");
        for (DataListener d:listeners) d.onDataChange();
    }


    /**
     * Reads current user Profile
     *
     */
    private static void getUserProfile(final SimpleListener simpleListener) {
        Log.d(TAG, "[DB] getUserProfile");
        if (!Authentication.isUserLogged()) {
            simpleListener.onAction();
            return;
        }
        final String userKey = Authentication.getUid();
        if (userKey == null) {
            Log.d(TAG, "[DB] User email null: [" + Authentication.getUid() + " , " + Authentication.getEmail() + "]");
            simpleListener.onAction();
        } else {
            mDatabase.child("users").child(userKey).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Get user value
                            Log.d(TAG, "[DB] getUserProfile - Set User Data");
                            //Check if it's me
                            User u = dataSnapshot.getValue(User.class);
                            //                        if (u != null && u.uid != null && u.uid.equals(Authentication.getUid()))
                            Authentication.setUser(u);
                            simpleListener.onAction();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w(TAG, "[DB] getUser:onCancelled [" + databaseError.getCode() + "]", databaseError.toException());
                            Authentication.setUser(null);
                            simpleListener.onAction();
                        }
                    });
        }
    }

    /**
     * Updates a User profile on Firebase database
     *
     * @param u User to update
     */
    public static void updateProfile(User u) {
        Log.d(TAG,"[DB] Update existing Profile");
        String userKey = u.uid;
        if (userKey == null) return;
        DatabaseReference userRef = mDatabase.child("users").child(userKey);
        userRef.child("uid").setValue(u.uid);
        userRef.child("email").setValue(u.email);
        userRef.child("name").setValue(u.name);
        userRef.child("gender").setValue(u.gender);
        userRef.child("address").setValue(u.address);

        //If it's me...
        if (u.uid != null && u.uid.equals(Authentication.getUid())) {
            User me = Authentication.getUserProfile();
            me.name = u.name;
            me.gender = u.gender;
            me.address = u.address;
        }
    }

    public static DatabaseReference getUserRef(){
        User u = Authentication.getUserProfile();
        if (u==null) return null;
        String userKey = u.uid;
        return mDatabase.child("users").child(userKey);
    }

    public static DatabaseReference getUserRef(String userKey){
        return mDatabase.child("users").child(userKey);
    }

    public long getServerTime(final Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                "appio", Context.MODE_PRIVATE);
        long serverDelay = sharedPref.getLong("serverDelay", 0);
        Log.d(TAG,"[DELAY] readServerDelay:"+serverDelay);
        long serverTimestamp = System.currentTimeMillis()-serverDelay;
        Log.d(TAG,"[DELAY] Server Timestamp:"+serverTimestamp);
        return serverTimestamp;
    }

    public void setFCMToken(String token){
        if (!Authentication.isUserLogged()) {
            return;
        }

        DatabaseReference userRef = getUserRef();
        if (userRef == null) return;
        userRef.child("fcmtoken").setValue(token);
    }

    public void synchronizeServerTimeDelay(final Context context, final DataListener listener) {
        if (!Authentication.isUserLogged()) {
            listener.onDataChange();
            return;
        }

        DatabaseReference userRef = getUserRef();
        if (userRef == null) return;
        userRef.child("lastConnected").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Long timestampServer = (Long) snapshot.getValue();
                        Long delay = System.currentTimeMillis()-timestampServer;
                        SharedPreferences sharedPref = context.getSharedPreferences(
                                "appio", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putLong("serverDelay", delay);
                        editor.commit();
                        Log.d(TAG,"[DELAY] saveServerDelay:"+delay);
                        listener.onDataChange();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG,"[DELAY] databaseError: "+databaseError);
                        listener.onDataChange();
                    }
                }
        );

        userRef.child("lastConnected").setValue(ServerValue.TIMESTAMP);
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String version = pInfo.versionName;
            userRef.child("version").setValue(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
