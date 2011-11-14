package org.breizhjug.breizhlib.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.google.inject.Inject;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.adapter.CommentairesAdapter;
import org.breizhjug.breizhlib.database.dao.CommentaireDAO;
import org.breizhjug.breizhlib.model.Commentaire;
import org.breizhjug.breizhlib.model.Livre;
import org.breizhjug.breizhlib.remote.OuvrageService;
import org.breizhjug.breizhlib.utils.images.ImageCache;
import roboguice.inject.InjectView;

import java.util.ArrayList;


public class LivreActivity extends AbstractNavigationActivity<Livre> {

    private static final String TAG = "BreizhLib.LivreActivity";

    @InjectView(R.id.titre)
    TextView titreView;
    @InjectView(R.id.editeur)
    TextView editeurView;
    @InjectView(R.id.img)
    ImageView icone;
    @InjectView(R.id.add)
    Button addBtn;
    @InjectView(R.id.addComment)
    Button avis;
    @InjectView(R.id.items)
    ListView commentaireItems;

    @Inject
    private OuvrageService service;
    @Inject
    private ImageCache imageCache;
    @Inject
    private CommentaireDAO commentaireDAO;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarContentView(R.layout.livre);
        initView();
    }

    @Override
    public void init(Intent intent) {

    }

    private void initView() {
        titreView.setText(item.titre);
        editeurView.setText(item.editeur);
        getActionBar().setTitle(item.titre);
        imageCache.getFromCache(item.iSBN, item.imgUrl, icone);

        initNavigation();

        initStars(item.note);

        initAvis(item);

        if (item.add) {
            initAjout(addBtn, item.iSBN);
        } else {
            initReservation(addBtn, item);
        }

        initCommentaires(item);

    }

    private void initAvis(final Livre livre) {
        if (!livre.add && prefs.getString(BreizhLibConstantes.ACCOUNT_NAME, null) != null) {
            if (prefs.getString(BreizhLibConstantes.USER, null) != null) {
                avis.setOnClickListener(new Button.OnClickListener() {

                    public void onClick(View view) {
                        Intent pIntent = new Intent(getApplicationContext(), AvisActivity.class);
                        pIntent.putExtra("livre", livre);
                        LivreActivity.this.startActivity(pIntent);
                    }
                });
            } else {
                avis.setOnClickListener(new Button.OnClickListener() {

                    public void onClick(View view) {
                        Log.d(TAG, "click avis");
                        Toast.makeText(LivreActivity.this.getApplicationContext(), getString(R.string.connexion_required), Toast.LENGTH_SHORT).show();
                    }
                });
            }

        } else {
            avis.setVisibility(View.INVISIBLE);
        }
    }

    private void initCommentaires(Livre livre) {
        final ArrayList<Commentaire> commentaires = commentaireDAO.findByIsbn(livre.iSBN);

        commentaireItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Commentaire commentaire = (Commentaire) commentaireItems.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), CommentaireActivity.class);
                intent.putExtra("item", commentaire);
                intent.putExtra("items", commentaires);
                intent.putExtra("index", position);
                startActivity(intent);
            }
        });

        Log.d(TAG, livre.iSBN + " size :" + commentaires.size());
        CommentairesAdapter commentairesAdapter = new CommentairesAdapter(this.getBaseContext(), commentaires);
        commentaireItems.setAdapter(commentairesAdapter);
    }

    private void initReservation(Button button, final Livre livre) {
        button.setEnabled(false);
        if (livre.etat.equals("RESERVE")) {
            button.setText(getString(R.string.reserveBtn));
        } else if (livre.etat.equals("DISP0NIBLE")) {
            button.setText(getString(R.string.reserverBtn));
            if (prefs.getString(BreizhLibConstantes.ACCOUNT_NAME, null) != null) {
                button.setEnabled(true);
                if (prefs.getString(BreizhLibConstantes.USER, null) != null) {
                    button.setOnClickListener(new Button.OnClickListener() {

                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(), ReservationActivity.class);
                            intent.putExtra("livre", livre);
                            LivreActivity.this.startActivity(intent);
                        }
                    });
                } else {
                    button.setOnClickListener(new Button.OnClickListener() {

                        public void onClick(View view) {

                            Toast.makeText(LivreActivity.this.getApplicationContext(), "Vous devez être connecté", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        } else {
            button.setText(getString(R.string.indispoBtn));
            button.setBackgroundColor(Color.RED);
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
                                intent.putExtra("item", result);
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
            button.setBackgroundColor(Color.RED);
        }
    }

    @Override
    protected Class<? extends Activity> getActivityClass() {
        return LivreActivity.class;
    }
}