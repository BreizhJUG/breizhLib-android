package org.breizhjug.breizhlib.database.dao;


import org.breizhjug.breizhlib.model.Livre;

import java.util.ArrayList;

public class LivreDAO extends AbstractDao{

    private static final String TAG = "BreizhLib.LivreDAO";

    public ArrayList<Livre> findByReservation() {
        closeCursor();
        cursor = db.executeSelectQuery("SELECT Livre.* FROM Livre  WHERE Livre.etat = 'RESERVE' ", null);
        try {
            final ArrayList<Livre> livres = new ArrayList<Livre>();
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    do {
                        Livre livre = new Livre(cursor);
                        livre.onLoad(db);
                        livres.add(livre);
                    } while (cursor.moveToNext());
                }
                closeCursor();
            }
            return livres;
        } finally {
            closeCursor();
        }
    }
}
