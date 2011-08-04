package org.breizhjug.breizhlib.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.model.Livre;


public class LivreActivity extends AbstractActivity {

    @Override
    public void init(Intent intent) {

        SharedPreferences prefs = breizhLib.getSharedPreferences(this);
        final Livre livre = (Livre) getIntent().getSerializableExtra("livre");


        TextView titreView = (TextView) findViewById(R.id.titre);
        titreView.setText(livre.titre);

        TextView editeurView = (TextView) findViewById(R.id.editeur);
        editeurView.setText(livre.editeur);

        ImageView icone = (ImageView) findViewById(R.id.img);
        breizhLib.getImageDownloader().download(livre.imgUrl, icone);

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
            avis.setEnabled(false);
        }

        Button button = (Button) findViewById(R.id.add);
        if (livre.add) {
            initAjout(button, livre.iSBN);
            avis.setEnabled(false);
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
            button.setEnabled(true);
            button.setText(getString(R.string.reserverBtn));
            if (breizhLib.getSharedPreferences(this).getString(breizhLib.USER, null) != null) {
                button.setOnClickListener(new Button.OnClickListener() {

                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), ReservationActivity.class);
                        intent.putExtra("isbn", isbn);
                        LivreActivity.this.startActivity(intent);
                    }
                });
            }
        } else {
            button.setText(getString(R.string.indispoBtn));
            button.setBackgroundColor(Color.RED);
        }
    }

    private void initAjout(Button button, final String isbn) {
        if (breizhLib.getSharedPreferences(this).getBoolean(breizhLib.USER_ADMIN, false)) {
            button.setText(getString(R.string.ajouterBtn));
            button.setOnClickListener(new Button.OnClickListener() {

                public void onClick(View view) {
                    final AsyncTask<Void, Void, Livre> initTask = new AsyncTask<Void, Void, Livre>() {

                        @Override
                        protected Livre doInBackground(Void... params) {
                            SharedPreferences prefs = breizhLib.getSharedPreferences(LivreActivity.this);
                            String authCookie = prefs.getString(breizhLib.AUTH_COOKIE, null);
                            Livre livre = breizhLib.getOuvrageService().add(authCookie, isbn);
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