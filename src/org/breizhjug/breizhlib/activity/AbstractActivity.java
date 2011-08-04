package org.breizhjug.breizhlib.activity;

import android.app.ProgressDialog;
import android.content.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.R;


public abstract class AbstractActivity extends BaseActivity {

    protected BroadcastReceiver receiver;
    protected LayoutInflater layoutInflater;

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

        final ProgressDialog waitDialog = ProgressDialog.show(this, getString(R.string.recherche), getString(R.string.chargement), true, true);

        final AsyncTask<Void, Void, Boolean> initTask = new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    if (breizhLib == null) {
                        breizhLib = BreizhLib.getInstance();
                    }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
