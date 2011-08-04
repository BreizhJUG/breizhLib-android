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
                return converter.convertUtilisateur(item);
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