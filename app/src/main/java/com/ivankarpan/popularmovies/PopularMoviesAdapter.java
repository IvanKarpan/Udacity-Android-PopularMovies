package com.ivankarpan.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.model.config.Configuration;
import com.omertron.themoviedbapi.model.movie.MovieInfo;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

/**
 * Created by ivankarpan on 8/13/15.
 */
public class PopularMoviesAdapter extends BaseAdapter {
    private Configuration mConfiguration;
    private Context mContext;
    private List<MovieInfo> mMovies;

    public PopularMoviesAdapter(Context context) {
        mContext = context;
    }

    public int getCount() {
        return mMovies == null ? 0 : mMovies.size();
    }

    public Object getItem(int position) {
        return mMovies.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setAdjustViewBounds(true);
        } else {
            imageView = (ImageView)convertView;
        }

        MovieInfo movie = (MovieInfo) this.getItem(position);
        try {
            URL posterURL = mConfiguration.createImageUrl(movie.getPosterPath(), "w342");
            Picasso.with(mContext).load(posterURL.toString()).placeholder(R.drawable.placeholder).into(imageView);
        } catch (MovieDbException e) {
            e.printStackTrace();
            return null;
        }

        return imageView;
    }

    public void setConfiguration(Configuration configuration) {
        mConfiguration = configuration;
    }

    public void setMovies(List<MovieInfo> movies) {
        mMovies = movies;
    }
}
