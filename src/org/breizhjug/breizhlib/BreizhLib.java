package org.breizhjug.breizhlib;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.breizhjug.breizhlib.database.Database;
import org.breizhjug.breizhlib.remote.*;
import org.breizhjug.breizhlib.utils.GoogleAuthentification;
import org.breizhjug.breizhlib.utils.ImageDownloader;
import org.breizhjug.breizhlib.utils.Version;


@ReportsCrashes(formKey = "dGFpb0t5YXF3a3J3Ui1GZjBVY1ROMGc6MQ",
        mode = ReportingInteractionMode.TOAST, resToastText = R.string.resToastText)
public class BreizhLib extends Application {

    private static BreizhLib instance;

    private static ImageDownloader imageDownloader;

    private static CommentaireService commentaireService;

    private static OuvrageService ouvrageService;

    private static UtilisateurService utilisateurService;

    private static ReservationService reservationService;

    private static GoogleAuthentification gAuth;

    private static Database databaseHelper;

    private static SyncManager syncManager;


    @Override
    public void onCreate() {
        ACRA.init(this);
        super.onCreate();

        imageDownloader = ImageDownloader.getInstance();
        databaseHelper = new Database(this);
        commentaireService = CommentaireService.getInstance();
        ouvrageService = OuvrageService.getInstance();

        reservationService = ReservationService.getInstance();
        utilisateurService = UtilisateurService.getInstance();

        gAuth = GoogleAuthentification.getInstance();
        instance = new BreizhLib();
        checkVersion.execute();

        syncManager = new SyncManager();
        syncManager.init(this);
        syncManager.run();
    }

    public static SyncManager getSyncManager() {
        return syncManager;
    }

    public static Database getDataBaseHelper() {
        return databaseHelper;
    }


    public static ImageDownloader getImageDownloader() {
        return imageDownloader;
    }

    public static GoogleAuthentification getGAuth() {
        return gAuth;
    }

    public static CommentaireService getCommentaireService() {
        return commentaireService;
    }

    public static UtilisateurService getUtilisateurService() {
        return utilisateurService;
    }

    public static ReservationService getReservationService() {
        return reservationService;
    }

    public static OuvrageService getOuvrageService() {
        return ouvrageService;
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(BreizhLibConstantes.SHARED_PREFS, 0);
    }

    public static void clearCache() {
        imageDownloader.clearCache();
        commentaireService.clearCache();
        ouvrageService.clearCache();
        reservationService.clearCache();
    }


    private AsyncTask<Void, Void, Integer> checkVersion = new AsyncTask<Void, Void, Integer>() {

        @Override
        protected Integer doInBackground(Void... params) {
            return Version.getVersionCodeMarket();
        }

        protected void onPostExecute(Integer result) {
            if (result != null && result > Version.getVersionCourante(BreizhLib.this)) {
                createNotification(Version.getVersionMarket());
            }
        }
    };

    private final int NOTIFICATION_VERSION_ID = 1;

    private void createNotification(String nouvelleVersion) {
        int icon = R.drawable.icon;
        CharSequence tickerText = getString(R.string.nouvelleVersion);
        long when = System.currentTimeMillis();
        Context context = getApplicationContext();
        CharSequence contentTitle = getString(R.string.nouvelleVersion);
        CharSequence contentText = getString(R.string.versionDisponible, nouvelleVersion);

        Uri uri = Uri.parse("market://details?id=org.breizhjug.breizhlib");
        Intent notificationIntent = new Intent(Intent.ACTION_VIEW, uri);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        // the next two lines initialize the Notification, using the
        // configurations above
        Notification notification = new Notification(icon, tickerText, when);
        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_VERSION_ID, notification);
    }
}
