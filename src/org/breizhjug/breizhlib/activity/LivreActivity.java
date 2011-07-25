package org.breizhjug.breizhlib.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import org.breizhjub.breizhlib.R;
import org.breizhjug.breizhlib.utils.ImageUtils;


public class LivreActivity extends Activity {


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.livre);

        String titre = getIntent().getStringExtra("titre");
        String editeur = getIntent().getStringExtra("editeur");
        String img = getIntent().getStringExtra("img");

        TextView titreView = (TextView)findViewById(R.id.titre);
        titreView.setText(titre);

        TextView editeurView = (TextView)findViewById(R.id.editeur);
        editeurView.setText(editeur);

        ImageView icone = (ImageView) findViewById(R.id.img);
		icone.setImageDrawable(ImageUtils.imageOperations(img));

    }

}