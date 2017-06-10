package com.example.android.booklistingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {
    private final String TITLE_SUB_SEPARATOR = ": ";
    private final String AUTHOR_SEPARATOR = ", ";

    public BookAdapter (Context context, List<Book> books){
        super(context,0,books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        Book currBook = getItem(position);

        //title
        TextView title = (TextView) convertView.findViewById(R.id.book_title);
        String titleString = "";
        if (currBook.getTitle()!=null) titleString+=currBook.getTitle();
        if (currBook.getSubtitle()!=null && !currBook.getSubtitle().isEmpty())
            titleString+=TITLE_SUB_SEPARATOR+currBook.getSubtitle();
        title.setText(titleString);

        //authors
        TextView authors = (TextView) convertView.findViewById(R.id.authors);
        String authorsString="";
        if (currBook.getAuthors()!=null && !currBook.getAuthors().isEmpty()){
            authorsString = currBook.getAuthors().get(0);
            if (currBook.getAuthors().size()>1){
                for (int i =1; i<currBook.getAuthors().size(); ++i){
                    authorsString+=AUTHOR_SEPARATOR + currBook.getAuthors().get(i);
                }
            }
        }
        authors.setText(authorsString);

        return convertView;
    }
}
