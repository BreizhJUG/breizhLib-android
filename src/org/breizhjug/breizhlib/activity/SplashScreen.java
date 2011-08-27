package org.breizhjug.breizhlib.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.google.inject.Inject;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.remote.SyncManager;

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
                    //remove SplashScreen from view
                    Intent intent = new Intent(SplashScreen.this, Menu.class);
                    startActivity(intent);

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
