package org.breizhjug.breizhlib.model;

import java.io.Serializable;

public class Livre implements Serializable {

    public String titre;
    public String editeur;
    public int note;
    public String imgUrl;
    public String iSBN;
    public boolean add;
    public String etat;

    public Livre(String titre, String iSBN, String editeur, String imgurl) {
        this.editeur = editeur;
        this.titre = titre;
        this.imgUrl = imgurl;
        this.iSBN = iSBN;
    }

}
