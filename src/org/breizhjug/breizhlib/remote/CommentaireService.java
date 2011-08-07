package org.breizhjug.breizhlib.remote;

import android.util.Log;
import org.acra.ErrorReporter;
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

    public boolean comment(String authCookie, String bookId, String nom, String content, int note) {
        Log.i(TAG, "b: " + bookId + " " + nom + " " + content + " " + note);
        Param param = new Param("bookId", bookId);
        Param paramNom = new Param("nom", nom);
        Param paramPrenom = new Param("content", content);
        Param paramEmail = new Param("note", note);
        String result = queryPostRESTurl(authCookie, URL_COMMENT, param, paramNom, paramPrenom, paramEmail);

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
                    commentaire = converter.convertCommentaire(item);
                    commentaires.add(commentaire);
                }
                return commentaires;
            } catch (JSONException e) {
                Log.e("JSON", "There was an error parsing the JSON", e);
                ErrorReporter.getInstance().handleSilentException(e);
            }
        }
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
