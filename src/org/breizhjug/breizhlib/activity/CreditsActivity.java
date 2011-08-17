package org.breizhjug.breizhlib.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.utils.version.Version;


public class CreditsActivity extends AbstractActivity {

    @Override
    public void init(Intent intent) {

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credits);

        TextView versionView = (TextView) findViewById(R.id.version);
        versionView.setText(getString(R.string.version) + " " + Version.version);
    }


    public void web(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(BreizhLibConstantes.SERVER_URL + "android")));
    }
}