package org.breizhjug.breizhlib;

import android.content.Context;
import android.content.SharedPreferences;
import org.breizhjug.breizhlib.remote.CommentaireService;
import org.breizhjug.breizhlib.remote.OuvrageService;
import org.breizhjug.breizhlib.remote.ReservationService;
import org.breizhjug.breizhlib.remote.UtilisateurService;
import org.breizhjug.breizhlib.utils.GoogleAuthentification;
import org.breizhjug.breizhlib.utils.ImageDownloader;

import java.util.Locale;

public class BreizhLib {

    private static BreizhLib instance;

    private ImageDownloader imageDownloader;

    private CommentaireService commentaireService;

    private OuvrageService ouvrageService;

    private UtilisateurService utilisateurService;

    private ReservationService reservationService;

    private GoogleAuthentification gAuth;

    public static final String SERVER_URL = "http://breizh-lib.appspot.com/";

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
    public static final String LOAD_IMG = "img_loading";
    public static final String GRID = "ouvrages_grid";


    private BreizhLib() {
        imageDownloader = ImageDownloader.getInstance();

        commentaireService = CommentaireService.getInstance();
        ouvrageService = OuvrageService.getInstance();

        reservationService = ReservationService.getInstance();
        utilisateurService = UtilisateurService.getInstance();

        gAuth = GoogleAuthentification.getInstance();
    }


    public ImageDownloader getImageDownloader() {
        return imageDownloader;
    }

    public GoogleAuthentification getGAuth() {
        return gAuth;
    }

    public CommentaireService getCommentaireService() {
        return commentaireService;
    }

    public UtilisateurService getUtilisateurService() {
        return utilisateurService;
    }

    public ReservationService getReservationService() {
        return reservationService;
    }

    public OuvrageService getOuvrageService() {
        return ouvrageService;
    }

    public static synchronized BreizhLib getInstance() {
        if (instance == null) {
            instance = new BreizhLib();
        }
        return instance;
    }

    public SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PREFS, 0);
    }

    public void clearCache() {
        imageDownloader.clearCache();
        commentaireService.clearCache();
        ouvrageService.clearCache();
        reservationService.clearCache();
    }
}
