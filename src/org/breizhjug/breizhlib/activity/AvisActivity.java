package org.breizhjug.breizhlib.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.exception.ResultException;
import org.breizhjug.breizhlib.model.Commentaire;
import org.breizhjug.breizhlib.model.Livre;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;


public class AvisActivity extends AbstractActivity {

    @InjectView(R.id.send)
    Button button;
    @InjectView(R.id.avisEdit)
    EditText avisEdit;
    @InjectView(R.id.nomEdit)
    EditText nomEdit;
    @InjectView(R.id.rating)
    RatingBar rating;


    @InjectExtra("livre")
    Livre livre;

    @Override
    public void init(Intent intent) {

        final SharedPreferences prefs = BreizhLib.getSharedPreferences(getApplicationContext());

        rating.setRating(livre.note);
        rating.setMax(5);


        String nom = prefs.getString(BreizhLibConstantes.USER_NOM, "") + " " +
                prefs.getString(BreizhLibConstantes.USER_PRENOM, "");
        nomEdit.setText(nom);

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

                    final AsyncTask<Void, Void, Commentaire> initTask = new AsyncTask<Void, Void, Commentaire>() {

                        @Override
                        protected Commentaire doInBackground(Void... params) {


                            String authCookie = prefs.getString(BreizhLibConstantes.AUTH_COOKIE, null);
                            Commentaire result = null;
                            try {
                                result = BreizhLib.getCommentaireService().comment(authCookie, livre.iSBN, nom, avis, Integer.valueOf("" + rating.getRating()));
                            } catch (ResultException e) {
                                showError(e.result.msg, true);
                                finish();
                            }
                            return result;
                        }

                        @Override
                        protected void onPostExecute(Commentaire result) {
                            waitDialog.dismiss();
                            if (result == null) {
                                showError("Erreur lors de l'envoi du commentaire", true);
                            } else {
                                Toast.makeText(AvisActivity.this, getString(R.string.commentaireSave), Toast.LENGTH_SHORT);
                                Intent intent = new Intent(getApplicationContext(), CommentaireActivity.class);
                                intent.putExtra("commentaire", result);
                                startActivity(intent);
                                finish();
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