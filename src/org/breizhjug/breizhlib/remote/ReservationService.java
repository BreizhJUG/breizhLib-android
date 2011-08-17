package org.breizhjug.breizhlib.remote;


import android.util.Log;
import org.acra.ErrorReporter;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.database.dao.LivreDAO;
import org.breizhjug.breizhlib.model.Reservation;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReservationService extends Service<Reservation> {
    private static final String TAG = "Breizhlib.ReservationService";

    private static String URL_BOOKS = BreizhLibConstantes.SERVER_URL + "api/reservations";
    private static String URL_RESA = BreizhLibConstantes.SERVER_URL + "api/book/reserver";

    private static ReservationService instance;

    @Override
    public String url() {
        return URL_BOOKS;
    }

    @Override
    protected boolean isInDB(Reservation entity) {
        Reservation searchEntity = new Reservation();
        searchEntity.isbn = entity.isbn;
        return db.selectSingle(searchEntity) != null;
    }

    @Override
    protected Class<Reservation> getEntityClass() {
        return Reservation.class;
    }

    public Result reserver(String authCookie, String isbn, String nom, String prenom, String email) {
        Param param = new Param("id", isbn);
        Param paramNom = new Param("nom", nom);
        Param paramPrenom = new Param("prenom", prenom);
        Param paramEmail = new Param("email", email);
        String result = queryPostRESTurl(authCookie, URL_RESA, param, paramNom, paramPrenom, paramEmail);
        Log.i(TAG, result);

        try{
            JSONObject item = new JSONObject(result);
            return converter.convertResult(item);
        } catch (JSONException e) {
            return new Result("");
        }
    }

    @Override
    public List<Reservation> load(String authCookie) {

        if (cache != null && LivreDAO.findByReservation().size() != cache.size()) {
            db.deleteAll(Reservation.class);
            forceCall = true;
        }
        return super.load(authCookie);
    }

    public List<Reservation> load(String authCookie, String urlString) {
        Log.d(TAG, urlString);
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
                Log.e(TAG, "There was an error parsing the JSON", e);
                ErrorReporter.getInstance().handleSilentException(e);
            }
        }
        return BOOKS;
    }

    private ReservationService() {
        super(BreizhLib.getDataBaseHelper());
    }

    public static synchronized ReservationService getInstance() {
        if (instance == null) {
            instance = new ReservationService();
        }
        return instance;
    }
}