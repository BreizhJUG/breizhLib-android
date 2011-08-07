package org.breizhjug.breizhlib.remote;


import android.util.Log;
import org.acra.ErrorReporter;
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

    private static ReservationService instance;

    @Override
    public String url() {
        return URL_BOOKS;
    }

    public boolean reserver(String authCookie, String isbn, String nom, String prenom, String email) {
        Param param = new Param("id", isbn);
        Param paramNom = new Param("nom", nom);
        Param paramPrenom = new Param("prenom", prenom);
        Param paramEmail = new Param("email", email);
        String result = queryPostRESTurl(authCookie, URL_RESA, param, paramNom, paramPrenom, paramEmail);
        Log.i(TAG, result);
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
                    livre = converter.convertReservation(item);
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