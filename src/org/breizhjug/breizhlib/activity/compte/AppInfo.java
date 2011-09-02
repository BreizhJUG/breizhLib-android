package org.breizhjug.breizhlib.activity.compte;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import com.google.inject.Inject;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.activity.AbstractActivity;
import org.breizhjug.breizhlib.utils.GoogleAuthentification;

public class AppInfo extends AbstractActivity {

    private static final String TAG = "BreizhLib.AppInfo";
    @Inject
    private SharedPreferences prefs;
    @Inject
    private GoogleAuthentification gAuth;

    @Override
    public void init(Intent intent) {
        String account = (String) intent.getExtras().get("account");
        register(account);
    }


    private void register(final String accountName) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(BreizhLibConstantes.ACCOUNT_NAME, accountName);
        editor.putString(BreizhLibConstantes.AUTH_COOKIE, null);
        editor.commit();

        AccountManager mgr = AccountManager.get(getApplicationContext());
        Account[] accts = mgr.getAccountsByType("com.google");
        for (final Account acct : accts) {
            if (acct.name.equals(accountName)) {
                final AsyncTask<Void, Void, String> initTask = new AsyncTask<Void, Void, String>() {

                    @Override
                    protected String doInBackground(Void... params) {
                        String auth_token = gAuth.getToken(AppInfo.this, acct);
                        String authCookie = gAuth.getAuthCookie(auth_token, AppInfo.this);
                        Log.d(TAG, "" + authCookie);
                        return authCookie;
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        if (result == null) {
                            showError(getString(R.string.connexion_error), true);
                        } else {
                            prefs.edit().putString(BreizhLibConstantes.AUTH_COOKIE, result).commit();
                            Intent intent = new Intent(AppInfo.this, ProfilActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                };
                initTask.execute();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_info);
    }
}
