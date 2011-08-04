package org.breizhjug.breizhlib.model;


import java.io.Serializable;

public class Reservation implements Serializable {

    public String nom;

    public String prenom;
    public Livre livre;

    public Reservation(String nom, String prenom, String imgUrl, String iSBN, String titre) {
        this.nom = nom;
        this.prenom = prenom;
        this.livre = new Livre(titre, iSBN,"", imgUrl);
        this.livre.etat = "RESERVE";
    }

    public Reservation(String nom, String prenom, Livre livre) {
        this.nom = nom;
        this.prenom = prenom;
        this.livre = livre;
        if(livre !=null){
            this.livre.etat = "RESERVE";
        }
    }
}
