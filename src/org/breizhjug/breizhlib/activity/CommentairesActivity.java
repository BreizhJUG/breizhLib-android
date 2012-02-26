package org.breizhjug.breizhlib.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.inject.Inject;
import com.google.inject.internal.Nullable;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.activity.common.AbstractGDActivity;
import org.breizhjug.breizhlib.adapter.CommentairesAdapter;
import org.breizhjug.breizhlib.model.Commentaire;
import org.breizhjug.breizhlib.remote.AsyncRemoteTask;
import org.breizhjug.breizhlib.remote.CommentaireService;
import org.breizhjug.breizhlib.utils.images.ImageCache;
import roboguice.inject.InjectView;
import static org.breizhjug.breizhlib.IntentConstantes.*;

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

        initView();
        getActionBar().setTitle(getText(R.string.commentaires_title));
    }

    public void init(Intent intent) {
    }

    public void initView() {
        final AsyncTask<Void, Void, Boolean> initTask = new AsyncRemoteTask<Commentaire>(this, service, commentairesListView, prefs) {

            @Override
            public ArrayAdapter<Commentaire> getAdapter() {
                return new CommentairesAdapter(CommentairesActivity.this.getBaseContext(), items);
            }

            public void onClick(int position) {
                Commentaire commentaire = (Commentaire) commentairesListView.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), CommentaireActivity.class);
                intent.putExtra(ITEM, commentaire);
                intent.putExtra(ITEMS, items);
                intent.putExtra(INDEX, position);
                startActivity(intent);
            }
        };
        initTask.execute((Void) null);
    }
}