package org.breizhjug.breizhlib.remote;


import android.app.AlarmManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.BreizhLibConstantes;

import java.util.Timer;
import java.util.TimerTask;

public class SyncManager implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "Breizhlib.SyncManager";
    private final int TIMER_DELAY = 1000;

    public static final String OUVRAGE_T = "ouvrages";

    public static final String OUVRAGE_T_PERIOD = "OUVRAGE_T_PERIOD";

    public static final String COMMENTAIRES_T = "commentaires";

    public static final String COMMENTAIRES_T_PERIOD = "COMMENTAIRES_T_PERIOD";

    Timer ouvragesTimer;

    Timer commentairesTimer;

    SharedPreferences prefs;

    public void init(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.registerOnSharedPreferenceChangeListener(this);
        SharedPreferences.Editor editor = prefs.edit();

        if (Long.valueOf(prefs.getString(OUVRAGE_T_PERIOD, "0")) == 0l) {
            editor.putString(OUVRAGE_T_PERIOD, "" + AlarmManager.INTERVAL_FIFTEEN_MINUTES);
        }

         if (Long.valueOf(prefs.getString(COMMENTAIRES_T_PERIOD, "0")) == 0l) {
            editor.putString(COMMENTAIRES_T_PERIOD, "" + AlarmManager.INTERVAL_FIFTEEN_MINUTES);
        }

        editor.commit();

    }

    public void run() {
        reschedule(OUVRAGE_T, Long.valueOf(prefs.getString(OUVRAGE_T_PERIOD, "0")));
        reschedule(COMMENTAIRES_T, Long.valueOf(prefs.getString(COMMENTAIRES_T_PERIOD, "0")));
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(OUVRAGE_T_PERIOD)) {
            reschedule(OUVRAGE_T, Long.valueOf(prefs.getString(OUVRAGE_T_PERIOD, "0")));
        } else if (s.equals(COMMENTAIRES_T_PERIOD))    {
            reschedule(COMMENTAIRES_T, Long.valueOf(prefs.getString(COMMENTAIRES_T_PERIOD, "0")));
        }
    }

    private class OuvragesTask extends TimerTask {
        @Override
        public void run() {
            Log.d(TAG, "run");
            BreizhLib.getOuvrageService().forceCall = true;
            BreizhLib.getOuvrageService().load(prefs.getString(BreizhLibConstantes.AUTH_COOKIE, null));
        }
    }

     private class CommentairesTask extends TimerTask {
        @Override
        public void run() {
            Log.d(TAG, "run");
            BreizhLib.getCommentaireService().forceCall = true;
            BreizhLib.getCommentaireService().load(prefs.getString(BreizhLibConstantes.AUTH_COOKIE, null));
        }
    }

    public void reschedule(String timerName, long periode) {
        if (timerName.equals(OUVRAGE_T)) {
            if (ouvragesTimer != null) {
                ouvragesTimer.cancel();
            }
            TimerTask syncOuvrages = new OuvragesTask();
            ouvragesTimer = new Timer(OUVRAGE_T);
            ouvragesTimer.schedule(syncOuvrages, TIMER_DELAY, periode);
        } else if (timerName.equals(COMMENTAIRES_T)) {
            if (commentairesTimer != null) {
                commentairesTimer.cancel();
            }
            TimerTask syncCommentaires = new CommentairesTask();
            commentairesTimer = new Timer(COMMENTAIRES_T);
            commentairesTimer.schedule(syncCommentaires, TIMER_DELAY, periode);
        }
    }
}
