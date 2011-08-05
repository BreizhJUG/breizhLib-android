package org.breizhjug.breizhlib.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.adapter.OuvrageAdapter;
import org.breizhjug.breizhlib.model.Livre;

import java.util.ArrayList;


public class OuvragesActivity extends AbstractActivity {

    private AbsListView ouvragesListView;

    private ArrayList<Livre> ouvrages;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        init(getIntent());
    }

    @Override
    public void init(Intent intent) {
        SharedPreferences prefs = BreizhLib.getSharedPreferences(this);
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

        ouvrages = new ArrayList<Livre>();
        ouvrages.addAll(BreizhLib.getOuvrageService().load(prefs.getString(BreizhLib.AUTH_COOKIE, null)));

        OuvrageAdapter mSchedule = new OuvrageAdapter(this.getBaseContext(), ouvrages, resource, prefs);

        ouvragesListView.setAdapter(mSchedule);

        ouvragesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                onClick(position);
            }
        });

    }

    private void onClick(int position) {
        Livre livre = (Livre) ouvragesListView.getItemAtPosition(position);
        Intent intent = new Intent(getApplicationContext(), LivreActivity.class);
        intent.putExtra("livres", ouvrages);
        intent.putExtra("index", position);
        Populator.populate(intent, livre);
        OuvragesActivity.this.startActivity(intent);
    }


}
