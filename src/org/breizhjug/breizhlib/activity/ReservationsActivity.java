package org.breizhjug.breizhlib.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.google.inject.Inject;
import greendroid.widget.ActionBarItem;
import greendroid.widget.LoaderActionBarItem;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.activity.common.AbstractGDActivity;
import org.breizhjug.breizhlib.adapter.ReservationsAdapter;
import org.breizhjug.breizhlib.model.Livre;
import org.breizhjug.breizhlib.model.Reservation;
import org.breizhjug.breizhlib.remote.AsyncRemoteTask;
import org.breizhjug.breizhlib.remote.ReservationService;
import org.breizhjug.breizhlib.utils.images.ImageCache;
import roboguice.inject.InjectView;

import java.util.ArrayList;


public class ReservationsActivity extends AbstractGDActivity {

    @InjectView(R.id.items)
    ListView reservationsListView;

    @Inject
    private ReservationService service;
    @Inject
    private ImageCache imageCache;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarContentView(R.layout.items);

        reservationsListView = (ListView) findViewById(R.id.items);
        initView(null);
        getActionBar().setTitle("Reservations");
        addActionBarItem(ActionBarItem.Type.Refresh, R.id.action_bar_refresh);

    }


    @Override
    public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
        switch (item.getItemId()) {
            case R.id.action_bar_refresh:
                final LoaderActionBarItem loaderItem = (LoaderActionBarItem) item;
                service.clearDBCache();
                initView(loaderItem);

                return true;
            default:

                return super.onHandleActionBarItemClick(item, position);
        }
    }

    @Override
    public void init(Intent intent) {
    }

    public void initView(final LoaderActionBarItem loaderItem) {

        final AsyncTask<Void, Void, Boolean> initTask = new AsyncRemoteTask<Reservation>(this, service, reservationsListView, prefs) {

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if (loaderItem != null)
                    loaderItem.setLoading(false);
            }

            @Override
            protected void onPreExecute() {
                if (loaderItem == null)
                    super.onPreExecute();
            }

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
                intent.putExtra("emailReservation", reservation.email);
                ReservationsActivity.this.startActivity(intent);
            }

            public void displayEmptyMessage() {
                Toast.makeText(getApplicationContext(), getText(R.string.no_reservation), Toast.LENGTH_SHORT).show();
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
