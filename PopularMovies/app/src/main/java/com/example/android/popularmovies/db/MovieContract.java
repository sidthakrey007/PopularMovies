package com.example.android.popularmovies.db;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import java.net.URI;

/**
 * Created by siddharth.thakrey on 21-09-2016.
 */

public class MovieContract {

    public static final String AUTHORITY = "com.example.android.popularmovies";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);

    static public class FavouriteMovieEntry implements BaseColumns
    {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath("favourite_movie").build();

        public static final String TABLE_NAME= "FAVOURITE_MOVIES";

        public static final String MOVIE_ID = "Movie_ID";

        public static final String CATEGORY = "Category";
        public static Uri buildMovieUriWithId(long id)
        {
            Uri appendedIdURI =ContentUris.withAppendedId(CONTENT_URI, id);
            return appendedIdURI;
        }

    }
    static public class TopRatedMovieEntry implements BaseColumns
    {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath("toprated_movie").build();

        public static final String TABLE_NAME= "TOPRATED_MOVIES";

        public static final String MOVIE_ID = "Movie_ID";

        public static final String TITLE = "Title";

        public static final String SYNOPSIS = "Synopsis";

        public static final String RATING = "Rating";

        public static final String RELEASE_DATE = "ReleaseDate";

        public static final String IMAGE_URL = "ImageUrl";


        public static Uri buildMovieUriWithId(long id)
        {
            Uri appendedIdURI =ContentUris.withAppendedId(CONTENT_URI, id);
            return appendedIdURI;
        }

    }
    static public class PopularMovieEntry implements BaseColumns
    {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath("popular_movie").build();

        public static final String TABLE_NAME= "POPULAR_MOVIES";

        public static final String MOVIE_ID = "Movie_ID";

        public static final String TITLE = "Title";

        public static final String SYNOPSIS = "Synopsis";

        public static final String RATING = "Rating";

        public static final String RELEASE_DATE = "ReleaseDate";

        public static final String IMAGE_URL = "ImageUrl";

        public static Uri buildMovieUriWithId(long id)
        {
            Uri appendedIdURI =ContentUris.withAppendedId(CONTENT_URI, id);
            return appendedIdURI;
        }
    }


    static public class VideosEntry implements BaseColumns
    {
        public static final String MOVIE_ID ="Movie_ID";

        public static final String VIDEO_KEY = "Video_key";

        public static final String NAME= "name";

        public static final String TYPE = "type";

        public static final String SIZE = "size";

        public static final String TABLE_NAME = "VIDEOS";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath("videos").build();

        public static Uri buildMovieUriWithId(long id)
        {
            Uri appendedIdURI =ContentUris.withAppendedId(CONTENT_URI, id);
            return appendedIdURI;
        }
    }


    static public class ReviewsEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "REVIEWS";
        public static final String AUTHOR= "author";

        public static final String REVIEW = "review";

        public static final String MOVIE_ID = "Movie_ID";

        public static final Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon().appendPath("reviews").build();

        public static Uri buildMovieUriWithId(long id)
        {
            Uri appendedIdURI =ContentUris.withAppendedId(CONTENT_URI, id);
            return appendedIdURI;
        }
    }


}
