package org.breizhjug.breizhlib.utils.authentification;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import com.google.inject.Inject;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.account.AuthentificatorActivity;
import org.breizhjug.breizhlib.model.Utilisateur;
import org.breizhjug.breizhlib.utils.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class BreizhLibAuthentification implements Authentification {


    private static final String AUTH_URI = BreizhLibConstantes.SERVER_URL + "/authentification";
    private static final String TAG = "BreizhLibAuthent";
    public static final int REGISTRATION_TIMEOUT = 30 * 1000; // ms

    private Cookie authCookie;

    @Inject
    protected SharedPreferences prefs;

    private static final String AUTH_COOKIE_NAME = "PLAY_SESSION";

    @Override
    public Intent getAuthentificationIntent(Context context) {
        //return new Intent(context,AuthentificatorActivity.class);
        return new Intent(Settings.ACTION_ADD_ACCOUNT);
    }

    @Override
    public String getAccountType() {
        return BreizhLibConstantes.ACCOUNT_TYPE;
    }

    ;

    @Override
    public String getToken(Context context, Account account) {
        String cookie = prefs.getString(AUTH_COOKIE_NAME,"");
        Log.d(TAG,"cookie : "+cookie);
        return cookie;
    }

    @Override
    public String getAuthCookie(String authToken, Context context) {
        Account[] accounts = AccountManager.get(context).getAccounts();
        for (Account account : accounts) {
            if (account.type.equals(getAccountType())) {
               String password = AccountManager.get(context).getPassword(account);
                authenticate(account.name, password,null,  context);
            }
        }
       
        return AUTH_COOKIE_NAME + "=" + authCookie.getValue();
    }

    @Override
    public List<String> getAccounts(Context context) {
        ArrayList<String> result = new ArrayList<String>();
        Account[] accounts = AccountManager.get(context).getAccounts();
        for (Account account : accounts) {
            if (account.type.equals(getAccountType())) {
                String email = AccountManager.get(context).getUserData(account,"email");
                result.add(email== null?account.name:account.name);
            }
        }
        return result;
    }

    @Override
    public void saveInfos(Utilisateur user,Context context) {
        Account[] accounts = AccountManager.get(context).getAccounts();
        for (Account account : accounts) {
            if (account.type.equals(getAccountType())) {
                AccountManager.get(context).setUserData(account,"email",user.email);
                AccountManager.get(context).setUserData(account,"nom",user.nom);
            }
        }
    }


    public boolean authenticate(String username, String password, Handler handler, Context context) {
        final HttpResponse resp;
        try {


            final HttpPost post = new HttpPost(AUTH_URI);
            String basic = username + ":" + password;
            String authToken =   "Basic " + Base64.encodeToString(basic.getBytes(), Base64.NO_WRAP);
            post.setHeader("Authorization",authToken );
            resp = NetworkUtils.getHttpClient().execute(post);
            for (Cookie cookie : NetworkUtils.getHttpClient().getCookieStore().getCookies()) {
                Log.d(TAG, cookie.getName() + " " + cookie.getValue());
                if (AUTH_COOKIE_NAME.equals(cookie.getName())) {
                    authCookie = cookie;
                    prefs.edit().putString(BreizhLibConstantes.AUTH_COOKIE, AUTH_COOKIE_NAME + "=" + cookie.getValue()).commit();
                }
            }
            Log.d(TAG, "StatusCode " + resp.getStatusLine().getStatusCode());
            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                Log.d(TAG, "Successful authentication");
                sendResult(true, handler, context);
                return true;
            } else {
                Log.d(TAG, "Error authenticating" + resp.getStatusLine());
                sendResult(false, handler, context);
                return false;
            }

        } catch (final IOException e) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "IOException when getting authtoken", e);
            }
            sendResult(false, handler, context);
            return false;
        } finally {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "getAuthtoken completing");
            }
        }
    }

    private static void sendResult(final Boolean result, final Handler handler,
                                   final Context context) {
        if (handler == null || context == null) {
            return;
        }
        handler.post(new Runnable() {
            public void run() {
                ((AuthentificatorActivity) context).onAuthenticationResult(result);
            }
        });
    }

    public Thread attemptAuth(final String username, final String password, final Handler handler, final Context context) {
        final Runnable runnable = new Runnable() {
            public void run() {
                authenticate(username, password, handler, context);
            }
        };

        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        t.start();
        return t;
    }


}
