package org.breizhjug.breizhlib.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import com.google.inject.Inject;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.exception.ResultException;
import org.breizhjug.breizhlib.model.Commentaire;
import org.breizhjug.breizhlib.model.Livre;
import org.breizhjug.breizhlib.remote.CommentaireService;
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

    @Inject
    private CommentaireService service;

    @Override
    public void init(Intent intent) {

        rating.setRating(livre.note);
        rating.setMax(5);


        String nom = prefs.getString(BreizhLibConstantes.USER_NOM, "") + " " +
                prefs.getString(BreizhLibConstantes.USER_PRENOM, "");
        nomEdit.setText(nom);

        button.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View view) {
                String avis = avisEdit.getText().toString();
                String nom = nomEdit.getText().toString();

                if (avis == null || avis.length() == 0) {
                    showError("Commentaite non renseigné", false);
                } else if (nom == null || nom.length() == 0) {
                    showError("Nom non renseigné", false);
                } else {
                    sendAvis();
                }
            }
        });
    }

    private void sendAvis() {
        final String avis = avisEdit.getText().toString();
        final String nom = nomEdit.getText().toString();
        final ProgressDialog waitDialog = ProgressDialog.show(this, "Envoi de votre item", getString(R.string.chargement), true, true);

        final AsyncTask<Void, Void, Commentaire> initTask = new AsyncTask<Void, Void, Commentaire>() {

            @Override
            protected Commentaire doInBackground(Void... params) {


                String authCookie = prefs.getString(BreizhLibConstantes.AUTH_COOKIE, null);
                Commentaire result = null;
                try {
                    result = service.comment(authCookie, livre.iSBN, nom, avis, Integer.valueOf("" + rating.getRating()));
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
                    showError("Erreur lors de l'envoi du item", true);
                } else {
                    Toast.makeText(AvisActivity.this, getString(R.string.commentaireSave), Toast.LENGTH_SHORT);
                    Intent intent = new Intent(getApplicationContext(), CommentaireActivity.class);
                    intent.putExtra("item", result);
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

        if (validate()) {
            initTask.execute();
        }
    }

    private boolean validate() {
        if (nomEdit == null || nomEdit.length() == 0) {
            showError(getString(R.string.nom_validation_msg), false);
            return false;
        }
        if (avisEdit == null || avisEdit.length() == 0) {
            showError(getString(R.string.avis_validation_msg), false);
            return false;
        }
        return true;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avis);
    }
}