package org.breizhjug.breizhlib.database.dao;

import org.breizhjug.breizhlib.model.Commentaire;

import java.util.ArrayList;
import java.util.List;


public class CommentaireDAO extends AbstractDao{

    private static final String TAG = "BreizhLib.CommentaireDAO";

    public ArrayList<Commentaire> findByIsbn(String isbn) {
        closeCursor();
        List<String> args = new ArrayList<String>();
        args.add(isbn);
        cursor = db.executeSelectQuery("SELECT Commentaire.* FROM Commentaire  WHERE Commentaire.isbn = :isbn", args);
        try {
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
                closeCursor();
            }
            return commentaires;
        } finally {
            closeCursor();
        }
    }

    public ArrayList<Commentaire> findByAutor(String nom) {
        closeCursor();
        List<String> args = new ArrayList<String>();
        args.add(nom);
        cursor = db.executeSelectQuery("SELECT Commentaire.* FROM Commentaire  WHERE Commentaire.nom = :nom", args);
        try {
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
                closeCursor();
            }
            return commentaires;
        } finally {
            if (cursor != null) {
               closeCursor();
            }
        }
    }
}
