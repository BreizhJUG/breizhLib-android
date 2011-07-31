package org.breizhjug.breizhlib.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.R;


public class CreditsActivity extends AbstractActivity {
    @Override
    public void init(Intent intent) {

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credits);
    }


    public void web(View view) {
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(BreizhLib.SERVER_URL+"android")));
	}
}