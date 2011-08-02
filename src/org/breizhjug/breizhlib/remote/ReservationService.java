package org.breizhjug.breizhlib.remote;


import android.util.Log;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.model.Reservation;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReservationService extends Service<Reservation> {
    private static final String TAG = ReservationService.class.getName();

    private static String URL_BOOKS = BreizhLib.SERVER_URL + "api/reservations";
    private static String URL_RESA = BreizhLib.SERVER_URL + "api/book/reserver";

    @Override
    public String url() {
        return URL_BOOKS;
    }

    public boolean reserver(String authCookie,String isbn,String nom, String prenom,String email){
        Param param = new Param();
        param.key = "id";
        param.value = isbn;
        Param paramNom = new Param();
        paramNom.key = "nom";
        paramNom.value = nom;
        Param paramPrenom = new Param();
        paramPrenom.key = "prenom";
        paramPrenom.value = prenom;
        Param paramEmail = new Param();
        paramEmail.key = "email";
        paramEmail.value = email;
        String result = queryPostRESTurl(authCookie, URL_RESA, param,paramNom,paramPrenom,paramEmail);
        Log.i(TAG,result);
        return result != null && result.startsWith("OK");
    }

    public List<Reservation> load(String authCookie, String urlString) {
        Log.i("REST", urlString);
        String result = queryRESTurl(authCookie, urlString);
        ArrayList<Reservation> BOOKS = new ArrayList<Reservation>();
        if (result != null) {
            try {
                JSONArray booksArray = new JSONArray(result);
                Reservation livre = null;
                for (int a = 0; a < booksArray.length(); a++) {
                    JSONObject item = booksArray.getJSONObject(a);
                    livre = new Reservation((String) item.get("nom"), (String) item.get("prenom"), (String) item.get("image"), (String) item.get("isbn"), (String) item.get("livre"));
                    BOOKS.add(livre);
                }
                return BOOKS;
            } catch (JSONException e) {
                Log.e("JSON", "There was an error parsing the JSON", e);
            }
        }

        Reservation livre = new Reservation("", "", String.valueOf(R.drawable.book), "", "No reservation found");
        BOOKS.add(livre);
        return BOOKS;
    }

    private static ReservationService instance;

    private ReservationService() {
        super();
    }

    public static synchronized ReservationService getInstance() {
        if (instance == null) {
            instance = new ReservationService();
        }
        return instance;
    }
}