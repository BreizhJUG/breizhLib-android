package org.breizhjug.breizhlib.activity.compte;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.inject.Inject;
import greendroid.widget.ActionBarItem;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.activity.common.AbstractGDActivity;
import org.breizhjug.breizhlib.adapter.InfoListAdapter;
import org.breizhjug.breizhlib.database.dao.CommentaireDAO;
import org.breizhjug.breizhlib.database.dao.EmpruntDAO;
import org.breizhjug.breizhlib.database.dao.ReservationDAO;
import org.breizhjug.breizhlib.model.*;
import org.breizhjug.breizhlib.remote.UtilisateurService;
import org.breizhjug.breizhlib.utils.IntentSupport;
import org.breizhjug.breizhlib.utils.authentification.Authentification;
import org.breizhjug.breizhlib.utils.images.Gravatar;
import org.breizhjug.breizhlib.utils.images.ImageCache;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import roboguice.inject.InjectView;

import java.util.ArrayList;


public class ProfilActivity extends AbstractGDActivity {

    @InjectView(R.id.nom)
    TextView nom;
    @InjectView(R.id.avatar)
    ImageView icon;
    @InjectView(R.id.profile_info)
    ListView profile_info;

    @Inject
    private SharedPreferences prefs;
    @Inject
    private UtilisateurService service;
    @Inject
    private ImageCache imageCache;
    @Inject
    private CommentaireDAO commentaireDAO;
    @Inject
    private ReservationDAO reservationDAO;
    @Inject
    private EmpruntDAO empruntDAO;
    @Inject
    private Converter converter;
    @Inject
    private Authentification authentification;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setTitle(getText(R.string.profil_title));
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
                    authentification.saveInfos(result,getApplicationContext());
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

    public static final class ViewHolder {
        TextView title;

        TextView content;
    }


    protected JSONObject buildListItem(final String title, final String content)
            throws JSONException {
        return new JSONObject().put("title", title).put("content", content);
    }

    protected boolean IsNotNullNorEmpty(final String subject) {
        if (subject != null && !subject.equals("")) {
            return true;
        } else {
            return false;
        }
    }


    private void initView(final Utilisateur user) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(BreizhLibConstantes.USER, user.email);
        editor.putBoolean(BreizhLibConstantes.USER_ADMIN, user.isAdmin);
        editor.putString(BreizhLibConstantes.USER_NOM, user.nom);
        editor.commit();

        final JSONArray listItems = new JSONArray();
        try {
            if (IsNotNullNorEmpty(user.nom)) {
                listItems.put(buildListItem("Nom", user.nom));
                nom.setText(user.nom);
            }
            if (IsNotNullNorEmpty(user.email)) {
                listItems.put(buildListItem("Email", user.email));
            }
            if (IsNotNullNorEmpty(user.username)) {
                listItems.put(buildListItem("Twitter", user.username));
            }
            if (IsNotNullNorEmpty(user.commentairesLabel)) {
                listItems.put(buildListItem("Commentaires", user.commentairesLabel));
            }
            if (IsNotNullNorEmpty(user.ouvragesEncoursLabel)) {
                listItems.put(buildListItem("Ouvrages", user.ouvragesEncoursLabel));
            }
            if (IsNotNullNorEmpty(user.reservationsLabel)) {
                listItems.put(buildListItem("Réservations", user.reservationsLabel));
            }

            //listItems.put(buildListItem("Emprunts", ""));


        } catch (JSONException e) {
            e.printStackTrace();
        }


        final ListView infoList = (ListView) findViewById(R.id.profile_info);
        final InfoListAdapter adapter = new InfoListAdapter(this, infoList);
        adapter.loadData(listItems);
        adapter.pushData();
        infoList.setAdapter(adapter);
        imageCache.download(Gravatar.getImage(user.email), icon);


        infoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(final AdapterView<?> parent, final View v,
                                    final int position, final long id) {
                try {
                    final String title = ((JSONObject) listItems.get(position))
                            .getString("title");
                    String content = ((JSONObject) listItems.get(position))
                            .getString("content");
                    Intent intent = null;

                    if (title.equals("Commentaires")) {


                        final ArrayList<Commentaire> items = commentaireDAO.findByAutor(user.nom + " " + user.prenom);
                        if (items != null && items.size() > 0) {
                            startActivity(IntentSupport.newCommentaireIntent(getApplicationContext(), items.get(0), 0, items));
                        }
                    }
                    if (title.equals("Réservations")) {
                        final ArrayList<Reservation> resaItems = reservationDAO.findByNom(user.nom, user.prenom);
                        if (resaItems != null && resaItems.size() > 0) {
                            startActivity(IntentSupport.newLivreIntent(getApplicationContext(), converter.toOuvrages(resaItems), 0, resaItems.get(0)));
                        }
                    }
                    if (title.equals("Ouvrages")) {
                        final ArrayList<Emprunt> empruntItems = empruntDAO.findByNom(user.nom, user.prenom);
                        if (empruntItems != null && empruntItems.size() > 0) {
                            startActivity(IntentSupport.newEmpruntsIntent(getApplicationContext(), converter.empruntToOuvrages(empruntItems), 0, empruntItems.get(0).livre));
                        }
                    } else {
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }


}