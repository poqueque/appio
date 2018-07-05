package net.poquesoft.appio.view.component;

import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.poquesoft.appio.R;
import net.poquesoft.appio.view.listeners.ActionListener;

/**
 * Standard Text component
 */

public class LoginComponent extends Component {

    private int loginAction = Component.NONE;
    private int forgotPassAction = Component.NONE;
    private ActionListener actionListener;
    private Button loginButton;
    private TextView forgotPassText;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private Context context;

    public LoginComponent(ActionListener actionListener, int loginActionId, int forgotPassActionId) {
        loginAction = loginActionId;
        forgotPassAction = forgotPassActionId;
        this.actionListener = actionListener;
    }

    @Override
    public int getLayout() {
        return R.layout.component_login;
    }

    @Override
    public void initView(final View v) {
        context = v.getContext();
        mEmailView = v.findViewById(R.id.email);

        mPasswordView = (EditText) v.findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        loginButton = v.findViewById(R.id.email_sign_in_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        forgotPassText = v.findViewById(R.id.rationale_text);
        forgotPassText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (actionListener!= null) actionListener.onAction(forgotPassAction);
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
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(context.getString(R.string.error_empty_password));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(context.getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

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

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    public String getEmail() {
        return mEmailView.getText().toString();
    }

    public String getPassword() {
        return mPasswordView.getText().toString();
    }
}
