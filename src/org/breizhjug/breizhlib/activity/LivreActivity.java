package org.breizhjug.breizhlib.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.model.Livre;

import java.util.ArrayList;


public class LivreActivity extends AbstractActivity {

    @Override
    public void init(Intent intent) {

        SharedPreferences prefs = BreizhLib.getSharedPreferences(this);
        final Livre livre = (Livre) getIntent().getSerializableExtra("livre");
        final ArrayList<Livre> ouvrages = (ArrayList<Livre>) getIntent().getSerializableExtra("livres");
        final int index = (int) getIntent().getIntExtra("index", 0);

        TextView titreView = (TextView) findViewById(R.id.titre);
        titreView.setText(livre.titre);

        TextView editeurView = (TextView) findViewById(R.id.editeur);
        editeurView.setText(livre.editeur);

        ImageView icone = (ImageView) findViewById(R.id.img);
        BreizhLib.getImageDownloader().download(livre.imgUrl, icone);



        LinearLayout nav = (LinearLayout) findViewById(R.id.nav);
        Button previous = (Button) nav.getChildAt(0);
        Button next = (Button) nav.getChildAt(1);
          if(ouvrages != null){
        if (index > 0) {
            previous.setOnClickListener(new Button.OnClickListener() {

                public void onClick(View view) {
                    Livre livre = ouvrages.get(index - 1);
                    Intent intent = new Intent(getApplicationContext(), LivreActivity.class);
                    intent.putExtra("livre", livre);
                    intent.putExtra("livres", ouvrages);
                    intent.putExtra("index", index - 1);
                    startActivity(intent);
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
                }
            });
        } else {
            next.setEnabled(false);
        }
          }else {
            nav.setVisibility(View.INVISIBLE);
          }

        Button avis = (Button) findViewById(R.id.addComment);


        if (prefs.getString(BreizhLib.ACCOUNT_NAME, null) != null) {
            avis.setOnClickListener(new Button.OnClickListener() {

                public void onClick(View view) {
                    Intent pIntent = new Intent(getApplicationContext(), AvisActivity.class);
                    pIntent.putExtra("isbn", livre.iSBN);
                    LivreActivity.this.startActivity(pIntent);
                }
            });

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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.livre);
    }

    private void initReservation(Button button, String etat, final String isbn) {
        button.setEnabled(false);
        if (etat.equals("RESERVE")) {
            button.setText(getString(R.string.reserveBtn));
        } else if (etat.equals("DISP0NIBLE")) {
            button.setText(getString(R.string.reserverBtn));
            if (BreizhLib.getSharedPreferences(this).getString(BreizhLib.ACCOUNT_NAME, null) != null) {
                button.setEnabled(true);
                if (BreizhLib.getSharedPreferences(this).getString(BreizhLib.USER, null) != null) {
                    button.setOnClickListener(new Button.OnClickListener() {

                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(), ReservationActivity.class);
                            intent.putExtra("isbn", isbn);
                            LivreActivity.this.startActivity(intent);
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
        if (BreizhLib.getSharedPreferences(this).getBoolean(BreizhLib.USER_ADMIN, false)) {
            button.setText(getString(R.string.ajouterBtn));
            button.setOnClickListener(new Button.OnClickListener() {

                public void onClick(View view) {
                    final AsyncTask<Void, Void, Livre> initTask = new AsyncTask<Void, Void, Livre>() {

                        @Override
                        protected Livre doInBackground(Void... params) {
                            SharedPreferences prefs = BreizhLib.getSharedPreferences(LivreActivity.this);
                            String authCookie = prefs.getString(BreizhLib.AUTH_COOKIE, null);
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