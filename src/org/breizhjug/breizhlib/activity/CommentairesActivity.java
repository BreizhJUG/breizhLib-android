package org.breizhjug.breizhlib.activity;

import android.content.Intent;
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
import roboguice.inject.InjectView;

import static org.breizhjug.breizhlib.utils.IntentSupport.newCommentaireIntent;

public class CommentairesActivity extends AbstractGDActivity {

    @InjectView(R.id.items)
    @Nullable
    ListView commentairesListView;

    @Inject
    private CommentaireService service;

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
                Commentaire commentaire = (Commentaire) commentairesListView.getItemAtPosition(position);
                startActivity(newCommentaireIntent(getApplicationContext(), commentaire, position, items));
            }
        };
        initTask.setDialogTitle(R.string.commentaires_title);
        initTask.execute((Void) null);
    }
}