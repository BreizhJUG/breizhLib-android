package org.breizhjug.breizhlib.account;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.utils.authentification.BreizhLibAuthentification;
import roboguice.application.RoboApplication;
import roboguice.inject.InjectorProvider;
import static org.breizhjug.breizhlib.IntentConstantes.*;

/**
 * User: Guernion Sylvain
 * Date: 26/02/12
 * Time: 23:29
 */
public class AuthentificatorActivity extends AccountAuthenticatorActivity implements InjectorProvider {


    private static final String TAG = "AuthentificatorActivity";

    private AccountManager mAccountManager;
    private Thread mAuthThread;
    private String mAuthtoken;
    private String mAuthtokenType;

    private Boolean mConfirmCredentials = false;

    private final Handler mHandler = new Handler();
    private TextView mMessage;
    private String mPassword;
    private EditText mPasswordEdit;

    protected boolean mRequestNewAccount = false;

    private String mUsername;
    private EditText mUsernameEdit;

    @Inject
    private BreizhLibAuthentification breizhLibAuthentification;


    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle icicle) {
        final Injector injector = getInjector();
        injector.injectMembers(this);
        Log.i(TAG, "onCreate(" + icicle + ")");
        super.onCreate(icicle);
        mAccountManager = AccountManager.get(this);

        final Intent intent = getIntent();
        mUsername = intent.getStringExtra(USERNAME);
        mAuthtokenType = intent.getStringExtra(AUTHTOKEN_TYPE);
        mRequestNewAccount = mUsername == null;
        mConfirmCredentials = intent.getBooleanExtra(CONFIRMCREDENTIALS, false);

        Log.i(TAG, "    request new: " + mRequestNewAccount);
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.login_activity);
        getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
                android.R.drawable.ic_dialog_alert);

        mMessage = (TextView) findViewById(R.id.message);
        mUsernameEdit = (EditText) findViewById(R.id.username_edit);
        mPasswordEdit = (EditText) findViewById(R.id.password_edit);

        mUsernameEdit.setText(mUsername);
        mMessage.setText(getMessage());
    }

    /*
     * {@inheritDoc}
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getText(R.string.ui_activity_authenticating));
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                Log.i(TAG, "dialog cancel has been invoked");
                if (mAuthThread != null) {
                    mAuthThread.interrupt();
                    finish();
                }
            }
        });
        return dialog;
    }

    public void handleLogin(View view) {
        if (mRequestNewAccount) {
            mUsername = mUsernameEdit.getText().toString();
        }
        mPassword = mPasswordEdit.getText().toString();
        if (TextUtils.isEmpty(mUsername) || TextUtils.isEmpty(mPassword)) {
            mMessage.setText(getMessage());
        } else {
            showProgress();
            // authentication...
            mAuthThread =
                    breizhLibAuthentification.attemptAuth(mUsername, mPassword, mHandler,
                            AuthentificatorActivity.this);
        }
    }


    protected void finishConfirmCredentials(boolean result) {
        Log.i(TAG, "finishConfirmCredentials()");
        final Account account = new Account(mUsername, BreizhLibConstantes.ACCOUNT_TYPE);
        mAccountManager.setPassword(account, mPassword);
        final Intent intent = new Intent();
        intent.putExtra(AccountManager.KEY_BOOLEAN_RESULT, result);
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }


    protected void finishLogin() {
        Log.i(TAG, "finishLogin()");
        final Account account = new Account(mUsername, BreizhLibConstantes.ACCOUNT_TYPE);

        if (mRequestNewAccount) {
            mAccountManager.addAccountExplicitly(account, mPassword, null);
            // Set contacts sync for this account.
            ContentResolver.setSyncAutomatically(account,
                    ContactsContract.AUTHORITY, true);
        } else {
            mAccountManager.setPassword(account, mPassword);
        }
        final Intent intent = new Intent();
        mAuthtoken = mPassword;
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, mUsername);
        intent
                .putExtra(AccountManager.KEY_ACCOUNT_TYPE, BreizhLibConstantes.ACCOUNT_TYPE);
        if (mAuthtokenType != null
                && mAuthtokenType.equals(BreizhLibConstantes.AUTHTOKEN_TYPE)) {
            intent.putExtra(AccountManager.KEY_AUTHTOKEN, mAuthtoken);
        }
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }

    protected void hideProgress() {
        dismissDialog(0);
    }

    public void onAuthenticationResult(boolean result) {
        Log.i(TAG, "onAuthenticationResult(" + result + ")");
        hideProgress();
        if (result) {
            if (!mConfirmCredentials) {
                finishLogin();
            } else {
                finishConfirmCredentials(true);
            }
        } else {
            Log.e(TAG, "onAuthenticationResult: failed to authenticate");
            if (mRequestNewAccount) {
                // "Please enter a valid username/password.
                mMessage
                        .setText(getText(R.string.login_activity_loginfail_text_both));
            } else {
                // "Please enter a valid password."
                mMessage
                        .setText(getText(R.string.login_activity_loginfail_text_pwonly));
            }
        }
    }

    private CharSequence getMessage() {
        if (TextUtils.isEmpty(mUsername)) {
            return getText(R.string.login_activity_newaccount_text);
        }
        if (TextUtils.isEmpty(mPassword)) {
            return getText(R.string.login_activity_loginfail_text_pwmissing);
        }
        return null;
    }


    protected void showProgress() {
        showDialog(0);
    }

    @Override
    public Injector getInjector() {
        return ((RoboApplication) getApplication()).getInjector();
    }
}