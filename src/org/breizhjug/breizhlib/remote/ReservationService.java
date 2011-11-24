package org.breizhjug.breizhlib.remote;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.google.inject.Inject;
import org.acra.ErrorReporter;
import org.breizhjug.breizhlib.database.dao.LivreDAO;
import org.breizhjug.breizhlib.guice.ServerUrl;
import org.breizhjug.breizhlib.model.Livre;
import org.breizhjug.breizhlib.model.Reservation;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReservationService extends Service<Reservation> {
    private static final String TAG = "Breizhlib.ReservationService";

    private final String URL_BOOKS = serverUrl + "api/reservations";
    private final String URL_RESA = serverUrl + "api/book/reserver";
    @Inject
    private LivreDAO livreDAO;
    private Context context;


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
        Log.i(TAG, "reserver :  " + result);

        try {
            JSONObject item = new JSONObject(result);
            return converter.convertResult(item);
        } catch (JSONException e) {
            return new Result("");
        }
    }

    @Override
    public List<Reservation> load(String authCookie) {
        forceCall = true;
        if (cache != null && livreDAO.findByReservation().size() != cache.size()) {
            db.deleteAll(Reservation.class);
            forceCall = true;
        }
        return super.load(authCookie);
    }

    public void update(Reservation entity) {
        db.update(entity);
        if (entity.livre != null) {
            Log.d(TAG, "update livre");
            db.update(entity.livre);
        }
    }

    public List<Reservation> load(String authCookie, String urlString) {
        Log.d(TAG, urlString);
        String result = queryRESTurl(authCookie, urlString);
        ArrayList<Reservation> BOOKS = new ArrayList<Reservation>();
        Log.i(TAG, result);
        if (result != null && result.length() > 2) {
            if (result.equals("Access denied")) {
                Toast.makeText(context, "Veuillez vouz reconnecter", Toast.LENGTH_SHORT).show();
            }
            try {
                JSONArray booksArray = new JSONArray(result);
                Reservation reservation = null;
                for (int a = 0; a < booksArray.length(); a++) {
                    JSONObject item = booksArray.getJSONObject(a);
                    reservation = converter.convertReservation(item);
                    BOOKS.add(reservation);
                }
                return BOOKS;
            } catch (JSONException e) {
                Log.e(TAG, "There was an error parsing the JSON", e);
                ErrorReporter.getInstance().handleSilentException(e);
            }
        }
        return BOOKS;
    }

    @Inject
    public ReservationService(@ServerUrl String serverUrl, Context context) {
        super(serverUrl);
        forceCall = true;
        this.context = context;
    }

    public void annuler(Livre livre) {
        //TODO appel au serveur pour annuler la reservation
    }
}