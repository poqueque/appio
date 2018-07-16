package net.poquesoft.appio.database;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.poquesoft.appio.authentication.Authentication;
import net.poquesoft.appio.authentication.User;

import java.util.ArrayList;
import java.util.Collections;
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
    public static void initUserData(){
        getUserProfile();
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
        Collections.sort(users);
        Log.d(TAG,"[DB] notifyListeners");
        for (DataListener d:listeners) d.onDataChange();
    }


    /**
     * Reads current user Profile
     *
     */
    private static void getUserProfile() {
        Log.d(TAG, "[DB] getUserProfile");
        if (!Authentication.isUserLogged()) {
            notifyListeners();
            return;
        }
        final String userKey = Authentication.getUid();
        if (userKey == null) {
            Log.d(TAG, "[DB] User email null: [" + Authentication.getUid() + " , " + Authentication.getEmail() + "]");
            notifyListeners();
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
                            notifyListeners();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w(TAG, "[DB] getUser:onCancelled [" + databaseError.getCode() + "]", databaseError.toException());
                            Authentication.setUser(null);
                            notifyListeners();
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
        String userKey = u.uid;
        return mDatabase.child("users").child(userKey);
    }
}
