package org.breizhjug.breizhlib.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.adapter.CommentairesAdapter;
import org.breizhjug.breizhlib.database.Database;
import org.breizhjug.breizhlib.database.dao.CommentaireDAO;
import org.breizhjug.breizhlib.model.Commentaire;
import org.breizhjug.breizhlib.model.Livre;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

import java.util.ArrayList;


public class LivreActivity extends AbstractActivity {

    protected static Database db = BreizhLib.getDataBaseHelper();
    private static final String TAG = "BreizhLib.LivreActivity";
    private SharedPreferences prefs;

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
    @InjectView(R.id.nav)
    LinearLayout nav;
    @InjectView(R.id.items)
    ListView commentaireItems;

    @InjectExtra("livre")
    Livre livre;
    @InjectExtra("livres")
    ArrayList<Livre> ouvrages;
    @InjectExtra("index")
    int index;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.livre);
        initView();
    }

    @Override
    public void init(Intent intent) {

    }

    private void initView() {
        prefs = BreizhLib.getSharedPreferences(getApplicationContext());

        titreView.setText(livre.titre);
        editeurView.setText(livre.editeur);

        BreizhLib.getImageCache().getFromCache(livre.iSBN, livre.imgUrl, icone);

        initNavigation();

        initStars(livre.note);

        initAvis(livre);

        if (livre.add) {
            initAjout(addBtn, livre.iSBN);
        } else {
            initReservation(addBtn, livre.etat, livre.iSBN);
        }

        initCommentaires(livre);

    }

    private void initNavigation() {
        Button previous = (Button) nav.getChildAt(0);
        Button next = (Button) nav.getChildAt(1);
        if (ouvrages != null) {
            if (index > 0) {
                previous.setOnClickListener(new Button.OnClickListener() {

                    public void onClick(View view) {
                        Livre livre = ouvrages.get(index - 1);
                        Intent intent = new Intent(getApplicationContext(), LivreActivity.class);
                        intent.putExtra("livre", livre);
                        intent.putExtra("livres", ouvrages);
                        intent.putExtra("index", index - 1);
                        startActivity(intent);
                        finish();
                    }
                });
            } else {
                previous.setEnabled(false);
            }

            if (ouvrages.size() - 1 > index) {
                next.setOnClickListener(new Button.OnClickListener() {

                    public void onClick(View view) {
                        Livre livre = ouvrages.get(index + 1);
                        Intent intent = new Intent(getApplicationContext(), LivreActivity.class);
                        intent.putExtra("livre", livre);
                        intent.putExtra("livres", ouvrages);
                        intent.putExtra("index", index + 1);
                        startActivity(intent);
                        finish();
                    }
                });
            } else {
                next.setEnabled(false);
            }
        } else {
            previous.setEnabled(false);
            next.setEnabled(false);
        }
    }

    private void initAvis(final Livre livre) {
        if (!livre.add && prefs.getString(BreizhLibConstantes.ACCOUNT_NAME, null) != null) {
            if (BreizhLib.getSharedPreferences(getApplicationContext()).getString(BreizhLibConstantes.USER, null) != null) {
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
                        Toast.makeText(LivreActivity.this.getApplicationContext(), "Vous devez être connecté", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        } else {
            avis.setVisibility(View.INVISIBLE);
        }
    }

    private void initCommentaires(Livre livre) {
        final ArrayList<Commentaire> commentaires = CommentaireDAO.findByIsbn(livre.iSBN);

        commentaireItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Commentaire commentaire = (Commentaire) commentaireItems.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), CommentaireActivity.class);
                intent.putExtra("commentaire", commentaire);
                intent.putExtra("commentaires", commentaires);
                intent.putExtra("index", position);
                startActivity(intent);
            }
        });

        Log.d(TAG, livre.iSBN + " size :" + commentaires.size());
        CommentairesAdapter commentairesAdapter = new CommentairesAdapter(this.getBaseContext(), commentaires);
        commentaireItems.setAdapter(commentairesAdapter);
    }


    private void initStars(int note) {
        LinearLayout nav = (LinearLayout) findViewById(R.id.stars);
        ImageView star1 = (ImageView) nav.getChildAt(0);
        ImageView star2 = (ImageView) nav.getChildAt(1);
        ImageView star3 = (ImageView) nav.getChildAt(2);
        ImageView star4 = (ImageView) nav.getChildAt(3);
        ImageView star5 = (ImageView) nav.getChildAt(4);
        switch (note) {
            case 0:
                star1.setVisibility(View.INVISIBLE);
            case 1:
                star2.setVisibility(View.INVISIBLE);
            case 2:
                star3.setVisibility(View.INVISIBLE);
            case 3:
                star4.setVisibility(View.INVISIBLE);
            case 4:
                star5.setVisibility(View.INVISIBLE);
            case 5:
                break;
        }
    }

    private void initReservation(Button button, String etat, final String isbn) {
        button.setEnabled(false);
        if (etat.equals("RESERVE")) {
            button.setText(getString(R.string.reserveBtn));
        } else if (etat.equals("DISP0NIBLE")) {
            button.setText(getString(R.string.reserverBtn));
            if (BreizhLib.getSharedPreferences(getApplicationContext()).getString(BreizhLibConstantes.ACCOUNT_NAME, null) != null) {
                button.setEnabled(true);
                if (BreizhLib.getSharedPreferences(getApplicationContext()).getString(BreizhLibConstantes.USER, null) != null) {
                    button.setOnClickListener(new Button.OnClickListener() {

                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(), ReservationActivity.class);
                            intent.putExtra("isbn", isbn);
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
        if (BreizhLib.getSharedPreferences(getApplicationContext()).getBoolean(BreizhLibConstantes.USER_ADMIN, false)) {
            button.setText(getString(R.string.ajouterBtn));
            button.setOnClickListener(new Button.OnClickListener() {

                public void onClick(View view) {
                    final AsyncTask<Void, Void, Livre> initTask = new AsyncTask<Void, Void, Livre>() {

                        @Override
                        protected Livre doInBackground(Void... params) {
                            SharedPreferences prefs = BreizhLib.getSharedPreferences(LivreActivity.this.getApplicationContext());
                            String authCookie = prefs.getString(BreizhLibConstantes.AUTH_COOKIE, null);
                            Livre livre = BreizhLib.getOuvrageService().add(authCookie, isbn);
                            return livre;
                        }

                        @Override
                        protected void onPostExecute(Livre result) {
                            if (result == null) {
                                showError("Erreur lors de l'ajout", true);
                            } else {
                                Intent intent = new Intent(getApplicationContext(), LivreActivity.class);
                                Toast.makeText(getApplicationContext(), getString(R.string.ajoutOK), Toast.LENGTH_SHORT).show();
                                intent.putExtra("livre", result);
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

}