package org.breizhjug.breizhlib.utils;

import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Guernion Sylvain
 * Date: 02/03/12
 * Time: 18:56
 */
public class NetworkUtils {

    private static final int CONNECTION_TIMEOUT = 20000;
    private static DefaultHttpClient httpclient = getHttpClient();
    private static final String TAG = "Breizhlib.NetWork";

    public static DefaultHttpClient getHttpClient() {
        if (httpclient == null) {
            HttpParams myHttpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(myHttpParams,
                    CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(myHttpParams, CONNECTION_TIMEOUT);
            return new DefaultHttpClient(myHttpParams);
        } else {
            return httpclient;
        }
    }

    public static String post(String auth, String url, Param... params) {
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

    public static class Param {
        public String key;
        public Object value;

        public Param(String key, Object value) {
            this.key = key;
            this.value = value;
        }
    }


    public static String get(String auth, String url, Param... params) {
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
}
