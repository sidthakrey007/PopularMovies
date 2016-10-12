package com.example.android.popularmovies;

import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.db.MovieContract;

/* * A placeholder fragment containing a simple view.
 */

public class MovieDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    static long movie_id =0;
    static int position=0;
    static Uri mdetail_uri = null;
    static final int LOAD_VIDEOS = 2;
    static final int LOAD_REVIEWS =3;
    static final int LOAD_DETAILS = 4;
    MyListCursorAdapter adapter = null;
    ReviewListCursorAdapter reviewadapter = null;
    static final String[] video_projection =
            {
                    MovieContract.VideosEntry.VIDEO_KEY,
                    MovieContract.VideosEntry.TYPE,
                    MovieContract.VideosEntry.SIZE,
                    MovieContract.VideosEntry.NAME,
                    MovieContract.VideosEntry.MOVIE_ID
            };

    static final int _IID =0;
    static final int IVIDEO_KEY=1;
    static final int ITYPE=2;
    static final int ISIZE=3;
    static final int INAME=4;
    static final int IMOVIE_ID=5;

    static final String[] review_projection={
            MovieContract.ReviewsEntry._ID,
            MovieContract.ReviewsEntry.MOVIE_ID,
            MovieContract.ReviewsEntry.AUTHOR,
            MovieContract.ReviewsEntry.REVIEW
    };

    static final int _RID=0;
    static final int RMOVIE_ID=1;
    static final int AUTHOR=2;
    static final int REVIEW=3;

    public MovieDetailsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        adapter = new MyListCursorAdapter(getActivity(), null);
        reviewadapter = new ReviewListCursorAdapter(getActivity(), null);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View movieDetailView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        RecyclerView l = (RecyclerView) movieDetailView.findViewById(R.id.trailers);
        RecyclerView r = (RecyclerView) movieDetailView.findViewById(R.id.reviews);

        r.setAdapter(reviewadapter);
        LinearLayoutManager lmr = new LinearLayoutManager(getActivity());
        lmr.setOrientation(LinearLayoutManager.VERTICAL);
        r.setLayoutManager(lmr);
        l.setAdapter(adapter);
        LinearLayoutManager lm= new LinearLayoutManager(getActivity());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        l.setLayoutManager(lm);
        Bundle b = this.getArguments();
        if(b==null) {
            Intent message = getActivity().getIntent();
            mdetail_uri = Uri.parse(message.getStringExtra(getString(R.string.uri)));
            position = getActivity().getIntent().getIntExtra(getString(R.string.position), -1);
            movie_id = Long.valueOf(mdetail_uri.getLastPathSegment());
        }
        else
        {
            String uri = b.getString(getString(R.string.uri));
            position = b.getInt(getString(R.string.position));
            mdetail_uri = Uri.parse(uri);
            movie_id = Long.valueOf(mdetail_uri.getLastPathSegment());

        }
        final GestureDetector mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {

            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });
       l.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                                    @Override
                                    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                                        View child = rv.findChildViewUnder(e.getX(),e.getY());



                                        if(child!=null && mGestureDetector.onTouchEvent(e)){

                                            ImageButton b = (ImageButton) rv.findViewById(R.id.play_button);
                                            String video_key =(String)b.getTag();
                                            Intent message = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.youtube_uri)+video_key));
                                            startActivity(message);
                                            return true;

                                        }
                                        return false;
                                    }

                                    @Override
                                    public void onTouchEvent(RecyclerView rv, MotionEvent e) {



                                    }

                                    @Override
                                    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                                    }
                                }
       );
        return movieDetailView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if(getActivity().findViewById(R.id.article_fragment)!=null|| getActivity().findViewById(R.id.details_container)!=null) {
            getLoaderManager().initLoader(LOAD_DETAILS, null, this);
            getLoaderManager().initLoader(LOAD_VIDEOS, null, this);
            getLoaderManager().initLoader(LOAD_REVIEWS, null, this);



        }super.onActivityCreated(savedInstanceState);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri detail_uri;
        CursorLoader cl=null;
        switch (id) {
            case LOAD_VIDEOS:
                 detail_uri= MovieContract.VideosEntry.buildMovieUriWithId(movie_id);
                 cl= new CursorLoader(getActivity(), detail_uri
                        , null, null, null, null);
                 break;
            case LOAD_REVIEWS:
                detail_uri= MovieContract.ReviewsEntry.buildMovieUriWithId(movie_id);
                cl= new CursorLoader(getActivity(), detail_uri
                        , review_projection, null, null, null);
                break;
            case LOAD_DETAILS:
         cl = new CursorLoader(getActivity(), mdetail_uri, null,
                        null,null,null);
                break;

            }
        return cl;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        int id = loader.getId();
        switch (id) {
            case LOAD_REVIEWS:
                reviewadapter.swapCursor(data);
                break;
            case LOAD_VIDEOS:
                adapter.swapCursor(data);
                break;
            case LOAD_DETAILS:
                if(data.getCount()>0) {
                    data.moveToFirst();
                    int idx = data.getColumnIndex(MovieContract.PopularMovieEntry.TITLE);
                    TextView titleTextView = (TextView) getActivity().findViewById(R.id.title);
                    titleTextView.setText(data.getString(idx));

                    idx = data.getColumnIndex(MovieContract.PopularMovieEntry.RATING);
                    TextView ratingTextView = (TextView) getActivity().findViewById(R.id.rating);
                    ratingTextView.setText(data.getString(idx) + getString(R.string.rate_on_ten));

                    idx = data.getColumnIndex(MovieContract.PopularMovieEntry.RELEASE_DATE);
                    TextView releaseDateTextView = (TextView) getActivity().findViewById(R.id.releasedate);
                    releaseDateTextView.setText(data.getString(idx));
                    Button b = (Button) getActivity().findViewById(R.id.favourite_button);

                    if(Utility.getSortedSetting(getActivity()).equals(getString(R.string.favourite_preference_key)))

                    {
                        b.setVisibility(View.INVISIBLE);
                    }
                    else
                    b.setVisibility(View.VISIBLE);
                    idx = data.getColumnIndex(MovieContract.PopularMovieEntry.SYNOPSIS);
                    TextView overviewTextView = (TextView) getActivity().findViewById(R.id.overview);
                    overviewTextView.setText(data.getString(idx));

                    idx = data.getColumnIndex(MovieContract.PopularMovieEntry.IMAGE_URL);
                    String url = data.getString(idx);
                    String image_path = null;
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT && getResources().getBoolean(R.bool.isTab) == false) {
                        image_path = getString(R.string.image_size) + url.substring(1);
                    } else
                        image_path = getString(R.string.image_size) + url.substring(1);
                    ImageView posterImage = (ImageView) getActivity().findViewById(R.id.poster);
                    Utility.getImageBitmap(getActivity(), image_path, Utility.getSortedSetting(getActivity()), posterImage);
                    b = (Button) getActivity().findViewById(R.id.favourite_button);
                    b.setTag(R.string.but_image_name_tag, getString(R.string.image_size) + url.substring(1));
                    b.setTag(R.string.but_setting_tag, Utility.getSortedSetting(getActivity()));
                    b.setTag(R.string.but_movie_id_tag, movie_id);
                    //posterImage.setImageBitmap(bm);
                    posterImage.setAdjustViewBounds(true);
                }
        }
    }





    class Dummy
    {
        int a;
        int b;
    }
}


