package org.breizhjug.breizhlib;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.breizhjug.breizhlib.database.Database;
import org.breizhjug.breizhlib.model.Commentaire;
import org.breizhjug.breizhlib.model.Livre;
import org.breizhjug.breizhlib.model.Reservation;
import org.breizhjug.breizhlib.remote.*;
import org.breizhjug.breizhlib.utils.GoogleAuthentification;
import org.breizhjug.breizhlib.utils.images.ImageCache;
import org.breizhjug.breizhlib.utils.version.VersionTask;


@ReportsCrashes(formKey = "dGFpb0t5YXF3a3J3Ui1GZjBVY1ROMGc6MQ",
        mode = ReportingInteractionMode.TOAST, resToastText = R.string.resToastText)
public class BreizhLib extends Application {

    private static ImageCache imageCache;

    private static CommentaireService commentaireService;

    private static OuvrageService ouvrageService;

    private static UtilisateurService utilisateurService;

    private static ReservationService reservationService;

    private static GoogleAuthentification gAuth;

    private static Database databaseHelper;

    private static SyncManager syncManager;

    private VersionTask checkVersion;


    @Override
    public void onCreate() {
        ACRA.init(this);
        super.onCreate();

        imageCache = new ImageCache("breizhlib");
        imageCache.init();
        databaseHelper = new Database(this);
        commentaireService = CommentaireService.getInstance();
        ouvrageService = OuvrageService.getInstance();

        reservationService = ReservationService.getInstance();
        utilisateurService = UtilisateurService.getInstance();

        gAuth = GoogleAuthentification.getInstance();
        checkVersion = new VersionTask(this);
        checkVersion.execute();

        syncManager = new SyncManager();
    }

    public static SyncManager getSyncManager() {
        return syncManager;
    }

    public static Database getDataBaseHelper() {
        return databaseHelper;
    }


    public static ImageCache getImageCache() {
        return imageCache;
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
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void clearCache() {
        commentaireService.clearCache();
        ouvrageService.clearCache();
        reservationService.clearCache();
    }

    public static void clearDB() {
        databaseHelper.deleteAll(Livre.class);
        databaseHelper.deleteAll(Commentaire.class);
        databaseHelper.deleteAll(Reservation.class);
    }


}
