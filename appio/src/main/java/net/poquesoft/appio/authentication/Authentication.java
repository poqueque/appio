package net.poquesoft.appio.authentication;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import net.poquesoft.appio.database.AppioData;
import net.poquesoft.appio.view.listeners.SimpleListener;
import net.poquesoft.appio.view.listeners.SuccessErrorListener;

/**
 * Authentication class manages User Authentication through Firebase platform
 *
 * Created by edi on 11/01/17.
 */

public class Authentication {

    private static final String TAG = "Authentication";
    private static FirebaseAuth mAuth;
    private static FirebaseAuth.AuthStateListener mAuthListener;
    private static FirebaseUser mUser;
    private static final String ERROR_EMAIL_ALREADY_IN_USE = "ERROR_EMAIL_ALREADY_IN_USE";
    public static final String ERROR_WEAK_PASSWORD = "ERROR_WEAK_PASSWORD";
    public static final String ERROR_INVALID_EMAIL = "ERROR_INVALID_EMAIL";
    public static final String ERROR_USER_NOT_FOUND = "ERROR_USER_NOT_FOUND";
    private static User userProfile;
    private static Context firebaseContext;

    public static void initFirebase(Context context) {
        firebaseContext = context;
        FirebaseApp.initializeApp(context);
        init();
    }

    /**
     * Initializes the Firebase Authentication.
     *
     */
    public static void init(){
        if (mAuth == null) mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if (mUser == null) {
            Log.d(TAG,"[PROFILE] User not logged");
        } else {
            Log.d(TAG,"[PROFILE] User: "+mUser.getDisplayName());
        }

        if (mAuthListener == null){
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        // User is signed in
                        Log.d(TAG, "[PROFILE] onAuthStateChanged:signed_in:" + user.getUid());
                    } else {
                        // User is signed out
                        Log.d(TAG, "[PROFILE] onAuthStateChanged:signed_out");
                    }
                    mUser = user;
                }
            };
            mAuth.addAuthStateListener(mAuthListener);
        }
    }

    /**
     * Check if user is logged
     *
     * @return true if there are valid credentials, false otherwise
     */
    public static boolean isUserLogged(){
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    /**
     * Removes Firebase listeners. To be called when app is terminated
     *
     */
    public static void deinit(){
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /**
     * Creates a new user
     *
     * @param successErrorListener Listener to receive callbacks
     * @param email Email of the new user
     * @param password Password of the new user
     */
    //TODO: Email Validation
    //TODO: Forgot Password
    public static void createUser (final SuccessErrorListener successErrorListener, final String email, final String password){
        Log.d(TAG, "[PROFILE] Check Account creation");
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "[PROFILE] createUserWithEmail:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (task.isSuccessful()) {
                            Log.d(TAG, "[PROFILE] Authentication succeed.");
                            AppioData.initUserData(new SimpleListener() {
                                @Override
                                public void onAction() {
                                    successErrorListener.onSuccess();
                                }
                            });
                        } else {
                            Log.d(TAG, "[PROFILE] Authentication failed.");
                            if (task.getException() instanceof FirebaseAuthException){
                                FirebaseAuthException fae = (FirebaseAuthException) task.getException();
                                if (ERROR_EMAIL_ALREADY_IN_USE.equals(fae.getErrorCode())) {
                                    Log.d(TAG, "[PROFILE] Trying login.");
                                    login(successErrorListener, email, password);
                                } else if (ERROR_WEAK_PASSWORD.equals(fae.getErrorCode())){
                                    successErrorListener.onError(ERROR_WEAK_PASSWORD);
                                } else if (ERROR_INVALID_EMAIL.equals(fae.getErrorCode())){
                                    successErrorListener.onError(ERROR_INVALID_EMAIL);
                                } else {
                                    Log.d(TAG, "[PROFILE] Error authenticating:"+fae.getMessage());
                                    successErrorListener.onError("Error de autenticación:" + fae.getErrorCode());
                                }
                            } else {
                                Log.d(TAG, "[PROFILE] Authentication failed.");
                                successErrorListener.onError("Error de autenticación:" + task.getException());
                            }
                        }
                    }
                });
    }

    /**
     * Performs login of an already registered user
     *
     * @param successErrorListener Listener to receive callbacks
     * @param email Email of the new user
     * @param password Password of the new user
     */
    public static void login(final SuccessErrorListener successErrorListener, final String email, final String password){
        Log.d(TAG, "[PROFILE] Login with Email");
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "[PROFILE] signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (task.isSuccessful()) {
                            Log.d(TAG,"[PROFILE] Succesful login");
                            AppioData.initUserData(new SimpleListener() {
                                @Override
                                public void onAction() {
                                    successErrorListener.onSuccess();
                                }
                            });
                        } else {
                            Log.w(TAG, "[PROFILE] signInWithEmail", task.getException());
                            if (task.getException() instanceof FirebaseAuthException) {
                                FirebaseAuthException fae = (FirebaseAuthException) task.getException();
                                if (ERROR_USER_NOT_FOUND.equals(fae.getErrorCode())) {
                                    Log.d(TAG, "[PROFILE] Creating user");
                                    successErrorListener.onError(ERROR_USER_NOT_FOUND);
                                } else {
                                    Log.d(TAG, "[PROFILE] Error authenticating:" + fae.getErrorCode());
                                    successErrorListener.onError("Error de autenticación: "+ fae.getErrorCode());
                                }
                            } else {
                                successErrorListener.onError("Error de autenticación");
                            }
                        }
                    }
                });
    }


    /**
     * Authenticate with Firebase Using Email Link in Android
     * @see {<a href="https://firebase.google.com/docs/auth/android/email-link-auth"></a>}
     *
     * @param successErrorListener Listener to receive callbacks
     * @param email Email of the new user
     */
    public static void loginLink(final SuccessErrorListener successErrorListener, final String email, final String returnUrl, final String packageName){
        Log.d(TAG, "[PROFILE] Login with Email link");

        ActionCodeSettings actionCodeSettings =
                ActionCodeSettings.newBuilder()
                        // URL you want to redirect back to. The domain (www.example.com) for this
                        // URL must be whitelisted in the Firebase Console.
                        .setUrl(returnUrl)
                        // This must be true
                        .setHandleCodeInApp(true)
                        .setAndroidPackageName(
                                packageName,
                                true, /* installIfNotAvailable */
                                "1"    /* minimumVersion */)
                        .build();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendSignInLinkToEmail(email, actionCodeSettings)
                .addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            if (firebaseContext != null){
                                SharedPreferences sharedPref =
                                        firebaseContext.getSharedPreferences("appio", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("emailAuthentication", email);
                                editor.apply();
                            }
                            successErrorListener.onSuccess();
                        } else {
                            if (task.getException() != null)
                                successErrorListener.onError(task.getException().getMessage());
                            else
                                successErrorListener.onError("Unknown error");
                        }
                    }
                });
    }


    public static void verifyLink(final SuccessErrorListener successErrorListener, final String emailLink){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email = null;
        if (firebaseContext != null) {
            SharedPreferences sharedPref =
                    firebaseContext.getSharedPreferences("appio", Context.MODE_PRIVATE);
            email = sharedPref.getString("emailAuthentication",null);
        }
        // Confirm the link is a sign-in with email link.
        if (auth.isSignInWithEmailLink(emailLink)) {
            auth.signInWithEmailLink(email, emailLink)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Successfully signed in with email link!");
                                AuthResult result = task.getResult();

                                AppioData.initUserData(new SimpleListener() {
                                    @Override
                                    public void onAction() {
                                        successErrorListener.onSuccess();
                                    }
                                });
                                // You can access the new user via result.getUser()
                                // Additional user info profile *not* available via:
                                // result.getAdditionalUserInfo().getProfile() == null
                                // You can check if the user is new or existing:
                                // result.getAdditionalUserInfo().isNewUser()
                            } else {
                                Log.e(TAG, "Error signing in with email link: "
                                        + task.getException().getMessage());

                                successErrorListener.onError(task.getException().getMessage());
                            }
                        }
                    });
        }
    }

    /**
     * Gets current user
     *
     * @return current user
     */
    public static FirebaseUser getUser() {
        if (mAuth == null) mAuth = FirebaseAuth.getInstance();
        if (mUser == null) mUser = mAuth.getCurrentUser();
        return mUser;
    }

    /**
     * Gets current User ID
     *
     * @return Current User ID
     */
    public static String getUid() {
        if (mAuth == null) mAuth = FirebaseAuth.getInstance();
        if (mUser == null) mUser = mAuth.getCurrentUser();
        if (mUser == null) return null;
        return mUser.getUid();
    }

    /**
     * Performs logout
     *
     */

    public static void logout() {
        userProfile = null;
        FirebaseAuth.getInstance().signOut();
    }

    /**
     * Gets current user email
     *
     * @return current user email
     */
    public static String getEmail() {
        if (mAuth == null) mAuth = FirebaseAuth.getInstance();
        if (mUser == null) mAuth.getCurrentUser();
        if (mUser == null) return null;
        return mUser.getEmail();
    }

    /**
     * Sets current user
     *
     * @param user user to set as current
     */
    public static void setUser(User user) {
        Log.d(TAG, "[DB] setUser");
        if (user == null) return;
        userProfile = user;
        Log.d(TAG, "[DB] User.names = " + userProfile.name);
        Log.d(TAG, "[DB] isUserAdmin = " + userProfile.user_admin);
    }

    /**
     * Checks if profile is complete
     *
     * @return true if profile is complete, false otherwise
     */
    public static boolean isProfileComplete() {
        return userProfile == null || (userProfile.isComplete());
    }

    /**
     * Returns user avatar
     *
     * @return user avatar
     */
    public static String getAvatar() {
        return "https://avatars3.githubusercontent.com/u/1476232?v=3&s=460";
    }

    /**
     * Returns user Name
     *
     * @return User Name
     */
    public static String getName() {
        if (userProfile == null) {
            Log.d(TAG,"[DB] UserProfile is null");
            return null;
        }
        return userProfile.name;
    }

    /**
     * Checks if the user profile has been read from database
     *
     * @return true if user profile is loaded, false otherwise
     */
    public static boolean isUserProfileLoaded() {
        return (userProfile != null);
    }

    /**
     * Gets user profile
     *
     * @return User profile
     */
    public static User getUserProfile() {

        if (userProfile != null) {
            userProfile.email = Authentication.getEmail();
            userProfile.uid = Authentication.getUid();
        }
        return userProfile;
    }

    /**
     * Checks if user is User Administrator
     *
     * @return true if is User Administrator, false otherwise
     */
    public static boolean isUserAdmin() {
        return userProfile != null && userProfile.user_admin;
    }

    /**
     * Checks if user is Administrator
     *
     * @return true if is Administrator, false otherwise
     */
    public static boolean isAdmin() {
        return userProfile != null && userProfile.admin;
    }

}
