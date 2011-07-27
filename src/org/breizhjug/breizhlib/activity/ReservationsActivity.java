package org.breizhjug.breizhlib.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import org.breizhjub.breizhlib.R;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.adapter.ReservationsAdapter;
import org.breizhjug.breizhlib.model.Reservation;
import org.breizhjug.breizhlib.remote.Service;

import java.util.List;


public class ReservationsActivity extends Activity {

    private ListView reservationsListView;

    private Service<Reservation> remoteCall = BreizhLib.getReservationService();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items);

        reservationsListView = (ListView) findViewById(R.id.items);

        List<Reservation> reservations = remoteCall.load();

        ReservationsAdapter mSchedule = new ReservationsAdapter(this.getBaseContext(), reservations);

        reservationsListView.setAdapter(mSchedule);

        reservationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressWarnings("unchecked")
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                Reservation reservation = (Reservation) reservationsListView.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), LivreActivity.class);
                intent.putExtra("titre", reservation.getLivre());
                intent.putExtra("img", reservation.getImgUrl());
                ReservationsActivity.this.startActivity(intent);
            }
        });

    }


}
