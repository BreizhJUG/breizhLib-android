package org.breizhjug.breizhlib.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.adapter.ReservationsAdapter;
import org.breizhjug.breizhlib.model.Reservation;

import java.util.List;


public class ReservationsActivity extends AbstractActivity {

    private ListView reservationsListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items);
    }

    @Override
    public void init(Intent intent) {
        reservationsListView = (ListView) findViewById(R.id.items);
        SharedPreferences prefs = breizhLib.getSharedPreferences(this);
        String authCookie = prefs.getString(breizhLib.AUTH_COOKIE, null);
        List<Reservation> reservations = breizhLib.getReservationService().load(authCookie);

        ReservationsAdapter mSchedule = new ReservationsAdapter(this.getBaseContext(), reservations, prefs);

        reservationsListView.setAdapter(mSchedule);

        reservationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressWarnings("unchecked")
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                Reservation reservation = (Reservation) reservationsListView.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), LivreActivity.class);
                Populator.populate(intent, reservation);
                ReservationsActivity.this.startActivity(intent);
            }
        });

    }


}
