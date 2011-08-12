package org.breizhjug.breizhlib.remote;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.model.Model;

import java.util.ArrayList;


public abstract class AsyncRemoteTask<T extends Model> extends AsyncTask<Void, Void, Boolean> {

    Service<T> service;
    SharedPreferences prefs;
    AbsListView listView;
    public ArrayList<T> items = new ArrayList<T>();
    private ProgressDialog waitDialog;

    public AsyncRemoteTask(final Activity context, Service<T> service, AbsListView listView, SharedPreferences prefs) {
        this.service = service;
        this.listView = listView;
        this.prefs = prefs;
        this.waitDialog = ProgressDialog.show(context, context.getString(R.string.recherche), context.getString(R.string.chargement), true, true);
        waitDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            public void onCancel(DialogInterface dialog) {
                if (AsyncRemoteTask.this != null) {
                    AsyncRemoteTask.this.cancel(true);
                }
                context.finish();
            }
        });
    }

    @Override
    protected void onPostExecute(Boolean result) {
        waitDialog.dismiss();
        ArrayAdapter<T> mSchedule = getAdapter();
        listView.setAdapter(mSchedule);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                onClick(position);
            }
        });
    }

    @Override
    protected Boolean doInBackground(Void... objects) {
        items.addAll(service.load(prefs.getString(BreizhLibConstantes.AUTH_COOKIE, null)));
        return true;
    }

    public abstract ArrayAdapter<T> getAdapter();

    public abstract void onClick(int position);


}
