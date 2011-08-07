package org.breizhjug.breizhlib.remote;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import org.breizhjug.breizhlib.BreizhLib;

import java.util.ArrayList;


public abstract class AsyncRemoteTask<T> extends AsyncTask<Void, Void, Boolean> {

    Service<T> service;
    SharedPreferences prefs;
    AbsListView listView;
    public ArrayList<T> items = new ArrayList<T>();

    public AsyncRemoteTask(Service<T> service, AbsListView listView, SharedPreferences prefs) {
        this.service = service;
        this.listView = listView;
        this.prefs = prefs;
    }

    @Override
    protected void onPostExecute(Boolean result) {
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
        items.addAll(service.load(prefs.getString(BreizhLib.AUTH_COOKIE, null)));
        return true;
    }

    public abstract ArrayAdapter<T> getAdapter();

    public abstract void onClick(int position);


}
