package org.breizhjug.breizhlib.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.adapter.OuvrageAdapter;
import org.breizhjug.breizhlib.model.Livre;

import java.util.List;


public class OuvragesActivity extends AbstractActivity {

    private ListView ouvragesListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items);

    }

    @Override
    public void init(Intent intent) {
        ouvragesListView = (ListView) findViewById(R.id.items);
        SharedPreferences prefs = breizhLib.getSharedPreferences(this);
        String authCookie = prefs.getString(breizhLib.AUTH_COOKIE, null);
        List<Livre> books = breizhLib.getOuvrageService().load(authCookie);

        OuvrageAdapter mSchedule = new OuvrageAdapter(this.getBaseContext(), books);

        ouvragesListView.setAdapter(mSchedule);

        ouvragesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressWarnings("unchecked")
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                Livre livre = (Livre) ouvragesListView.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), LivreActivity.class);
                Populator.populate(intent, livre);
                OuvragesActivity.this.startActivity(intent);
            }
        });

    }


}
