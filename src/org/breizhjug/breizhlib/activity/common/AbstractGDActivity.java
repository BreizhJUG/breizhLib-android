package org.breizhjug.breizhlib.activity.common;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import greendroid.widget.ActionBarItem;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.activity.ConfigurationActivity;
import org.breizhjug.breizhlib.activity.CreditsActivity;
import org.breizhjug.breizhlib.utils.IntentSupport;


public abstract class AbstractGDActivity extends GDBaseActivity {

    protected LayoutInflater layoutInflater;

    public abstract void init(Intent intent);

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActionBar().setTitle("...");
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

    @Override
    public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
        Log.d("ActionBar ", "" + item.getItemId());
        switch (item.getItemId()) {
            case R.id.action_bar_share:
                Intent pIntent = IntentSupport.newShareIntent(this, getString(R.string.app_name), getString(R.string.shareText), getString(R.string.app_name));
                startActivity(pIntent);
                return true;
            case R.id.action_bar_info:
                pIntent = new Intent(getApplicationContext(), CreditsActivity.class);
                pIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(pIntent);
                return true;
            case R.id.action_bar_settings:
                pIntent = new Intent(getApplicationContext(), ConfigurationActivity.class);
                pIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(pIntent);
                return true;
            default:

                return super.onHandleActionBarItemClick(item, position);
        }
    }

}
