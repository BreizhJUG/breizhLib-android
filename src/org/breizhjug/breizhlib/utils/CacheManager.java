package org.breizhjug.breizhlib.utils;


import com.google.inject.Inject;
import org.breizhjug.breizhlib.database.Database;
import org.breizhjug.breizhlib.model.Commentaire;
import org.breizhjug.breizhlib.model.Livre;
import org.breizhjug.breizhlib.model.Reservation;
import org.breizhjug.breizhlib.remote.CommentaireService;
import org.breizhjug.breizhlib.remote.OuvrageService;
import org.breizhjug.breizhlib.remote.ReservationService;

public class CacheManager {

    @Inject
    private CommentaireService commentaireService;
    @Inject
    private OuvrageService ouvrageService;
    @Inject
    private ReservationService reservationService;
    @Inject
    private Database databaseHelper;

    public void clearCache() {
        commentaireService.clearCache();
        ouvrageService.clearCache();
        reservationService.clearCache();
    }

    public void clearDB() {
        databaseHelper.deleteAll(Livre.class);
        databaseHelper.deleteAll(Commentaire.class);
        databaseHelper.deleteAll(Reservation.class);
    }

}
