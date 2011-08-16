package org.breizhjug.breizhlib.database.dao;


import android.database.Cursor;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.database.Database;
import org.breizhjug.breizhlib.model.Livre;

import java.util.ArrayList;

public class LivreDAO {

    protected static Database db = BreizhLib.getDataBaseHelper();
    private static final String TAG = "BreizhLib.LivreDAO";


    public static ArrayList<Livre> findByReservation() {

        Cursor cursor = db.executeSelectQuery("SELECT Livre.* FROM Livre  WHERE Livre.etat = 'RESERVE' ",null);

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
            cursor.close();
        }
        return livres;
    }
}
