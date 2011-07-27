package org.breizhjug.breizhlib.model;


public class Commentaire {

    private String nom;

    private String commentaire;

    private int note;

    private String livre;

    public Commentaire(String nom, String commentaire, int note, String livre) {
        this.nom = nom;
        this.commentaire = commentaire;
        this.note = note;
        this.livre = livre;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getLivre() {
        return livre;
    }

    public void setLivre(String livre) {
        this.livre = livre;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
}
