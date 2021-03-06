package org.breizhjug.breizhlib.activity.common;

import android.app.AlertDialog;
import android.content.*;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.inject.Inject;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.database.Database;
import org.breizhjug.breizhlib.utils.IntentSupport;
import org.breizhjug.breizhlib.utils.Tracker;
import roboguice.activity.RoboActivity;
import static org.breizhjug.breizhlib.BreizhLibConstantes.*;


public class BaseActivity extends RoboActivity {


    protected BroadcastReceiver receiver;
    @Inject
    protected SharedPreferences prefs;
    @Inject
    private Tracker tracker;
    @Inject
    protected Database db;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tracker.trackPageView('/' + getClass().getSimpleName());


        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_LOGOUT);
        registerReceiver(receiver, intentFilter);
    }

    protected void showError(String message) {
        showError(message, false);
    }

    protected void showError(String message, final boolean finish) {
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setMessage(message);
        build.setPositiveButton(getString(R.string.ok),
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (finish)
                            finish();
                    }

                });
        build.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        String authCookie = prefs.getString(AUTH_COOKIE, null);
        if (authCookie == null) {
            MenuItem item = (MenuItem) menu.findItem(R.id.connexion);
            String message = getString(R.string.connexion);
            item.setTitle(message);
        } else {
            MenuItem item = (MenuItem) menu.findItem(R.id.connexion);
            String message = getString(R.string.deconnexion);
            item.setTitle(message);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.connexion:
                String authCookie = prefs.getString(BreizhLibConstantes.AUTH_COOKIE, null);
                if (authCookie == null) {
                    startActivity(IntentSupport.newComptesIntent(getApplicationContext()));
                } else {
                    onLogout();
                    startActivity(IntentSupport.newMenuIntent(getApplicationContext()));
                }
                return true;
            case R.id.quitter:
                onExit();
                return true;
            case R.id.share:
                Intent pIntent = IntentSupport.newShareIntent(this, getString(R.string.app_name), getString(R.string.shareText), getString(R.string.app_name));
                startActivity(pIntent);
                return true;
            case R.id.parametre:
                startActivity(IntentSupport.newConfigurationIntent(getApplicationContext()));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onLogout() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(AUTH_COOKIE, null);
        editor.putString(ACCOUNT_NAME, null);
        editor.putString(USER, null);
        editor.putString(USER_NOM, null);
        editor.putString(USER_PRENOM, null);
        editor.putString(USER_ADMIN, null);
        editor.commit();
    }

    private void onExit() {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(ACTION_LOGOUT);
        sendBroadcast(broadcastIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
        unregisterReceiver(receiver);
    }
}
