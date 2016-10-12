package com.example.android.popularmovies;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.android.popularmovies.db.MovieContract;
import com.squareup.picasso.Picasso;

import java.io.File;
import android.support.v4.app.Fragment;
import android.widget.Spinner;
import android.widget.TextView;

public class MoviesFragment extends Fragment implements LoaderCallbacks<Cursor> {
    private ImageAdapter posterImageAdapter = null;
    int check =0;
    DetailCallback detailCallback;
    static Cursor mCursor =null;
    static int spinner_position = 0;
    static final int POPULAR_MOVIES_LOAD = 1;
    static final int TOPRATED_MOVIES_LOAD = 2;
    static final int FAVOURITE_MOVIES_LOAD = 3;
    public interface DetailCallback
    {
        public void onItemSelected(Uri detailURI, int position);
    }


    static  final String[] popularmovie_projection =
            {
                    MovieContract.PopularMovieEntry._ID,
                    MovieContract.PopularMovieEntry.MOVIE_ID,
                    MovieContract.PopularMovieEntry.IMAGE_URL,
                    MovieContract.PopularMovieEntry.RATING,
                    MovieContract.PopularMovieEntry.RELEASE_DATE,
                    MovieContract.PopularMovieEntry.SYNOPSIS,
                    MovieContract.PopularMovieEntry.TITLE,

            };

    int IMOVIE_ID=1;
    int IIMAGE_URL=2;
    int IRATING=3;
    int IRELEASE_DATE=4;
    int ISYNOPSIS=5;
    int ITITLE=6;



    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        final int rotation = getResources().getConfiguration().orientation;
        final Intent message = getActivity().getIntent();
        TextView t =(TextView) getActivity().findViewById(R.id.nomoviemessage);
        ImageView i = (ImageView) getActivity().findViewById(R.id.sorry);
        int id = loader.getId();
        switch (id)
        {
            case POPULAR_MOVIES_LOAD:
                t = (TextView) getActivity().findViewById(R.id.nomoviemessage);
                if(Utility.getSortedSetting(getActivity()).equals(getString(R.string.popular_preference_key))) {
                    if(data.getCount()==0) {
                        t.setText(getString(R.string.no_fav_movie_message));
                        posterImageAdapter.swapCursor(null);
                        return;
                    }
                    t.setVisibility(View.INVISIBLE);
                    i.setVisibility(View.INVISIBLE);
                    posterImageAdapter.swapCursor(data);
                    mCursor = data;
                    data.moveToFirst();
                    GridView g =(GridView)getActivity().findViewById(R.id.movie_thumbnail_view);
                    g.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                             // Save a local reference rather than calling `getListView()` three times
                            if( rotation==Configuration.ORIENTATION_LANDSCAPE && (message!=null &&
                                    message.getIntExtra(getString(R.string.position),-9)!=-9 ) )
                                doNext2();
                            else {
                                if (rotation == Configuration.ORIENTATION_LANDSCAPE &&
                                        getActivity().findViewById(R.id.tab_detail_container) != null &&
                                        ((message != null && message.getIntExtra(getString(R.string.position), -9) == -9) || (message == null)))
                                    doNext();
                            }}
                    }, 0);

                }break;
            case TOPRATED_MOVIES_LOAD:

                if(Utility.getSortedSetting(getActivity()).equals(getString(R.string.toprated_preference_key))) {
                    if(data.getCount()==0) {
                        t.setText(getString(R.string.no_fav_movie_message));
                        posterImageAdapter.swapCursor(null);
                        return;
                    }
                    i.setVisibility(View.INVISIBLE);
                    t.setVisibility(View.INVISIBLE);
                    posterImageAdapter.swapCursor(data);
                    mCursor = data;
                    data.moveToFirst();
                    GridView g =(GridView)getActivity().findViewById(R.id.movie_thumbnail_view);
                    g.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if( rotation==Configuration.ORIENTATION_LANDSCAPE && (message!=null &&  message.getIntExtra(getString(R.string.position),-9)!=-9 ) )
                                doNext2();
                            else {
                                if (rotation == Configuration.ORIENTATION_LANDSCAPE && getActivity().findViewById(R.id.tab_detail_container) != null &&
                                        ((message != null && message.getIntExtra(getString(R.string.position), -9) == -9) || (message == null)))
                                    doNext();
                            }}
                    },0);
                }

                break;
            case FAVOURITE_MOVIES_LOAD:

                if(Utility.getSortedSetting(getActivity()).equals(getString(R.string.favourite_preference_key))) {

                    if(data.getCount()==0) {
                        t.setText(getString(R.string.no_fav_movie_message));
                        t.setVisibility(View.VISIBLE);
                        i.setVisibility(View.VISIBLE);
                        posterImageAdapter.swapCursor(null);
                        return;
                    }
                    t.setVisibility(View.INVISIBLE);
                    i.setVisibility(View.INVISIBLE);
                    posterImageAdapter.swapCursor(data);
                    mCursor = data;
                    data.moveToFirst();
                    GridView g =(GridView)getActivity().findViewById(R.id.movie_thumbnail_view);
                    g.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if( rotation==Configuration.ORIENTATION_LANDSCAPE && (message!=null &&  message.getIntExtra(getString(R.string.position),-9)!=-9 ) )
                                doNext2();
                            else {
                                if (rotation == Configuration.ORIENTATION_LANDSCAPE && getActivity().findViewById(R.id.tab_detail_container) != null &&
                                        ((message != null && message.getIntExtra(getString(R.string.position), -9) == -9) || (message == null)))
                                    doNext();
                            }}
                    }, 0);
                }

                break;

        }





    }

    public void doNext()
    {

        GridView g = (GridView) getActivity().findViewById(R.id.movie_thumbnail_view);
        g.getOnItemClickListener().onItemClick(g, null, 0, 1);
        g.setSelection(0);
    }

    public void doNext2()
    {

        {
            GridView g = (GridView) getActivity().findViewById(R.id.movie_thumbnail_view);
            g.getOnItemClickListener().onItemClick(g, null, getActivity().getIntent().getIntExtra(getString(R.string.position),-9), 1);
            g.setSelection(getActivity().getIntent().getIntExtra(getString(R.string.position),-9));
            getActivity().setIntent(null);

        }}



    @Override
    public void onAttach(Activity activity) {

         detailCallback = (DetailCallback) activity;
        super.onAttach(activity);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id)
        {

            case POPULAR_MOVIES_LOAD:
                CursorLoader cl= new CursorLoader(getActivity(), MovieContract.PopularMovieEntry.CONTENT_URI,popularmovie_projection
                        ,null,null,null);
                return cl;

            case TOPRATED_MOVIES_LOAD:
                CursorLoader cl2= new CursorLoader(getActivity(), MovieContract.TopRatedMovieEntry.CONTENT_URI,popularmovie_projection
                        ,null,null,null);
                return cl2;
            case FAVOURITE_MOVIES_LOAD:
                CursorLoader cl3 = new CursorLoader(getActivity(), MovieContract.FavouriteMovieEntry.CONTENT_URI, null
                        , null,null,null);
                return cl3;
        }
         return null;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        posterImageAdapter.swapCursor(null);
    }

    public MoviesFragment() {
        // Required empty public constructor
    }

    /*method which created fragment and initializes the image adapter */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        String pref = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(getString(R.string.sort_preference_key), getString(R.string.popular_preference_key));
        check =0;

        {
            if(pref.equals(getString(R.string.popular_preference_key)))
            {
                spinner_position = 0;

            }
            else
            {if(pref.equals(getString(R.string.popular_preference_key)))
                {
                    spinner_position =1;
                }
             if(pref.equals(getString(R.string.popular_preference_key)))
            {       spinner_position =2;
             }
            }
        }


        posterImageAdapter = new ImageAdapter(getContext(), null);
        super.onCreate(savedInstanceState);


    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Loader<Cursor> loader = getLoaderManager().getLoader(POPULAR_MOVIES_LOAD);

        if (loader != null && loader.isReset()) {
            getLoaderManager().restartLoader(POPULAR_MOVIES_LOAD, null,this);
        } else {
            getLoaderManager().initLoader(POPULAR_MOVIES_LOAD, null, this);
        }
        loader = getLoaderManager().getLoader(TOPRATED_MOVIES_LOAD);

        if (loader != null && loader.isReset()) {
            getLoaderManager().restartLoader(TOPRATED_MOVIES_LOAD, null,this);
        } else {
            getLoaderManager().initLoader(TOPRATED_MOVIES_LOAD, null, this);
        }
         loader = getLoaderManager().getLoader(FAVOURITE_MOVIES_LOAD);
        if (loader != null && loader.isReset()) {
            getLoaderManager().restartLoader(FAVOURITE_MOVIES_LOAD, null,this);
        } else {
            getLoaderManager().initLoader(FAVOURITE_MOVIES_LOAD, null, this);
        }


            super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_settings, menu);
        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.SortBy_Entries, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                     int code = position;
                        spinner_position = position;
                        SharedPreferences.Editor e;
                     SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        switch (code)
                        {
                            case 0:
                                e = sp.edit().putString(getString(R.string.sort_preference_key), getString(R.string.popular_preference_key));
                                e.commit();
                                check++;

                                if(check>1) {
                                    restartLoad(POPULAR_MOVIES_LOAD);
                                }
                                break;
                            case 1:
                                e = sp.edit().putString(getString(R.string.sort_preference_key), getString(R.string.toprated_preference_key));
                                e.commit();
                                check++;
                                if(check>1) {
                                    restartLoad(TOPRATED_MOVIES_LOAD);
                                    }
                                break;
                            case 2:
                                e = sp.edit().putString(getString(R.string.sort_preference_key), getString(R.string.favourite_preference_key));
                                e.commit();
                                check++;
                                if(check>1) {
                                    restartLoad(FAVOURITE_MOVIES_LOAD);
                                }break;
                        }

                    }


                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {


                    }
                }

        );
        spinner.setSelection(spinner_position);
        super.onCreateOptionsMenu(menu, inflater);

    }



    void restartLoad(int id)
    {
        Loader<Cursor> loader = getLoaderManager().getLoader(id);

        if (loader != null && loader.isReset()) {
            getLoaderManager().restartLoader(id, null,this);
        } else {
            getLoaderManager().initLoader(id, null, this);
        }
    }



    @Override
    public void onResume() {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        super.onResume();
    }

    void createNetworkErrorDialogue() {
        String alertTitle=getString(R.string.network_dialog_title);
        String alertMessage = getString(R.string.network_dialog_message);
        AlertDialog.Builder networkErrorDialog = new AlertDialog.Builder(getActivity());
        networkErrorDialog.setTitle(alertTitle);
        networkErrorDialog.setMessage(alertMessage);
        networkErrorDialog.setPositiveButton(getString(R.string.okButtonTitle), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {dialog.dismiss();

            }
        });
        networkErrorDialog.create().show();
    }
    boolean isDeviceOnline()
    {
        ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork= connManager.getActiveNetworkInfo();
        return (activeNetwork!=null && activeNetwork.isConnected());
    }









    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final String URI = getString(R.string.uri);
        View movieFragmentView = inflater.inflate(R.layout.fragment_movies, container, false);
        GridView posterGridView = (GridView) movieFragmentView.findViewById(R.id.movie_thumbnail_view);
        posterGridView.setAdapter(posterImageAdapter);
        posterGridView = (GridView) movieFragmentView.findViewById(R.id.movie_thumbnail_view);

        posterGridView.setAdapter(posterImageAdapter);
        posterGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mCursor==null)
                    return;
                mCursor.moveToPosition(position);
                String movie_id = mCursor.getString(IMOVIE_ID);
                Intent movieDetailIntent = new Intent(getActivity(), MovieDetailsActivity.class);
                Uri detailuri=null;
                if(Utility.getSortedSetting(getActivity()).equals(getString(R.string.popular_preference_key))) {
                    detailuri = MovieContract.PopularMovieEntry.buildMovieUriWithId(Long.valueOf(movie_id));
                    movieDetailIntent.putExtra(URI, detailuri.toString());
                    movieDetailIntent.putExtra(getString(R.string.position),position);
                }
                else {
                    if (Utility.getSortedSetting(getActivity()).equals(getString(R.string.toprated_preference_key))) {
                        detailuri = MovieContract.TopRatedMovieEntry.buildMovieUriWithId(Long.valueOf(movie_id));
                        movieDetailIntent.putExtra(URI, detailuri.toString());
                        movieDetailIntent.putExtra(getString(R.string.position),position);
                    } else {
                        int idx = mCursor.getColumnIndex(MovieContract.FavouriteMovieEntry.MOVIE_ID);
                        movie_id = mCursor.getString(idx);
                        detailuri = MovieContract.FavouriteMovieEntry.buildMovieUriWithId(Long.valueOf(movie_id));
                        movieDetailIntent.putExtra(URI, detailuri.toString());
                        movieDetailIntent.putExtra(getString(R.string.position),position);
                    }
                }

                if(getActivity().findViewById(R.id.tab_detail_container)!=null)
                {
                   detailCallback.onItemSelected(detailuri, position);
                }
                else
                startActivity(movieDetailIntent);
            }
        });

        return movieFragmentView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }



    @Override
    public void onPause() {
        onSaveInstanceState(new Bundle());
        super.onPause();
    }



    public class ImageAdapter extends CursorAdapter {
        public ImageAdapter(Context ctx,  Cursor crsr) {
            super(ctx, crsr, false);
         }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ImageView imageView = (ImageView) view.findViewById(R.id.movie_poster);
            int imageurl_index = cursor.getColumnIndex(MovieContract.PopularMovieEntry.IMAGE_URL);
            String setting = Utility.getSortedSetting(getActivity());
            if(getActivity().getResources().getBoolean(R.bool.isTab)==true)
               Utility.getImageBitmap(getActivity(), getString(R.string.image_size)+cursor.getString(imageurl_index).substring(1), setting, imageView);
            else
            {
                try{

                    File f1 = getActivity().getFilesDir();
                    File f2 =  new File(f1,getString(R.string.slash)+setting+getString(R.string.slash)+getString(R.string.image_size)
                            +cursor.getString(imageurl_index).substring(1));
                    Picasso.with(getActivity()).load(f2).into(imageView);
                    return;
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(0,0,0,0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.movie_poster_view, parent, false);
            return v;
        }

    }


}

