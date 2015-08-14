package com.ivankarpan.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.model.config.Configuration;
import com.omertron.themoviedbapi.model.movie.MovieInfo;

import java.util.List;

public class PopularMovies extends AppCompatActivity {
    private TheMovieDbApi mTheMovieDbApi;
    private PopularMoviesAdapter mPopularMoviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        try {
            mTheMovieDbApi = new TheMovieDbApi(getString(R.string.tmdb_api_key));
        } catch (MovieDbException e) {
            e.printStackTrace();
        }

        mPopularMoviesAdapter = new PopularMoviesAdapter(this);

        GridView gridview = (GridView) findViewById(R.id.gridView);
        gridview.setAdapter(mPopularMoviesAdapter);

        new InitializeConfiguration().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_popular_movies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class InitializeConfiguration extends AsyncTask<Void, Void, Configuration> {
        @Override
        protected Configuration doInBackground(Void... arg0) {
            try {
                return mTheMovieDbApi.getConfiguration();
            } catch (MovieDbException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Configuration configuration) {
            mPopularMoviesAdapter.setConfiguration(configuration);

            new RefreshMovies().execute();
        }
    }

    private class RefreshMovies extends AsyncTask<Void, Void, List<MovieInfo>> {
        @Override
        protected List<MovieInfo> doInBackground(Void... arg0) {
            try {
                return mTheMovieDbApi.getPopularMovieList(1, "en").getResults();
            } catch (MovieDbException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<MovieInfo> movies) {
            mPopularMoviesAdapter.setMovies(movies);
            mPopularMoviesAdapter.notifyDataSetChanged();
        }
    }
}
