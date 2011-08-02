package org.breizhjug.breizhlib.remote;

import android.util.Log;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.model.Commentaire;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CommentaireService extends Service<Commentaire> {

    private static final String TAG = CommentaireService.class.getName();

    private static String URL_COMMENTS = BreizhLib.SERVER_URL + "api/commentaires";

    private static String URL_COMMENT = BreizhLib.SERVER_URL + "api/comment";

    public boolean comment(String authCookie,String bookId,String nom, String content, int note){
        Log.i(TAG,"b: "+bookId+" "+nom+" "+content+" "+note);
        Param param = new Param();
        param.key = "bookId";
        param.value = bookId;
        Param paramNom = new Param();
        paramNom.key = "nom";
        paramNom.value = nom;
        Param paramPrenom = new Param();
        paramPrenom.key = "content";
        paramPrenom.value = content;
        Param paramEmail = new Param();
        paramEmail.key = "note";
        paramEmail.value = note;
        String result = queryPostRESTurl(authCookie, URL_COMMENT, param,paramNom,paramPrenom,paramEmail);

        return result != null && result.startsWith("OK");

    }

    @Override
    public List<Commentaire> load(String authCookie, String urlString) {

        String result = queryRESTurl(authCookie, urlString);
        ArrayList<Commentaire> commentaires = new ArrayList<Commentaire>();
        if (result != null) {
            Log.i("REST", result);
            try {
                JSONArray commentairesArray = new JSONArray(result);
                Commentaire commentaire = null;
                for (int a = 0; a < commentairesArray.length(); a++) {
                    JSONObject item = commentairesArray.getJSONObject(a);
                    commentaire = new Commentaire((String) item.get("nom"), (String) item.get("avis"), Integer.valueOf((String) item.get("note")).intValue(), (String) item.get("titre"));
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

    private CommentaireService() {
        super();
    }

    public static synchronized CommentaireService getInstance() {
        if (instance == null) {
            instance = new CommentaireService();
        }
        return instance;
    }
}
