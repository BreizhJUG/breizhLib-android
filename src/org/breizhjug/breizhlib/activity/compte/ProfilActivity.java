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
import greendroid.widget.ActionBarItem;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.activity.CommentaireActivity;
import org.breizhjug.breizhlib.activity.LivreActivity;
import org.breizhjug.breizhlib.activity.common.AbstractGDActivity;
import org.breizhjug.breizhlib.database.dao.CommentaireDAO;
import org.breizhjug.breizhlib.database.dao.ReservationDAO;
import org.breizhjug.breizhlib.model.Commentaire;
import org.breizhjug.breizhlib.model.Livre;
import org.breizhjug.breizhlib.model.Reservation;
import org.breizhjug.breizhlib.model.Utilisateur;
import org.breizhjug.breizhlib.remote.UtilisateurService;
import org.breizhjug.breizhlib.utils.images.Gravatar;
import org.breizhjug.breizhlib.utils.images.ImageCache;
import roboguice.inject.InjectView;

import java.util.ArrayList;


public class ProfilActivity extends AbstractGDActivity {

    @InjectView(R.id.nom)
    TextView nom;
    @InjectView(R.id.prenom)
    TextView prenom;
    @InjectView(R.id.email)
    TextView emailV;
    @InjectView(R.id.username)
    TextView username;
    @InjectView(R.id.commentaires)
    TextView commentaires;
    @InjectView(R.id.emprunts)
    TextView emprunts;
    @InjectView(R.id.reservations)
    TextView reservations;
    @InjectView(R.id.avatar)
    ImageView icon;
    @InjectView(R.id.commentairesbtn)
    Button commentairesBtn;
    @InjectView(R.id.resabtn)
    Button resaBtn;
    @InjectView(R.id.empruntsbtn)
    Button empruntsBtn;

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
        getActionBar().setTitle("Profil");
        getActionBar().addItem(ActionBarItem.Type.Settings, R.id.action_bar_settings);
        setActionBarContentView(R.layout.profil);
    }

    public void init(Intent intent) {
        final String authCookie = prefs.getString(BreizhLibConstantes.AUTH_COOKIE, null);

        final ProgressDialog waitDialog = ProgressDialog.show(this, getString(R.string.find_data), getString(R.string.chargement), true, true);

        final AsyncTask<Void, Void, Utilisateur> initTask = new AsyncTask<Void, Void, Utilisateur>() {

            @Override
            protected Utilisateur doInBackground(Void... params) {
                return service.find(authCookie);
            }

            @Override
            protected void onPostExecute(Utilisateur result) {
                waitDialog.dismiss();
                if (result == null) {
                    showError(getString(R.string.data_unavailable), true);
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

        nom.setText(user.nom);
        prenom.setText(user.prenom);
        emailV.setText(user.email);
        username.setText(user.username);
        commentaires.setText(user.commentairesLabel);
        emprunts.setText(user.ouvragesEncoursLabel);
        reservations.setText(user.reservationsLabel);
        imageCache.download(Gravatar.getImage(user.email), icon);

        //TODO synchronisation
        final ArrayList<Commentaire> items = commentaireDAO.findByAutor(user.nom + " " + user.prenom);
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

        //TODO synchronisation
        final ArrayList<Reservation> resaItems = reservationDAO.findByNom(user.nom, user.prenom);
        resaBtn.setVisibility(View.INVISIBLE);
        if (resaItems != null && resaItems.size() > 0) {
            resaBtn.setVisibility(View.VISIBLE);
            resaBtn.setOnClickListener(new Button.OnClickListener() {

                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), LivreActivity.class);
                    intent.putExtra("items", toOuvrages(resaItems));
                    intent.putExtra("index", 0);
                    intent.putExtra("item", resaItems.get(0).livre);
                    intent.putExtra("emailReservation", resaItems.get(0).email);
                    startActivity(intent);


                }
            });
        }
        if (prefs.getBoolean(BreizhLibConstantes.BETA, false)) {
            empruntsBtn.setOnClickListener(new Button.OnClickListener() {

                public void onClick(View view) {
                    Toast.makeText(ProfilActivity.this, getString(R.string.upcoming), Toast.LENGTH_SHORT).show();
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