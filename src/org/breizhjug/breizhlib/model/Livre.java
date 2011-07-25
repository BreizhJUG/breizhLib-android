package org.breizhjug.breizhlib.model;

public class Livre {

    private String titre;
    private String editeur;
    private String imgUrl;
    private String iSBN;

    public Livre(String titre, String iSBN, String editeur, String imgurl) {
        this.editeur = editeur;
        this.titre = titre;
        this.imgUrl = imgurl;
        this.iSBN = iSBN;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getEditeur() {
        return editeur;
    }

    public void setEditeur(String editeur) {
        this.editeur = editeur;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getiSBN() {
        return iSBN;
    }

    public void setiSBN(String iSBN) {
        this.iSBN = iSBN;
    }
}
