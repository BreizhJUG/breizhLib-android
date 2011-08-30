package org.breizhjug.breizhlib.database.dao;

import android.database.Cursor;
import com.google.inject.Inject;
import org.breizhjug.breizhlib.database.Database;
import org.breizhjug.breizhlib.model.Reservation;

import java.util.ArrayList;
import java.util.List;


public class ReservationDAO {

    private static final String TAG = "BreizhLib.ReservationDAODAO";

    @Inject
    protected Database db;


    public ArrayList<Reservation> findByNom(String nom, String prenom) {
        List<String> args = new ArrayList<String>();
        args.add(nom);
        args.add(prenom);
        Cursor cursor = db.executeSelectQuery("SELECT Reservation.* FROM Reservation  WHERE Reservation.nom = :nom AND Reservation.prenom = :prenom", args);
        try {
            final ArrayList<Reservation> items = new ArrayList<Reservation>();
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    do {
                        Reservation resa = new Reservation(cursor);
                        resa.onLoad(db);
                        items.add(resa);
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
            return items;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
