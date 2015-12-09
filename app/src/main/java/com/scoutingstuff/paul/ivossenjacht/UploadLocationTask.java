package com.scoutingstuff.paul.ivossenjacht;

/**
 * Created by Paul on 12/5/2015.
 */

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static android.support.v4.widget.SearchViewCompat.getQuery;

/**
 * Created by Paul on 12/4/2015.
 */
public class UploadLocationTask extends AsyncTask<String, Void, String> {


    public interface Uploadistener{
        void onUploadComplete(String input);
        HashMap<String, String> onUploadStart();
    }
    private MainActivity main;
    private String result;
    private Uploadistener listener;
    private boolean hasResult;
    private HashMap<String, String> data;
    private boolean hasError;
    private int lastError;

    UploadLocationTask(MainActivity main) {
        this.main = main;
        this.listener = null;
        this.result = "";
        this.hasResult = false;
        this.hasError = false;
    }
    UploadLocationTask(MainActivity main, Uploadistener listener) {
        this(main);
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        this.hasResult = false;
        this.result = "";
        this.hasError = false;
        if (listener != null) {
            data = listener.onUploadStart();
        } else {
            data = null;
        }
    }

    @Override
    protected String doInBackground(String... urls) {
        // params comes from the execute() call: params[0] is the url.
        try {
            return downloadUrl(urls[0]);
        } catch (IOException e) {
            return "Unable to retrieve web page. URL may be invalid.";
        }
    }
    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        this.result = result;
        //main.buildAlert(result);
        if (this.hasError) {
            if (lastError == 403) {
                main.buildLogin();
            } else {
                main.buildAlert(result);
            }
        } else {
            this.hasResult = true;
            if (this.listener != null) {
                this.listener.onUploadComplete(result);
            }
            this.data = null;
        }

    }
    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        OutputStream oss = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        try {
            HashMap<String, String> out;
            if (data != null) {
                out = new HashMap<>(data);
            } else {
                out = new HashMap<>();
            }
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            // Starts the query
            conn.connect();
            oss = conn.getOutputStream();
            writeIt(oss, out);
            int response = conn.getResponseCode();
            if (response >= 300){
                this.hasError = true;
                this.lastError = response;
            }

            is = conn.getInputStream();
            return readIt(is);

            // finished using it.
        } catch (ConnectException e) {
            this.hasError =  true;
            this.lastError = 404;
            return e.getMessage();
        } catch( Exception e) {
            return e.getMessage();
        }finally {
            if (oss != null) {
                oss.close();
            }
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
    private BufferedWriter writeIt(OutputStream os, HashMap<String, String> params ) throws IOException {
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, "UTF-8"));
        writer.write(getPostDataString(params));
        //writer.write(getQuery(params));
        writer.flush();
        writer.close();
        return writer;
    }
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
    public boolean isHasResult() {
        return this.hasResult;
    }

    public String getResult() {
        return this.result;
    }
}