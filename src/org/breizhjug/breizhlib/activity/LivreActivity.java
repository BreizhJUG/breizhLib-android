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
import org.breizhjug.breizhlib.model.Livre;
import org.breizhjug.breizhlib.utils.ISBNImageCache;

import java.util.ArrayList;


public class LivreActivity extends AbstractActivity {

    private String backActivity;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.livre);
        initView();
    }

    @Override
    public void init(Intent intent) {

    }

    private void initView() {
        SharedPreferences prefs = BreizhLib.getSharedPreferences(getApplicationContext());
        final Livre livre = (Livre) getIntent().getSerializableExtra("livre");
        backActivity = getIntent().getStringExtra("backActivity");
        final ArrayList<Livre> ouvrages = (ArrayList<Livre>) getIntent().getSerializableExtra("livres");
        final int index = (int) getIntent().getIntExtra("index", 0);

        TextView titreView = (TextView) findViewById(R.id.titre);
        titreView.setText(livre.titre);

        TextView editeurView = (TextView) findViewById(R.id.editeur);
        editeurView.setText(livre.editeur);

        ImageView icone = (ImageView) findViewById(R.id.img);
        ISBNImageCache.getIsbnImageFromCache(livre.iSBN, livre.imgUrl, icone);

        initStars(livre.note);

        LinearLayout nav = (LinearLayout) findViewById(R.id.nav);
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
                        intent.putExtra("backActivity", backActivity);
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
                        intent.putExtra("backActivity", backActivity);
                        startActivity(intent);
                        finish();
                    }
                });
            } else {
                next.setEnabled(false);
            }
        } else {
            nav.setVisibility(View.INVISIBLE);
        }

        Button avis = (Button) findViewById(R.id.addComment);


        if (prefs.getString(BreizhLibConstantes.ACCOUNT_NAME, null) != null) {
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
                        Log.d("breizhlib","click avis");
                        Toast.makeText(LivreActivity.this.getApplicationContext(), "Vous devez être connecté", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        } else {
            avis.setVisibility(View.INVISIBLE);
        }

        Button button = (Button) findViewById(R.id.add);
        if (livre.add) {
            initAjout(button, livre.iSBN);
            avis.setVisibility(View.INVISIBLE);
        } else {
            initReservation(button, livre.etat, livre.iSBN);
        }
    }


    private void initStars(int note) {
        ImageView star1 = (ImageView) findViewById(R.id.star1);
        ImageView star2 = (ImageView) findViewById(R.id.star2);
        ImageView star3 = (ImageView) findViewById(R.id.star3);
        ImageView star4 = (ImageView) findViewById(R.id.star4);
        ImageView star5 = (ImageView) findViewById(R.id.star5);
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
                                Populator.populate(intent, result);
                                LivreActivity.this.startActivity(intent);
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