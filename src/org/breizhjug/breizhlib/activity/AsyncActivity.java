package org.breizhjug.breizhlib.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import org.breizhjug.breizhlib.BreizhLib;


public abstract class AsyncActivity extends Activity{

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
		intentFilter.addAction("");
		registerReceiver(receiver, intentFilter);

        final ProgressDialog waitDialog = ProgressDialog.show(this,"recherche", "Chargement",true, true);

        final AsyncTask<Void, Void, Boolean> initTask = new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
                try {
					breizhLib = BreizhLib.getInstance(AsyncActivity.this);
				} catch (Exception ex) {
                    Log.d("ASYNC",ex.toString());
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
}
