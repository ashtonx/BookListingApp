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

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getSubtitle() {
        return mSubtitle;
    }

    public void setSubtitle(String subtitle) {
        this.mSubtitle = subtitle;
    }

    public ArrayList<String> getAuthors() {
        return mAuthors;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.mAuthors = authors;
    }

    public String getInfoLink() {
        return mInfoLink;
    }

    public void setInfoLink(String infoLink) {
        this.mInfoLink = infoLink;
    }


}
