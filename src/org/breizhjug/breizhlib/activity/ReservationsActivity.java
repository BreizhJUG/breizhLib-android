package org.breizhjug.breizhlib.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.adapter.ReservationsAdapter;
import org.breizhjug.breizhlib.model.Reservation;
import org.breizhjug.breizhlib.remote.AsyncRemoteTask;


public class ReservationsActivity extends AbstractActivity {

    private ListView reservationsListView;

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
        reservationsListView = (ListView) findViewById(R.id.items);
        final SharedPreferences prefs = BreizhLib.getSharedPreferences(this);

        final AsyncTask<Void, Void, Boolean> initTask = new AsyncRemoteTask<Reservation>(this, BreizhLib.getReservationService(), reservationsListView, prefs) {

            @Override
            public ArrayAdapter<Reservation> getAdapter() {
                return new ReservationsAdapter(ReservationsActivity.this.getBaseContext(), items, prefs);
            }

            public void onClick(int position) {
                Reservation reservation = (Reservation) reservationsListView.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), LivreActivity.class);
                Populator.populate(intent, reservation);
                ReservationsActivity.this.startActivity(intent);
            }
        };


        initTask.execute((Void) null);

    }


}
