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
import org.breizhjug.breizhlib.remote.CommentaireService;
import org.breizhjug.breizhlib.remote.OuvrageService;
import org.breizhjug.breizhlib.remote.ReservationService;
import org.breizhjug.breizhlib.remote.UtilisateurService;
import org.breizhjug.breizhlib.utils.GoogleAuthentification;
import org.breizhjug.breizhlib.utils.ImageDownloader;
import org.breizhjug.breizhlib.utils.Version;

import java.util.Locale;


@ReportsCrashes(formKey = "0AhXuEwofgu06dDVQS1VxY19iU3RXNm81SkxVQ3BWNkE", mode = ReportingInteractionMode.TOAST )
public class BreizhLib extends Application {

    private static BreizhLib instance;

    private static ImageDownloader imageDownloader;

    private static CommentaireService commentaireService;

    private static OuvrageService ouvrageService;

    private static UtilisateurService utilisateurService;

    private static ReservationService reservationService;

    private static GoogleAuthentification gAuth;

    public static final String SERVER_URL = "http://0-1-2.breizh-lib.appspot.com/";

    /**
     * clés pour shared preferences.
     */
    private static final String SHARED_PREFS = "breizhLib".toUpperCase(Locale.FRANCE) + "_PREFS";
    public static final String ACCOUNT_NAME = "accountName";
    public static final String AUTH_COOKIE = "authCookie";
    public static final String USER = "user";
    public static final String USER_ADMIN = "userAdmin";
    public static final String USER_NOM = "user_nom";
    public static final String USER_PRENOM = "user_prenom";
    public static final String GRID = "ouvrages_grid";

    @Override
    public void onCreate() {
        ACRA.init(this);
        super.onCreate();

        imageDownloader = ImageDownloader.getInstance();

        commentaireService = CommentaireService.getInstance();
        ouvrageService = OuvrageService.getInstance();

        reservationService = ReservationService.getInstance();
        utilisateurService = UtilisateurService.getInstance();

        gAuth = GoogleAuthentification.getInstance();
        instance = new BreizhLib();
        checkVersion.execute();
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
        return context.getSharedPreferences(SHARED_PREFS, 0);
    }

    public static void clearCache() {
        imageDownloader.clearCache();
        commentaireService.clearCache();
        ouvrageService.clearCache();
        reservationService.clearCache();
    }


    private AsyncTask<Void, Void, String> checkVersion = new AsyncTask<Void, Void, String>() {

		@Override
		protected String doInBackground(Void... params) {
			return Version.getVersionMarket();
		}

		protected void onPostExecute(String result) {
			if (result != null && !result.equals(Version.getVersionCourante(BreizhLib.this))) {
				createNotification(result);
			}
		};
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
