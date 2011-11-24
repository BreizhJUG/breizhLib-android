package org.breizhjug.breizhlib.remote;


import android.util.Log;
import com.google.inject.Inject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.breizhjug.breizhlib.database.Database;
import org.breizhjug.breizhlib.model.Livre;
import org.breizhjug.breizhlib.model.Model;
import org.breizhjug.breizhlib.utils.Cache;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public abstract class Service<T extends Model> implements Cache {

    private static final String TAG = "Breizhlib.Service";
    private static final int CONNECTION_TIMEOUT = 20000;
    private static HttpClient httpclient = getHttpClient();
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
            Log.d("UPDATE","load cache : "+cache.size());
            if (forceCall ||  cache.isEmpty()) {
                List<T> entities = db.selectAll(getEntityClass());
                if (forceCall ||  entities.isEmpty()) {
                    forceCall = false;
                    Log.d(TAG, "load");
                    this.cache = load(authCookie, url());
                    Log.d("UPDATE","load service : "+cache.size());

                    updateDB(cache);
                } else {
                    this.cache = entities;
                    Log.d("UPDATE","load  db : "+cache.size());
                    loadDB(cache);
                }
            }
            Log.d("UPDATE","load  : "+cache.size());
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
        for (T entity : cache) {
            if (isInDB(entity)) {
                update(entity);
            } else {
                db.insert(entity);
            }
        }
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

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    protected String queryPostRESTurl(String auth, String url, Param... params) {
        // URLConnection connection;
        HttpPost httpPost = new HttpPost();


        HttpResponse response;
        try {

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(params.length);
            for (Param param : params) {
                nameValuePairs.add(new BasicNameValuePair(param.key, param.value.toString()));
            }

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpPost.addHeader("Cookie", auth);

            httpPost.setURI(new URI(url));
            response = httpclient.execute(httpPost);
            Log.d(TAG, "Status:[" + response.getStatusLine().toString() + "]");
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                String result = convertStreamToString(instream);
                Log.d(TAG, "Result of converstion: [" + result + "]");
                instream.close();
                return result;
            }
        } catch (ClientProtocolException e) {
            httpPost.abort();
            Log.w(TAG, "There was a protocol based error", e);
        } catch (IOException e) {
            httpPost.abort();
            Log.w(TAG, "There was an IO Stream related error", e);
        } catch (URISyntaxException e) {
            httpPost.abort();
            Log.w(TAG, "There was an IO Stream related error", e);
        }
        return null;
    }

    public class Param {
        public String key;
        public Object value;

        public Param(String key, Object value) {
            this.key = key;
            this.value = value;
        }
    }


    protected String queryRESTurl(String auth, String url, Param... params) {
        // URLConnection connection;
        HttpGet httpget = new HttpGet();

        HttpResponse response;
        try {

            BasicHttpParams httpParams = new BasicHttpParams();
            for (Param param : params) {
                httpParams.setParameter(param.key, param.value);
            }
            httpget.setParams(httpParams);

            httpget.addHeader("Cookie", auth);

            httpget.setURI(new URI(url));
            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                String result = convertStreamToString(instream);
                instream.close();
                return result;
            }
        } catch (ClientProtocolException e) {
            httpget.abort();
            Log.w(TAG, "There was a protocol based error", e);
        } catch (IOException e) {
            httpget.abort();
            Log.w(TAG, "There was an IO Stream related error", e);
        } catch (URISyntaxException e) {
            httpget.abort();
            Log.w(TAG, "There was an IO Stream related error", e);
        }
        return null;
    }

    public static HttpClient getHttpClient() {
        HttpParams myHttpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(myHttpParams,
                CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(myHttpParams, CONNECTION_TIMEOUT);
        return new DefaultHttpClient(myHttpParams);
    }
}
