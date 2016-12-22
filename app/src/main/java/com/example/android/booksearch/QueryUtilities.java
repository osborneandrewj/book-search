package com.example.android.booksearch;

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

/**
 * Created by Zark on 12/13/2016.
 */

public class QueryUtilities {

    /**
     * Used for log tags in this class
     */
    private static final String LOG_TAG = QueryUtilities.class.getSimpleName();

    /**
     * Private constructor because you should never make a QueryUtilities object
     */
    private QueryUtilities() {
    }

    /**
     * Create a URL object based on the user's input
     * This URL object will then be used to make the HTTP request
     *
     * @return a customized URL object
     */
    public static URL createURL(String aSearchTerm) {
        // Initialize a URL object
        URL url = null;

        // Create a test string to be converted into a URL
        String testUrl = "https://www.googleapis.com/books/v1/volumes?q=";

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(testUrl);
        stringBuilder.append(aSearchTerm);

        try {
            url = new URL(stringBuilder.toString());
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "MalformedURLException created while trying to create URL: " + e);
        }

        return url;
    }

    /**
     * Make an HTTP request using the provided URL object and return the server's
     * response as a String
     *
     * @param url for desired HTTP request
     * @return the server's response as a String
     * @throws IOException
     */
    public static String makeHttpRequest(URL url) throws IOException {

        // String used to capture the server's response to our JSON query
        String responseFromServer = "";
        // Initialize a HttpURLConnection object
        HttpURLConnection httpURLConnection = null;
        // Initialize an InputStream object
        InputStream inputStream = null;

        try {
            // Attempt a network connection
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(10000); // in ms
            httpURLConnection.setReadTimeout(10000); // in ms
            httpURLConnection.connect();

            // If the connection is successful, get the incoming data from the server
            // Note: A response code of "200" means the connection was successful
            if (httpURLConnection.getResponseCode() == 200) {
                // Get the data and store it in a String
                inputStream = httpURLConnection.getInputStream();
                responseFromServer = readFromInputStream(inputStream);
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error while creating URL connection with server: " + e);
        } finally {
            // Close both the HTTP connection and the InputStream object
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return responseFromServer;
    }

    /**
     * Read the data from the server and convert it into a String. This method is used by
     * the makeHttpRequest method.
     *
     * @param aInputStream from the server
     * @return the data converted into a String object
     * @throws IOException
     */
    private static String readFromInputStream(InputStream aInputStream) throws IOException {
        StringBuilder stringOutputFromServer = new StringBuilder();
        if (aInputStream != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(aInputStream, Charset.forName("UTF-8")));
            String currentLine = reader.readLine();
            // Read the response from the server and create a String from it
            while (currentLine != null) {
                // Get the current line and append it to the StringBuilder
                stringOutputFromServer.append(currentLine);
                // Then read the next line and repeat
                currentLine = reader.readLine();
            }
        }
        return stringOutputFromServer.toString();
    }

    /**
     * Method to parse the JSON data into formatted Strings which are added to an ArrayList
     *
     * @param aDataString is the JSON to be parsed
     * @return an ArrayList containing the relevant information
     */
    public static ArrayList<Book> interrogateDataForJsonObjects(String aDataString) {

        // Make sure that the app doesn't crash if there is no data to be parsed
        if (TextUtils.isEmpty(aDataString)) {
            return null;
        }

        // Create a list to which we will add any Book objects
        ArrayList<Book> booksArrayList = new ArrayList<>();

        try {
            // Convert the data from a String into a JSON object
            JSONObject baseJsonObject = new JSONObject(aDataString);
            // Then get an array of the returned books
            JSONArray itemsArray = baseJsonObject.getJSONArray("items");

            // For each item (book), extract the information desired
            // Note: The Google API returns only 10 items for each search, so
            // itemsArray.length will always equal 10 here
            for (int i = 0; i < itemsArray.length(); i++) {

                // Get the each item in the array and cast it to a JSON object
                JSONObject currentItem = itemsArray.getJSONObject(i);

                // Now extract relevant information from this JSON object
                String title = currentItem.getJSONObject("volumeInfo").getString("title");
                String publishedDate = currentItem.getJSONObject("volumeInfo").getString("publishedDate");
                // The "textSnippet" section is only intermittent and tends to throw lots of
                // JSONExceptions, so it has it's own catch to avoid cutting the parsing short
                String description = "";
                try {
                    description = currentItem.getJSONObject("searchInfo").getString("textSnippet");
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Error parsing JSON: " + e);
                }
                String previewLink = currentItem.getJSONObject("volumeInfo").getString("previewLink");
                // The list of author(s) is enclosed in a JSONArray
                JSONArray jsonArrayOfAuthors = currentItem.getJSONObject("volumeInfo").getJSONArray("authors");
                ArrayList<String> authorList = new ArrayList<>();
                for (int x = 0; x < jsonArrayOfAuthors.length(); x++) {
                    authorList.add(jsonArrayOfAuthors.getString(x));
                }

                // Finally add all this information to the booksArrayList
                booksArrayList.add(new Book(title, description, publishedDate, previewLink, authorList));
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error parsing JSON: " + e);
        }
        return booksArrayList;
    }

}
