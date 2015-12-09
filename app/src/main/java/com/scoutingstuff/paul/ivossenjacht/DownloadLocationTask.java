package com.scoutingstuff.paul.ivossenjacht;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Paul on 12/4/2015.
 */
public class DownloadLocationTask extends AsyncTask<String, Void, String> {


    public interface DownloadListener{
        void onDownloadComplete(String input);
        void onDownloadStart();
    }
    private MainActivity main;
    private String result;
    private DownloadListener listener;
    private boolean hasResult;

    DownloadLocationTask(MainActivity main) {
        this.main = main;
        this.listener = null;
        this.result = "";
        this.hasResult = false;
    }
    DownloadLocationTask(MainActivity main, DownloadListener listener) {
        this(main);
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        this.hasResult = false;
        this.result = "";
        if (listener != null) {
            listener.onDownloadStart();
        }
    }

    @Override
    protected String doInBackground(String... urls) {
        // params comes from the execute() call: params[0] is the url.
        try {
            return  downloadUrl(urls[0]);
        } catch (IOException e) {
            return "Unable to retrieve web page. URL may be invalid.";
        }
    }
    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        this.result = result;
        this.hasResult = true;
        if (this.listener != null) {
            this.listener.onDownloadComplete(result);
        }

    }
    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            if (response < 300) {
                //Log.d("DBG", "The response is: " + response);
                is = conn.getInputStream();
                // Convert the InputStream into a string
                return readIt(is);
            } else {
                return "";
            }

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } catch( Exception e) {
            e.getCause();
            return "";
        }finally {
            if (is != null) {
                is.close();
            }
        }
    }

    private String readIt(InputStream stream) throws IOException {
        Reader reader;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[500];
        reader.read(buffer);
        return new String(buffer);
    }

    public boolean isHasResult() {
        return this.hasResult;
    }

    public String getResult() {
        return this.result;
    }
}