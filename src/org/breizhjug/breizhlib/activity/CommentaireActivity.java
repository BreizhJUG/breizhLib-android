package org.breizhjug.breizhlib.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.model.Commentaire;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

import java.util.ArrayList;


public class CommentaireActivity extends AbstractActivity {

    @InjectView(R.id.titre)
    TextView titre;
    @InjectView(R.id.user)
    TextView user;
    @InjectView(R.id.description)
    TextView description;
    @InjectView(R.id.img)
    ImageView icone;
    @InjectView(R.id.nav)
    LinearLayout nav;

    @InjectExtra("commentaire")
    Commentaire commentaire;
    @InjectExtra("index")
    int index;
    @InjectExtra("commentaires")
    ArrayList<Commentaire> commentaires;

    @Override
    public void init(Intent intent) {
    }

    public void initView() {

        titre.setText(commentaire.livre.titre);
        user.setText(commentaire.nom);
        description.setText(commentaire.commentaire);

        BreizhLib.getImageCache().getFromCache(commentaire.livre.iSBN, commentaire.livre.imgUrl, icone);

        icone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LivreActivity.class);
                //intent.putExtra("livres", items);
                intent.putExtra("index", 0);
                intent.putExtra("livre", commentaire.livre);
                CommentaireActivity.this.startActivity(intent);
                finish();
            }
        });

        initNavigation();

        initStars(commentaire.note);


    }

    private void initNavigation() {
        Button previous = (Button) nav.getChildAt(0);
        Button next = (Button) nav.getChildAt(1);

        if (commentaires != null) {
            if (index > 0) {
                previous.setOnClickListener(new Button.OnClickListener() {

                    public void onClick(View view) {
                        Commentaire commentaire = commentaires.get(index - 1);
                        Intent intent = new Intent(getApplicationContext(), CommentaireActivity.class);
                        intent.putExtra("commentaire", commentaire);
                        intent.putExtra("commentaires", commentaires);
                        intent.putExtra("index", index - 1);
                        startActivity(intent);
                        finish();
                    }
                });
            } else {
                previous.setEnabled(false);
            }


            if (commentaires.size() - 1 > index) {
                next.setOnClickListener(new Button.OnClickListener() {

                    public void onClick(View view) {
                        Commentaire commentaire = commentaires.get(index + 1);
                        Intent intent = new Intent(getApplicationContext(), CommentaireActivity.class);
                        intent.putExtra("commentaire", commentaire);
                        intent.putExtra("commentaires", commentaires);
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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commentaire);
        initView();
    }

}