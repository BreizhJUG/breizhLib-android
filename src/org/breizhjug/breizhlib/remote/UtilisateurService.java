package org.breizhjug.breizhlib.remote;


import android.util.Log;
import com.google.inject.Inject;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.model.Utilisateur;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class UtilisateurService extends Service<Utilisateur> {

    private static final String TAG = "Breizhlib.UtilisateurService";
    private static final String URL_USER = BreizhLibConstantes.SERVER_URL + "api/profil";

    @Override
    public String url() {
        return URL_USER;
    }

    @Override
    protected boolean isInDB(Utilisateur entity) {
        Utilisateur searchEntity = new Utilisateur();
        searchEntity.email = entity.email;
        return db.selectSingle(searchEntity) != null;
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
            Log.d(TAG, result);
            try {
                JSONObject item = new JSONObject(result);
                return converter.convertUtilisateur(item);
            } catch (JSONException e) {
                Log.e(TAG, "There was an error parsing the JSON", e);
            }
        }
        return null;
    }


    @Inject
    public UtilisateurService() {
        super();
    }

}