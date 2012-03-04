package org.breizhjug.breizhlib.activity;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.google.inject.Inject;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.activity.common.BaseActivity;
import org.breizhjug.breizhlib.remote.SyncManager;

import static org.breizhjug.breizhlib.utils.IntentSupport.newMenuIntent;

public class SplashScreen extends BaseActivity {

    private static final int STOPSPLASH = 0;
    private static final long SPLASHTIME = 1000;

    @Inject
    private SyncManager syncManager;

    private Handler splashHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STOPSPLASH:
                    startActivity(newMenuIntent(getApplicationContext()));
                    break;
            }
            finish();
            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        Message msg = new Message();
        msg.what = STOPSPLASH;
        splashHandler.sendMessageDelayed(msg, SPLASHTIME);

        // syncManager.init(this.getApplicationContext());
        // syncManager.run();
    }
}
