package com.example.android.booklistingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    private String mQuery;
    private int mMaxResults;

    public BookLoader(Context context, String query, int maxResults) {
        super(context);
        mQuery = query;
        mMaxResults = maxResults;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        if (mQuery == null || mMaxResults < 1) return null;
        return Utils.fetchBookData(mQuery, mMaxResults);
    }
}
