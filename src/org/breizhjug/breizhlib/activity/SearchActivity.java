package org.breizhjug.breizhlib.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.inject.Inject;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.activity.gd.AbstractGDActivity;
import org.breizhjug.breizhlib.adapter.OuvrageAdapter;
import org.breizhjug.breizhlib.model.Livre;
import org.breizhjug.breizhlib.remote.AsyncRemoteTask;
import org.breizhjug.breizhlib.remote.OuvrageService;
import org.breizhjug.breizhlib.utils.images.ImageCache;

import java.util.HashMap;
import java.util.List;


public class SearchActivity extends AbstractGDActivity {

    private String[] from;
    private int[] to;
    private List<HashMap<String, String>> data;
    @Inject
    private OuvrageService service;
    @Inject
    private ImageCache imageCache;

    @Override
    public void init(Intent intent) {

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        setActionBarContentView(R.layout.items);
        final AbsListView ouvragesListView = (ListView) findViewById(R.id.items);
        final Intent queryIntent = getIntent();
        final String queryAction = queryIntent.getAction();
        if (Intent.ACTION_SEARCH.equals(queryAction)) {
            String searchKeywords = queryIntent.getStringExtra(SearchManager.QUERY);
        if(searchKeywords != "" && searchKeywords != null){
            final AsyncTask<Void, Void, Boolean> initTask = new AsyncRemoteTask<Livre>(this, service, ouvragesListView, prefs) {


                @Override
                public ArrayAdapter<Livre> getAdapter() {
                    return new OuvrageAdapter(SearchActivity.this.getBaseContext(), items, R.layout.ouvrage, prefs);
                }

                public void onClick(int position) {
                    Livre livre = (Livre) ouvragesListView.getItemAtPosition(position);
                    Intent intent = new Intent(getApplicationContext(), LivreActivity.class);
                    intent.putExtra("livres", items);
                    intent.putExtra("index", position);
                    intent.putExtra("livre", livre);
                    SearchActivity.this.startActivity(intent);
                }
            };
            initTask.execute((Void) null);
        }

        }else{
            onSearchRequested();
        }
    }
}