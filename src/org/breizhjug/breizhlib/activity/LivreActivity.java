package org.breizhjug.breizhlib.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import org.breizhjub.breizhlib.R;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.utils.ImageDownloader;


public class LivreActivity extends Activity {

    private final ImageDownloader imageDownloader = BreizhLib.getImageDownloader();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.livre);

        String titre = getIntent().getStringExtra("titre");
        String editeur = getIntent().getStringExtra("editeur");
        String img = getIntent().getStringExtra("img");
        boolean add = getIntent().getBooleanExtra("add",false);

        TextView titreView = (TextView) findViewById(R.id.titre);
        titreView.setText(titre);

        TextView editeurView = (TextView) findViewById(R.id.editeur);
        editeurView.setText(editeur);

        ImageView icone = (ImageView) findViewById(R.id.img);
        imageDownloader.download(img, icone);

        Button button = (Button) findViewById(R.id.add);
        if(add){

            button.setText("Ajouter");

            //TODO gestion de l'ajout
        } else {
            button.setEnabled(false);
            button.setText("Réserver");
            // TODO gestion de la reservation
        }

    }

}