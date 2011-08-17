package org.breizhjug.breizhlib.activity.compte;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.activity.AbstractActivity;
import org.breizhjug.breizhlib.model.Utilisateur;
import org.breizhjug.breizhlib.utils.images.Gravatar;


public class ProfilActivity extends AbstractActivity {

    SharedPreferences prefs;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil);
    }

    public void init(Intent intent) {
        prefs = BreizhLib.getSharedPreferences(getApplicationContext());
        final String authCookie = prefs.getString(BreizhLibConstantes.AUTH_COOKIE, null);

        final ProgressDialog waitDialog = ProgressDialog.show(this, "Recherche de vos informations", getString(R.string.chargement), true, true);

        final AsyncTask<Void, Void, Utilisateur> initTask = new AsyncTask<Void, Void, Utilisateur>() {

            @Override
            protected Utilisateur doInBackground(Void... params) {
                return BreizhLib.getUtilisateurService().find(authCookie);
            }

            @Override
            protected void onPostExecute(Utilisateur result) {
                waitDialog.dismiss();
                if (result == null) {
                    showError("Information indisponible", true);
                } else {
                    initView(result);
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
        initTask.execute((Void) null);
    }

    private void initView(Utilisateur user) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(BreizhLibConstantes.USER, user.email);
        editor.putBoolean(BreizhLibConstantes.USER_ADMIN, user.isAdmin);
        editor.putString(BreizhLibConstantes.USER_NOM, user.nom);
        editor.putString(BreizhLibConstantes.USER_PRENOM, user.prenom);
        editor.commit();


        TextView nom = (TextView) findViewById(R.id.nom);
        nom.setText(user.nom);

        TextView prenom = (TextView) findViewById(R.id.prenom);
        prenom.setText(user.prenom);

        TextView emailV = (TextView) findViewById(R.id.email);
        emailV.setText(user.email);

        TextView username = (TextView) findViewById(R.id.username);
        username.setText(user.username);

        TextView commentaires = (TextView) findViewById(R.id.commentaires);
        commentaires.setText(user.commentairesLabel);

        TextView emprunts = (TextView) findViewById(R.id.emprunts);
        emprunts.setText(user.ouvragesEncoursLabel);

        TextView reservations = (TextView) findViewById(R.id.reservations);
        reservations.setText(user.reservationsLabel);

        ImageView icon = (ImageView) findViewById(R.id.avatar);
        BreizhLib.getImageCache().getFromCache(user.email, Gravatar.getImage(user.email), icon);
    }

}