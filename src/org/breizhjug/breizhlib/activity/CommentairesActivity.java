package org.breizhjug.breizhlib.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.inject.Inject;
import com.google.inject.internal.Nullable;
import greendroid.widget.ActionBarItem;
import greendroid.widget.LoaderActionBarItem;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.activity.gd.AbstractGDActivity;
import org.breizhjug.breizhlib.adapter.CommentairesAdapter;
import org.breizhjug.breizhlib.model.Commentaire;
import org.breizhjug.breizhlib.remote.AsyncRemoteTask;
import org.breizhjug.breizhlib.remote.CommentaireService;
import org.breizhjug.breizhlib.utils.images.ImageCache;
import roboguice.inject.InjectView;

public class CommentairesActivity extends AbstractGDActivity {

    @InjectView(R.id.items)
    @Nullable
    ListView commentairesListView;

    @Inject
    private CommentaireService service;
    @Inject
    private ImageCache imageCache;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarContentView(R.layout.items);
        commentairesListView = (ListView) findViewById(R.id.items);

        initView(null);
        getActionBar().setTitle("Commentaires");
        addActionBarItem(ActionBarItem.Type.Refresh, R.id.action_bar_refresh);

    }

    @Override
    public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
        switch (item.getItemId()) {
            case R.id.action_bar_refresh:
                final LoaderActionBarItem loaderItem = (LoaderActionBarItem) item;
                service.clearCache();
                initView(loaderItem);

                return true;
            default:

                return super.onHandleActionBarItemClick(item, position);
        }
    }

    public void init(Intent intent) {
    }

    public void initView(final LoaderActionBarItem loaderItem) {
        final AsyncTask<Void, Void, Boolean> initTask = new AsyncRemoteTask<Commentaire>(this, service, commentairesListView, prefs) {

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
            public ArrayAdapter<Commentaire> getAdapter() {
                return new CommentairesAdapter(CommentairesActivity.this.getBaseContext(), items);
            }

            public void onClick(int position) {
                Commentaire commentaire = (Commentaire) commentairesListView.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), CommentaireActivity.class);
                intent.putExtra("item", commentaire);
                intent.putExtra("items", items);
                intent.putExtra("index", position);
                startActivity(intent);
            }
        };
        initTask.execute((Void) null);
    }
}