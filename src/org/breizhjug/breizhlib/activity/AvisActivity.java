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
import org.breizhjug.breizhlib.activity.common.AbstractGDActivity;
import org.breizhjug.breizhlib.exception.ResultException;
import org.breizhjug.breizhlib.model.Commentaire;
import org.breizhjug.breizhlib.model.Livre;
import org.breizhjug.breizhlib.remote.CommentaireService;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import static org.breizhjug.breizhlib.IntentConstantes.*;


public class AvisActivity extends AbstractGDActivity {

    @InjectView(R.id.send)
    Button button;
    @InjectView(R.id.avisEdit)
    EditText avisEdit;
    @InjectView(R.id.nomEdit)
    EditText nomEdit;
    @InjectView(R.id.rating)
    RatingBar rating;


    @InjectExtra(LIVRE)
    Livre livre;

    @Inject
    private CommentaireService service;

    @Override
    public void init(Intent intent) {

        rating.setRating(livre.note);
        rating.setMax(5);
        getActionBar().setTitle(livre.titre);

        String nom = prefs.getString(BreizhLibConstantes.USER_NOM, "") + " " +
                prefs.getString(BreizhLibConstantes.USER_PRENOM, "");
        nomEdit.setText(nom);

        button.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View view) {
                String avis = avisEdit.getText().toString();
                String nom = nomEdit.getText().toString();

                if (avis == null || avis.length() == 0) {
                    showError(R.string.commentaire_validation_msg, false);
                } else if (nom == null || nom.length() == 0) {
                    showError(R.string.nom_validation_msg, false);
                } else {
                    sendAvis();
                }
            }
        });
    }

    private void sendAvis() {
        final String avis = avisEdit.getText().toString();
        final String nom = nomEdit.getText().toString();
        final ProgressDialog waitDialog = ProgressDialog.show(this,getString(R.string.commentaire_send),  getString(R.string.chargement), true, true);

        final AsyncTask<Void, Void, Commentaire> initTask = new AsyncTask<Void, Void, Commentaire>() {

            @Override
            protected Commentaire doInBackground(Void... params) {


                String authCookie = prefs.getString(BreizhLibConstantes.AUTH_COOKIE, null);
                Commentaire result = null;
                try {
                    result = service.comment(authCookie, livre.iSBN, nom, avis,  rating.getNumStars());
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
                    showError(getString(R.string.commentaire_send_error), true);
                } else {
                    startCommentaireActivity(result);
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
        setActionBarContentView(R.layout.avis);
    }

    private void startCommentaireActivity(Commentaire result) {
        Toast.makeText(AvisActivity.this, getString(R.string.commentaireSave), Toast.LENGTH_SHORT);
        Intent intent = new Intent(getApplicationContext(), CommentaireActivity.class);
        intent.putExtra(ITEM, result);
        startActivity(intent);
        finish();
    }
}