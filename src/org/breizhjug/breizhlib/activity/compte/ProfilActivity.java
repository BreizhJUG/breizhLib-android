package org.breizhjug.breizhlib.activity.compte;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.activity.AbstractActivity;
import org.breizhjug.breizhlib.model.Utilisateur;
import org.breizhjug.breizhlib.utils.Gravatar;


public class ProfilActivity extends AbstractActivity {


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil);

    }

    public void init(Intent intent) {
        SharedPreferences prefs = BreizhLib.getSharedPreferences(this);
        String authCookie = prefs.getString(BreizhLib.AUTH_COOKIE, null);
        Utilisateur user = BreizhLib.getUtilisateurService().find(authCookie);

        if (user != null) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(BreizhLib.USER, user.email);
            editor.putBoolean(BreizhLib.USER_ADMIN, user.isAdmin);
            editor.putString(BreizhLib.USER_NOM, user.nom);
            editor.putString(BreizhLib.USER_PRENOM, user.prenom);
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

            ImageView icone = (ImageView) findViewById(R.id.avatar);
            BreizhLib.getImageDownloader().download(Gravatar.getImage(user.email), icone);

        } else {
            //TODO message Dialogue
            Toast.makeText(this, "information indisponible", Toast.LENGTH_SHORT);
        }
    }

}