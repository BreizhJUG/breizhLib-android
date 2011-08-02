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


public class BaseActivity extends Activity {

    public static final String ACTION_LOGOUT = "org.breizhjug.breizhlib.LOGOUT";
    ;

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
        build.setPositiveButton("Ok",
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
            String message = "Connexion";
            item.setTitle(message);
        } else {
            MenuItem item = (MenuItem) menu.findItem(R.id.connexion);
            String message = "Deconnexion";
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
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(BreizhLib.AUTH_COOKIE, null);
                    editor.putString(BreizhLib.ACCOUNT_NAME, null);
                    editor.putString(BreizhLib.USER, null);
                    editor.putString(BreizhLib.USER_NOM, null);
                    editor.putString(BreizhLib.USER_PRENOM, null);
                    editor.putString(BreizhLib.USER_ADMIN, null);
                    editor.commit();
                    startActivity(intent);
                }
                return true;
            case R.id.quitter:
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(ACTION_LOGOUT);
                sendBroadcast(broadcastIntent);
                return true;
            case R.id.share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.shareText));
                startActivity(Intent.createChooser(shareIntent, getString(R.string.app_name)));
                return true;
            case R.id.apropos:
                Intent pIntent = new Intent(getApplicationContext(), CreditsActivity.class);
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

}
