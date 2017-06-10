package com.example.android.booklistingapp;

import java.util.ArrayList;

public class Book {
    private String mTitle;
    private String mSubtitle;
    private ArrayList<String> mAuthors;
    private String mInfoLink;

    public Book(String title, String subtitle, ArrayList<String> authors, String infoLink) {
        this.mTitle = title;
        this.mSubtitle = subtitle;
        this.mAuthors = authors;
        this.mInfoLink = infoLink;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSubtitle() {
        return mSubtitle;
    }

    public ArrayList<String> getAuthors() {
        return mAuthors;
    }

    public String getInfoLink() {
        return mInfoLink;
    }


}
