package org.breizhjug.breizhlib.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.inject.Inject;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.remote.ReservationService;
import org.breizhjug.breizhlib.remote.Result;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

public class ReservationActivity extends AbstractActivity {
    private static final String TAG = "BreizhLib.ReservationActivity";


    @InjectView(R.id.emailEdit)
    EditText email;
    @InjectView(R.id.nomEdit)
    EditText nom;
    @InjectView(R.id.prenomEdit)
    EditText prenom;
    @InjectView(R.id.send)
    Button button;

    @InjectExtra("isbn")
    String isbn;
    @Inject
    private ReservationService service;

    @Override
    public void init(Intent intent) {

    }

    public void initView() {

        prenom.setText(prefs.getString(BreizhLibConstantes.USER_PRENOM, null));

        nom.setText(prefs.getString(BreizhLibConstantes.USER_NOM, null));

        email.setText(prefs.getString(BreizhLibConstantes.USER, null));
        email.setEnabled(false);

        button.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View view) {
                onSend(isbn, prenom.getText().toString(), nom.getText().toString(), email.getText().toString());
            }
        });


    }

    private void onSend(final String isbn, final String prenom, final String nom, final String email) {
        final AsyncTask<Void, Void, Result> initTask = new AsyncTask<Void, Void, Result>() {

            @Override
            protected Result doInBackground(Void... params) {
                String authCookie = prefs.getString(BreizhLibConstantes.AUTH_COOKIE, null);
                Result result = service.reserver(authCookie, isbn, nom, prenom, email);
                return result;
            }

            @Override
            protected void onPostExecute(Result result) {
                if (!result.valid) {
                    showError(result.msg, true);
                } else {
                    Intent intent = new Intent(getApplicationContext(), Menu.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        };

        if (validate()) {
            initTask.execute();
        }
    }

    private boolean validate() {
        if (email == null || email.length() == 0) {
            showError("Email non renseigné", false);
            return false;
        }
        if (prenom == null || prenom.length() == 0) {
            showError("Prénom non renseigné", false);
            return false;
        }
        if (nom == null || nom.length() == 0) {
            showError("Nom non renseigné", false);
            return false;
        }
        return true;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserver);
        initView();
    }
}