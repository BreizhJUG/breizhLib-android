package org.breizhjug.breizhlib.utils;


import android.content.Context;
import android.content.Intent;
import org.breizhjug.breizhlib.activity.*;
import org.breizhjug.breizhlib.activity.compte.CompteList;
import org.breizhjug.breizhlib.model.Commentaire;
import org.breizhjug.breizhlib.model.Livre;
import org.breizhjug.breizhlib.model.Reservation;

import java.util.ArrayList;

import static org.breizhjug.breizhlib.IntentConstantes.*;
import static org.breizhjug.breizhlib.IntentConstantes.LIVRE;

public class IntentSupport {

    private static final String MIME_TYPE_TEXT = "text/plain";

    public static Intent newShareIntent(Context context, String subject, String message,
                                        String chooserDialogTitle) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, message);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        shareIntent.setType(MIME_TYPE_TEXT);
        return Intent.createChooser(shareIntent, chooserDialogTitle);
    }


    public static Intent newShareComment(Context context, Commentaire item,
                                         String chooserDialogTitle) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, item.commentaire + "\n\n" + item.nom);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Commentaire de l'ouvrage " + item.livre.titre);
        shareIntent.setType(MIME_TYPE_TEXT);
        return Intent.createChooser(shareIntent, chooserDialogTitle);
    }

    public static Intent newLivreIntent(Context context, ArrayList<Livre> items, int index, Livre livre) {
        Intent intent = new Intent(context, LivreActivity.class);
        intent.putExtra(ITEMS, items);
        intent.putExtra(ITEM, livre);
        intent.putExtra(INDEX, index);
        return intent;
    }

    public static  Intent newLivreIntent(Context context, ArrayList<Livre> items, int position, Reservation reservation) {
        Intent intent = new Intent(context, LivreActivity.class);
        intent.putExtra(ITEMS, items);
        intent.putExtra(INDEX, position);
        intent.putExtra(ITEM, reservation.livre);
        intent.putExtra(EMAIL_RESERVATION, reservation.email);
        return intent;
    }

    public static Intent newCommentaireIntent(Context context, Commentaire commentaire, int position, ArrayList<Commentaire> items) {
        Intent intent = new Intent(context, CommentaireActivity.class);
        intent.putExtra(ITEM, commentaire);
        intent.putExtra(ITEMS, items);
        intent.putExtra(INDEX, position);
        return intent;
    }

    public static Intent newReservationIntent(Context context, Livre livre) {
        Intent intent = new Intent(context, ReservationActivity.class);
        intent.putExtra(LIVRE, livre);
        return intent;
    }

    public static Intent newAvisIntent(Context context, Livre item) {
        Intent pIntent = new Intent(context, AvisActivity.class);
        pIntent.putExtra(LIVRE, item);
        return pIntent;
    }

    public static Intent newMenuIntent(Context context) {
        Intent intent = new Intent(context, Menu.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    public static Intent newEmpruntsIntent(Context context, ArrayList<Livre> items, int position, Livre item) {
        Intent intent = new Intent(context, EmpruntsActivity.class);
        intent.putExtra(ITEMS, items);
        intent.putExtra(INDEX, position);
        intent.putExtra(ITEM, item);
        return intent;
    }

    public static Intent newComptesIntent(Context context) {
        Intent intent = new Intent(context, CompteList.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    public static Intent newConfigurationIntent(Context context) {
        Intent intent = new Intent(context, ConfigurationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }
}
