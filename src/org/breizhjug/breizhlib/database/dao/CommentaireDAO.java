package org.breizhjug.breizhlib.database.dao;

import android.database.Cursor;
import com.google.inject.Inject;
import org.breizhjug.breizhlib.database.Database;
import org.breizhjug.breizhlib.model.Commentaire;

import java.util.ArrayList;
import java.util.List;


public class CommentaireDAO {

    private static final String TAG = "BreizhLib.CommentaireDAO";

    @Inject
    protected Database db;

    public ArrayList<Commentaire> findByIsbn(String isbn) {
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

    public ArrayList<Commentaire> findByAutor(String nom) {
        List<String> args = new ArrayList<String>();
        args.add(nom);
        Cursor cursor = db.executeSelectQuery("SELECT Commentaire.* FROM Commentaire  WHERE Commentaire.nom = :nom", args);

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
