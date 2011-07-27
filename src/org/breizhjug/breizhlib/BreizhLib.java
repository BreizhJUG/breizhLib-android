package org.breizhjug.breizhlib;

import org.breizhjug.breizhlib.remote.CommentaireService;
import org.breizhjug.breizhlib.remote.OuvrageService;
import org.breizhjug.breizhlib.remote.ReservationService;
import org.breizhjug.breizhlib.utils.ImageDownloader;

public class BreizhLib {

    private static final ImageDownloader imageDownloader = new ImageDownloader();

    private static final CommentaireService commentaireService = new CommentaireService();

    private static final OuvrageService ouvrageService = new OuvrageService();

    private static final ReservationService reservationService = new ReservationService();


    public static ImageDownloader getImageDownloader() {
        return imageDownloader;
    }

    public static CommentaireService getCommentaireService() {
        return commentaireService;
    }

    public static ReservationService getReservationService() {
        return reservationService;
    }

    public static OuvrageService getOuvrageService() {
        return ouvrageService;
    }
}
