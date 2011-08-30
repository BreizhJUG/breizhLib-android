package org.breizhjug.breizhlib.remote;


import android.util.Log;
import com.google.inject.Inject;
import org.acra.ErrorReporter;
import org.breizhjug.breizhlib.database.dao.LivreDAO;
import org.breizhjug.breizhlib.guice.ServerUrl;
import org.breizhjug.breizhlib.model.Emprunt;
import org.breizhjug.breizhlib.model.Reservation;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EmpruntService extends Service<Emprunt> {
    private static final String TAG = "Breizhlib.EmpruntService";

    private final String URL_BOOKS = serverUrl + "api/emprunts";

    @Inject
    private LivreDAO livreDAO;


    @Override
    public String url() {
        return URL_BOOKS;
    }

    @Override
    protected boolean isInDB(Emprunt entity) {
        Emprunt searchEntity = new Emprunt();
        searchEntity.isbn = entity.isbn;
        return db.selectSingle(searchEntity) != null;
    }

    @Override
    protected Class<Emprunt> getEntityClass() {
        return Emprunt.class;
    }


    @Override
    public List<Emprunt> load(String authCookie) {

        if (cache != null && livreDAO.findByReservation().size() != cache.size()) {
            db.deleteAll(Reservation.class);
            forceCall = true;
        }
        return super.load(authCookie);
    }

    public List<Emprunt> load(String authCookie, String urlString) {
        Log.d(TAG, urlString);
        String result = queryRESTurl(authCookie, urlString);
        ArrayList<Emprunt> BOOKS = new ArrayList<Emprunt>();
        if (result != null) {
            try {
                JSONArray booksArray = new JSONArray(result);
                Emprunt livre = null;
                for (int a = 0; a < booksArray.length(); a++) {
                    JSONObject item = booksArray.getJSONObject(a);
                    livre = converter.convertEmprunt(item);
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

    @Inject
    public EmpruntService(@ServerUrl String serverUrl) {
        super(serverUrl);
        forceCall = true;
    }

}