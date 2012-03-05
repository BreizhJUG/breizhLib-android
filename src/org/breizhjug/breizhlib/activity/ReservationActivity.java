package org.breizhjug.breizhlib.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
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

import static org.breizhjug.breizhlib.IntentConstantes.LIVRE;
import static org.breizhjug.breizhlib.utils.IntentSupport.*;

public class ReservationActivity extends AbstractGDActivity {
    private static final String TAG = "BreizhLib.ReservationActivity";


    @InjectView(R.id.emailEdit)
    EditText email;
    @InjectView(R.id.nomEdit)
    EditText nom;
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
        nom.setText(prefs.getString(BreizhLibConstantes.USER_NOM, null));

        email.setText(prefs.getString(BreizhLibConstantes.USER, null));
        email.setEnabled(false);
    }

    public void onSend(View view) {
        onSendReservation(livre.iSBN, nom.getText().toString(), email.getText().toString());
    }

    private void onSendReservation(final String isbn, final String nom, final String email) {
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
                Result result = service.reserver(authCookie, isbn, nom, email);
                return result;
            }

            @Override
            protected void onPostExecute(Result result) {
                waitDialog.dismiss();
                if (!result.valid) {
                    showError(result.msg, true);
                } else {
                    startActivity(newMenuIntent(getApplicationContext()));
                }
            }
        };

        if (validate()) {
            initTask.execute();
        }
    }


    private boolean validate() {
        if (email == null || email.length() == 0) {
            showError(getString(R.string.email_validation_msg));
            return false;
        }

        if (nom == null || nom.length() == 0) {
            showError(getString(R.string.nom_validation_msg));
            return false;
        }
        return true;
    }

}