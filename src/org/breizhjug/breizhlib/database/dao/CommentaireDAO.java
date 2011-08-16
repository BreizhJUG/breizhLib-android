package org.breizhjug.breizhlib.database.dao;

import android.database.Cursor;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.database.Database;
import org.breizhjug.breizhlib.model.Commentaire;

import java.util.ArrayList;
import java.util.List;


public class CommentaireDAO {

    protected static Database db = BreizhLib.getDataBaseHelper();
    private static final String TAG = "BreizhLib.CommentaireDAO";

    public static ArrayList<Commentaire> findByIsbn(String isbn) {
        List<String> args = new ArrayList<String>();
        args.add(isbn);

        Cursor cursor = db.executeSelectQuery("SELECT Commentaire.* FROM Commentaire  WHERE Commentaire.isbn = :isbn", args);

        final ArrayList<Commentaire> commentaires = new ArrayList<Commentaire>();
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    Commentaire commentaire = new Commentaire(cursor);
                    commentaire.onLoad(db);
                    commentaires.add(commentaire);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return commentaires;
    }
}
