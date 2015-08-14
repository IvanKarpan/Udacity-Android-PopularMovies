package com.ivankarpan.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MovieDetails extends AppCompatActivity {
    public final static String MOVIE_ID = "com.ivankarpan.popularmovies.MOVIE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
    }
}
