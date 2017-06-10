package com.example.android.booklistingapp;

import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    private static final String QUERY_KEY = "query";
    private static final String MAX_RESULTS_KEY = "maxResults";
    private static final int LOADER_ID = 1;
    private static final int MAX_RESULTS = 20;
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

        if (getIntent() != null) {
            handleIntent(getIntent());
        }
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            Bundle bundle = new Bundle();
            bundle.putString(QUERY_KEY, query);
            bundle.putInt(MAX_RESULTS_KEY, MAX_RESULTS);

            performSearch(bundle);
        }
    }

    private void performSearch(Bundle bundle) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            ProgressBar progressBarView = (ProgressBar) findViewById(R.id.progress_bar);
            progressBarView.setVisibility(View.VISIBLE);
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(LOADER_ID, bundle, this);
        } else {
            mEmptyStateView.setText(getString(R.string.no_internet));
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        String query = args.getString(QUERY_KEY);
        int maxResults = args.getInt(MAX_RESULTS_KEY);
        return new BookLoader(this, query, maxResults);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }
}
