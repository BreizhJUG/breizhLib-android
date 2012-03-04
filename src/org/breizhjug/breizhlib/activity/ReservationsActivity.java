package org.breizhjug.breizhlib.activity;

import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.google.inject.Inject;
import greendroid.widget.ActionBarItem;
import greendroid.widget.LoaderActionBarItem;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.activity.common.AbstractGDActivity;
import org.breizhjug.breizhlib.adapter.ReservationsAdapter;
import org.breizhjug.breizhlib.model.Converter;
import org.breizhjug.breizhlib.model.Reservation;
import org.breizhjug.breizhlib.remote.AsyncRemoteTask;
import org.breizhjug.breizhlib.remote.ReservationService;
import static org.breizhjug.breizhlib.utils.IntentSupport.*;

import org.breizhjug.breizhlib.utils.images.ImageCache;
import roboguice.inject.InjectView;


public class ReservationsActivity extends AbstractGDActivity {

    @InjectView(R.id.items)
    ListView reservationsListView;

    @Inject
    private ReservationService service;
    @Inject
    private ImageCache imageCache;
    @Inject
    private Converter converter;

    @Override
    public void init(Intent intent) {
        setActionBarContentView(R.layout.items);
        initView(null);
        getActionBar().setTitle(getText(R.string.reservations_title));
        addActionBarItem(ActionBarItem.Type.Refresh, R.id.action_bar_refresh);
    }

    public void initView(final LoaderActionBarItem loaderItem) {

        final AsyncRemoteTask<Reservation> initTask = new AsyncRemoteTask<Reservation>(this, service, reservationsListView, prefs) {

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
                return new ReservationsAdapter(ReservationsActivity.this.getBaseContext(), items);
            }

            public void onClick(int position) {
                Reservation reservation = (Reservation) reservationsListView.getItemAtPosition(position);
                startActivity(newLivreIntent(getApplicationContext(), converter.toOuvrages(items), position, reservation));
            }

            public void displayEmptyMessage() {
                Toast.makeText(getApplicationContext(), getText(R.string.no_reservation), Toast.LENGTH_SHORT).show();
            }
        };

        initTask.setDialogTitle(R.string.reservations_title);
        initTask.execute((Void) null);

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


}
