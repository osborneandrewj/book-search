package com.example.android.booksearch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Zark on 12/13/2016.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    /**
     * Create a new BookAdapter
     *
     * @param context
     * @param objects
     */
    public BookAdapter(Context context, ArrayList<Book> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the inflater and inflate the xml
        View bookItem = convertView;

        if ( bookItem == null) {
            bookItem = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_item, parent, false);
        }

        // Get the current object we are displaying
        Book currentBook = getItem(position);

        // Get each View from book_item.xml and set the appropriate data from the book object
        TextView title = (TextView) bookItem.findViewById(R.id.title_textview);
        title.setText(currentBook.getTitle());

        TextView publishedDate = (TextView) bookItem.findViewById(R.id.date_textview);
        publishedDate.setText(currentBook.getDate());

        TextView description = (TextView) bookItem.findViewById(R.id.description_textview);
        description.setText(currentBook.getDescription());

        TextView authors = (TextView) bookItem.findViewById(R.id.author_textview);
        authors.setText(currentBook.getAuthors());

        return bookItem;
    }
}
