package org.breizhjug.breizhlib.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.breizhjub.breizhlib.R;


public class ScanActivity extends Activity {


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.scan);

        Button btn = (Button)findViewById(R.id.scanbtn);

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

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                // Handle successful scan
                TextView value =  (TextView)findViewById(R.id.value);
                value.setText(contents);
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            }
        }
    }
}