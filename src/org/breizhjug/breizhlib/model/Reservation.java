package org.breizhjug.breizhlib.model;


import android.database.Cursor;
import fr.ybo.database.annotation.Column;
import fr.ybo.database.annotation.Entity;
import org.breizhjug.breizhlib.database.Database;
import static org.breizhjug.breizhlib.model.Columns.*;
import java.io.Serializable;

@Entity
public class Reservation implements Serializable, Model {

    @Column
    public String nom;
    @Column
    public String isbn;

    public String email;


    public Livre livre;

    public Reservation() {

    }

    public Reservation(String nom,String imgUrl, String iSBN, String titre) {
        this.nom = nom;
        this.livre = new Livre(titre, iSBN, "", imgUrl);
        this.livre.etat = "RESERVE";
        this.isbn = livre.iSBN;
    }

    public Reservation(String nom,  Livre livre) {
        this.nom = nom;
        this.livre = livre;
        if (livre != null) {
            this.livre.etat = "RESERVE";
            this.isbn = livre.iSBN;
        }
    }

    public Reservation from(Cursor cursor) {
        nom = cursor.getString(cursor.getColumnIndex(Columns.NOM));
        isbn = cursor.getString(cursor.getColumnIndex(ISBN));
        return this;
    }

    public void onLoad(Database db) {
        Livre entity = new Livre();
        entity.iSBN = isbn;
        livre = db.selectSingle(entity);
    }
}
