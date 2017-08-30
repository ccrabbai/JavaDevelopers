package com.example.android.javadevelopers;

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

/**
 * Created by onyekachukwu on 8/29/2017.
 */

public class QueryGit {

    /** Tag for the log messages */
    private static final String LOG_TAG = QueryGit.class.getSimpleName();

    //Nobody should create an object of this class
    private QueryGit() {
    }

    //Query the github dataset and return a list of {@link developers} objects.
    public static List<Developers> fetchDeveloperData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<Developers> developers = null;
        try {
            developers = extractFeatureFromJson(jsonResponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Return the list of {@link Earthquake}s
        return developers;
    }

    //Returns new URL object from the given string URL.
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try { url = new URL(stringUrl); }
        catch (MalformedURLException e) { Log.e(LOG_TAG, "Problem building the URL ", e); }
        return url;
    }

    //Make an HTTP request to the given URL and return a String as the response.
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) { return jsonResponse; }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(2000000 /* milliseconds */);
            urlConnection.setConnectTimeout(2000000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) { urlConnection.disconnect(); }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    //Convert the {@link InputStream} into a String which contains the
    //whole JSON response from the server.
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    //Return a list of {@link Earthquake} objects that has been built up from
    //parsing the given JSON response.
    private static List<Developers> extractFeatureFromJson(String developerJSON) throws JSONException {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(developerJSON)) { return null; }

        // Create an empty ArrayList that we can start adding earthquakes to
        List<Developers> developers = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONObject from the JSON response string
            JSONObject rootJSON = new JSONObject(developerJSON);

            // Extract the JSONArray associated with the key called "items",
            // which represents a list of items (or developers).
            JSONArray developerArray = rootJSON.getJSONArray("items");

            // For each developer in the developerArray
            for (int i = 0; i < developerArray.length(); i++) {

                // Get a single earthquake at position i within the list of earthquakes
                JSONObject currentDeveloper = developerArray.getJSONObject(i);

                //Extract the username for the key "username"
                String username = currentDeveloper.getString("login");

                //Extract the avatar_url for the key "avatar_url"
                String avatar_url = currentDeveloper.getString("avatar_url");

                //Extract the html_url for the key "html_url"
                String url = currentDeveloper.getString("html_url");

                // Create a new {@link Earthquake} object with the magnitude, location, time,
                // and url from the JSON response.
                //Earthquake earthquake = new Earthquake(magnitude, location, time, url);
                Developers developer = new Developers(username, url, avatar_url);

                // Add the new {@link Earthquake} to the list of earthquakes.
                developers.add(developer);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return developers;
    }

}
