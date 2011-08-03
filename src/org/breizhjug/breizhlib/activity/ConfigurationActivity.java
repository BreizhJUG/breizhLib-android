package org.breizhjug.breizhlib.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.R;


public class ConfigurationActivity extends Activity {

    private CheckBox checkGrid;
    private CheckBox checkImg;
    private BreizhLib breizhLib;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuration);
        breizhLib = BreizhLib.getInstance();

        SharedPreferences prefs = breizhLib.getSharedPreferences(this);

        checkImg = (CheckBox) findViewById(R.id.checkImg);
        checkImg.setChecked(prefs.getBoolean(BreizhLib.LOAD_IMG, false));

        checkGrid = (CheckBox) findViewById(R.id.checkGrid);
        checkGrid.setChecked(prefs.getBoolean(BreizhLib.GRID, false));

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
        SharedPreferences.Editor editor = breizhLib.getSharedPreferences(this).edit();
        editor.putBoolean(BreizhLib.LOAD_IMG, checkImg.isChecked());
        editor.putBoolean(BreizhLib.GRID, checkGrid.isChecked());
        editor.commit();

        setResult(RESULT_OK);
        finish();
    }

    public void onClearCache() {
        breizhLib.clearCache();
        setResult(RESULT_OK);
        finish();
    }
}