package org.breizhjug.breizhlib.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.model.Livre;
import org.breizhjug.breizhlib.remote.OuvrageService;
import org.breizhjug.breizhlib.utils.IntentIntegrator;


public class ScanActivity extends AbstractActivity {
    private static final String TAG = ScanActivity.class.getSimpleName();

    private String isbn;
    private static final String USER_AGENT = "ZXing (Android)";

    @Override
    public void init(Intent intent) {
        IntentIntegrator.initiateScan(this);
        finish();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public DialogInterface.OnClickListener mSend = new DialogInterface.OnClickListener() {


        public void onClick(DialogInterface dialogInterface, int i) {
            OuvrageService service = BreizhLib.getOuvrageService();
            SharedPreferences prefs = BreizhLib.getSharedPreferences(ScanActivity.this);
            Livre livre = service.find(prefs.getString(BreizhLibConstantes.AUTH_COOKIE, null), isbn);

            if (livre != null) {
                Log.d(TAG, livre.titre);
                Intent intent = new Intent(ScanActivity.this, LivreActivity.class);
                Populator.populate(intent, livre);
                startActivity(intent);
            }
        }
    };


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                if (format.equals("EAN_13") && isIsbn13(contents)) {
                    isbn = contents;
                    AlertDialog.Builder adb = new AlertDialog.Builder(ScanActivity.this);
                    adb.setTitle(getString(R.string.isbn_scan_titre_ok));
                    adb.setMessage(getString(R.string.isbn_scan_msg_ok) + contents);
                    adb.setNegativeButton(getString(R.string.retour), null);
                    adb.setNeutralButton(getString(R.string.rechercher), mSend);
                    adb.show();
                } else {
                    AlertDialog.Builder adb = new AlertDialog.Builder(ScanActivity.this);
                    adb.setTitle(getString(R.string.isbn_scan_titre_ko));
                    adb.setMessage(getString(R.string.isbn_scan_msg_ko));
                    adb.setNegativeButton(getString(R.string.retour), null);
                    adb.show();
                }
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            }
        }
    }

    public boolean isIsbn13(String isbn) {
        return isbn.length() == 13 && (isbn.startsWith("978") || isbn.startsWith("979"));
    }
}