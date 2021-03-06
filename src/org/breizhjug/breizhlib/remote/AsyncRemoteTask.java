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
import android.widget.Toast;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.model.Model;

import java.util.ArrayList;


public abstract class AsyncRemoteTask<T extends Model> extends AsyncTask<Void, Void, Boolean> {

    private Service<T> service;
    private SharedPreferences prefs;
    private AbsListView listView;
    public ArrayList<T> items = new ArrayList<T>();
    private ProgressDialog waitDialog;
    Activity context;

   private int dialogueTitle = R.string.chargement;

    public AsyncRemoteTask(final Activity context, Service<T> service, AbsListView listView, SharedPreferences prefs) {
        this.service = service;
        this.listView = listView;
        this.prefs = prefs;
        this.context = context;
    }
    
    public void setDialogTitle(int dialogueTitle){
       this.dialogueTitle = dialogueTitle;
    }

    @Override
    protected void onPreExecute() {
        if (waitDialog == null) {
            waitDialog = new ProgressDialog(context);
            waitDialog.setIndeterminate(true);
        }

        waitDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            public void onCancel(DialogInterface dialog) {
                if (AsyncRemoteTask.this != null) {
                    AsyncRemoteTask.this.cancel(true);
                }
                context.finish();
            }
        });
        waitDialog.setTitle(context.getString(dialogueTitle));
        waitDialog.setMessage(context.getString(R.string.recherche));
        waitDialog.show();
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (waitDialog != null)
            waitDialog.dismiss();
        if (items != null && items.isEmpty()) {
            displayEmptyMessage();
        } else {
            ArrayAdapter<T> mSchedule = getAdapter();
            listView.setAdapter(mSchedule);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                    onClick(position);
                }
            });
        }
    }

    public void displayEmptyMessage() {
        Toast.makeText(context, R.string.no_info, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Boolean doInBackground(Void... objects) {
        items.addAll(service.load(prefs.getString(BreizhLibConstantes.AUTH_COOKIE, null)));
        return true;
    }

    public abstract ArrayAdapter<T> getAdapter();

    public abstract void onClick(int position);


}
