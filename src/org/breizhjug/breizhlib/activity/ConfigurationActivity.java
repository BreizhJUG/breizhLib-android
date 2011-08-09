package org.breizhjug.breizhlib.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.remote.SyncManager;

import java.util.ArrayList;
import java.util.List;


public class ConfigurationActivity extends Activity {

    private CheckBox checkGrid;
    private CheckBox checkImg;
    private Spinner frequence;
    private long period;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuration);

        SharedPreferences prefs = BreizhLib.getSharedPreferences(this);

        checkGrid = (CheckBox) findViewById(R.id.checkGrid);
        checkGrid.setChecked(prefs.getBoolean(BreizhLibConstantes.GRID, false));

        Button button = (Button) findViewById(R.id.buttonValid);
        button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View paramView) {
                onSave();
            }
        });

        Button cache = (Button) findViewById(R.id.buttonCache);
        cache.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View paramView) {
                onClearCache();
            }
        });

        frequence = (Spinner) findViewById(R.id.frequence);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        List<Option> options = new ArrayList<Option>();
        options.add(new Option("15 minutes", AlarmManager.INTERVAL_FIFTEEN_MINUTES));
        options.add(new Option("30 minutes", AlarmManager.INTERVAL_HALF_HOUR));
        options.add(new Option("1 heure", AlarmManager.INTERVAL_HOUR));
        options.add(new Option("1 jour", AlarmManager.INTERVAL_DAY));

        period = prefs.getLong(SyncManager.OUVRAGE_T_PERIOD,0l);

        int selectedIndex = 0;
        for(Option option :options){
            adapter.add(option);
            if(period == option.valeur){
              selectedIndex= adapter.getPosition(option);
            }
        }

        frequence.setAdapter(adapter);

        frequence.setSelection(selectedIndex);

    }


    private class Option {
        public String label;
        public long valeur;

        private Option(String label, long valeur) {
            this.label = label;
            this.valeur = valeur;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    public void onSave() {
        SharedPreferences.Editor editor = BreizhLib.getSharedPreferences(this).edit();
        editor.putBoolean(BreizhLibConstantes.GRID, checkGrid.isChecked());

         Option option = (Option)   frequence.getSelectedItem();
         if(period != option.valeur) {
            editor.putLong(SyncManager.OUVRAGE_T_PERIOD, option.valeur);
            BreizhLib.getSyncManager().reschedule(SyncManager.OUVRAGE_T,option.valeur);
         }
        editor.commit();
        setResult(RESULT_OK);
        finish();
    }

    public void onClearCache() {
        BreizhLib.clearCache();
        setResult(RESULT_OK);
        finish();
    }
}