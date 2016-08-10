package com.example.android.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    MoviesFragment internalFragment = null;
    String MOVIE_FRAGMENT_KEY;
    //During creation of activity we are making
    // activity single top and maintaining isPreference change key
    // in shared preference which is boolean and
    // which tells wheather to load this activity
    // again based on change of preference of sorting
    @Override
    protected void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
           MOVIE_FRAGMENT_KEY= getString(R.string.movie_thumbnail_fragment);

        if(savedInstanceState==null) {
           // if this activity is created first time the set preference change key as true
           //so that activity will reload the content
            pref.edit().putBoolean(getString(R.string.preference_change_key), true).commit();
            FragmentTransaction transactionManager = getSupportFragmentManager().beginTransaction();
            internalFragment = new MoviesFragment();
            transactionManager.replace(android.R.id.content, internalFragment).commit();
        }
        else
        {
            //else reload the same fragment
            internalFragment = (MoviesFragment) getSupportFragmentManager().getFragment(savedInstanceState, MOVIE_FRAGMENT_KEY);
            getSupportFragmentManager().beginTransaction().replace(android.R.id.content, internalFragment).commit();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, MOVIE_FRAGMENT_KEY, internalFragment);

    }

    @Override
    protected void onPause() {
        onSaveInstanceState(new Bundle());
        super.onPause();
    }
}
