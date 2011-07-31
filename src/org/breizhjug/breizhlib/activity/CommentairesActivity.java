package org.breizhjug.breizhlib.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.adapter.CommentairesAdapter;
import org.breizhjug.breizhlib.model.Commentaire;

import java.util.List;

public class CommentairesActivity extends AbstractActivity {

    private ListView commentairesListView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items);
    }

    public void init(Intent intent) {
        commentairesListView = (ListView) findViewById(R.id.items);
        SharedPreferences prefs = breizhLib.getSharedPreferences(this);
        String authCookie = prefs.getString(breizhLib.AUTH_COOKIE, null);

        List<Commentaire> commentaires = breizhLib.getCommentaireService().load(authCookie);

        CommentairesAdapter mSchedule = new CommentairesAdapter(this.getBaseContext(), commentaires);
        commentairesListView.setAdapter(mSchedule);

        commentairesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressWarnings("unchecked")
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Commentaire commentaire = (Commentaire) commentairesListView.getItemAtPosition(position);
                AlertDialog.Builder adb = new AlertDialog.Builder(CommentairesActivity.this);
                adb.setTitle("Commentaire de l'ouvrage " + commentaire.livre);

                adb.setMessage(commentaire.commentaire);
                adb.setPositiveButton("Ok", null);
                adb.show();
            }
        });
    }
}