package org.breizhjug.breizhlib.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.breizhjub.breizhlib.R;
import org.breizhjug.breizhlib.model.Livre;


public class LivreActivity extends AsyncActivity {

    @Override
    public void init(Intent intent) {
        String titre = getIntent().getStringExtra("titre");
        String editeur = getIntent().getStringExtra("editeur");
        final String isbn = getIntent().getStringExtra("isbn");

        String img = getIntent().getStringExtra("img");
        String etat = getIntent().getStringExtra("etat");
        boolean add = getIntent().getBooleanExtra("add",false);

        TextView titreView = (TextView) findViewById(R.id.titre);
        titreView.setText(titre);

        TextView editeurView = (TextView) findViewById(R.id.editeur);
        editeurView.setText(editeur);

        ImageView icone = (ImageView) findViewById(R.id.img);
        breizhLib.getImageDownloader().download(img, icone);

        Button button = (Button) findViewById(R.id.add);
        if(add){

            button.setText("Ajouter");
            button.setOnClickListener(new Button.OnClickListener(){

                public void onClick(View view) {
                    //TODO asynchrone call
                    Livre livre =   breizhLib.getOuvrageService().add(isbn);
                    Intent intent = new Intent(getApplicationContext(), LivreActivity.class);
                    Toast.makeText(getApplicationContext(), "Livre Ajouté", Toast.LENGTH_SHORT).show();
                    Populator.populate(intent, livre);
                    LivreActivity.this.startActivity(intent);
                }
            });

            //TODO gestion de l'ajout
        } else {
            button.setEnabled(false);
            if(etat.equals("RESERVE")){
                button.setText("Réservé");
                // TODO gestion de la reservation
            }else  if(etat.equals("DISP0NIBLE")){
                button.setEnabled(true);
                button.setText("Réserver");
                button.setBackgroundColor(Color.GREEN);
                // TODO gestion de la reservation
            } else{
               button.setText("Indisponible");
                button.setBackgroundColor(Color.RED);
            }
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.livre);
    }

}