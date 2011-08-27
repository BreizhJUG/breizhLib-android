package org.breizhjug.breizhlib.activity.compte;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.inject.Inject;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.activity.AbstractActivity;
import org.breizhjug.breizhlib.activity.CommentaireActivity;
import org.breizhjug.breizhlib.activity.LivreActivity;
import org.breizhjug.breizhlib.database.dao.CommentaireDAO;
import org.breizhjug.breizhlib.database.dao.ReservationDAO;
import org.breizhjug.breizhlib.model.Commentaire;
import org.breizhjug.breizhlib.model.Livre;
import org.breizhjug.breizhlib.model.Reservation;
import org.breizhjug.breizhlib.model.Utilisateur;
import org.breizhjug.breizhlib.remote.UtilisateurService;
import org.breizhjug.breizhlib.utils.images.Gravatar;
import org.breizhjug.breizhlib.utils.images.ImageCache;

import java.util.ArrayList;


public class ProfilActivity extends AbstractActivity {

    @Inject
    SharedPreferences prefs;
    @Inject
    private UtilisateurService service;
    @Inject
    private ImageCache imageCache;
    @Inject
    private CommentaireDAO commentaireDAO;
    @Inject
    private ReservationDAO reservationDAO;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil);
    }

    public void init(Intent intent) {
        final String authCookie = prefs.getString(BreizhLibConstantes.AUTH_COOKIE, null);

        final ProgressDialog waitDialog = ProgressDialog.show(this, "Recherche de vos informations", getString(R.string.chargement), true, true);

        final AsyncTask<Void, Void, Utilisateur> initTask = new AsyncTask<Void, Void, Utilisateur>() {

            @Override
            protected Utilisateur doInBackground(Void... params) {
                return service.find(authCookie);
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

    private void initView(final Utilisateur user) {
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
        imageCache.download(Gravatar.getImage(user.email), icon);

        final ArrayList<Commentaire> items = commentaireDAO.findByAutor(user.nom + " " + user.prenom);
        Button commentairesBtn = (Button) findViewById(R.id.commentairesbtn);
        commentairesBtn.setVisibility(View.INVISIBLE);
        if (items != null && items.size() > 0) {
            commentairesBtn.setVisibility(View.VISIBLE);
            commentairesBtn.setOnClickListener(new Button.OnClickListener() {

                public void onClick(View view) {

                    Intent intent = new Intent(getApplicationContext(), CommentaireActivity.class);
                    intent.putExtra("item", items.get(0));
                    intent.putExtra("items", items);
                    intent.putExtra("index", 0);
                    startActivity(intent);


                }
            });
        }

        final ArrayList<Reservation> resaItems = reservationDAO.findByNom(user.nom, user.prenom);
        Button resaBtn = (Button) findViewById(R.id.resabtn);
        resaBtn.setVisibility(View.INVISIBLE);
        if (resaItems != null && resaItems.size() > 0) {
            resaBtn.setVisibility(View.VISIBLE);
            resaBtn.setOnClickListener(new Button.OnClickListener() {

                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), LivreActivity.class);
                    intent.putExtra("items", toOuvrages(resaItems));
                    intent.putExtra("index", 0);
                    intent.putExtra("item", resaItems.get(0).livre);
                    startActivity(intent);


                }
            });
        }

        Button empruntsBtn = (Button) findViewById(R.id.empruntsbtn);

        if (prefs.getBoolean("beta", false)) {
            empruntsBtn.setOnClickListener(new Button.OnClickListener() {

                public void onClick(View view) {
                    Toast.makeText(ProfilActivity.this, "Prochainement!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            empruntsBtn.setVisibility(View.INVISIBLE);
        }
    }

    private ArrayList<Livre> toOuvrages(ArrayList<Reservation> items) {
        ArrayList<Livre> livres = new ArrayList<Livre>();

        for (Reservation item : items) {
            livres.add(item.livre);
        }

        return livres;
    }

}