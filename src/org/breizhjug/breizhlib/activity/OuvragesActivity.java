package org.breizhjug.breizhlib.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.adapter.OuvrageAdapter;
import org.breizhjug.breizhlib.model.Livre;
import org.breizhjug.breizhlib.remote.AsyncRemoteTask;


public class OuvragesActivity extends AbstractActivity {

    private AbsListView ouvragesListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init(getIntent());
    }

    @Override
    public void init(Intent intent) {
    }

    public void initView() {
        final SharedPreferences prefs = BreizhLib.getSharedPreferences(this);
        int resource = R.layout.ouvrage;
        if (prefs.getBoolean(BreizhLib.GRID, false)) {
            setContentView(R.layout.main);
            ouvragesListView = (GridView) findViewById(R.id.grilleBoutons);
            ((GridView) ouvragesListView).setNumColumns(4);
            resource = R.layout.ouvrage_simple;
        } else {
            setContentView(R.layout.items);
            ouvragesListView = (ListView) findViewById(R.id.items);
        }

        final ProgressDialog waitDialog = ProgressDialog.show(this, getString(R.string.recherche), getString(R.string.chargement), true, true);

        final int finalResource = resource;
        final AsyncTask<Void, Void, Boolean> initTask = new AsyncRemoteTask<Livre>(BreizhLib.getOuvrageService(),ouvragesListView, prefs) {

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                waitDialog.dismiss();
            }

            @Override
            public ArrayAdapter<Livre> getAdapter() {
                return new OuvrageAdapter(OuvragesActivity.this.getBaseContext(), items, finalResource, prefs);
            }

            public void onClick(int position) {
                Livre livre = (Livre) ouvragesListView.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), LivreActivity.class);
                intent.putExtra("livres", items);
                intent.putExtra("index", position);
                Populator.populate(intent, livre);
                OuvragesActivity.this.startActivity(intent);
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

        initTask.execute((Void)null);


    }


}
