package org.breizhjug.breizhlib.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import org.breizhjub.breizhlib.R;
import org.breizhjug.breizhlib.adapter.OuvrageAdapter;
import org.breizhjug.breizhlib.model.Livre;
import org.breizhjug.breizhlib.remote.OuvrageService;
import org.breizhjug.breizhlib.remote.Service;

import java.util.List;


public class OuvragesActivity extends Activity {

    private ListView ouvragesListView;

    private Service<Livre> remoteCall = new OuvrageService();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items);

        ouvragesListView = (ListView) findViewById(R.id.items);

        List<Livre> books = remoteCall.load();

        OuvrageAdapter mSchedule = new OuvrageAdapter(this.getBaseContext(), books);

        ouvragesListView.setAdapter(mSchedule);

        ouvragesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressWarnings("unchecked")
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                Livre livre = (Livre) ouvragesListView.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), LivreActivity.class);
                intent.putExtra("titre",livre.getTitre());
                intent.putExtra("editeur",livre.getEditeur());
                intent.putExtra("img",livre.getImgUrl());
                OuvragesActivity.this.startActivity(intent);
            }
        });

    }


}
