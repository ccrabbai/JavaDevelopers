package com.example.android.javadevelopers;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.facebook.drawee.backends.pipeline.Fresco;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Developers>>{

    /** Github URL where our dataset lies */
    private static final String GITHUB_URL =
            "https://api.github.com/search/users?q=location:lagos+type:user+language:java&sort=repositories";

    //Constant value for the developer loader ID.
    //This really only comes into play if you're using multiple loaders.
    private static final int DEVELOPERS_LOADER_ID = 1;

    /** Adapter for the list of earthquakes */
    private DevelopersAdapter mAdapter;

    //Check if there is internet connection
    private boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initialize Fresco
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        // Find a reference to the {@link ListView} in the layout
        ListView developerListView = (ListView) findViewById(R.id.list);

        //Check if there is internet connection
        ConnectivityManager cm =
                (ConnectivityManager) (this).getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        developerListView.setEmptyView(emptyView);

        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new DevelopersAdapter(this, new ArrayList<Developers>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        developerListView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.
        developerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                Developers currentDeveloper = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                String GithubUrl = currentDeveloper.getGithubUrl();
                String ImageUrl = currentDeveloper.getImageUrl();
                String Username = currentDeveloper.getUsername();

                // Create a new intent to view the developers complete activity_profile
                Intent developerIntent = new Intent(MainActivity.this, DevelopersActivity.class);

                //Data that will be sent with the intent
                developerIntent.putExtra("username", Username);
                developerIntent.putExtra("githubUrl", GithubUrl);
                developerIntent.putExtra("imageUrl", ImageUrl);

                startActivity(developerIntent);

                // Send the intent to launch a new activity
                startActivity(developerIntent);
            }
        });

        // Get a reference to the LoaderManager, in order to interact with loaders.
        LoaderManager loaderManager = getLoaderManager();

        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        loaderManager.initLoader(DEVELOPERS_LOADER_ID, null, this);
    }

    @Override
    public Loader<List<Developers>> onCreateLoader(int id, Bundle args) {
        // Create a new loader for the given URL
        return new DevelopersLoader(this, GITHUB_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Developers>> loader, List<Developers> developers) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No earthquakes found."
        TextView emptyTitle = (TextView) findViewById(R.id.empty_title_text);
        TextView emptySubTitle = (TextView) findViewById(R.id.empty_subtitle_text);

        if(!isConnected){
            emptyTitle.setText(R.string.internet);
            emptySubTitle.setText(R.string.empty_view_subtitle);
        }
        else{
            emptyTitle.setText(R.string.empty_view_title);
            emptySubTitle.setText(R.string.empty_view_subtitle);
        }

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (developers != null && !developers.isEmpty()) {
            mAdapter.addAll(developers);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Developers>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}