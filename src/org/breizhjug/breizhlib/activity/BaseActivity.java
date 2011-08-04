package org.breizhjug.breizhlib.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.activity.compte.CompteList;
import org.breizhjug.breizhlib.utils.IntentSupport;


public class BaseActivity extends Activity {

    public static final String ACTION_LOGOUT = "org.breizhjug.breizhlib.LOGOUT";


    protected BreizhLib breizhLib;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        breizhLib = BreizhLib.getInstance();
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
        SharedPreferences prefs = breizhLib.getSharedPreferences(this);
        String authCookie = prefs.getString(breizhLib.AUTH_COOKIE, null);
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
        Intent intent = new Intent(getApplicationContext(), Menu.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        switch (item.getItemId()) {
            case R.id.accueil:
                startActivity(intent);
                return true;
            case R.id.connexion:
                SharedPreferences prefs = breizhLib.getSharedPreferences(this);
                String authCookie = prefs.getString(breizhLib.AUTH_COOKIE, null);
                if (authCookie == null) {
                    Intent pIntent = new Intent(getApplicationContext(), CompteList.class);
                    pIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(pIntent);
                } else {
                    onLogout();
                    startActivity(intent);
                }
                return true;
            case R.id.quitter:
                onExit();
                return true;
            case R.id.share:
                Intent pIntent = IntentSupport.newShareIntent(this, getString(R.string.app_name), getString(R.string.shareText), getString(R.string.app_name));
                startActivity(pIntent);
                return true;
            case R.id.apropos:
                pIntent = new Intent(getApplicationContext(), CreditsActivity.class);
                pIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(pIntent);
                return true;
            case R.id.parametre:
                pIntent = new Intent(getApplicationContext(), ConfigurationActivity.class);
                pIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(pIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onLogout() {
        SharedPreferences.Editor editor = breizhLib.getSharedPreferences(this).edit();
        editor.putString(BreizhLib.AUTH_COOKIE, null);
        editor.putString(BreizhLib.ACCOUNT_NAME, null);
        editor.putString(BreizhLib.USER, null);
        editor.putString(BreizhLib.USER_NOM, null);
        editor.putString(BreizhLib.USER_PRENOM, null);
        editor.putString(BreizhLib.USER_ADMIN, null);
        editor.commit();
    }

    private void onExit() {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(ACTION_LOGOUT);
        sendBroadcast(broadcastIntent);
    }

}
