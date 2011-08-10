package org.breizhjug.breizhlib.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import org.breizhjug.breizhlib.R;


public class ConfigurationActivity extends PreferenceActivity{


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.configuration);
    }
}