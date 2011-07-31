package org.breizhjug.breizhlib.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.breizhjug.breizhlib.R;


public class ReservationActivity extends AbstractActivity {


    @Override
    public void init(Intent intent) {
        SharedPreferences prefs = breizhLib.getSharedPreferences(this);


        EditText prenom = (EditText) findViewById(R.id.prenomEdit);
        prenom.setText(prefs.getString(breizhLib.USER_PRENOM, null));

        EditText nom = (EditText) findViewById(R.id.nomEdit);
        nom.setText(prefs.getString(breizhLib.USER_NOM, null));

        EditText email = (EditText) findViewById(R.id.emailEdit);
        email.setText(prefs.getString(breizhLib.USER, null));

        Button button = (Button) findViewById(R.id.send);

        button.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View view) {
                Toast.makeText(ReservationActivity.this, "TODO send", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserver);
    }
}