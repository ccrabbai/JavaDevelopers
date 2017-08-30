package com.example.android.javadevelopers;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;


/**
 * Loads a list of developers by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class DevelopersLoader extends AsyncTaskLoader<List<Developers>> {

    /** Tag for log messages */
    private static final String LOG_TAG = DevelopersLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link DevelopersLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public DevelopersLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "onStartLoading called");
        forceLoad();
    }

    //This is on a background thread.
    @Override
    public List<Developers> loadInBackground() {
        Log.i(LOG_TAG, "loadInBackground: called");
        if (mUrl == null) { return null; }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<Developers> developers = QueryGit.fetchDeveloperData(mUrl);
        return developers;
    }
}