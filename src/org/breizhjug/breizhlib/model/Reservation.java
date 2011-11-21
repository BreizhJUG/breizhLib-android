package org.breizhjug.breizhlib.model;


import android.database.Cursor;
import fr.ybo.database.annotation.Column;
import fr.ybo.database.annotation.Entity;
import org.breizhjug.breizhlib.database.Database;

import java.io.Serializable;

@Entity
public class Reservation implements Serializable, Model {

    @Column
    public String nom;
    @Column
    public String prenom;
    @Column
    public String isbn;

    public String email;


    public Livre livre;

    public Reservation() {

    }

    public Reservation(String nom, String prenom, String imgUrl, String iSBN, String titre) {
        this.nom = nom;
        this.prenom = prenom;
        this.livre = new Livre(titre, iSBN, "", imgUrl);
        this.livre.etat = "RESERVE";
        this.isbn = livre.iSBN;
    }

    public Reservation(String nom, String prenom, Livre livre) {
        this.nom = nom;
        this.prenom = prenom;
        this.livre = livre;
        if (livre != null) {
            this.livre.etat = "RESERVE";
            this.isbn = livre.iSBN;
        }
    }

    public Reservation(Cursor cursor) {
        nom = cursor.getString(cursor.getColumnIndex("nom"));
        isbn = cursor.getString(cursor.getColumnIndex("isbn"));
        prenom = cursor.getString(cursor.getColumnIndex("prenom"));
    }

    public void onLoad(Database db) {
        Livre entity = new Livre();
        entity.iSBN = isbn;
        livre = db.selectSingle(entity);
    }
}
