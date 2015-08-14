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
    public enum MoviesSortOrder {
        POPULAR,
        TOP_RATED;
    }

    private MoviesSortOrder mMoviesSortOrder;
    private PopularMoviesAdapter mPopularMoviesAdapter;
    private TheMovieDbApi mTheMovieDbApi;

    private static final int MENU_SORT_BY_POPULARITY = Menu.FIRST;
    private static final int MENU_SORT_BY_RATING = Menu.FIRST + 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        mMoviesSortOrder = MoviesSortOrder.POPULAR;

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_SORT_BY_POPULARITY:
                mMoviesSortOrder = MoviesSortOrder.POPULAR;
                break;

            case MENU_SORT_BY_RATING:
                mMoviesSortOrder = MoviesSortOrder.TOP_RATED;
                break;
        }

        new RefreshMovies().execute();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();

        switch (mMoviesSortOrder) {
            case POPULAR:
                menu.add(0, MENU_SORT_BY_RATING, Menu.NONE, R.string.popular_movies_menu_sort_by_rating);
                break;

            case TOP_RATED:
                menu.add(0, MENU_SORT_BY_POPULARITY, Menu.NONE, R.string.popular_movies_menu_sort_by_popularity);
                break;
        }

        return true;
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
                if (mMoviesSortOrder == MoviesSortOrder.POPULAR) {
                    return mTheMovieDbApi.getPopularMovieList(1, "en").getResults();
                }
                else {
                    return mTheMovieDbApi.getTopRatedMovies(1, "en").getResults();
                }
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
