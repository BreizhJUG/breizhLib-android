package org.breizhjug.breizhlib.utils;


import android.accounts.*;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.breizhjug.breizhlib.BreizhLib;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class GoogleAuthentification {

    private static final String TAG = GoogleAuthentification.class.getSimpleName();

    private static GoogleAuthentification instance;
    private Cookie authCookie = null;

    /**
     * Cookie name for authorization.
     */
    private static final String AUTH_COOKIE_NAME = "ACSID";


    public String getToken(Context context, Account account) {
        AccountManager mgr = AccountManager.get(context);
        Log.v("Account", account.name);
        AccountManagerFuture<Bundle> accountManagerFuture = mgr.getAuthToken(account, "ah", null, (Activity) context, null, null);
        Bundle authTokenBundle = null;
        try {
            authTokenBundle = accountManagerFuture.getResult();
        } catch (OperationCanceledException e) {
           Log.w(TAG, "Got OperationCanceledException " + e);
            Log.w(TAG, Log.getStackTraceString(e));
        } catch (AuthenticatorException e) {
            Log.w(TAG, "Got AuthenticatorException " + e);
            Log.w(TAG, Log.getStackTraceString(e));
        } catch (IOException e) {
           Log.w(TAG, "Got OperationCanceledException " + e);
            Log.w(TAG, Log.getStackTraceString(e));
        }
        String authToken = authTokenBundle.get(AccountManager.KEY_AUTHTOKEN).toString();
        Log.v("TOKEN", authToken);

        return authToken;


    }

    public String getAuthCookie(String authToken) {
        try {
            // Get SACSID cookie
            DefaultHttpClient client = new DefaultHttpClient();
            String continueURL = BreizhLib.SERVER_URL;
            URI uri = new URI(BreizhLib.SERVER_URL + "/_ah/login?continue="
                    + URLEncoder.encode(continueURL, "UTF-8") + "&auth=" + authToken);
            HttpGet method = new HttpGet(uri);
            final HttpParams getParams = new BasicHttpParams();
            HttpClientParams.setRedirecting(getParams, false);
            method.setParams(getParams);

            HttpResponse res = client.execute(method);
            Header[] headers = res.getHeaders("Set-Cookie");
            if (res.getStatusLine().getStatusCode() != 302 || headers.length == 0) {
                return null;
            }

            for (Cookie cookie : client.getCookieStore().getCookies()) {
                Log.d(TAG, "" + cookie.getName());
                if (AUTH_COOKIE_NAME.equals(cookie.getName())) {
                    authCookie = cookie;
                    return AUTH_COOKIE_NAME + "=" + cookie.getValue();
                }
            }
        } catch (IOException e) {
            Log.w(TAG, "Got IOException " + e);
            Log.w(TAG, Log.getStackTraceString(e));
        } catch (URISyntaxException e) {
            Log.w(TAG, "Got URISyntaxException " + e);
            Log.w(TAG, Log.getStackTraceString(e));
        }

        return null;
    }

    public List<String> getGoogleAccounts(Context context) {
        ArrayList<String> result = new ArrayList<String>();
        Account[] accounts = AccountManager.get(context).getAccounts();
        for (Account account : accounts) {
            if (account.type.equals("com.google")) {
                result.add(account.name);
            }
        }

        return result;
    }


    public GoogleAuthentification() {
        super();
    }

    public static synchronized GoogleAuthentification getInstance() {
        if (instance == null) {
            instance = new GoogleAuthentification();
        }
        return instance;
    }

    public Cookie getAuthCookie() {
        return authCookie;
    }
}
