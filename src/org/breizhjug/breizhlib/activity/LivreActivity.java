package org.breizhjug.breizhlib.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.inject.Inject;
import greendroid.widget.ActionBarItem;
import greendroid.widget.LoaderActionBarItem;
import greendroid.widget.PagedView;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.activity.common.AbstractPagednActivity;
import org.breizhjug.breizhlib.adapter.CommentairesAdapter;
import org.breizhjug.breizhlib.adapter.OuvragesPagedAdapter;
import org.breizhjug.breizhlib.database.dao.CommentaireDAO;
import org.breizhjug.breizhlib.database.dao.LivreDAO;
import org.breizhjug.breizhlib.model.Commentaire;
import org.breizhjug.breizhlib.model.Livre;
import org.breizhjug.breizhlib.model.Utilisateur;
import org.breizhjug.breizhlib.remote.OuvrageService;
import org.breizhjug.breizhlib.remote.ReservationService;
import org.breizhjug.breizhlib.remote.UtilisateurService;
import org.breizhjug.breizhlib.utils.images.ImageCache;
import static org.breizhjug.breizhlib.IntentConstantes.*;

import java.util.ArrayList;


public class LivreActivity extends AbstractPagednActivity<Livre> {

    private static final String TAG = "BreizhLib.LivreActivity";

    TextView titreView;
    TextView editeurView;
    ImageView icone;
    Button addBtn;
    Button avis;
    ListView commentaireItems;

    @Inject
    private OuvrageService service;
    @Inject
    private UtilisateurService userService;
    @Inject
    private ReservationService reservationService;
    @Inject
    private ImageCache imageCache;
    @Inject
    private CommentaireDAO commentaireDAO;
    @Inject
    private LivreDAO livreDAO;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!item.add && prefs.getString(BreizhLibConstantes.ACCOUNT_NAME, null) != null) {
            if (prefs.getString(BreizhLibConstantes.USER, null) != null) {
                getActionBar().addItem(ActionBarItem.Type.Compose, R.id.action_bar_avis);
            }
        }
    }

    @Override
    public void init(Intent intent) {

    }

    @Override
    public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
        switch (item.getItemId()) {
            case R.id.action_bar_avis:
                Intent pIntent = new Intent(getApplicationContext(), AvisActivity.class);
                pIntent.putExtra("livre", LivreActivity.this.item);
                LivreActivity.this.startActivity(pIntent);

                return true;
            default:

                return super.onHandleActionBarItemClick(item, position);
        }
    }


    public void initView(final LoaderActionBarItem loaderItem) {
        final PagedView pagedView = (PagedView) findViewById(R.id.paged_view);

        OuvragesPagedAdapter adapter = new OuvragesPagedAdapter(LivreActivity.this, items) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LivreActivity.this.getLayoutInflater().inflate(R.layout.livre, parent, false);
                }
                item = (Livre) getItem(position);
                if (item != null)
                    initView(convertView, item);

                return convertView;
            }
        };

        pagedView.setAdapter(adapter);
        mPageIndicatorOther.setDotCount(adapter.getCount());


    }


    private void initView(View convertView, Livre item) {
        titreView = (TextView) convertView.findViewById(R.id.titre);
        editeurView = (TextView) convertView.findViewById(R.id.editeur);
        icone = (ImageView) convertView.findViewById(R.id.img);
        addBtn = (Button) convertView.findViewById(R.id.add);
        commentaireItems = (ListView) convertView.findViewById(R.id.items);

        titreView.setText(item.titre);
        editeurView.setText(item.editeur);
        getActionBar().setTitle(item.titre);
        imageCache.getFromCache(item.iSBN, item.imgUrl, icone);

        initStars(convertView, item.note);


        if (item.add) {
            initAjout(addBtn, item.iSBN);
        } else {
            initReservation(addBtn, item);
        }

        initCommentaires(item);

    }


    private void initCommentaires(Livre livre) {
        Log.d("UPDATE","update commentaire");
        final ArrayList<Commentaire> commentaires = commentaireDAO.findByIsbn(livre.iSBN);
        livre.nbCommentaire =  commentaires.size();
        service.update(livre);
        commentaireItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Commentaire commentaire = (Commentaire) commentaireItems.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), CommentaireActivity.class);
                intent.putExtra(ITEM, commentaire);
                intent.putExtra(ITEMS, commentaires);
                intent.putExtra(INDEX, position);
                startActivity(intent);
            }
        });

        Log.d(TAG, livre.iSBN + " size :" + commentaires.size());
        CommentairesAdapter commentairesAdapter = new CommentairesAdapter(this.getBaseContext(), commentaires,false,R.layout.commentaire_item_short);
        commentaireItems.setAdapter(commentairesAdapter);
    }

    private void initReservation(Button button, final Livre livre) {
        button.setEnabled(false);
        if (livre.etat.equals("RESERVE")) {
            String email = getIntent().getStringExtra(EMAIL_RESERVATION);
            String authCookie = prefs.getString(BreizhLibConstantes.AUTH_COOKIE, null);
            Utilisateur user = userService.find(authCookie);
            if (user!= null && user.email.equals(email)) {
                //TODO si l'utilisateeur est la personne qui a réservé l'ouvrage
                //TODO lui permettre d'annuler la réservation
                button.setText(getString(R.string.annuler));
                button.setEnabled(true);
                button.setOnClickListener(new Button.OnClickListener() {

                    public void onClick(View view) {
                        Toast.makeText(LivreActivity.this.getApplicationContext(), getString(R.string.annulation_en_cours), Toast.LENGTH_SHORT).show();
                        reservationService.annuler(livre);
                    }
                });
            } else {
                button.setText(getString(R.string.reserveBtn));
            }

        } else if (livre.etat.equals("DISP0NIBLE")) {
            button.setText(getString(R.string.reserverBtn));
            if (prefs.getString(BreizhLibConstantes.ACCOUNT_NAME, null) != null) {
                button.setEnabled(true);
                if (prefs.getString(BreizhLibConstantes.USER, null) != null) {
                    button.setOnClickListener(new Button.OnClickListener() {

                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(), ReservationActivity.class);
                            intent.putExtra(LIVRE, livre);
                            LivreActivity.this.startActivity(intent);
                        }
                    });
                } else {
                    button.setOnClickListener(new Button.OnClickListener() {
                        public void onClick(View view) {
                            Toast.makeText(LivreActivity.this.getApplicationContext(), getString(R.string.connexion_required), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        } else {
            button.setText(getString(R.string.indispoBtn));
        }
    }

    private void initAjout(Button button, final String isbn) {
        if (prefs.getBoolean(BreizhLibConstantes.USER_ADMIN, false)) {
            button.setText(getString(R.string.ajouterBtn));
            button.setOnClickListener(new Button.OnClickListener() {

                public void onClick(View view) {
                    final AsyncTask<Void, Void, Livre> initTask = new AsyncTask<Void, Void, Livre>() {

                        @Override
                        protected Livre doInBackground(Void... params) {
                            String authCookie = prefs.getString(BreizhLibConstantes.AUTH_COOKIE, null);
                            Livre livre = service.add(authCookie, isbn);
                            return livre;
                        }

                        @Override
                        protected void onPostExecute(Livre result) {
                            if (result == null) {
                                showError(getString(R.string.add_error), true);
                            } else {
                                Intent intent = new Intent(getApplicationContext(), LivreActivity.class);
                                Toast.makeText(getApplicationContext(), getString(R.string.ajoutOK), Toast.LENGTH_SHORT).show();
                                intent.putExtra(ITEM, result);
                                LivreActivity.this.startActivity(intent);
                                finish();
                            }
                        }
                    };
                    initTask.execute();
                }
            });
        } else {
            button.setText(getString(R.string.nonDispoBtn));
            button.setEnabled(false);
        }
    }

    @Override
    protected Class<? extends Activity> getActivityClass() {
        return LivreActivity.class;
    }
}