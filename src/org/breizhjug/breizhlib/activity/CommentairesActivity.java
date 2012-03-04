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

import java.util.ArrayList;

import static org.breizhjug.breizhlib.IntentConstantes.*;

public class CommentairesActivity extends AbstractGDActivity {

    @InjectView(R.id.items)
    @Nullable
    ListView commentairesListView;

    @Inject
    private CommentaireService service;
    @Inject
    private ImageCache imageCache;

    public void init(Intent intent) {
        setActionBarContentView(R.layout.items);
        getActionBar().setTitle(getText(R.string.commentaires_title));
        initView();
    }

    public void initView() {
        final AsyncRemoteTask<Commentaire> initTask = new AsyncRemoteTask<Commentaire>(this, service, commentairesListView, prefs) {

            @Override
            public ArrayAdapter<Commentaire> getAdapter() {
                return new CommentairesAdapter(CommentairesActivity.this.getBaseContext(), items);
            }

            public void onClick(int position) {
                startCommentaireActivity(position,items);
            }
        };
        initTask.setDialogTitle(R.string.commentaires_title);
        initTask.execute((Void) null);
    }

    private void startCommentaireActivity(int position, ArrayList<Commentaire> items) {
        Commentaire commentaire = (Commentaire) commentairesListView.getItemAtPosition(position);
        Intent intent = new Intent(getApplicationContext(), CommentaireActivity.class);
        intent.putExtra(ITEM, commentaire);
        intent.putExtra(ITEMS, items);
        intent.putExtra(INDEX, position);
        startActivity(intent);
    }
}