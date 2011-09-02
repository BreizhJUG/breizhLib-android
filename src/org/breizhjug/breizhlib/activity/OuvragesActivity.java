package org.breizhjug.breizhlib.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.*;
import com.google.inject.Inject;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.adapter.OuvrageAdapter;
import org.breizhjug.breizhlib.model.Livre;
import org.breizhjug.breizhlib.remote.AsyncRemoteTask;
import org.breizhjug.breizhlib.remote.OuvrageService;
import org.breizhjug.breizhlib.utils.images.ImageCache;


public class OuvragesActivity extends AbstractActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "Breizhlib.OuvragesActivity";
    private AbsListView ouvragesListView;

    private boolean modeGrid;

    @Inject
    private ImageCache imageCache;

    @Inject
    private OuvrageService service;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        modeGrid = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(BreizhLibConstantes.GRID, false);
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
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).registerOnSharedPreferenceChangeListener(this);
        int resource = R.layout.ouvrage;
        if (modeGrid) {
            setContentView(R.layout.main);
            ouvragesListView = (GridView) findViewById(R.id.grilleBoutons);
            ((GridView) ouvragesListView).setNumColumns(4);
            resource = R.layout.ouvrage_simple;
        } else {


            if (prefs.getBoolean("beta", false)) {
                setContentView(R.layout.items_search);
                EditText editText = (EditText) findViewById(R.id.editText);
                editText.clearFocus();
            } else {
                setContentView(R.layout.items);
            }
            ouvragesListView = (ListView) findViewById(R.id.items);
        }

        final int finalResource = resource;
        final AsyncTask<Void, Void, Boolean> initTask = new AsyncRemoteTask<Livre>(this, service, ouvragesListView, prefs) {


            @Override
            public ArrayAdapter<Livre> getAdapter() {
                return new OuvrageAdapter(OuvragesActivity.this.getBaseContext(), items, finalResource, prefs);
            }

            public void onClick(int position) {
                Livre livre = (Livre) ouvragesListView.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), LivreActivity.class);
                intent.putExtra("items", items);
                intent.putExtra("index", position);
                intent.putExtra("item", livre);
                OuvragesActivity.this.startActivity(intent);
            }
        };
        initTask.execute((Void) null);


    }


    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(BreizhLibConstantes.GRID)) {
            Log.d(TAG, "grid " + s);
            modeGrid = sharedPreferences.getBoolean(BreizhLibConstantes.GRID, false);
        }
    }
}
