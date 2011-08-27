package org.breizhjug.breizhlib.activity.compte;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.google.inject.Inject;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.activity.AbstractActivity;
import org.breizhjug.breizhlib.adapter.AccountsAdapter;
import org.breizhjug.breizhlib.utils.GoogleAuthentification;
import org.breizhjug.breizhlib.utils.images.ImageCache;

import java.util.List;


public class CompteList extends AbstractActivity {

    private ListView listView;
    private String authcookie;
    @Inject
    private SharedPreferences prefs;
    @Inject
    private ImageCache imageCache;
    @Inject
    private GoogleAuthentification gAuth;

    @Override
    public void init(Intent intent) {

        authcookie = prefs.getString(BreizhLibConstantes.AUTH_COOKIE, null);
        if (authcookie == null) {
            setContentView(R.layout.items);
        }

        if (authcookie == null) {
            listView = (ListView) findViewById(R.id.items);

            List<String> accounts = gAuth.getGoogleAccounts(this);

            listView.setAdapter(new AccountsAdapter(this.getBaseContext(), accounts));

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                    String account = (String) listView.getItemAtPosition(position);
                    Intent intent = new Intent(getApplicationContext(), AppInfo.class);
                    intent.putExtra("account", account);
                    startActivity(intent);
                    finish();
                }
            });
        } else {
            Intent pIntent = new Intent(this, ProfilActivity.class);
            pIntent.putExtra("email", prefs.getString(BreizhLibConstantes.ACCOUNT_NAME, null));
            startActivity(pIntent);
            finish();
        }


    }
}