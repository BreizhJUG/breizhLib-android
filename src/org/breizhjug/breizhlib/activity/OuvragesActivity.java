package org.breizhjug.breizhlib.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.adapter.OuvrageAdapter;
import org.breizhjug.breizhlib.model.Livre;
import org.breizhjug.breizhlib.remote.AsyncRemoteTask;


public class OuvragesActivity extends AbstractActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    private AbsListView ouvragesListView;

    private boolean modeGrid;
    private SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        modeGrid = prefs.getBoolean(BreizhLibConstantes.GRID, false);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    @Override
    public void init(Intent intent) {
    }

    public void initView() {
        prefs.registerOnSharedPreferenceChangeListener(this);
        int resource = R.layout.ouvrage;
        if (modeGrid) {
            setContentView(R.layout.main);
            ouvragesListView = (GridView) findViewById(R.id.grilleBoutons);
            ((GridView) ouvragesListView).setNumColumns(4);
            resource = R.layout.ouvrage_simple;
        } else {
            setContentView(R.layout.items);
            ouvragesListView = (ListView) findViewById(R.id.items);
        }

        final int finalResource = resource;
        final AsyncTask<Void, Void, Boolean> initTask = new AsyncRemoteTask<Livre>(this, BreizhLib.getOuvrageService(), ouvragesListView, prefs) {


            @Override
            public ArrayAdapter<Livre> getAdapter() {
                return new OuvrageAdapter(OuvragesActivity.this.getBaseContext(), items, finalResource, prefs);
            }

            public void onClick(int position) {
                Livre livre = (Livre) ouvragesListView.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), LivreActivity.class);
                intent.putExtra("livres", items);
                intent.putExtra("index", position);
                intent.putExtra("backActivity", "OuvragesActivity");
                Populator.populate(intent, livre);
                OuvragesActivity.this.startActivity(intent);
            }
        };
        initTask.execute((Void) null);


    }


    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if( s.equals(BreizhLibConstantes.GRID)){
          modeGrid = sharedPreferences.getBoolean(BreizhLibConstantes.GRID, false);
        }
    }
}
