package org.breizhjug.breizhlib.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.activity.compte.CompteList;


public abstract class AbstractActivity extends Activity {

    public static final String ACTION_LOGOUT = "org.breizhjug.breizhlib.LOGOUT";

    protected BroadcastReceiver receiver;
    protected LayoutInflater layoutInflater;

    protected BreizhLib breizhLib;

    public abstract void init(Intent intent);

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layoutInflater = this.getLayoutInflater();

        receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }

        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_LOGOUT);
        registerReceiver(receiver, intentFilter);

        final ProgressDialog waitDialog = ProgressDialog.show(this, "recherche", "Chargement", true, true);

        final AsyncTask<Void, Void, Boolean> initTask = new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    breizhLib = BreizhLib.getInstance();
                } catch (Exception ex) {
                    Log.d("ASYNC", ex.toString());
                    return false;
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                waitDialog.dismiss();

                if (result == null || !result) {
                    showError("Error", true);
                } else {
                    init(getIntent());
                }
            }
        };
        waitDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            public void onCancel(DialogInterface dialog) {
                if (initTask != null) {
                    initTask.cancel(true);
                }
                finish();
            }
        });

        initTask.execute(null);
    }


    protected void showError(String message) {
        showError(message, false);
    }

    protected void showError(String message, final boolean finish) {
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setMessage(message);
        build.setPositiveButton("Ok",
                new android.content.DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (finish)
                            finish();
                    }

                });
        build.create().show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
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
                    editor.putString(breizhLib.AUTH_COOKIE, null);
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
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
				shareIntent.setType("text/plain");
				shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.app_name));
				shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Partager");
				startActivity(Intent.createChooser(shareIntent, getString(R.string.app_name)));
                return true;
             case R.id.apropos:
                 Intent pIntent = new Intent(getApplicationContext(), CreditsActivity.class);
                    pIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(pIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
