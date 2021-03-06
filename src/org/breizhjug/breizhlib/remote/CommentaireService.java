package org.breizhjug.breizhlib.remote;

import android.util.Log;
import com.google.inject.Inject;
import org.acra.ErrorReporter;
import org.breizhjug.breizhlib.exception.ResultException;
import org.breizhjug.breizhlib.guice.ServerUrl;
import org.breizhjug.breizhlib.model.Commentaire;
import org.breizhjug.breizhlib.utils.NetworkUtils;
import org.breizhjug.breizhlib.utils.NetworkUtils.Param;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CommentaireService extends Service<Commentaire> {

    private static final String TAG = "Breizhlib.CommentaireService";

    private final String URL_COMMENTS = serverUrl + "api/commentaires";

    private final String URL_COMMENT = serverUrl + "api/comment";

    public Commentaire comment(String authCookie, String bookId, String nom, String content, int note) throws ResultException {
        Log.i(TAG, "b: " + bookId + " " + nom + " " + content + " " + note);
        Param param = new Param("bookId", bookId);
        Param paramNom = new Param("nom", nom);
        Param paramPrenom = new Param("content", content);
        Param paramEmail = new Param("note", note);
        String result = NetworkUtils.post(authCookie, URL_COMMENT, param, paramNom, paramPrenom, paramEmail);
        try {
            JSONObject item = new JSONObject(result);
            Commentaire commentaire = converter.convertCommentaire(item);
            db.insert(commentaire);
            return commentaire;
        } catch (JSONException e) {
            try {
                JSONObject item = new JSONObject(result);
                throw new ResultException(converter.convertResult(item));
            } catch (JSONException e1) {
                Log.e(TAG, "There was an error parsing the JSON", e);
                ErrorReporter.getInstance().handleSilentException(e);
                return null;
            }
        }

    }

    @Override
    public List<Commentaire> load(String authCookie, String urlString) {

        String result = NetworkUtils.get(authCookie, urlString);
        ArrayList<Commentaire> commentaires = new ArrayList<Commentaire>();
        if (result != null) {
            Log.d("REST", result);
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
        Log.d(TAG, URL_COMMENTS);
        return URL_COMMENTS;
    }

    @Override
    protected boolean isInDB(Commentaire entity) {
        Commentaire searchEntity = new Commentaire();
        searchEntity.isbn = entity.isbn;
        searchEntity.uid = entity.uid;
        return db.selectSingle(searchEntity) != null;
    }

    @Override
    protected Class<Commentaire> getEntityClass() {
        return Commentaire.class;
    }

    @Inject
    public CommentaireService(@ServerUrl String serverUrl) {
        super(serverUrl);
    }
}
