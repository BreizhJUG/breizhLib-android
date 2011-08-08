package org.breizhjug.breizhlib.remote;


import android.util.Log;
import org.acra.ErrorReporter;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.model.Livre;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OuvrageService extends Service<Livre> {

    private static String URL_BOOKS = BreizhLibConstantes.SERVER_URL + "api/ouvrages";

    private static String URL_FIND_BOOKS = BreizhLibConstantes.SERVER_URL + "api/find";

    private static String URL_ADD_BOOK = BreizhLibConstantes.SERVER_URL + "api/add";

    @Override
    public String url() {
        return URL_BOOKS;
    }

    @Override
    protected Class<Livre> getEntityClass() {
        return Livre.class;
    }

    public Livre find(String authCookie, String isbn) {
        return find(authCookie, URL_FIND_BOOKS, isbn);
    }

    private Livre find(String authCookie, String urlString, String isbn) {
        String result = queryPostRESTurl(authCookie, urlString, new Param("iSBN", isbn));
        Log.d("REST", result);
        if (result != null) {
            try {
                JSONArray booksArray = new JSONArray(result);
                JSONObject item = booksArray.getJSONObject(0);
                Livre livre = converter.convertLivre(item);
                return livre;
            } catch (JSONException e) {
                Log.e("JSON", "There was an error parsing the JSON", e);
                ErrorReporter.getInstance().handleSilentException(e);
            }
        }
        return null;
    }

    public Livre add(String authCookie, String isbn) {
        return add(authCookie, URL_ADD_BOOK, isbn);
    }

    private Livre add(String authCookie, String urlString, String isbn) {
        String result = queryPostRESTurl(authCookie, urlString, new Param("iSBN", isbn));
        Log.i("REST", result);
        if (result != null) {
            try {
                JSONArray booksArray = new JSONArray(result);
                JSONObject item = booksArray.getJSONObject(0);
                Livre livre = converter.convertLivre(item);
                return livre;
            } catch (JSONException e) {
                Log.e("JSON", "There was an error parsing the JSON", e);
                ErrorReporter.getInstance().handleSilentException(e);
            }
        }
        return null;
    }

    public List<Livre> load(String authCookie, String urlString) {
        Log.i("REST", urlString);
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
                Log.e("JSON", "There was an error parsing the JSON", e);
                ErrorReporter.getInstance().handleSilentException(e);
            }
        }
        return BOOKS;
    }

    private static OuvrageService instance;

    private OuvrageService() {
        super();
    }

    public static synchronized OuvrageService getInstance() {
        if (instance == null) {
            instance = new OuvrageService();
        }
        return instance;
    }
}