package org.breizhjug.breizhlib.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.model.Commentaire;

import java.util.ArrayList;


public class CommentaireActivity extends AbstractActivity {

    @Override
    public void init(Intent intent) {

        final Commentaire commentaire = (Commentaire) getIntent().getSerializableExtra("commentaire");
        final ArrayList<Commentaire> commentaires = (ArrayList<Commentaire>) getIntent().getSerializableExtra("commentaires");
        final int index = (int) getIntent().getIntExtra("index", 0);
        TextView text = (TextView) findViewById(R.id.titre);
        text.setText(commentaire.livre.titre);

        text = (TextView) findViewById(R.id.user);
        text.setText(commentaire.nom);

        text = (TextView) findViewById(R.id.description);
        text.setText(commentaire.commentaire);

        ImageView icone = (ImageView) findViewById(R.id.img);
        breizhLib.getImageDownloader().download(commentaire.livre.imgUrl, icone);


        Button button = (Button) findViewById(R.id.previous);
        if (index > 1) {
            button.setOnClickListener(new Button.OnClickListener() {

                public void onClick(View view) {
                    Commentaire commentaire = commentaires.get(index - 1);
                    Intent intent = new Intent(getApplicationContext(), CommentaireActivity.class);
                    intent.putExtra("commentaire", commentaire);
                    intent.putExtra("commentaires", commentaires);
                    intent.putExtra("index", index - 1);
                    startActivity(intent);
                }
            });
        } else {
            button.setEnabled(false);
        }

        Button next = (Button) findViewById(R.id.next);
        if (commentaires.size() - 2 > index) {
            next.setOnClickListener(new Button.OnClickListener() {

                public void onClick(View view) {
                    Commentaire commentaire = commentaires.get(index + 1);
                    Intent intent = new Intent(getApplicationContext(), CommentaireActivity.class);
                    intent.putExtra("commentaire", commentaire);
                    intent.putExtra("commentaires", commentaires);
                    intent.putExtra("index", index + 1);
                    startActivity(intent);
                }
            });
        } else {
            next.setEnabled(false);
        }

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commentaire);
    }
}