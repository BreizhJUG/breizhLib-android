package org.breizhjug.breizhlib.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.inject.Inject;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.activity.common.AbstractGDActivity;
import org.breizhjug.breizhlib.model.Livre;
import org.breizhjug.breizhlib.remote.ReservationService;
import org.breizhjug.breizhlib.remote.Result;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import static org.breizhjug.breizhlib.IntentConstantes.*;

public class ReservationActivity extends AbstractGDActivity {
    private static final String TAG = "BreizhLib.ReservationActivity";


    @InjectView(R.id.emailEdit)
    EditText email;
    @InjectView(R.id.nomEdit)
    EditText nom;
    @InjectView(R.id.prenomEdit)
    EditText prenom;
    @InjectView(R.id.send)
    Button button;

    @InjectExtra(LIVRE)
    Livre livre;
    @Inject
    private ReservationService service;

    @Override
    public void init(Intent intent) {
        setActionBarContentView(R.layout.reserver);
        initView();
    }

    public void initView() {
        getActionBar().setTitle(livre.titre);
        prenom.setText(prefs.getString(BreizhLibConstantes.USER_PRENOM, null));

        nom.setText(prefs.getString(BreizhLibConstantes.USER_NOM, null));

        email.setText(prefs.getString(BreizhLibConstantes.USER, null));
        email.setEnabled(false);

        button.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View view) {
                onSend(livre.iSBN, prenom.getText().toString(), nom.getText().toString(), email.getText().toString());
            }
        });
    }

    private void onSend(final String isbn, final String prenom, final String nom, final String email) {
        final AsyncTask<Void, Void, Result> initTask = new AsyncTask<Void, Void, Result>() {
            private ProgressDialog waitDialog;

            @Override
            protected void onPreExecute() {
                if (waitDialog == null) {
                    waitDialog = new ProgressDialog(ReservationActivity.this);
                    waitDialog.setIndeterminate(true);
                }

                waitDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                    public void onCancel(DialogInterface dialog) {
                        if (this != null) {
                            //this.cancel(true);
                        }
                        ReservationActivity.this.finish();
                    }
                });
                waitDialog.setTitle(livre.titre);
                waitDialog.setMessage(getString(R.string.send_data));
                waitDialog.show();
            }

            @Override
            protected Result doInBackground(Void... params) {
                String authCookie = prefs.getString(BreizhLibConstantes.AUTH_COOKIE, null);
                Result result = service.reserver(authCookie, isbn, nom, prenom, email);
                return result;
            }

            @Override
            protected void onPostExecute(Result result) {
                waitDialog.dismiss();
                if (!result.valid) {
                    showError(result.msg, true);
                } else {
                    startMenuActivity();
                }
            }
        };

        if (validate()) {
            initTask.execute();
        }
    }

    private void startMenuActivity() {
        Intent intent = new Intent(getApplicationContext(), Menu.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private boolean validate() {
        if (email == null || email.length() == 0) {
            showError(getString(R.string.email_validation_msg), false);
            return false;
        }
        if (prenom == null || prenom.length() == 0) {
            showError(getString(R.string.prenom_validation_msg), false);
            return false;
        }
        if (nom == null || nom.length() == 0) {
            showError(getString(R.string.nom_validation_msg), false);
            return false;
        }
        return true;
    }

}