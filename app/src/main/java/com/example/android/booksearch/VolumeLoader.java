package com.example.android.booksearch;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Andrew Osborne on 12/15/2016.
 *
 * This class provides the AsyncTaskLoader thread for performing a book API search
 * on the user's input
 */

public class VolumeLoader extends AsyncTaskLoader<ArrayList<Book>> {

    private static final String LOG_TAG = VolumeLoader.class.getName();
    /** Query URL */
    private URL mUrl = null;
    /** User search term(s) */

    public VolumeLoader(Context context, String userInput) {
        super(context);
        // Create a new URL based on the user's input
        mUrl = QueryUtilities.createURL(userInput);
        Log.i(LOG_TAG, "VolumeLoader here. We have package: " + userInput + mUrl.toString());
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Book> loadInBackground() {
        // If no URL exists, exit early
        if (mUrl == null) {
            return null;
        }

        // Create an ArrayList that will hold the search results
       ArrayList<Book> booksList;

        // Get raw data response from server
        String data = "";
        try {
            data = QueryUtilities.makeHttpRequest(mUrl);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error making HTTP request: " + e);
        }



        // Now extract the relevant information from the data
        booksList = QueryUtilities.interrogateDataForJsonObjects(data);

        return booksList;
    }
}
