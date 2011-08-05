package org.breizhjug.breizhlib.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import org.breizhjug.breizhlib.R;


public abstract class AbstractActivity extends BaseActivity {


    protected LayoutInflater layoutInflater;

    public abstract void init(Intent intent);

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layoutInflater = this.getLayoutInflater();



        final ProgressDialog waitDialog = ProgressDialog.show(this, getString(R.string.recherche), getString(R.string.chargement), true, true);

        final AsyncTask<Void, Void, Boolean> initTask = new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
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
