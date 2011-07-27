package org.breizhjug.breizhlib.remote;


import android.util.Log;
import org.breizhjub.breizhlib.R;
import org.breizhjug.breizhlib.model.Livre;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OuvrageService extends Service<Livre> {

    private static String URL_BOOKS = "http://breizh-lib.appspot.com/ouvrages.json";

    private static String URL_FIND_BOOKS = "http://breizh-lib.appspot.com/findisbn";

    @Override
    public String url() {
        return URL_BOOKS;
    }

    public Livre find(String isbn) {
        return find(URL_FIND_BOOKS, isbn);
    }

    private Livre find(String urlString, String isbn) {
        String result = queryPostRESTurl(urlString, isbn);
        Log.i("REST", result);
        if (result != null) {
            try {
                JSONArray booksArray = new JSONArray(result);
                JSONObject item = booksArray.getJSONObject(0);
                Livre livre = new Livre((String) item.get("titre"), (String) item.get("isbn"), (String) item.get("editeur"), (String) item.get("image"));
                return livre;
            } catch (JSONException e) {
                Log.e("JSON", "There was an error parsing the JSON", e);
            }
        }
        return null;
    }

    public List<Livre> load(String urlString) {
        Log.i("REST", urlString);
        String result = queryRESTurl(urlString);
        ArrayList<Livre> BOOKS = new ArrayList<Livre>();
        if (result != null) {
            try {
                JSONArray booksArray = new JSONArray(result);
                Livre livre = null;
                for (int a = 0; a < booksArray.length(); a++) {
                    JSONObject item = booksArray.getJSONObject(a);
                    livre = new Livre((String) item.get("titre"), (String) item.get("isbn"), (String) item.get("editeur"), (String) item.get("image"));
                    BOOKS.add(livre);
                }
                return BOOKS;
            } catch (JSONException e) {
                Log.e("JSON", "There was an error parsing the JSON", e);
            }
        }

        Livre livre = new Livre("No livre found", "", "", String.valueOf(R.drawable.book));
        BOOKS.add(livre);
        return BOOKS;
    }
}