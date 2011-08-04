package org.breizhjug.breizhlib.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.adapter.CommentairesAdapter;
import org.breizhjug.breizhlib.model.Commentaire;

import java.util.ArrayList;

public class CommentairesActivity extends AbstractActivity {

    private ListView commentairesListView;

    private ArrayList<Commentaire> commentaires;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items);
    }

    public void init(Intent intent) {
        commentairesListView = (ListView) findViewById(R.id.items);
        SharedPreferences prefs = breizhLib.getSharedPreferences(this);
        commentaires = new ArrayList<Commentaire>();
        commentaires.addAll( breizhLib.getCommentaireService().load(prefs.getString(breizhLib.AUTH_COOKIE, null)));

        CommentairesAdapter mSchedule = new CommentairesAdapter(this.getBaseContext(), commentaires);
        commentairesListView.setAdapter(mSchedule);

        commentairesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                onSend(position);
            }
        });
    }


    public void onSend(int position) {
        Commentaire commentaire = (Commentaire) commentairesListView.getItemAtPosition(position);
        Intent intent = new Intent(getApplicationContext(), CommentaireActivity.class);
        intent.putExtra("commentaire", commentaire);
        intent.putExtra("commentaires", commentaires);
        intent.putExtra("index", position);
        startActivity(intent);
    }
}