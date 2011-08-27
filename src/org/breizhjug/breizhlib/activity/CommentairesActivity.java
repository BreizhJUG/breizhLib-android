package org.breizhjug.breizhlib.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.adapter.CommentairesAdapter;
import org.breizhjug.breizhlib.model.Commentaire;
import org.breizhjug.breizhlib.remote.AsyncRemoteTask;
import roboguice.inject.InjectView;

public class CommentairesActivity extends AbstractActivity {

    @InjectView(R.id.items)
    ListView commentairesListView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items);
        initView();
    }

    public void init(Intent intent) {
    }

    public void initView() {
        SharedPreferences prefs = BreizhLib.getSharedPreferences(getApplicationContext());
        final AsyncTask<Void, Void, Boolean> initTask = new AsyncRemoteTask<Commentaire>(this, BreizhLib.getCommentaireService(), commentairesListView, prefs) {

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