package org.breizhjug.breizhlib.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.R;


public class AvisActivity extends AbstractActivity {

    @Override
    public void init(Intent intent) {
        final String isbn = intent.getStringExtra("isbn");

        final SharedPreferences prefs = breizhLib.getSharedPreferences(this);


        final Spinner note = (Spinner) findViewById(R.id.spinnerNote);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add("1");
        adapter.add("2");
        adapter.add("3");
        adapter.add("4");
        adapter.add("5");
        note.setAdapter(adapter);

        String nom = prefs.getString(BreizhLib.USER_NOM, "") + " " +
                prefs.getString(BreizhLib.USER_PRENOM, "");
        final EditText nomEdit = (EditText) findViewById(R.id.nomEdit);
        nomEdit.setText(nom);

        final EditText avisEdit = (EditText) findViewById(R.id.avisEdit);

        Button button = (Button) findViewById(R.id.send);

        button.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View view) {
                //TODO Async call
                String authCookie = prefs.getString(breizhLib.AUTH_COOKIE, null);
                boolean result = breizhLib.getCommentaireService().comment(authCookie, isbn, nomEdit.getText().toString(), avisEdit.getText().toString(), Integer.valueOf("" + note.getSelectedItem()));
                if (result) {
                    Toast.makeText(AvisActivity.this, getString(R.string.commentaireSave), Toast.LENGTH_SHORT);
                }
            }
        });
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avis);
    }
}