package org.breizhjug.breizhlib.activity.compte;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import org.apache.http.impl.client.DefaultHttpClient;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.activity.AbstractActivity;
import org.breizhjug.breizhlib.activity.Menu;

public class AppInfo extends AbstractActivity {

    DefaultHttpClient http_client = new DefaultHttpClient();
    String email;

    @Override
    public void init(Intent intent) {
        String account = (String) intent.getExtras().get("account");
        register(account);
    }


    private void register(final String accountName) {

        final SharedPreferences prefs = breizhLib.getSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(breizhLib.ACCOUNT_NAME, accountName);
        editor.putString(breizhLib.AUTH_COOKIE, null);
        editor.commit();

        AccountManager mgr = AccountManager.get(getApplicationContext());
        Account[] accts = mgr.getAccountsByType("com.google");
        for (final Account acct : accts) {
            if (acct.name.equals(accountName)) {
                final AsyncTask<Void, Void, String> initTask = new AsyncTask<Void, Void, String>() {

                    @Override
                    protected String doInBackground(Void... params) {
                        String auth_token = breizhLib.getGAuth().getToken(AppInfo.this, acct);
                        String authCookie = breizhLib.getGAuth().getAuthCookie(auth_token);
                        return authCookie;
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        if (result == null) {
                            showError("Error", true);
                        } else {
                            prefs.edit().putString(breizhLib.AUTH_COOKIE, result).commit();
                            Intent intent = new Intent(AppInfo.this, ProfilActivity.class);
                            startActivity(intent);
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
