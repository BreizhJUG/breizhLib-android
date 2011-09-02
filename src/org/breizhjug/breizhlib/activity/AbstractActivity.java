package org.breizhjug.breizhlib.activity;

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
        final AsyncTask<Void, Void, Boolean> initTask = new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                return true;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (result == null || !result) {
                    showError(getString(R.string.error), true);
                } else {
                    init(getIntent());
                }
            }
        };
        initTask.execute((Void) null);
    }

}
