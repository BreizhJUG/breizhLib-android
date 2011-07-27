package org.breizhjug.breizhlib.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import org.breizhjub.breizhlib.R;
import org.breizhjug.breizhlib.utils.Utils;


public class ProfilActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil);
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
        icone.setImageDrawable(ImageUtils.imageOperations(getImageUrl(email)));
    }

    public String getImageUrl(String email) {
        if (TextUtils.isEmpty(email)) return null;
        String emailHash = Utils.md5Hex(email.toLowerCase().trim());
        return "http://www.gravatar.com/avatar/" + emailHash;
    }
}