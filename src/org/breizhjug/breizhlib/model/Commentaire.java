package org.breizhjug.breizhlib.model;


import fr.ybo.database.annotation.Column;
import fr.ybo.database.annotation.Entity;
import org.breizhjug.breizhlib.database.Database;

import java.io.Serializable;

@Entity
public class Commentaire implements Serializable, Model {

    @Column
    public String nom;

    @Column
    public String commentaire;

    @Column(type = Column.TypeColumn.INTEGER)
    public int note;

    @Column
    public String isbn;

    public Livre livre;

    public Commentaire() {

    }

    public Commentaire(String nom, String commentaire, int note, Livre livre) {
        this.nom = nom;
        this.commentaire = commentaire;
        this.note = note;
        this.livre = livre;
        this.isbn = livre.iSBN;
    }

    public void onLoad(Database db) {
        Livre entity = new Livre();
        entity.iSBN = isbn;
        this.livre = db.selectSingle(entity);
    }
}
