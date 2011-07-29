package org.breizhjug.breizhlib.activity;

import android.content.Intent;
import org.breizhjug.breizhlib.model.Livre;
import org.breizhjug.breizhlib.model.Reservation;


public class Populator {

    public static void populate(Intent intent, Livre livre) {
        intent.putExtra("titre", livre.getTitre());
        intent.putExtra("editeur", livre.getEditeur());
        intent.putExtra("img", livre.getImgUrl());
        intent.putExtra("isbn", livre.getiSBN());
        intent.putExtra("add", livre.add);
        intent.putExtra("etat", livre.etat);
    }

    public static void populate(Intent intent, Reservation reservation) {
        intent.putExtra("titre", reservation.getLivre());
        intent.putExtra("img", reservation.getImgUrl());
        intent.putExtra("etat", "RESERVE");
    }
}
