package org.breizhjug.breizhlib.view;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import com.google.inject.Inject;
import org.breizhjug.breizhlib.utils.CacheManager;
import org.breizhjug.breizhlib.utils.images.ImageCache;


public class CacheDialogPreference extends DialogPreference {
    private static final String TAG = "Breizhlib.CacheDialogPreference";

    @Inject
    private static ImageCache imageCache;
    @Inject
    private static CacheManager cacheManager;


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
            cacheManager.clearCache();
        } else if (getKey().equals("cacheImg")) {
            Log.d(TAG, "clear cache img");
            imageCache.clearCache();
        } else if (getKey().equals("cacheDb")) {
            Log.d(TAG, "clear cache db");
            cacheManager.clearDB();
        }

    }
}
