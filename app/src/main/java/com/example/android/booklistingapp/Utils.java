package com.example.android.booklistingapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    private static final String LOG_TAG = Utils.class.getSimpleName();
    private static final int READ_TIMEOUT = 10000;
    private static final int CONNECTION_TIMEOUT = 15000;
    private static final String REQUEST_METHOD = "GET";

    private static final String QUERY_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private static final String MAX_RESULTS_ARG = "&maxResults=";
    private static final String CHARACTER_ENCODING = "UTF-8";

    private Utils() {
    }


    public static List<Book> fetchBookData(String queryUrl, int maxResults) {
        URL url = generateSearchUrl(queryUrl, maxResults);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        return parseBookJSON(jsonResponse);
    }

    private static URL generateSearchUrl(String query, int maxResults) {
        URL url = null;

        StringBuilder stringUrl = new StringBuilder();
        stringUrl.append(QUERY_URL);
        stringUrl.append(query);
        stringUrl.append(MAX_RESULTS_ARG);
        stringUrl.append(maxResults);

        try {
            url = new URL(stringUrl.toString());
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error creating search url", e);
        }

        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) return jsonResponse;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setRequestMethod(REQUEST_METHOD);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Connection error, response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem Retrieving JSON result", e);
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
            if (inputStream != null) inputStream.close();
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder out = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(
                    inputStream, Charset.forName(CHARACTER_ENCODING));
            BufferedReader buffer = new BufferedReader(inputStreamReader);
            String line = buffer.readLine();
            while (line != null) {
                out.append(line);
                line = buffer.readLine();
            }
        }
        return out.toString();
    }

    private static List<Book> parseBookJSON(String bookJson) {
        if (TextUtils.isEmpty(bookJson)) return null;
        List<Book> out = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(bookJson);
            JSONArray items = root.optJSONArray("items");
            if (items == null) return null;
            for (int i = 0; i < items.length(); ++i) {
                JSONObject volumeInfo = items.getJSONObject(i).optJSONObject("volumeInfo");
                if (volumeInfo != null) {
                    String title = volumeInfo.optString("title");
                    String subtitle = volumeInfo.optString("subtitle");
                    JSONArray authorsArray = volumeInfo.optJSONArray("authors");
                    ArrayList<String> authors = (ArrayList<String>) getAuthors(authorsArray);
                    String infoLink = volumeInfo.optString("infoLink");
                    out.add(new Book(title, subtitle, authors, infoLink));
                }
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing json", e);
        }
        return out;
    }

    private static List<String> getAuthors(JSONArray authorsArray) {
        if (authorsArray == null) return null;
        ArrayList<String> authors = new ArrayList<>();
        for (int i = 0; i < authorsArray.length(); i++) {
            try {
                authors.add(authorsArray.getString(i));
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error parsing authors", e);
            }
        }
        return authors;
    }
}
