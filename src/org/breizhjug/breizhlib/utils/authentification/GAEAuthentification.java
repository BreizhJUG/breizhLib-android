package org.breizhjug.breizhlib.utils.authentification;


import android.accounts.*;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.model.Utilisateur;
import org.breizhjug.breizhlib.utils.authentification.Authentification;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class GAEAuthentification implements Authentification {

    private static final String TAG = "Breizhlib.GAEAuthentification";

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

    public String getAuthCookie(String authToken, Context context) {
        try {
            // Get ACSID cookie
            DefaultHttpClient client = new DefaultHttpClient();
            Log.d(TAG, "url : " + BreizhLibConstantes.SERVER_URL);
            String continueURL = BreizhLibConstantes.SERVER_URL;
            URI uri = new URI(BreizhLibConstantes.SERVER_URL + "/_ah/login?continue="
                    + URLEncoder.encode(continueURL, "UTF-8") + "&auth=" + authToken);
            HttpGet method = new HttpGet(uri);

            final HttpParams getParams = new BasicHttpParams();
            HttpClientParams.setRedirecting(getParams, false);
            method.setParams(getParams);
            HttpResponse res = client.execute(method);
            Header[] headers = res.getHeaders("Set-Cookie");
            Log.d(TAG, "reponse code : " + res.getStatusLine().getStatusCode());
            if (res.getStatusLine().getStatusCode() != HttpStatus.SC_UNAUTHORIZED) {
                Log.d("COOKIE", "erreur reponse : " + res.getStatusLine().getStatusCode());
                AccountManager accountManager = AccountManager.get(context);
                accountManager.invalidateAuthToken("com.google", authToken);
                return null;
            }
            for (Cookie cookie : client.getCookieStore().getCookies()) {
                Log.d(TAG, "" + cookie.getName() + " " + cookie.getValue());
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

    @Override
    public void saveInfos(Utilisateur user, Context context) {
        // nothing to do
    }


    @Override
    public Intent getAuthentificationIntent(Context context) {
        return new Intent(Settings.ACTION_ADD_ACCOUNT);
    }

    @Override
    public String getAccountType() {
        return "com.google";
    }

    public GAEAuthentification() {
        super();
    }

    public Cookie getAuthCookie() {
        return authCookie;
    }
}
