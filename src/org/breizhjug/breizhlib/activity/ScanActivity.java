package org.breizhjug.breizhlib.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.google.inject.Inject;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.activity.common.AbstractGDActivity;
import org.breizhjug.breizhlib.exception.ResultException;
import org.breizhjug.breizhlib.model.Livre;
import org.breizhjug.breizhlib.remote.OuvrageService;
import org.breizhjug.breizhlib.utils.scancode.IntentIntegrator;
import org.breizhjug.breizhlib.utils.scancode.IntentResult;

import java.util.ArrayList;


public class ScanActivity extends AbstractGDActivity {
    private static final String TAG = "Breizhlib.ScanActivity";

    private String isbn;
    private static final String USER_AGENT = "ZXing (Android)";

    @Inject
    private OuvrageService service;

    @Override
    public void init(Intent intent) {
        IntentIntegrator.initiateScan(this);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public DialogInterface.OnClickListener mSend = new DialogInterface.OnClickListener() {


        public void onClick(DialogInterface dialogInterface, int i) {

            Livre livre = null;
            try {
                livre = service.find(prefs.getString(BreizhLibConstantes.AUTH_COOKIE, null), isbn);
                if (livre != null) {
                    Log.d(TAG, livre.titre);
                    Intent intent = new Intent(ScanActivity.this, LivreActivity.class);
                    ArrayList<Livre> items = new ArrayList<Livre>();
                                         items.add(livre);
                                         intent.putExtra("items", items);
                    intent.putExtra("item", livre);
                    intent.putExtra("index", 0);
                    startActivity(intent);
                    finish();
                }
            } catch (ResultException e) {
                showError(e.result.msg, true);
                finish();
            }


        }
    };


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            Log.d(TAG, "result OK");
            String contents = scanResult.getContents();
            String format = scanResult.getFormatName();
            if (format != null && format.equals("EAN_13") && isIsbn13(contents)) {
                isbn = contents;
                Log.d(TAG, "isbn : " + isbn);
                AlertDialog.Builder adb = new AlertDialog.Builder(ScanActivity.this);
                adb.setTitle(getString(R.string.isbn_scan_titre_ok));
                adb.setMessage(getString(R.string.isbn_scan_msg_ok) + contents);
                adb.setNegativeButton(getString(R.string.retour), null);
                adb.setNeutralButton(getString(R.string.rechercher), mSend);
                adb.show();
            } else {
                Log.d(TAG, "isbn ko ");
                AlertDialog.Builder adb = new AlertDialog.Builder(ScanActivity.this);
                adb.setTitle(getString(R.string.isbn_scan_titre_ko));
                adb.setMessage(getString(R.string.isbn_scan_msg_ko));
                adb.setNegativeButton(getString(R.string.retour), null);
                adb.show();
                finish();
            }
        } else {
            // Handle cancel
            Log.d(TAG, "result KO");
        }
    }

    public boolean isIsbn13(String isbn) {
        return isbn != null && isbn.length() == 13 && (isbn.startsWith("978") || isbn.startsWith("979"));
    }
}