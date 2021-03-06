package org.breizhjug.breizhlib.model;

import android.database.Cursor;
import android.util.Log;
import fr.ybo.database.annotation.Column;
import fr.ybo.database.annotation.Entity;
import fr.ybo.database.annotation.PrimaryKey;
import org.breizhjug.breizhlib.database.Database;

import java.io.Serializable;
import static org.breizhjug.breizhlib.model.Columns.*;

@Entity
public class Livre implements Serializable, Model {

    @Column
    public String titre;
    @Column
    public String editeur;
    @Column(type = Column.TypeColumn.INTEGER)
    public Integer note;
    @Column
    public String imgUrl;
    @Column
    @PrimaryKey
    public String iSBN;

    public boolean add;
    @Column
    public String etat;

    public int nbCommentaire;

    public Livre() {

    }

    public Livre(String titre, String iSBN, String editeur, String imgurl) {
        this.editeur = editeur;
        this.titre = titre;
        this.imgUrl = imgurl;
        this.iSBN = iSBN;
    }

    public Livre from(Cursor cursor) {
        note = cursor.getInt(cursor.getColumnIndex(NOTE));
        editeur = cursor.getString(cursor.getColumnIndex(EDITEUR));
        iSBN = cursor.getString(cursor.getColumnIndex("iSBN"));
        imgUrl = cursor.getString(cursor.getColumnIndex(IMG_URL));
        titre = cursor.getString(cursor.getColumnIndex(TITRE));
        return this;
    }

    public void onLoad(Database db) {
        Log.d("Livre", "isbn " + iSBN);
    }

}
