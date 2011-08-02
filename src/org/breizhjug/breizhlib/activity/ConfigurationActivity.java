package org.breizhjug.breizhlib.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.remote.CommentaireService;
import org.breizhjug.breizhlib.remote.OuvrageService;
import org.breizhjug.breizhlib.remote.ReservationService;
import org.breizhjug.breizhlib.utils.ImageDownloader;


public class ConfigurationActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuration);

		final SharedPreferences prefs = BreizhLib.getInstance().getSharedPreferences(this);

		final CheckBox checkImg = (CheckBox) findViewById(R.id.checkImg);
		checkImg.setChecked(prefs.getBoolean(BreizhLib.LOAD_IMG, false));

        final CheckBox checkGrid = (CheckBox) findViewById(R.id.checkGrid);
		checkGrid.setChecked(prefs.getBoolean(BreizhLib.GRID, false));

        final Button button = (Button) findViewById(R.id.buttonValid);
		button.setOnClickListener(new Button.OnClickListener() {


			public void onClick(View paramView) {
				SharedPreferences.Editor editor = prefs.edit();
				editor.putBoolean(BreizhLib.LOAD_IMG, checkImg.isChecked());
                editor.putBoolean(BreizhLib.GRID, checkGrid.isChecked());
				editor.commit();

				setResult(RESULT_OK);
				finish();
			}
		});

        final Button cache = (Button) findViewById(R.id.buttonCache);
		cache.setOnClickListener(new Button.OnClickListener() {


			public void onClick(View paramView) {
                ImageDownloader.getInstance().clearCache();
                CommentaireService.getInstance().clearCache();
                OuvrageService.getInstance().clearCache();
                ReservationService.getInstance().clearCache();
				setResult(RESULT_OK);
				finish();
			}
		});
    }
}