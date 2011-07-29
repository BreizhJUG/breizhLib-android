package org.breizhjug.breizhlib;

import android.content.Context;
import org.breizhjug.breizhlib.remote.CommentaireService;
import org.breizhjug.breizhlib.remote.OuvrageService;
import org.breizhjug.breizhlib.remote.ReservationService;
import org.breizhjug.breizhlib.utils.ImageDownloader;

public class BreizhLib {

    private ImageDownloader imageDownloader;

    private CommentaireService commentaireService;

    private OuvrageService ouvrageService;

    private ReservationService reservationService;

    private BreizhLib(Context context) {
        imageDownloader = ImageDownloader.getInstance(context);

        commentaireService = new CommentaireService(context);
        ouvrageService = new OuvrageService(context);

        reservationService = new ReservationService(context);
    }


    public ImageDownloader getImageDownloader() {
        return imageDownloader;
    }

    public CommentaireService getCommentaireService() {
        return commentaireService;
    }

    public ReservationService getReservationService() {
        return reservationService;
    }

    public OuvrageService getOuvrageService() {
        return ouvrageService;
    }


    private static BreizhLib instance;

    public static synchronized BreizhLib getInstance(Context context) {
        if (instance == null) {
            instance = new BreizhLib(context);
        }
        return instance;
    }
}
