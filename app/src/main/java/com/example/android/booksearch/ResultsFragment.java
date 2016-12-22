package com.example.android.booksearch;


import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * This fragment displays the results of the book API search using an async task
 */
public class ResultsFragment extends Fragment
        implements android.support.v4.app.LoaderManager.LoaderCallbacks<ArrayList<Book>> {

    /**
     * Adapter for the list of book results
     */
    private BookAdapter mBookAdapter;
    /**
     * TextvView for empty state of search results list
     */
    private TextView mEmptyStateTextView;
    /**
     * Used to set the empty state text
     */
    private static final int NO_NETWORK = 1;
    /**
     * Used to set the empty state text
     */
    private static final int NO_SEARCH = -1;
    /**
     * Used to set the empty state text
     */
    private static final int NORMAL = -2;
    /**
     * Used to set the empty state text
     */
    private static final int NO_RESULTS = -3;
    /**
     * This will store the user's search input
     */
    private String mUserInput = "";

    public ResultsFragment() {
        // Required empty public constructor
    }


    /**
     * The system calls this when creating the Fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.book_list, container, false);

        // Get a reference to the empty state TextView
        mEmptyStateTextView = (TextView) rootView.findViewById(R.id.empty);

        // Display "No network found!" if no network exists
        if (!checkNetworkStatus()) {
            setEmptyStateTextView(NO_NETWORK);
            return rootView;
        }

        // Get a reference to the ListView in the book_list.xml layout file
        ListView bookListView = (ListView) rootView.findViewById(R.id.list);

        // Make the adapter take an empty holder list
        mBookAdapter = new BookAdapter(getActivity(), new ArrayList<Book>());

        // Set the adapter onto the ListView
        bookListView.setAdapter(mBookAdapter);

        // Populate the ListView using an Async thread
        getActivity().getSupportLoaderManager().initLoader(0, null, this);

        return rootView;
    }


    public void refreshFragment() {
        getActivity().getSupportLoaderManager().restartLoader(0, null, this);
    }


    @Override
    public Loader<ArrayList<Book>> onCreateLoader(int id, Bundle args) {
        return new VolumeLoader(getActivity(), mUserInput);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Book>> loader) {
        // Clear the existing adapter
        mBookAdapter.clear();
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Book>> loader, ArrayList<Book> data) {
        // Clear the current adapter
        mBookAdapter.clear();

        // If there is data, set it to the adapter
        if (data != null && !data.isEmpty()) {
            setEmptyStateTextView(NORMAL);
            mBookAdapter.addAll(data);
        // But if there is no data and no search has been attempted, display
        // "Try a search." to the user
        } else if (data == null && mUserInput.isEmpty()) {
            // If no search has been performed yet
            setEmptyStateTextView(NO_SEARCH);
        // But if there is no data and the user has tried a search, ask the
        // user to attempt another search
        } else {
            setEmptyStateTextView(NO_RESULTS);
        }

    }

    /**
     * This method sets the empty state TextView to display either no results or
     * no network access.
     *
     * @param selection is the state of the error
     */
    public void setEmptyStateTextView(int selection) {
        try {
            if (selection == NO_NETWORK) {
                mEmptyStateTextView.setText(R.string.no_network_empty_state_text);
            } else if (selection == NO_SEARCH) {
                mEmptyStateTextView.setText(R.string.empty_state_text);
            } else if (selection == NO_RESULTS) {
                mEmptyStateTextView.setVisibility(View.VISIBLE);
                mEmptyStateTextView.setText(R.string.no_results_found_text);
            } else if (selection == NORMAL) {
                mEmptyStateTextView.setText("");
                mEmptyStateTextView.setVisibility(View.INVISIBLE);
            }
        } catch (NullPointerException e) {
            Log.e("ResultsFragment", "Error setting empty state text: " + e);
        }
    }

    /**
     * Method to check for network connectivity.
     *
     * @return isConnectedOrConnecting is false if no network is present.
     */
    private boolean checkNetworkStatus() {
        // Get ConnectivityManager to determine if a network is present for the API search
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);

        // Set up boolean "isConnected to be true if network is present or trying to connect
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnectedOrConnecting = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnectedOrConnecting;
    }

    /**
     * This method is used by the parent activity and passes this Fragment the user search term
     *
     * @param userInput is the user search term which originated from the SearchFragment
     */
    public void getUserSearchInput(String userInput) {
        mUserInput = userInput;
    }

}
