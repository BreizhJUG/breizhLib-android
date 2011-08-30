package org.breizhjug.breizhlib.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.google.inject.Inject;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.adapter.ReservationsAdapter;
import org.breizhjug.breizhlib.model.Livre;
import org.breizhjug.breizhlib.model.Reservation;
import org.breizhjug.breizhlib.remote.AsyncRemoteTask;
import org.breizhjug.breizhlib.remote.ReservationService;
import org.breizhjug.breizhlib.utils.images.ImageCache;
import roboguice.inject.InjectView;

import java.util.ArrayList;


public class ReservationsActivity extends AbstractActivity {

    @InjectView(R.id.items)
    ListView reservationsListView;

    @Inject
    ReservationService service;
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

        final AsyncTask<Void, Void, Boolean> initTask = new AsyncRemoteTask<Reservation>(this, service, reservationsListView, prefs) {

            @Override
            public ArrayAdapter<Reservation> getAdapter() {
                return new ReservationsAdapter(ReservationsActivity.this.getBaseContext(), items, prefs);
            }

            public void onClick(int position) {
                Reservation reservation = (Reservation) reservationsListView.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), LivreActivity.class);
                intent.putExtra("items", toOuvrages(items));
                intent.putExtra("index", position);
                intent.putExtra("item", reservation.livre);
                ReservationsActivity.this.startActivity(intent);
            }

            public void displayEmptyMessage() {
                Toast.makeText(getApplicationContext(), "Aucune réservation ", Toast.LENGTH_SHORT).show();
            }
        };


        initTask.execute((Void) null);

    }

    private ArrayList<Livre> toOuvrages(ArrayList<Reservation> items) {
        ArrayList<Livre> livres = new ArrayList<Livre>();

        for (Reservation item : items) {
            livres.add(item.livre);
        }

        return livres;
    }


}
