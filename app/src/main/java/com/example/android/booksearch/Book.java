package com.example.android.booksearch;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Zark on 12/13/2016.
 */

public class Book {

    private String mTitle;
    private ArrayList<String> mAuthors;
    private String mDescription;
    private String mDate;
    private String mUrl;


    /**
     * Create a new Book Object
     *
     * @param aTitle       is the title of the Book
     * @param aDescription is a description of the Book
     * @param aDate        is the date the Book was published
     * @param aUrl         is the URL of the book
     * @param aAuthorList  is an array of the books authors
     */
    public Book(String aTitle, String aDescription, String aDate, String aUrl,
                ArrayList<String> aAuthorList) {
        mTitle = aTitle;
        mDescription = aDescription;
        mDate = aDate;
        mUrl = aUrl;
        mAuthors = aAuthorList;
    }

    /**
     * Return the Book's title as a String
     *
     * @return the Book's title
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Return a list of the Book's authors as a String array
     *
     * @return an array of the books author's
     */
    public String getAuthors() {
        return mAuthors.get(0);
    }

    /**
     * Return the Book's description
     *
     * @return the description of the Book
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     * Return the Book's publish date in a readable format
     *
     * @return the date the Book was published
     */
    public String getDate() {
        Date date = new Date();
        // Parse the string for the date
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        // If the date is not able to be parsed (it is in a format "yyyy"), then we
        // just return the raw date in String format
        try {
            date = format.parse(mDate);
        } catch (ParseException e) {
            Log.e("Book.java", "Error parsing Date! " + e);
            return mDate;
        }
        // Then set the correct date format
        DateFormat finalFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);

        return finalFormat.format(date);
    }

    /**
     * Return the Book's preview URL
     *
     * @return the Book's preview URL as a String
     */
    public String getUrl() {
        return mUrl;
    }


}
