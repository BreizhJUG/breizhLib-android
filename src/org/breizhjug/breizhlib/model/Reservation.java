package org.breizhjug.breizhlib.model;


import java.io.Serializable;

public class Reservation implements Serializable {

    public String nom;

    public String prenom;

    public String livre;

    public String iSBN;

    public String imgUrl;

    public Reservation(String nom, String prenom, String imgUrl, String iSBN, String livre) {
        this.nom = nom;
        this.imgUrl = imgUrl;
        this.iSBN = iSBN;
        this.livre = livre;
        this.prenom = prenom;
    }
}
