package org.breizhjug.breizhlib.activity;

import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.inject.Inject;
import greendroid.widget.ActionBarItem;
import greendroid.widget.LoaderActionBarItem;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.activity.common.AbstractGDActivity;
import org.breizhjug.breizhlib.adapter.EmpruntsAdapter;
import org.breizhjug.breizhlib.model.Converter;
import org.breizhjug.breizhlib.model.Emprunt;
import org.breizhjug.breizhlib.model.Livre;
import org.breizhjug.breizhlib.remote.AsyncRemoteTask;
import org.breizhjug.breizhlib.remote.EmpruntService;
import roboguice.inject.InjectView;

import java.util.ArrayList;

import static org.breizhjug.breizhlib.utils.IntentSupport.newLivreIntent;


public class EmpruntsActivity extends AbstractGDActivity {

    @InjectView(R.id.items)
    ListView empruntsListView;

    @Inject
    private EmpruntService service;
    @Inject
    private Converter converter;

    @Override
    public void init(Intent intent) {
        setActionBarContentView(R.layout.items);
        initView(null);
        getActionBar().setTitle(getText(R.string.emprunts_title));
        addActionBarItem(ActionBarItem.Type.Refresh, R.id.action_bar_refresh);
    }

    public void initView(final LoaderActionBarItem loaderItem) {

        final AsyncRemoteTask<Emprunt> initTask = new AsyncRemoteTask<Emprunt>(this, service, empruntsListView, prefs) {

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
            public ArrayAdapter<Emprunt> getAdapter() {
                return new EmpruntsAdapter(EmpruntsActivity.this.getBaseContext(), items);
            }

            public void onClick(int position) {
                Emprunt emprunt = (Emprunt) empruntsListView.getItemAtPosition(position);
                ArrayList<Livre> itemsL = converter.empruntToOuvrages(items);
                startActivity(newLivreIntent(getApplicationContext(), itemsL, position, emprunt.livre));
            }
        };

        initTask.setDialogTitle(R.string.emprunts_title);
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
