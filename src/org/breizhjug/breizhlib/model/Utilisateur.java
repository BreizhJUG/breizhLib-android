package org.breizhjug.breizhlib.model;


import org.breizhjug.breizhlib.database.Database;

import java.io.Serializable;

public class Utilisateur implements Serializable, Model{

    public String nom;
    public String prenom;
    public String email;
    public String username;

    public boolean isAdmin;

    public String commentairesLabel;
    public String ouvragesLlabel;
    public String ouvragesEncoursLabel;
    public String reservationsLabel;

    public void onLoad(Database db) {

    }
}
