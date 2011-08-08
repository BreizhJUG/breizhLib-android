package org.breizhjug.breizhlib.activity.compte;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.activity.AbstractActivity;
import org.breizhjug.breizhlib.adapter.AccountsAdapter;

import java.util.List;


public class CompteList extends AbstractActivity {

    private ListView listView;
    private String authcookie;
    private SharedPreferences prefs;

    @Override
    public void init(Intent intent) {

        prefs = BreizhLib.getSharedPreferences(this);
        authcookie = prefs.getString(BreizhLibConstantes.AUTH_COOKIE, null);
        if (authcookie == null) {
            setContentView(R.layout.items);
        }

        if (authcookie == null) {
            listView = (ListView) findViewById(R.id.items);

            List<String> accounts = BreizhLib.getGAuth().getGoogleAccounts(this);

            listView.setAdapter(new AccountsAdapter(this.getBaseContext(), accounts));

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                    String account = (String) listView.getItemAtPosition(position);
                    Intent intent = new Intent(getApplicationContext(), AppInfo.class);
                    intent.putExtra("account", account);
                    startActivity(intent);
                }
            });
        } else {
            Intent pIntent = new Intent(this, ProfilActivity.class);
            pIntent.putExtra("email", prefs.getString(BreizhLibConstantes.ACCOUNT_NAME, null));
            startActivity(pIntent);
        }


    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}