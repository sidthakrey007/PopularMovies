package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MovieDetailsActivity extends AppCompatActivity {


    MovieDetailsFragment detailFragment ;
    String mDetailFragmentKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDetailFragmentKey = getString(R.string.movie_detail_fragment);
        if(savedInstanceState==null) {
            detailFragment = new MovieDetailsFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.details_container, detailFragment).commit();
            getSupportActionBar().show();
            setContentView(R.layout.movie_details);
        }
        else {
        detailFragment =(MovieDetailsFragment) getSupportFragmentManager().getFragment(savedInstanceState, mDetailFragmentKey);
        getSupportFragmentManager().beginTransaction().replace(R.id.details_container, detailFragment);
        setContentView(R.layout.movie_details);
        }
}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        getSupportFragmentManager().putFragment(outState, mDetailFragmentKey, detailFragment);
        super.onSaveInstanceState(outState);
    }
}
