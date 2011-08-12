package org.breizhjug.breizhlib.activity.compte;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import org.apache.http.impl.client.DefaultHttpClient;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.activity.AbstractActivity;

public class AppInfo extends AbstractActivity {

    DefaultHttpClient http_client = new DefaultHttpClient();
    String email;

    @Override
    public void init(Intent intent) {
        String account = (String) intent.getExtras().get("account");
        register(account);
    }


    private void register(final String accountName) {

        final SharedPreferences prefs = BreizhLib.getSharedPreferences(getApplicationContext());
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
                        String auth_token = BreizhLib.getGAuth().getToken(AppInfo.this, acct);
                        String authCookie = BreizhLib.getGAuth().getAuthCookie(auth_token);
                        Log.d("BREIZHLIB",""+authCookie);
                        return authCookie;
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        if (result == null) {
                            showError("Error", true);
                        } else {
                            prefs.edit().putString(BreizhLibConstantes.AUTH_COOKIE, result).commit();
                            Intent intent = new Intent(AppInfo.this, ProfilActivity.class);
                            startActivity(intent);
                        }
                    }
                };
                initTask.execute();
                // finish();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_info);
    }
}
