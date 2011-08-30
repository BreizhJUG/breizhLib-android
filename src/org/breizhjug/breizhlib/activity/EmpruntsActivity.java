package org.breizhjug.breizhlib.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.inject.Inject;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.adapter.EmpruntsAdapter;
import org.breizhjug.breizhlib.model.Emprunt;
import org.breizhjug.breizhlib.model.Livre;
import org.breizhjug.breizhlib.remote.AsyncRemoteTask;
import org.breizhjug.breizhlib.remote.EmpruntService;
import org.breizhjug.breizhlib.utils.images.ImageCache;
import roboguice.inject.InjectView;

import java.util.ArrayList;


public class EmpruntsActivity extends AbstractActivity {

    @InjectView(R.id.items)
    ListView empruntsListView;

    @Inject
    EmpruntService service;
    @Inject
    private ImageCache imageCache;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items);
        initView();
    }

    @Override
    public void init(Intent intent) {
    }

    public void initView() {

        final AsyncTask<Void, Void, Boolean> initTask = new AsyncRemoteTask<Emprunt>(this, service, empruntsListView, prefs) {

            @Override
            public ArrayAdapter<Emprunt> getAdapter() {
                return new EmpruntsAdapter(EmpruntsActivity.this.getBaseContext(), items, prefs);
            }

            public void onClick(int position) {
                Emprunt emprunt = (Emprunt) empruntsListView.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), LivreActivity.class);
                intent.putExtra("items", toOuvrages(items));
                intent.putExtra("index", position);
                intent.putExtra("item", emprunt.livre);
                EmpruntsActivity.this.startActivity(intent);
            }
        };


        initTask.execute((Void) null);

    }

    private ArrayList<Livre> toOuvrages(ArrayList<Emprunt> items) {
        ArrayList<Livre> livres = new ArrayList<Livre>();

        for (Emprunt item : items) {
            livres.add(item.livre);
        }

        return livres;
    }


}
