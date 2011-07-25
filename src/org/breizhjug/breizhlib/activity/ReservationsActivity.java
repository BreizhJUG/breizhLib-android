package org.breizhjug.breizhlib.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import org.breizhjub.breizhlib.R;
import org.breizhjug.breizhlib.model.Reservation;
import org.breizhjug.breizhlib.remote.ReservationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ReservationsActivity extends Activity {

    private ListView reservationsListView;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items);

        reservationsListView = (ListView) findViewById(R.id.items);

        ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;

        ReservationService remoteCall = new ReservationService();
        List<Reservation> reservations = remoteCall.load();

        for (Reservation reservation : reservations) {
            map = new HashMap<String, String>();
            map.put("livre", reservation.getLivre());
            map.put("nom", reservation.getNom()+" "+reservation.getPrenom());
            map.put("isbn", reservation.getiSBN());
            map.put("image", reservation.getImgUrl());
            listItem.add(map);
        }

        SimpleAdapter mSchedule = new SimpleAdapter(this.getBaseContext(), listItem, R.layout.reservation,
                new String[]{"image", "livre", "nom"}, new int[]{R.id.img, R.id.livre, R.id.nom});

        reservationsListView.setAdapter(mSchedule);

        reservationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressWarnings("unchecked")
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                HashMap<String, String> map = (HashMap<String, String>) reservationsListView.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), LivreActivity.class);
                intent.putExtra("titre",map.get("livre"));
                intent.putExtra("img",map.get("image"));
                ReservationsActivity.this.startActivity(intent);
            }
        });

    }


}
