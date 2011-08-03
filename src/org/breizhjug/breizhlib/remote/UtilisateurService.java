package org.breizhjug.breizhlib.remote;


import android.util.Log;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.model.Utilisateur;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class UtilisateurService extends Service<Utilisateur> {

    private static String URL_USER = BreizhLib.SERVER_URL + "api/profil";

    private static UtilisateurService instance;


    @Override
    public String url() {
        return URL_USER;
    }

    public List<Utilisateur> load(String authCookie, String urlString) {
        return null;
    }

    public Utilisateur find(String authCookie) {
        return find(authCookie, URL_USER);
    }

    private Utilisateur find(String authCookie, String urlString) {
        String result = queryRESTurl(authCookie, urlString);

        if (result != null) {
            Log.i("REST", result);
            try {
                JSONArray booksArray = new JSONArray(result);
                JSONObject item = booksArray.getJSONObject(0);
                Utilisateur user = new Utilisateur();
                user.email = (String) item.get("email");
                user.nom = (String) item.get("nom");
                user.prenom = (String) item.get("prenom");
                user.username = (String) item.get("username");
                user.commentairesLabel = (String) item.get("commentaires");
                user.ouvragesEncoursLabel = (String) item.get("ouvragesEncours");
                user.ouvragesLlabel = (String) item.get("ouvrages");
                user.reservationsLabel = (String) item.get("reservations");
                user.isAdmin = ((String) item.get("isAdmin")).equals("true");

                return user;
            } catch (JSONException e) {
                Log.e("JSON", "There was an error parsing the JSON", e);
            }
        }
        return null;
    }

    private UtilisateurService() {
        super();
    }

    public static synchronized UtilisateurService getInstance() {
        if (instance == null) {
            instance = new UtilisateurService();
        }
        return instance;
    }
}