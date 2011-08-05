package org.breizhjug.breizhlib.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.R;


public class AvisActivity extends AbstractActivity {

    @Override
    public void init(Intent intent) {
        final String isbn = intent.getStringExtra("isbn");

        final SharedPreferences prefs = BreizhLib.getSharedPreferences(this);


        final Spinner note = (Spinner) findViewById(R.id.spinnerNote);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add("1");
        adapter.add("2");
        adapter.add("3");
        adapter.add("4");
        adapter.add("5");
        note.setAdapter(adapter);

        String nom = prefs.getString(BreizhLib.USER_NOM, "") + " " +
                prefs.getString(BreizhLib.USER_PRENOM, "");
        final EditText nomEdit = (EditText) findViewById(R.id.nomEdit);
        nomEdit.setText(nom);

        final EditText avisEdit = (EditText) findViewById(R.id.avisEdit);

        Button button = (Button) findViewById(R.id.send);

        button.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View view) {
                final AsyncTask<Void, Void, Boolean> initTask = new AsyncTask<Void, Void, Boolean>() {

                    @Override
                    protected Boolean doInBackground(Void... params) {
                        String authCookie = prefs.getString(BreizhLib.AUTH_COOKIE, null);
                        boolean result = BreizhLib.getCommentaireService().comment(authCookie, isbn, nomEdit.getText().toString(), avisEdit.getText().toString(), Integer.valueOf("" + note.getSelectedItem()));
                        return result;
                    }

                    @Override
                    protected void onPostExecute(Boolean result) {
                        if (result == null || !result) {
                            showError("Erreur lors de l'envoi du commentaire", true);
                        } else {
                            Toast.makeText(AvisActivity.this, getString(R.string.commentaireSave), Toast.LENGTH_SHORT);
                            Intent intent = new Intent(getApplicationContext(), Menu.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }
                };
                initTask.execute();
            }
        });
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avis);
    }
}