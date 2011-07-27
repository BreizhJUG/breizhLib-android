package org.breizhjug.breizhlib.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import org.breizhjub.breizhlib.R;
import org.breizhjug.breizhlib.model.Livre;
import org.breizhjug.breizhlib.remote.OuvrageService;


public class ScanActivity extends Activity {
    private static final String TAG = ScanActivity.class.getSimpleName();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan);

        Button btn = (Button) findViewById(R.id.scanbtn);

        btn.setOnClickListener(mScan);
    }


    public Button.OnClickListener mScan = new Button.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.setPackage("com.google.zxing.client.android");
            intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
            startActivityForResult(intent, 0);
        }
    };

    private static final String USER_AGENT = "ZXing (Android)";

    public DialogInterface.OnClickListener mSend = new DialogInterface.OnClickListener() {


        public void onClick(DialogInterface dialogInterface, int i) {
            OuvrageService service = new OuvrageService();

            Livre livre = service.find(isbn);

            if (livre != null) {
                Log.d("ISBN", livre.getTitre());
                Intent intent = new Intent(ScanActivity.this, LivreActivity.class);
                intent.putExtra("titre", livre.getTitre());
                intent.putExtra("editeur", livre.getEditeur());
                intent.putExtra("img", livre.getImgUrl());
                intent.putExtra("add", livre.add);
                startActivity(intent);
            }
        }
    };

    private String isbn;

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                if (format.equals("EAN_13")) {
                    isbn = contents;
                    AlertDialog.Builder adb = new AlertDialog.Builder(ScanActivity.this);
                    adb.setTitle("Livre trouvé");
                    adb.setMessage("isbn : " + contents);
                    adb.setNegativeButton("Retour", null);
                    adb.setNeutralButton("Rechercher", mSend);
                    adb.show();
                }
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            }
        }
    }
}