package org.breizhjug.breizhlib.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import org.breizhjub.breizhlib.R;

public class SplashScreen extends Activity {

    private static final int STOPSPLASH = 0;
    private static final long SPLASHTIME = 5000;


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
    }
}
