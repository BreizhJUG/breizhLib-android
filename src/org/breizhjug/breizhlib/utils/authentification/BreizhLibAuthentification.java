package org.breizhjug.breizhlib.utils.authentification;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.account.AuthentificatorActivity;
import org.breizhjug.breizhlib.utils.NetworkUtils;
import org.breizhjug.breizhlib.utils.authentification.Authentification;

import static org.breizhjug.breizhlib.IntentConstantes.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class BreizhLibAuthentification implements Authentification {


    private static final String AUTH_URI = BreizhLibConstantes.SERVER_URL+"/authent";
    private static final String TAG = "BreizhLibAuthent";
    public static final int REGISTRATION_TIMEOUT = 30 * 1000; // ms


    @Override
    public Intent getAuthentificationIntent(Context context) {
        //return new Intent(context,AuthentificatorActivity.class);
        return new Intent(Settings.ACTION_ADD_ACCOUNT);
    }

    @Override
    public String getAccountType() {
        return BreizhLibConstantes.ACCOUNT_TYPE;
    }

    @Override
    public String getToken(Context context, Account account) {
        return "";
    }

    @Override
    public String getAuthCookie(String authToken, Context context) {
        return "";
    }

    @Override
    public List<String> getAccounts(Context context) {
        ArrayList<String> result = new ArrayList<String>();
        Account[] accounts = AccountManager.get(context).getAccounts();
        for (Account account : accounts) {
            if (account.type.equals(getAccountType())) {
                result.add(account.name);
            }
        }
        return result;
    }


    public boolean authenticate(String username, String password, Handler handler, Context context) {
        final HttpResponse resp;

        final ArrayList<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair(USERNAME, username));
        params.add(new BasicNameValuePair(PASSWORD, password));
        HttpEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(params);
        } catch (final UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
        final HttpPost post = new HttpPost(AUTH_URI);
        post.addHeader(entity.getContentType());
        post.setEntity(entity);

        try {
            resp = NetworkUtils.getHttpClient().execute(post);
            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                if (Log.isLoggable(TAG, Log.VERBOSE)) {
                    Log.v(TAG, "Successful authentication");
                }
                sendResult(true, handler, context);
                return true;
            } else {
                if (Log.isLoggable(TAG, Log.VERBOSE)) {
                    Log.v(TAG, "Error authenticating" + resp.getStatusLine());
                }
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
