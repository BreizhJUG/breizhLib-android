package org.breizhjug.breizhlib.activity.compte;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import org.apache.http.impl.client.DefaultHttpClient;
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

        final SharedPreferences prefs = breizhLib.getSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(breizhLib.ACCOUNT_NAME, accountName);
        editor.putString(breizhLib.AUTH_COOKIE, null);
        editor.commit();

        AccountManager mgr = AccountManager.get(getApplicationContext());
        Account[] accts = mgr.getAccountsByType("com.google");
        for (Account acct : accts) {
            if (acct.name.equals(accountName)) {
                email = accountName;
                String auth_token = breizhLib.getGAuth().getToken(this, acct);
                String authCookie = breizhLib.getGAuth().getAuthCookie(auth_token);
                prefs.edit().putString(breizhLib.AUTH_COOKIE, authCookie).commit();
                Intent intent = new Intent(AppInfo.this, ProfilActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_info);
    }
}
