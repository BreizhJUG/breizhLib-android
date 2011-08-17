package org.breizhjug.breizhlib.view;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import org.breizhjug.breizhlib.BreizhLib;


public class CacheDialogPreference extends DialogPreference {
    private static final String TAG = "Breizhlib.CacheDialogPreference";


    public CacheDialogPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CacheDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onClick() {

        if (getKey().equals("cache")) {
            Log.d(TAG, "clear cache");
            BreizhLib.clearCache();
        } else if (getKey().equals("cacheImg")) {
            Log.d(TAG, "clear cache img");
            BreizhLib.getImageCache().clearCache();
        } else if (getKey().equals("cacheDb")) {
            Log.d(TAG, "clear cache db");
            BreizhLib.clearDB();
        }

    }
}
