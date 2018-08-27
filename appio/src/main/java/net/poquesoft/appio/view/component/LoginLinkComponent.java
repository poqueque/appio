package net.poquesoft.appio.view.component;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import net.poquesoft.appio.R;
import net.poquesoft.appio.view.listeners.IntegerListener;

/**
 * Standard Text component
 */

public class LoginLinkComponent extends Component {

    private int loginAction = Component.NONE;
    private IntegerListener actionListener;
    private Button loginButton;
    private AutoCompleteTextView mEmailView;
    private Context context;

    public LoginLinkComponent(IntegerListener actionListener, int loginActionId) {
        loginAction = loginActionId;
        this.actionListener = actionListener;
    }

    @Override
    public int getLayout() {
        return R.layout.component_login_link;
    }

    @Override
    public void initView(final View v) {
        context = v.getContext();
        mEmailView = v.findViewById(R.id.email);

        loginButton = v.findViewById(R.id.email_sign_in_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            String errorText = context.getString(R.string.error_field_required);
            mEmailView.setError(errorText);
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            String errorText = context.getString(R.string.error_invalid_email);
            mEmailView.setError(errorText);
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            if (actionListener!= null) actionListener.onAction(loginAction);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    public String getEmail() {
        return mEmailView.getText().toString();
    }

}
