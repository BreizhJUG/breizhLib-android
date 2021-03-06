package org.breizhjug.breizhlib.database.dao;

import org.breizhjug.breizhlib.model.Reservation;

import java.util.ArrayList;
import java.util.List;


public class ReservationDAO extends AbstractDao {

    private static final String TAG = "BreizhLib.ReservationDAODAO";


    public ArrayList<Reservation> findByNom(String nom) {
        closeCursor();
        List<String> args = new ArrayList<String>();
        args.add(nom);
        cursor = db.executeSelectQuery("SELECT Reservation.* FROM Reservation  WHERE Reservation.nom = :nom", args);
        try {
            ArrayList<Reservation> items = new ArrayList<Reservation>();
            if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    do {
                        Reservation resa = new Reservation().from(cursor);
                        resa.onLoad(db);
                        items.add(resa);
                    } while (cursor.moveToNext());
                closeCursor();
            }
            return items;
        } finally {
            closeCursor();
        }
    }
}
