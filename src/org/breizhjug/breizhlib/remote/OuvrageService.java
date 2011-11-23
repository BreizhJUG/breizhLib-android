package org.breizhjug.breizhlib.remote;


import android.util.Log;
import com.google.inject.Inject;
import org.acra.ErrorReporter;
import org.breizhjug.breizhlib.exception.ResultException;
import org.breizhjug.breizhlib.guice.ServerUrl;
import org.breizhjug.breizhlib.model.Livre;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OuvrageService extends Service<Livre> {

    private static final String TAG = "Breizhlib.OuvrageService";

    private final String URL_BOOKS = serverUrl + "api/ouvrages";

    private final String URL_BOOKS_SEARCH = serverUrl + "api/ouvrages?p=";

    private final String URL_FIND_BOOKS = serverUrl + "api/find";

    private final String URL_ADD_BOOK = serverUrl + "api/add";

    @Override
    public String url() {
        return URL_BOOKS;
    }

    @Override
    protected boolean isInDB(Livre entity) {
        Livre searchEntity = new Livre();
        searchEntity.iSBN = entity.iSBN;
        return db.selectSingle(searchEntity) != null;
    }

    @Override
    protected Class<Livre> getEntityClass() {
        return Livre.class;
    }

    public Livre find(String authCookie, String isbn) throws ResultException {
        return find(authCookie, URL_FIND_BOOKS, isbn);
    }

    private Livre find(String authCookie, String urlString, String isbn) throws ResultException {
        String result = queryPostRESTurl(authCookie, urlString, new Param("iSBN", isbn));
        Log.d(TAG, "" + result);
        if (result != null) {
            try {
                JSONObject item = new JSONObject(result);
                Livre livre = converter.convertLivre(item);
                return livre;
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
        return null;
    }

    public Livre add(String authCookie, String isbn) {
        return add(authCookie, URL_ADD_BOOK, isbn);
    }

    private Livre add(String authCookie, String urlString, String isbn) {
        String result = queryPostRESTurl(authCookie, urlString, new Param("iSBN", isbn));
        Log.i(TAG, result);
        if (result != null) {
            try {
                JSONArray booksArray = new JSONArray(result);
                JSONObject item = booksArray.getJSONObject(0);
                Livre livre = converter.convertLivre(item);
                return livre;
            } catch (JSONException e) {
                Log.e(TAG, "There was an error parsing the JSON", e);
                ErrorReporter.getInstance().handleSilentException(e);
            }
        }
        return null;
    }

    public List<Livre> load(String authCookie, String urlString) {
        Log.i(TAG, urlString);
        String result = queryRESTurl(authCookie, urlString);
        ArrayList<Livre> BOOKS = new ArrayList<Livre>();
        if (result != null) {
            try {
                JSONArray booksArray = new JSONArray(result);
                Livre livre = null;
                for (int a = 0; a < booksArray.length(); a++) {
                    JSONObject item = booksArray.getJSONObject(a);
                    livre = converter.convertLivre(item);

                    BOOKS.add(livre);
                }
                return BOOKS;
            } catch (JSONException e) {
                Log.e(TAG, "There was an error parsing the JSON", e);
            }
        }
        return BOOKS;
    }

    public List<Livre> search(String authCookie, String searchKey) {
        Log.i(TAG, URL_BOOKS_SEARCH);
               String result = queryRESTurl(authCookie, URL_BOOKS_SEARCH+searchKey);
               ArrayList<Livre> BOOKS = new ArrayList<Livre>();
               if (result != null) {
                   try {
                       JSONArray booksArray = new JSONArray(result);
                       Livre livre = null;
                       for (int a = 0; a < booksArray.length(); a++) {
                           JSONObject item = booksArray.getJSONObject(a);
                           livre = converter.convertLivre(item);

                           BOOKS.add(livre);
                       }
                       return BOOKS;
                   } catch (JSONException e) {
                       Log.e(TAG, "There was an error parsing the JSON", e);
                   }
               }
               return BOOKS;
        }

    @Inject
    public OuvrageService(@ServerUrl String serverUrl) {
        super(serverUrl);
    }
}