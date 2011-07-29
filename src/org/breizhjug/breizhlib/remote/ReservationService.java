package org.breizhjug.breizhlib.remote;


import android.content.Context;
import android.util.Log;
import org.breizhjub.breizhlib.R;
import org.breizhjug.breizhlib.model.Reservation;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReservationService extends Service<Reservation> {

    private static String URL_BOOKS = "http://breizh-lib.appspot.com/reservations.json";

    @Override
    public String url() {
        return URL_BOOKS;
    }

    public List<Reservation> load(String urlString) {
        Log.i("REST", urlString);
        String result = queryRESTurl(urlString);
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

    public ReservationService(Context context) {
        super();
    }

    public static synchronized ReservationService getInstance(Context context) {
		if (instance == null) {
			instance = new ReservationService(context);
		}
		return instance;
	}
}