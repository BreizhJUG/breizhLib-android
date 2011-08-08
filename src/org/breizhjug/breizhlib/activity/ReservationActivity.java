package org.breizhjug.breizhlib.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.R;


public class ReservationActivity extends AbstractActivity {
    private static final String TAG = ReservationActivity.class.getName();
    private SharedPreferences prefs;

    @Override
    public void init(Intent intent) {
        final String isbn = intent.getStringExtra("isbn");

        prefs = BreizhLib.getSharedPreferences(this);

        final EditText prenom = (EditText) findViewById(R.id.prenomEdit);
        prenom.setText(prefs.getString(BreizhLibConstantes.USER_PRENOM, null));

        final EditText nom = (EditText) findViewById(R.id.nomEdit);
        nom.setText(prefs.getString(BreizhLibConstantes.USER_NOM, null));

        final EditText email = (EditText) findViewById(R.id.emailEdit);
        email.setText(prefs.getString(BreizhLibConstantes.USER, null));

        Button button = (Button) findViewById(R.id.send);
        button.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View view) {
                onSend(isbn, prenom.getText().toString(), nom.getText().toString(), email.getText().toString());
            }
        });


    }

    private void onSend(final String isbn, final String prenom, final String nom, final String email) {
        final AsyncTask<Void, Void, Boolean> initTask = new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                String authCookie = prefs.getString(BreizhLibConstantes.AUTH_COOKIE, null);
                boolean result = BreizhLib.getReservationService().reserver(authCookie, isbn, nom, prenom, email);
                Log.d(TAG, "result " + result);
                return result;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (result == null || !result) {
                    showError("Erreur lors de la réservation", true);
                } else {
                    Intent intent = new Intent(getApplicationContext(), Menu.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        };

        if (email == null || email.length() == 0) {
            showError("Email non renseigné", false);
        } else if (prenom == null || prenom.length() == 0) {
            showError("Prénom non renseigné", false);
        } else if (nom == null || nom.length() == 0) {
            showError("Nom non renseigné", false);
        } else {
            initTask.execute();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserver);
    }
}