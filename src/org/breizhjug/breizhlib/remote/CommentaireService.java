package org.breizhjug.breizhlib.remote;

import android.content.Context;
import android.util.Log;
import org.breizhjug.breizhlib.model.Commentaire;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CommentaireService extends Service<Commentaire> {

    private static String URL_COMMENTS = "http://breizh-lib.appspot.com/commentaires.json";

    
    @Override
    public List<Commentaire> load(String urlString) {
        Log.i("REST", urlString);
        String result = queryRESTurl(urlString);
        ArrayList<Commentaire> commentaires = new ArrayList<Commentaire>();
        if (result != null) {
            try {
                JSONArray commentairesArray = new JSONArray(result);
                Commentaire commentaire = null;
                for (int a = 0; a < commentairesArray.length(); a++) {
                    JSONObject item = commentairesArray.getJSONObject(a);
                    commentaire = new Commentaire((String) item.get("nom"), (String) item.get("description"), Integer.valueOf((String) item.get("note")).intValue(), (String) item.get("livre"));
                    commentaires.add(commentaire);
                }
                return commentaires;
            } catch (JSONException e) {
                Log.e("JSON", "There was an error parsing the JSON", e);
            }
        }

        Commentaire commentaire = new Commentaire("No commentaire found", "", 0, "");
        commentaires.add(commentaire);
        return commentaires;
    }

    @Override
    public String url() {
        return URL_COMMENTS;
    }

    private static CommentaireService instance;

    public CommentaireService(Context context) {
        super();
    }

    public static synchronized CommentaireService getInstance(Context context) {
		if (instance == null) {
			instance = new CommentaireService(context);
		}
		return instance;
	}
}
