package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.popularmovies.db.MovieContract;
import com.example.android.popularmovies.service.SyncAdapter;

import java.io.FileNotFoundException;

public class MovieDetailsActivity extends AppCompatActivity {


    MovieDetailsFragment detailFragment ;
    String mDetailFragmentKey;
    int position = -9;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDetailFragmentKey = getString(R.string.movie_detail_fragment);
        if(getIntent()!=null)
        {
            position = getIntent().getIntExtra(getString(R.string.position), 1);
        }
        if(getResources().getConfiguration().orientation!=Configuration.ORIENTATION_PORTRAIT && getResources().getBoolean(R.bool.isTab)==true)

        {

            Intent message = new Intent(this, MainActivity.class);

            MovieDetailsFragment md = (MovieDetailsFragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.detail_frag_tag));
            message.putExtra(getString(R.string.position), md.position);
            startActivity(message);
            finish();


        }

        if(savedInstanceState==null) {
            detailFragment = new MovieDetailsFragment();
            setContentView(R.layout.movie_details);
            getSupportFragmentManager().beginTransaction().replace(R.id.details_container, detailFragment, getString(R.string.detail_frag_tag)).commit();
            getSupportActionBar().show();
          }
        else {
            setContentView(R.layout.movie_details);
            detailFragment =(MovieDetailsFragment) getSupportFragmentManager().getFragment(savedInstanceState, mDetailFragmentKey);
        getSupportFragmentManager().beginTransaction().replace(R.id.details_container, detailFragment).commit();
        }
}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        getSupportFragmentManager().putFragment(outState, mDetailFragmentKey, detailFragment);
        super.onSaveInstanceState(outState);
    }


    void saveFavourite(View v) throws FileNotFoundException {
        Button fav_button = (Button) findViewById(R.id.favourite_button);
        Long movie_id = (Long) fav_button.getTag(R.string.but_movie_id_tag);
        String setting =  (String) fav_button.getTag(R.string.but_setting_tag);
        String file_name = (String) fav_button.getTag(R.string.but_image_name_tag);
        ContentValues cv = new ContentValues();
        cv.put(MovieContract.FavouriteMovieEntry.MOVIE_ID, movie_id);
        cv.put(MovieContract.FavouriteMovieEntry.CATEGORY, setting);
        getContentResolver().insert(MovieContract.FavouriteMovieEntry.CONTENT_URI,cv );
          Utility.copyImageBitmap(this, file_name, setting, getString(R.string.favourite_preference_key));
        Toast t = Toast.makeText(this,getString(R.string.toast_message_fav_movie_save),Toast.LENGTH_SHORT);

        t.show();
    }

}
