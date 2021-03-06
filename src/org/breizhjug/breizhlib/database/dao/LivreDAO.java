package org.breizhjug.breizhlib.database.dao;


import org.breizhjug.breizhlib.model.Livre;

import java.util.ArrayList;

public class LivreDAO extends AbstractDao {

    private static final String TAG = "BreizhLib.LivreDAO";

    public ArrayList<Livre> findByReservation() {
        closeCursor();
        cursor = db.executeSelectQuery("SELECT Livre.* FROM Livre  WHERE Livre.etat = 'RESERVE' ", null);
        try {
            ArrayList<Livre> livres = new ArrayList<Livre>();
            if (cursor != null && cursor.getCount() > 0 ) {
                    cursor.moveToFirst();
                    do {
                        Livre livre = new Livre().from(cursor);
                        livre.onLoad(db);
                        livres.add(livre);
                    } while (cursor.moveToNext());
                closeCursor();
            }
            return livres;
        } finally {
            closeCursor();
        }
    }

    public void save(Livre livre){
        db.beginTransaction();
        db.update(livre);
        db.endTransaction();
    }
}
