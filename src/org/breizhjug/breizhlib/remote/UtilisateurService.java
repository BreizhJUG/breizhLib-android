package org.breizhjug.breizhlib.remote;


import android.util.Log;
import org.acra.ErrorReporter;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.model.Utilisateur;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class UtilisateurService extends Service<Utilisateur> {

    private static String URL_USER = BreizhLibConstantes.SERVER_URL + "api/profil";

    private static UtilisateurService instance;


    @Override
    public String url() {
        return URL_USER;
    }

    @Override
    protected Class<Utilisateur> getEntityClass() {
        return Utilisateur.class;
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
            Log.d("REST", result);
            try {
                JSONObject item = new JSONObject(result);
                return converter.convertUtilisateur(item);
            } catch (JSONException e) {
                Log.e("JSON", "There was an error parsing the JSON", e);
                ErrorReporter.getInstance().handleSilentException(e);
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