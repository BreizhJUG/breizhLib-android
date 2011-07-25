package org.breizhjug.breizhlib.model;


public class Reservation {

   private String nom;

   private String prenom;

   private String livre;

   private String iSBN;

   private String imgUrl;

    public Reservation(String nom, String prenom, String imgUrl, String iSBN, String livre) {
        this.nom = nom;
        this.imgUrl = imgUrl;
        this.iSBN = iSBN;
        this.livre = livre;
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getiSBN() {
        return iSBN;
    }

    public void setiSBN(String iSBN) {
        this.iSBN = iSBN;
    }

    public String getLivre() {
        return livre;
    }

    public void setLivre(String livre) {
        this.livre = livre;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
