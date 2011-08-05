package org.breizhjug.breizhlib.model;


import java.io.Serializable;

public class Commentaire implements Serializable {

    public String nom;

    public String commentaire;

    public int note;

    public Livre livre;

    public Commentaire(String nom, String commentaire, int note, Livre livre) {
        this.nom = nom;
        this.commentaire = commentaire;
        this.note = note;
        this.livre = livre;
    }
}
