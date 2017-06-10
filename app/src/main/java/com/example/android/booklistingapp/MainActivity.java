package com.example.android.booklistingapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    private String searchString;
    private static int MAX_RESULTS = 5;

    private TextView mEmptyStateView;
    private BookAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //views
        mEmptyStateView = (TextView) findViewById(R.id.empty_view);
        mAdapter = new BookAdapter(this, new ArrayList<Book>());
        ListView bookListView = (ListView) findViewById(R.id.list);
        bookListView.setEmptyView(mEmptyStateView);
        bookListView.setAdapter(mAdapter);
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                        mAdapter.getItem(position).getInfoLink()
                ));
                if (browserIntent.resolveActivity(view.getContext().getPackageManager()) != null)
                    view.getContext().startActivity(browserIntent);
            }
        });

        final EditText mSearchEditText = (EditText) findViewById(R.id.search_field);
        mSearchEditText.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEND) {
                            if (v.getText().toString().trim() != null) {
                                if ()
                                searchString = v.getText().toString().trim();
                                updateResults();
                                return true;
                            }
                        }
                        return false;
                    }
                }
        );

    }

    private void updateResults() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            ProgressBar progressBarView = (ProgressBar) findViewById(R.id.progress_bar);
            progressBarView.setVisibility(View.VISIBLE);
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(1, null, this);
        } else {
            mEmptyStateView.setText(getString(R.string.no_internet));
        }
    }


    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        return new BookLoader(this, searchString, MAX_RESULTS);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {
        ProgressBar progressBarView = (ProgressBar) findViewById(R.id.progress_bar);
        progressBarView.setVisibility(View.GONE);
        mEmptyStateView.setText(getString(R.string.no_results));
        mAdapter.clear();
        if (data != null && !data.isEmpty()) mAdapter.addAll(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();
    }
}
