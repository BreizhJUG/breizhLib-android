package org.breizhjug.breizhlib.activity.compte;

import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.google.inject.Inject;
import com.google.inject.internal.Nullable;
import greendroid.widget.ActionBarItem;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.activity.common.AbstractGDActivity;
import org.breizhjug.breizhlib.adapter.AccountsAdapter;
import org.breizhjug.breizhlib.utils.authentification.Authentification;
import org.breizhjug.breizhlib.utils.images.ImageCache;
import roboguice.inject.InjectView;

import java.util.List;

import static org.breizhjug.breizhlib.IntentConstantes.ACCOUNT;
import static org.breizhjug.breizhlib.IntentConstantes.EMAIL;


public class CompteList extends AbstractGDActivity {

    @InjectView(R.id.items)
    @Nullable
    private ListView listView;
    private String authcookie;
    @Inject
    private SharedPreferences prefs;
    @Inject
    private ImageCache imageCache;
    @Inject
    private Authentification gAuth;

    @Override
    public void init(Intent intent) {
        authcookie = prefs.getString(BreizhLibConstantes.AUTH_COOKIE, null);
        if (authcookie == null) {
            List<String> accounts = gAuth.getAccounts(this);
            if (accounts.size() == 0) {
                startAccountActivity();
            } else {
                initView(accounts);
            }
        } else {
            startProfilActivity();
        }
    }

    private void startAccountActivity() {
        Intent intentAccount = gAuth.getAuthentificationIntent(getApplicationContext());
        startActivity(intentAccount);
        finish();
    }

    private void startProfilActivity() {
        Intent pIntent = new Intent(this, ProfilActivity.class);
        pIntent.putExtra(EMAIL, prefs.getString(BreizhLibConstantes.ACCOUNT_NAME, null));
        startActivity(pIntent);
        finish();
    }

    private void initView(List<String> accounts) {
        getActionBar().addItem(ActionBarItem.Type.Add, R.id.action_bar_add_account);
        getActionBar().setTitle(getText(R.string.compte_title));
        setActionBarContentView(R.layout.items);

        listView.setAdapter(new AccountsAdapter(this.getBaseContext(), accounts));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                String account = (String) listView.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), AppInfo.class);
                intent.putExtra(ACCOUNT, account);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
        switch (item.getItemId()) {
            case R.id.action_bar_add_account:
                startActivity(new Intent(Settings.ACTION_ADD_ACCOUNT));
                finish();
                return true;
            default:

                return super.onHandleActionBarItemClick(item, position);
        }
    }
}