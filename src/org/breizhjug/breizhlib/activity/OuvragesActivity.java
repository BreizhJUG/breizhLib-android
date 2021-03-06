package org.breizhjug.breizhlib.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.*;
import com.google.inject.Inject;
import greendroid.widget.ActionBarItem;
import greendroid.widget.LoaderActionBarItem;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.activity.common.AbstractGDActivity;
import org.breizhjug.breizhlib.adapter.OuvrageAdapter;
import org.breizhjug.breizhlib.model.Livre;
import org.breizhjug.breizhlib.remote.AsyncRemoteTask;
import org.breizhjug.breizhlib.remote.OuvrageService;
import org.breizhjug.breizhlib.utils.images.ImageCache;

import static org.breizhjug.breizhlib.utils.IntentSupport.newLivreIntent;


public class OuvragesActivity extends AbstractGDActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "Breizhlib.OuvragesActivity";

    private AbsListView ouvragesListView;

    private boolean modeGrid;

    @Inject
    private ImageCache imageCache;

    @Inject
    private OuvrageService service;


    @Override
    protected void onResume() {
        super.onResume();
        initView(null);
    }

    @Override
    public void init(Intent intent) {
        modeGrid = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(BreizhLibConstantes.GRID, false);
        initView(null);
        addActionBarItem(ActionBarItem.Type.Refresh, R.id.action_bar_refresh);
        getActionBar().setTitle(getText(R.string.ouvrages_title));
    }

    public void initView(final LoaderActionBarItem loaderItem) {
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).registerOnSharedPreferenceChangeListener(this);
        int resource = R.layout.ouvrage;
        if (modeGrid) {
            setActionBarContentView(R.layout.main);
            ouvragesListView = (GridView) findViewById(R.id.grilleBoutons);
            ((GridView) ouvragesListView).setNumColumns(4);
            resource = R.layout.ouvrage_simple;
        } else {
            if (prefs.getBoolean("beta", false)) {
                setActionBarContentView(R.layout.items_search);
                EditText editText = (EditText) findViewById(R.id.editText);
                editText.clearFocus();
            } else {
                setActionBarContentView(R.layout.items);
            }
            ouvragesListView = (ListView) findViewById(R.id.items);
        }

        final int finalResource = resource;
        final AsyncRemoteTask<Livre> initTask = new AsyncRemoteTask<Livre>(this, service, ouvragesListView, prefs) {

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if (loaderItem != null)
                    loaderItem.setLoading(false);
            }

            @Override
            protected void onPreExecute() {
                if (loaderItem == null)
                    super.onPreExecute();
            }

            @Override
            public ArrayAdapter<Livre> getAdapter() {
                return new OuvrageAdapter(OuvragesActivity.this.getBaseContext(), items, finalResource);
            }

            public void onClick(int position) {
                Livre livre = (Livre) ouvragesListView.getItemAtPosition(position);
                startActivity(newLivreIntent(getApplicationContext(), items, position, livre));
            }
        };
        initTask.setDialogTitle(R.string.ouvrages_title);
        initTask.execute((Void) null);
    }


    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(BreizhLibConstantes.GRID)) {
            Log.d(TAG, "grid " + s);
            modeGrid = sharedPreferences.getBoolean(BreizhLibConstantes.GRID, false);
        }
    }

    @Override
    public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
        Log.d("ActionBar ", "" + item.getItemId());
        switch (item.getItemId()) {
            case R.id.action_bar_refresh:
                final LoaderActionBarItem loaderItem = (LoaderActionBarItem) item;
                service.clearDBCache();
                initView(loaderItem);
                return true;
            default:
                return super.onHandleActionBarItemClick(item, position);
        }
    }
}
