package org.breizhjug.breizhlib.database.dao;

import org.breizhjug.breizhlib.model.Emprunt;
import org.breizhjug.breizhlib.model.Reservation;

import java.util.ArrayList;
import java.util.List;


public class EmpruntDAO extends AbstractDao {

    private static final String TAG = "BreizhLib.EmpruntDAO";


    public ArrayList<Emprunt> findByNom(String nom, String prenom) {
        closeCursor();
        List<String> args = new ArrayList<String>();
        args.add(nom);
        args.add(prenom);
        cursor = db.executeSelectQuery("SELECT Emprunt.* FROM Emprunt  WHERE Emprunt.nom = :nom AND Emprunt.prenom = :prenom", args);
        try {
            final ArrayList<Emprunt> items = new ArrayList<Emprunt>();
            if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    do {
                        Emprunt emprunt = new Emprunt().from(cursor);
                        emprunt.onLoad(db);
                        items.add(emprunt);
                    } while (cursor.moveToNext());
                closeCursor();
            }
            return items;
        } finally {
            closeCursor();
        }
    }
}
