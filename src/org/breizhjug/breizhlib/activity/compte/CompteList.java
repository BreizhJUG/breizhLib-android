package org.breizhjug.breizhlib.activity.compte;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import org.breizhjub.breizhlib.R;
import org.breizhjug.breizhlib.adapter.AccountsAdapter;


public class CompteList extends Activity {

    private ListView listView;

    protected AccountManager accountManager;
    protected Intent intent;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items);

        listView = (ListView) findViewById(R.id.items);
        accountManager = AccountManager.get(getApplicationContext());
        Account[] accounts = accountManager.getAccountsByType("com.google");
        listView.setAdapter(new AccountsAdapter(this.getBaseContext(), accounts));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressWarnings("unchecked")
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                Account account = (Account) listView.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), AppInfo.class);
                intent.putExtra("account", account);
                startActivity(intent);
            }
        });

    }
}