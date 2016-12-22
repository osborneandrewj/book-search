package com.example.android.booksearch;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

/**
 * This fragment displays the search bar to the user
 */
public class SearchFragment extends Fragment {

    private static final String LOG_TAG = SearchFragment.class.getSimpleName();
    /**
     * EditText that gathers user input for search
     */
    private EditText mEditText;
    /**
     * Used for Fragment to Activity communication
     */
    OnUserSearchListener mOnUserSearchListenerCallback;


    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_box, container, false);


        mEditText = (EditText) view.findViewById(R.id.search_edittext);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String userInput;
                    userInput = mEditText.getText().toString();
                    Log.i("Search Fragment", "User input: " + userInput);
                    // Get user input and search
                    // Slash send the user input to the container activity
                    mOnUserSearchListenerCallback.onUserSearch(userInput);

                    // Clear the EditText to be ready for the next search
                    mEditText.setText("");

                    // Hide the virtual keyboard
                    InputMethodManager inputMethodManager = (InputMethodManager)
                            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
                }
                return false;
            }
        });

        return view;
    }

    /**
     * Container Activity must implement this interface
     */
    public interface OnUserSearchListener {
        void onUserSearch(String userInput);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented the
        // callback interface.
        try {
            mOnUserSearchListenerCallback = (OnUserSearchListener) activity;
        } catch (ClassCastException e) {
            Log.e(LOG_TAG, "Error: " +
                    activity.toString() + " must implement OnUserSearchListener");
        }
    }
}
