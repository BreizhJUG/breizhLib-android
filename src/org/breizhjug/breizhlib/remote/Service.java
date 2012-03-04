package org.breizhjug.breizhlib.remote;


import android.util.Log;
import com.google.inject.Inject;
import org.breizhjug.breizhlib.database.Database;
import org.breizhjug.breizhlib.model.Model;
import org.breizhjug.breizhlib.utils.Cache;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public abstract class Service<T extends Model> implements Cache {

    private static final String TAG = "Breizhlib.Service";
    protected List<T> cache = new ArrayList<T>();

    @Inject
    protected JsonConverter converter;
    @Inject
    protected Database db;

    protected String serverUrl;

    public Service(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public abstract List<T> load(String authCookie, String urlString);

    public abstract String url();

    public boolean forceCall = false;


    public List<T> load(String authCookie) {
        try {
            if (cache != null) {
                Log.d("UPDATE", "load cache : " + cache.size());
            }
            if (forceCall || (cache == null || cache.isEmpty())) {
                List<T> entities = db.selectAll(getEntityClass());
                if (forceCall || entities.isEmpty()) {
                    forceCall = false;
                    Log.d(TAG, "load");
                    this.cache = load(authCookie, url());
                    Log.d("UPDATE", "load service : " + cache.size());

                    updateDB(cache);
                } else {
                    this.cache = entities;
                    Log.d("UPDATE", "load  db : " + cache.size());
                    loadDB(cache);
                }
            }
            Log.d("UPDATE", "load  : " + cache.size());
            return cache != null ? new ArrayList<T>(cache) : cache;
        } finally {
            db.close();
        }
    }

    private void loadDB(List<T> cache) {
        for (T entity : cache) {
            entity.onLoad(db);
        }
    }

    private void updateDB(List<T> cache) {
        db.beginTransaction();
        List<T> tmpcache = new ArrayList<T>(cache);
        for (T entity : tmpcache) {
            if (isInDB(entity)) {
                update(entity);
            } else {
                db.insert(entity);
            }
        }
        cache = tmpcache;
        db.endTransaction();
    }

    public void update(T entity) {
    }

    protected abstract boolean isInDB(T entity);

    protected abstract Class<T> getEntityClass();

    public void clearCache() {
        cache = null;
    }

    public void clearDBCache() {
        cache = null;
        db.deleteAll(getEntityClass());
    }


    protected boolean isJsonResult(String result) {
        if (result == null) {
            return false;
        }
        try {
            new JSONObject(result);
            return true;
            // Valid.
        } catch (JSONException e) {
            return false;
        }
    }

}
