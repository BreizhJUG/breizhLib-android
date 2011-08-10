package org.breizhjug.breizhlib.view;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;
import org.breizhjug.breizhlib.BreizhLib;


public class CacheDialogPreference extends DialogPreference {


    public CacheDialogPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CacheDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onClick() {
        Log.d("CACHE","clear cache");
        BreizhLib.clearCache();

    }
}
