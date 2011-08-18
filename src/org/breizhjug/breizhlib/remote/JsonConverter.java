package org.breizhjug.breizhlib.remote;

import org.breizhjug.breizhlib.model.Commentaire;
import org.breizhjug.breizhlib.model.Livre;
import org.breizhjug.breizhlib.model.Reservation;
import org.breizhjug.breizhlib.model.Utilisateur;
import org.json.JSONException;
import org.json.JSONObject;


public class JsonConverter {

    private String getFacultatifString(JSONObject item, String name) {
        try {
            return item.getString(name);
        } catch (JSONException e) {
            return null;
        }
    }

    private int getFacultatifInt(JSONObject item, String name) {
        try {
            return item.getInt(name);
        } catch (JSONException e) {
            return 0;
        }
    }

    public Livre convertLivre(JSONObject item) throws JSONException {
        Livre livre = null;
        if (item != null) {
            String editeur = getFacultatifString(item, "editeur");
            String image = getFacultatifString(item, "image");
            livre = new Livre(item.getString("titre"), item.getString("isbn"), editeur, image);
            livre.add = item.getBoolean("aAjouter");
            livre.etat = item.getString("etat");
            livre.note = getFacultatifInt(item, "note");
        }
        return livre;
    }

    public Commentaire convertCommentaire(JSONObject item) throws JSONException {
        Commentaire commentaire = null;
        if (item != null) {
            Livre livre = convertLivre(item.getJSONObject("livre"));
            commentaire = new Commentaire(item.getString("nom"), item.getString("avis"), item.getInt("note"), livre);
            commentaire.uid = item.getString("uid");
        }
        return commentaire;
    }

    public Reservation convertReservation(JSONObject item) throws JSONException {
        Reservation reservation = null;
        if (item != null) {
            Livre livre = convertLivre(item.getJSONObject("livre"));
            reservation = new Reservation(item.getString("nom"), item.getString("prenom"), livre);
        }
        return reservation;
    }

    public Utilisateur convertUtilisateur(JSONObject item) throws JSONException {
        Utilisateur user = null;
        if (item != null) {
            user = new Utilisateur();
            user.email = item.getString("email");
            user.nom = getFacultatifString(item, "nom");
            user.prenom = getFacultatifString(item, "prenom");
            user.username = getFacultatifString(item, "username");
            user.commentairesLabel = item.getString("commentaires");
            user.ouvragesEncoursLabel = item.getString("ouvragesEncours");
            user.ouvragesLlabel = item.getString("ouvrages");
            user.reservationsLabel = item.getString("reservations");
            user.isAdmin = item.getBoolean("isAdmin");
        }
        return user;
    }

    public Result convertResult(JSONObject item) throws JSONException {
        Result result = null;
        if (item != null) {
            result = new Result(item.getString("message"));
            result.valid = false;
        }
        return result;

    }
}
