package org.breizhjug.breizhlib.activity.compte;

import android.accounts.*;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.breizhjub.breizhlib.R;
import org.breizhjug.breizhlib.activity.ProfilActivity;
import org.breizhjug.breizhlib.remote.Service;

import java.io.IOException;

public class AppInfo extends Activity {
    DefaultHttpClient http_client = new DefaultHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_info);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        AccountManager accountManager = AccountManager.get(getApplicationContext());
        Account account = (Account) intent.getExtras().get("account");
        accountManager.getAuthToken(account, "ah", false, new GetAuthTokenCallback(), null);


        intent = new Intent(this, ProfilActivity.class);
        intent.putExtra("email", account.name);
        startActivity(intent);
    }

    private class GetAuthTokenCallback implements AccountManagerCallback<Bundle> {
        public void run(AccountManagerFuture<Bundle> result) {
            Bundle bundle;
            try {
                bundle = result.getResult();
                Intent intent = (Intent) bundle.get(AccountManager.KEY_INTENT);
                if (intent != null) {
                    // User input required
                    startActivity(intent);
                } else {
                    onGetAuthToken(bundle);
                }
            } catch (OperationCanceledException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (AuthenticatorException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    ;

    protected void onGetAuthToken(Bundle bundle) {
        String auth_token = bundle.getString(AccountManager.KEY_AUTHTOKEN);
        new GetCookieTask().execute(auth_token);
    }

    private class GetCookieTask extends AsyncTask<String, Void, Boolean> {
        protected Boolean doInBackground(String... tokens) {
            try {
                // Don't follow redirects
                http_client.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, false);

                HttpGet http_get = new HttpGet("https://breizh-lib.appspot.com/_ah/login?continue=http://localhost/&auth=" + tokens[0]);
                HttpResponse response;
                response = http_client.execute(http_get);
                if (response.getStatusLine().getStatusCode() != 302)
                    // Response should be a redirect
                    return false;

                for (Cookie cookie : http_client.getCookieStore().getCookies()) {
                    if (cookie.getName().equals("ACSID"))
                        return true;
                }
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                http_client.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, true);
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            new AuthenticatedRequestTask().execute("http://breizh-lib.appspot.com/login-gae");
        }
    }

    private class AuthenticatedRequestTask extends AsyncTask<String, Void, HttpResponse> {
        @Override
        protected HttpResponse doInBackground(String... urls) {
            try {
                HttpGet http_get = new HttpGet(urls[0]);
                return http_client.execute(http_get);
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(HttpResponse result) {
            try {
                String retour = Service.convertStreamToString(result.getEntity().getContent());
                Log.d("GAE", retour);
                Toast.makeText(getApplicationContext(), retour, Toast.LENGTH_LONG).show();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
