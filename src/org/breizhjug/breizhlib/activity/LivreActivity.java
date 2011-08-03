package org.breizhjug.breizhlib.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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

        String titre = null;
        String editeur = null;
        String img = null;
        String etat = null;
        boolean add;
        final String isbn = getIntent().getStringExtra("isbn");
        if (getIntent().hasExtra("livre")) {
            Livre livre = (Livre) getIntent().getSerializableExtra("livre");
            titre = livre.titre;
            editeur = livre.editeur;
            img = livre.imgUrl;
            etat = livre.etat;
            add = livre.add;
        } else {
            titre = getIntent().getStringExtra("titre");
            editeur = getIntent().getStringExtra("editeur");
            img = getIntent().getStringExtra("img");
            etat = getIntent().getStringExtra("etat");
            add = getIntent().getBooleanExtra("add", false);
        }


        TextView titreView = (TextView) findViewById(R.id.titre);
        titreView.setText(titre);

        TextView editeurView = (TextView) findViewById(R.id.editeur);
        editeurView.setText(editeur);

        ImageView icone = (ImageView) findViewById(R.id.img);
        breizhLib.getImageDownloader().download(img, icone);

        Button avis = (Button) findViewById(R.id.addComment);


        if (prefs.getString(BreizhLib.ACCOUNT_NAME, null) != null) {
            avis.setOnClickListener(new Button.OnClickListener() {

                public void onClick(View view) {
                    Intent pIntent = new Intent(getApplicationContext(), AvisActivity.class);
                    pIntent.putExtra("isbn", isbn);
                    LivreActivity.this.startActivity(pIntent);
                }
            });

        } else {
            avis.setEnabled(false);
        }

        Button button = (Button) findViewById(R.id.add);
        if (add) {
            if (prefs.getBoolean(breizhLib.USER_ADMIN, false)) {
                button.setText(getString(R.string.ajouterBtn));
                button.setOnClickListener(new Button.OnClickListener() {

                    public void onClick(View view) {
                        SharedPreferences prefs = breizhLib.getSharedPreferences(LivreActivity.this);
                        String authCookie = prefs.getString(breizhLib.AUTH_COOKIE, null);
                        //TODO asynchrone call
                        Livre livre = breizhLib.getOuvrageService().add(authCookie, isbn);
                        Intent intent = new Intent(getApplicationContext(), LivreActivity.class);
                        Toast.makeText(getApplicationContext(), getString(R.string.ajoutOK), Toast.LENGTH_SHORT).show();
                        Populator.populate(intent, livre);
                        LivreActivity.this.startActivity(intent);
                    }
                });
            } else {
                button.setText(getString(R.string.nonDispoBtn));
                button.setEnabled(false);
                button.setBackgroundColor(Color.RED);
            }
        } else {
            button.setEnabled(false);
            if (etat.equals("RESERVE")) {
                button.setText(getString(R.string.reserveBtn));
            } else if (etat.equals("DISP0NIBLE")) {
                button.setEnabled(true);
                button.setText(getString(R.string.reserverBtn));
                if (prefs.getString(breizhLib.USER, null) != null) {
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
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.livre);
    }

}