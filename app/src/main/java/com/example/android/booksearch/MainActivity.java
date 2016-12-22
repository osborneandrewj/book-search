package com.example.android.booksearch;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


public class MainActivity extends AppCompatActivity
        implements SearchFragment.OnUserSearchListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private ResultsFragment resultsFragment;
    /**
     * Used to save the search data upon device rotation
     */
    private String mUserInput = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the FragmentManager to insert fragments for the search and results sections
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Create instance of the search fragments
        SearchFragment searchFragment = new SearchFragment();
        fragmentTransaction.add(R.id.search_container, searchFragment);
        fragmentTransaction.commit();

        // If there is previously saved information (i.e. the user rotated device after performing
        // a search, recreate that fragment and attach it to the proper view
        if (savedInstanceState != null) {
            FragmentTransaction newFragmentTransaction = fragmentManager.beginTransaction();
            resultsFragment = new ResultsFragment();
            newFragmentTransaction.add(R.id.results_container, resultsFragment).commit();
        } else {
            // If there is no saved information, create an empty results fragment to display the
            // empty state text
            onUserSearch("");
        }
    }

    /**
     * This method is used to create a new fragment to display the results of a user's search.
     * This method is called when the user clicks the search button on the virtual keyboard.
     *
     * @param userInput is the user's search term which originated from the SearchFragment
     */
    public void onUserSearch(String userInput) {
        // Capture the user input
        Log.i(LOG_TAG, "Fragment communicated with Activity! " + userInput);

        // Save the search term for device rotation
        mUserInput = userInput;

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (fragmentManager.findFragmentById(R.id.results_container) == null) {
            // If a resultsFragment doesn't exist, create a new one
            resultsFragment = new ResultsFragment();
            resultsFragment.getUserSearchInput(userInput);
            fragmentTransaction.add(R.id.results_container, resultsFragment).commit();
        } else {
            // But if the Fragment already exists (i.e., the user previously performed
            // a search), then refresh the fragment with the new data
            resultsFragment.getUserSearchInput(userInput);
            resultsFragment.refreshFragment();
            resultsFragment.getUserSearchInput(userInput);
            fragmentTransaction.replace(R.id.results_container, resultsFragment).commit();
        }

    }

    /**
     * This callback invoked when the Activity is temporarily destroyed, like when the
     * device is rotated. We need to save the user search term
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mUserInput != null) {
            outState.putString("INPUT_KEY", mUserInput);
        }
        super.onSaveInstanceState(outState);
    }

}
