package org.breizhjug.breizhlib.remote;


import android.app.AlarmManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.BreizhLibConstantes;

import java.util.Timer;
import java.util.TimerTask;

public class SyncManager {

    private final int TIMER_DELAY = 1000;

    public static final String OUVRAGE_T = "ouvrages";

    public static final String OUVRAGE_T_PERIOD = "OUVRAGE_T_PERIOD";

    Timer ouvragesTimer = new Timer(OUVRAGE_T);

    SharedPreferences prefs;

    public void init(Context context) {
        prefs = BreizhLib.getSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        if (prefs.getLong(OUVRAGE_T_PERIOD, 0l) == 0l) {
            editor.putLong(OUVRAGE_T_PERIOD, AlarmManager.INTERVAL_FIFTEEN_MINUTES);
        }

        editor.commit();

    }

    public void run() {
        ouvragesTimer.schedule(syncOuvrages, TIMER_DELAY, prefs.getLong(OUVRAGE_T_PERIOD, 0l));
    }


    private TimerTask syncOuvrages = new OuvragesTask();

    private class OuvragesTask extends TimerTask {
        @Override
        public void run() {
            Log.d("TIMEROUVRAGE", "run");
            BreizhLib.getOuvrageService().forceCall = true;
            BreizhLib.getOuvrageService().load(prefs.getString(BreizhLibConstantes.AUTH_COOKIE, null));
        }
    }

    public void reschedule(String timerName, long periode) {
        if (timerName.equals(OUVRAGE_T)) {
            ouvragesTimer.cancel();
            syncOuvrages = new OuvragesTask();
            ouvragesTimer = new Timer(OUVRAGE_T);
            ouvragesTimer.schedule(syncOuvrages, TIMER_DELAY, periode);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong(OUVRAGE_T_PERIOD, periode);
            editor.commit();
        }
    }
}
