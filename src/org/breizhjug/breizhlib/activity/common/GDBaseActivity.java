package org.breizhjug.breizhlib.activity.common;

import android.app.AlertDialog;
import android.content.*;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import com.google.inject.Inject;
import com.google.inject.Injector;
import greendroid.app.GDActivity;
import greendroid.app.GDApplication;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.activity.ConfigurationActivity;
import org.breizhjug.breizhlib.activity.Menu;
import org.breizhjug.breizhlib.activity.compte.CompteList;
import org.breizhjug.breizhlib.database.Database;
import org.breizhjug.breizhlib.utils.IntentSupport;
import org.breizhjug.breizhlib.utils.Tracker;
import roboguice.activity.event.*;
import roboguice.application.RoboApplication;
import roboguice.event.EventManager;
import roboguice.inject.ContextScope;
import roboguice.inject.InjectorProvider;
import static org.breizhjug.breizhlib.BreizhLibConstantes.*;


public class GDBaseActivity extends GDActivity implements InjectorProvider {

    public static final String ACTION_LOGOUT = "org.breizhjug.breizhlib.LOGOUT";

    protected BroadcastReceiver receiver;
    @Inject
    protected SharedPreferences prefs;
    @Inject
    private Tracker tracker;
    @Inject
    protected Database db;


    public void beforeCreate() {
        final Injector injector = getInjector();
        eventManager = injector.getInstance(EventManager.class);
        scope = injector.getInstance(ContextScope.class);
        scope.enter(this);
        injector.injectMembers(this);
    }

    public void onCreate(Bundle savedInstanceState) {
        beforeCreate();
        super.onCreate(savedInstanceState);
        eventManager.fire(new OnCreateEvent(savedInstanceState));


        tracker.trackPageView('/' + getClass().getSimpleName());


        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_LOGOUT);
        registerReceiver(receiver, intentFilter);
    }

    protected void showError(String message) {
        showError(message, false);
    }

    protected void showError(String message, final boolean finish) {
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setMessage(message);
        build.setPositiveButton(getString(R.string.ok),
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (finish)
                            finish();
                    }

                });
        build.create().show();
    }

    protected void showError(int resId, final boolean finish) {
        showError(getString(resId), finish);
    }


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        String authCookie = prefs.getString(BreizhLibConstantes.AUTH_COOKIE, null);
        if (authCookie == null) {
            MenuItem item = (MenuItem) menu.findItem(R.id.connexion);
            String message = getString(R.string.connexion);
            item.setTitle(message);
        } else {
            MenuItem item = (MenuItem) menu.findItem(R.id.connexion);
            String message = getString(R.string.deconnexion);
            item.setTitle(message);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), Menu.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        switch (item.getItemId()) {
            case R.id.connexion:
                String authCookie = prefs.getString(BreizhLibConstantes.AUTH_COOKIE, null);
                if (authCookie == null) {
                    Intent pIntent = new Intent(getApplicationContext(), CompteList.class);
                    pIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(pIntent);
                } else {
                    onLogout();
                    startActivity(intent);
                }
                return true;
            case R.id.quitter:
                onExit();
                return true;
            case R.id.share:
                Intent pIntent = IntentSupport.newShareIntent(this, getString(R.string.app_name), getString(R.string.shareText), getString(R.string.app_name));
                startActivity(pIntent);
                return true;
            case R.id.parametre:
                pIntent = new Intent(getApplicationContext(), ConfigurationActivity.class);
                pIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(pIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onLogout() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(AUTH_COOKIE, null);
        editor.putString(ACCOUNT_NAME, null);
        editor.putString(USER, null);
        editor.putString(USER_NOM, null);
        editor.putString(USER_PRENOM, null);
        editor.putString(USER_ADMIN, null);
        editor.commit();
    }

    private void onExit() {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(ACTION_LOGOUT);
        sendBroadcast(broadcastIntent);
    }


    @Override
    protected void onDestroy() {
        scope.enter(this);
        db.close();

        try {
            eventManager.fire(new OnDestroyEvent());
        } finally {
            eventManager.clear(this);
            scope.exit(this);
            scope.dispose(this);
            unregisterReceiver(receiver);
            super.onDestroy();
        }
    }


    public GDApplication getGDApplication() {
        if (getApplication() instanceof GDApplication) {
            return (GDApplication) getApplication();
        } else {
            return ((BreizhLib) getApplication()).gdapp;
        }

    }


    protected EventManager eventManager;
    protected ContextScope scope;

    @Override
    public void setActionBarContentView(int resID) {
        beforeCreate();
        super.setActionBarContentView(resID);
        scope.injectViews();
        eventManager.fire(new OnContentViewAvailableEvent());
    }

    @Override
    public void setContentView(int layoutResID) {
        beforeCreate();
        super.setContentView(layoutResID);
        //scope.injectViews();
        eventManager.fire(new OnContentViewAvailableEvent());
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        beforeCreate();
        super.setContentView(view, params);
        scope.injectViews();
        eventManager.fire(new OnContentViewAvailableEvent());
    }

    @Override
    public void setContentView(View view) {
        beforeCreate();
        super.setContentView(view);
        scope.injectViews();
        eventManager.fire(new OnContentViewAvailableEvent());
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        return this;
    }

    @Override
    protected void onRestart() {
        scope.enter(this);
        super.onRestart();
        eventManager.fire(new OnRestartEvent());
    }

    @Override
    protected void onStart() {
        scope.enter(this);
        super.onStart();
        eventManager.fire(new OnStartEvent());
    }

    @Override
    protected void onResume() {
        scope.enter(this);
        super.onResume();
        eventManager.fire(new OnResumeEvent());
    }

    @Override
    protected void onPause() {
        super.onPause();
        eventManager.fire(new OnPauseEvent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        scope.enter(this);
        eventManager.fire(new OnNewIntentEvent());
    }

    @Override
    protected void onStop() {
        scope.enter(this);
        try {
            eventManager.fire(new OnStopEvent());
        } finally {
            scope.exit(this);
            super.onStop();
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        final Configuration currentConfig = getResources().getConfiguration();
        super.onConfigurationChanged(newConfig);
        eventManager.fire(new OnConfigurationChangedEvent(currentConfig, newConfig));
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        eventManager.fire(new OnContentChangedEvent());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        scope.enter(this);
        try {
            eventManager.fire(new OnActivityResultEvent(requestCode, resultCode, data));
        } finally {
            scope.exit(this);
        }
    }


    @Override
    public Injector getInjector() {
        return ((RoboApplication) getApplication()).getInjector();
    }

}
