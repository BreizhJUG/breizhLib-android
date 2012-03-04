package org.breizhjug.breizhlib.model;

import java.util.ArrayList;

/**
 * User: Guernion Sylvain
 * Date: 03/03/12
 * Time: 20:06
 */
public class Converter {


    public ArrayList<Livre> toOuvrages(ArrayList<Reservation> items) {
        ArrayList<Livre> livres = new ArrayList<Livre>();

        for (Reservation item : items) {
            livres.add(item.livre);
        }

        return livres;
    }

    public ArrayList<Livre> empruntToOuvrages(ArrayList<Emprunt> items) {
        ArrayList<Livre> livres = new ArrayList<Livre>();

        for (Emprunt item : items) {
            livres.add(item.livre);
        }

        return livres;
    }
}
