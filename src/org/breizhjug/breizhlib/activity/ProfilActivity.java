package org.breizhjug.breizhlib.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import org.breizhjub.breizhlib.R;
import org.breizhjug.breizhlib.utils.Utils;


public class ProfilActivity extends AsyncActivity {


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil);

     }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Menu.class);
		startActivity(intent);
    }

    public void init(Intent intent){
        String email = (String) getIntent().getExtras().get("email");

        TextView nom = (TextView) findViewById(R.id.nom);
        nom.setText("Guernion");

        TextView prenom = (TextView) findViewById(R.id.prenom);
        prenom.setText("Sylvain");

        TextView emailV = (TextView) findViewById(R.id.email);
        emailV.setText(email);

        TextView username = (TextView) findViewById(R.id.username);
        username.setText("sguernion");

        ImageView icone = (ImageView) findViewById(R.id.avatar);
        breizhLib.getImageDownloader().download(Utils.getGravatarImage(email), icone);
    }
}