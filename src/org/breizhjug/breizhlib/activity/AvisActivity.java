package org.breizhjug.breizhlib.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.model.Livre;


public class AvisActivity extends AbstractActivity {

    @Override
    public void init(Intent intent) {
        final Livre livre = (Livre) intent.getSerializableExtra("livre");

        final SharedPreferences prefs = BreizhLib.getSharedPreferences(getApplicationContext());

        final Spinner note = (Spinner) findViewById(R.id.spinnerNote);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.stars,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        note.setAdapter(adapter);
        if (livre.note > 0) {
            note.setSelection(livre.note - 1);
        }

        String nom = prefs.getString(BreizhLibConstantes.USER_NOM, "") + " " +
                prefs.getString(BreizhLibConstantes.USER_PRENOM, "");
        final EditText nomEdit = (EditText) findViewById(R.id.nomEdit);
        nomEdit.setText(nom);

        final EditText avisEdit = (EditText) findViewById(R.id.avisEdit);

        Button button = (Button) findViewById(R.id.send);

        button.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View view) {
                final String avis = avisEdit.getText().toString();
                final String nom = nomEdit.getText().toString();

                if (avis == null || avis.length() == 0) {
                    showError("Commentaite non renseigné", false);
                } else if (nom == null || nom.length() == 0) {
                    showError("Nom non renseigné", false);
                } else {
                    final ProgressDialog waitDialog = ProgressDialog.show(AvisActivity.this, "Envoi de votre commentaire", getString(R.string.chargement), true, true);

                    final AsyncTask<Void, Void, Boolean> initTask = new AsyncTask<Void, Void, Boolean>() {

                        @Override
                        protected Boolean doInBackground(Void... params) {


                            String authCookie = prefs.getString(BreizhLibConstantes.AUTH_COOKIE, null);
                            boolean result = BreizhLib.getCommentaireService().comment(authCookie, livre.iSBN, nom, avis, Integer.valueOf("" + note.getSelectedItem()));
                            return result;
                        }

                        @Override
                        protected void onPostExecute(Boolean result) {
                            waitDialog.dismiss();
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


                    waitDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                        public void onCancel(DialogInterface dialog) {
                            if (initTask != null) {
                                initTask.cancel(true);
                            }
                            finish();
                        }
                    });
                    initTask.execute();
                }
            }
        });
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avis);
    }
}