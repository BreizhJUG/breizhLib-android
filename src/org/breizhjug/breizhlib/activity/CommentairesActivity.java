package org.breizhjug.breizhlib.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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

public class CommentairesActivity extends AbstractActivity {

    private ListView commentairesListView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items);
        initView();
    }

    public void init(Intent intent) {             }

    public void initView() {
        commentairesListView = (ListView) findViewById(R.id.items);
        SharedPreferences prefs = BreizhLib.getSharedPreferences(this);
        final ProgressDialog waitDialog = ProgressDialog.show(this, getString(R.string.recherche), getString(R.string.chargement), true, true);
        final AsyncTask<Void, Void, Boolean> initTask = new AsyncRemoteTask<Commentaire>(BreizhLib.getCommentaireService(),commentairesListView, prefs) {

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                 waitDialog.dismiss();
            }

            @Override
            public ArrayAdapter<Commentaire> getAdapter() {
                return new CommentairesAdapter(CommentairesActivity.this.getBaseContext(), items);
            }

            public void onClick(int position) {
                Commentaire commentaire = (Commentaire) commentairesListView.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), CommentaireActivity.class);
                intent.putExtra("commentaire", commentaire);
                intent.putExtra("commentaires", items);
                intent.putExtra("index", position);
                startActivity(intent);
            }
        };
        waitDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            public void onCancel(DialogInterface dialog) {
                if (initTask != null) {
                    initTask.cancel(true);
                }
                finish();
            }
        });
        initTask.execute((Void)null);
    }
}