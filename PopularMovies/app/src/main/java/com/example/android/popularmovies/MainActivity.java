package com.example.android.popularmovies;
import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.android.popularmovies.db.MovieContract;
import com.example.android.popularmovies.service.SyncAdapter;

import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity implements MoviesFragment.DetailCallback{

    static long MID = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           setContentView(R.layout.activity_main);
          SyncAdapter.initializeSyncAdapter(this);

    }


    @Override
    public void onItemSelected(Uri detailURI, int position) {
        MovieDetailsFragment df = new MovieDetailsFragment();
        Bundle b = new Bundle();
        MID = Long.valueOf(detailURI.getLastPathSegment());
        b.putString(getString(R.string.uri), detailURI.toString());
        b.putInt(getString(R.string.position), position);
        df.setArguments(b);
        getSupportFragmentManager().beginTransaction().replace(R.id.tab_detail_container, df, "DETAIL").commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        onSaveInstanceState(new Bundle());
        super.onPause();
    }



    @Override
    protected void onStop() {
        super.onStop();
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

