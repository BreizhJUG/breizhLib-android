package org.breizhjug.breizhlib.activity;

import android.content.Intent;
import org.breizhjug.breizhlib.model.Livre;
import org.breizhjug.breizhlib.model.Reservation;


public class Populator {

    public static void populate(Intent intent, Livre livre) {
        intent.putExtra("livre", livre);
        intent.putExtra("isbn", livre.iSBN);
    }

    public static void populate(Intent intent, Reservation reservation) {
        intent.putExtra("titre", reservation.livre);
        intent.putExtra("img", reservation.imgUrl);
        intent.putExtra("etat", "RESERVE");
    }
}
