package org.breizhjug.breizhlib.remote;


import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.breizhjug.breizhlib.model.Commentaire;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public abstract class Service<T> {

    private static HttpClient httpclient = new DefaultHttpClient();
    private List<T> cache = null;

    public abstract List<T> load(String urlString);

    public abstract String url();

     public List<T> load() {
		if (cache == null) {
			cache = load(url());
		}
		return cache != null ? new ArrayList<T>(cache) : cache;
	}

    private String convertStreamToString(InputStream is) {
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


    protected String queryRESTurl(String url) {
        // URLConnection connection;
        HttpGet httpget = new HttpGet();

        HttpResponse response;
        try {
            httpget.setURI(new URI(url));
            response = httpclient.execute(httpget);
            Log.i("REST", "Status:[" + response.getStatusLine().toString() + "]");
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                String result = convertStreamToString(instream);
                Log.i("REST", "Result of converstion: [" + result + "]");
                instream.close();
                return result;
            }
        } catch (ClientProtocolException e) {
            Log.e("REST", "There was a protocol based error", e);
        } catch (IOException e) {
            Log.e("REST", "There was an IO Stream related error", e);
        } catch (URISyntaxException e) {
            Log.e("REST", "There was an IO Stream related error", e);
        }
        return null;
    }
}
