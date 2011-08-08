package org.breizhjug.breizhlib.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.R;


public class ConfigurationActivity extends Activity {

    private CheckBox checkGrid;
    private CheckBox checkImg;

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
    }

    public void onSave() {
        SharedPreferences.Editor editor = BreizhLib.getSharedPreferences(this).edit();
        editor.putBoolean(BreizhLibConstantes.GRID, checkGrid.isChecked());
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