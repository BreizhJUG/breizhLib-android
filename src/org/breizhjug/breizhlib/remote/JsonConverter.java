package org.breizhjug.breizhlib.remote;

import org.breizhjug.breizhlib.model.Commentaire;
import org.breizhjug.breizhlib.model.Livre;
import org.breizhjug.breizhlib.model.Reservation;
import org.breizhjug.breizhlib.model.Utilisateur;
import org.json.JSONException;
import org.json.JSONObject;


public class JsonConverter {

    public Livre convertLivre(JSONObject item) throws JSONException {
        Livre livre = null;
        if (item != null) {
            livre = new Livre((String) item.get("titre"), (String) item.get("isbn"), (String) item.get("editeur"), (String) item.get("image"));
            livre.add = ((String) item.get("aAjouter")).equals("true");
            livre.etat = (String) item.get("etat");
        }
        return livre;
    }

    public Commentaire convertCommentaire(JSONObject item) throws JSONException {
        Commentaire commentaire = null;
        if (item != null) {
            Livre livre = convertLivre(item.getJSONObject("livre"));
            commentaire = new Commentaire((String) item.get("nom"), (String) item.get("avis"), Integer.valueOf((String) item.get("note")).intValue(), livre);
        }
        return commentaire;
    }

    public Reservation convertReservation(JSONObject item) throws JSONException {
        Reservation reservation = null;
        if (item != null) {
            Livre livre = convertLivre(item.getJSONObject("livre"));
            reservation = new Reservation((String) item.get("nom"), (String) item.get("prenom"), livre);
        }
        return reservation;
    }

    public Utilisateur convertUtilisateur(JSONObject item) throws JSONException {
        Utilisateur user = null;
        if (item != null) {
            user = new Utilisateur();
            user.email = (String) item.get("email");
            user.nom = (String) item.get("nom");
            user.prenom = (String) item.get("prenom");
            user.username = (String) item.get("username");
            user.commentairesLabel = (String) item.get("commentaires");
            user.ouvragesEncoursLabel = (String) item.get("ouvragesEncours");
            user.ouvragesLlabel = (String) item.get("ouvrages");
            user.reservationsLabel = (String) item.get("reservations");
            user.isAdmin = ((String) item.get("isAdmin")).equals("true");
        }
        return user;
    }
}
