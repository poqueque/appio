package net.poquesoft.appio.authentication;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Data representing a User
 *
 * Created by edi on 12/01/17.
 */

public class User implements Comparable<User>, Serializable {

    public String uid;
    public String email;
    public String name;
    public String gender;
    public String address;
    public String fcmtoken;
    public boolean user_admin = false;
    public boolean admin = false;
    public boolean mail_validated = false;
    public static final String GENDER_MAN = "M";
    public static final String GENDER_WOMAN = "W";

    /**
     * Default constructor required for calls to DataSnapshot.getValue(Workout.class)
     *
     */
    public User() {
    }

    /**
     * Constructor
     *
     * @param email Email
     * @param uid User ID
     * @param name User Name
     * @param gender Gender ("M" or "W")
     */
    public User(String email, String uid, String name, String gender) {
        this.email = email;
        this.uid = uid;
        this.name = name;
        this.gender = gender;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("email", email);
        result.put("name", name);
        result.put("gender", gender);
        result.put("address", address);

        return result;
    }

    @Override
    public int compareTo(@NonNull User user) {
        if (user.name == null) return 1;
        if (name == null) return -1;
        return name.compareTo(user.name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            User u = (User) obj;
            return (uid != null && u.uid != null && uid.equals(u.uid));
        } else
            return false;
    }

    /**
     * Checks if the user profile is completed
     *
     * @return true if complete, false otherwise
     */
    public boolean isComplete() {
        return (name != null && !"".equals(name) && gender != null && !"".equals(gender));
    }
}
