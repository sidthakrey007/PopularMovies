package com.example.android.popularmovies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by siddharth.thakrey on 21-09-2016.
 */
public class MovieDBHelper extends SQLiteOpenHelper {

    final static int DATABASE_VERSION = 2;
    final static String DATABASE_NAME = "Movie.db";
    MovieDBHelper(Context c)
    {
        super(c, DATABASE_NAME,null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        final String create_popular_movieDB_query = "CREATE TABLE POPULAR_MOVIES ("+

                MovieContract.PopularMovieEntry._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                MovieContract.PopularMovieEntry.TITLE+ " TEXT NOT NULL,"+
                MovieContract.PopularMovieEntry.MOVIE_ID + " INTEGER NOT NULL UNIQUE, "+
                MovieContract.PopularMovieEntry.IMAGE_URL+ " TEXT NOT NULL,"+
                MovieContract.PopularMovieEntry.RATING+ " REAL NOT NULL,"+
                MovieContract.PopularMovieEntry.RELEASE_DATE+" TEXT NOT NULL,"+
                MovieContract.PopularMovieEntry.SYNOPSIS+" TEXT NOT NULL);";

        final String create_toprated_movieDB_query = "CREATE TABLE TOPRATED_MOVIES ("+

                MovieContract.TopRatedMovieEntry._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                MovieContract.TopRatedMovieEntry.TITLE+ " TEXT NOT NULL,"+
                MovieContract.TopRatedMovieEntry.MOVIE_ID + " INTEGER NOT NULL UNIQUE, "+
                MovieContract.TopRatedMovieEntry.IMAGE_URL+ " TEXT NOT NULL,"+
                MovieContract.TopRatedMovieEntry.RATING+ " REAL NOT NULL,"+
                MovieContract.TopRatedMovieEntry.RELEASE_DATE+" TEXT NOT NULL,"+
                MovieContract.TopRatedMovieEntry.SYNOPSIS+ " TEXT NOT NULL);";

        final String create_favouriteDB_query = "CREATE TABLE FAVOURITE_MOVIES ("+
                MovieContract.FavouriteMovieEntry._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                MovieContract.FavouriteMovieEntry.MOVIE_ID+ " INTEGER NOT NULL UNIQUE," +
                MovieContract.FavouriteMovieEntry.CATEGORY+ " TEXT NOT NULL);";


        final String create_videoDB_query = "CREATE TABLE VIDEOS ("+
                MovieContract.VideosEntry._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                MovieContract.VideosEntry.MOVIE_ID+" INTEGER NOT NULL,"+
                MovieContract.VideosEntry.NAME + " TEXT NOT NULL,"+
                MovieContract.VideosEntry.SIZE + " INTEGER NOT NULL,"+
                MovieContract.VideosEntry.TYPE+ " TEXT NOT NULL,"+
                MovieContract.VideosEntry.VIDEO_KEY+ " TEXT NOT NULL);";

        final String created_reviewDB_query = "CREATE TABLE REVIEWS ("+
                MovieContract.ReviewsEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                MovieContract.ReviewsEntry.MOVIE_ID+" INTEGER NOT NULL,"+
                MovieContract.ReviewsEntry.AUTHOR+" TEXT NOT NULL, "+
                MovieContract.ReviewsEntry.REVIEW+" TEXT NOT NULL);";

        db.execSQL(create_popular_movieDB_query);
        db.execSQL(create_toprated_movieDB_query);
        db.execSQL(create_favouriteDB_query);
        db.execSQL(create_videoDB_query);
        db.execSQL(created_reviewDB_query);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        getWritableDatabase().execSQL("DROP TABLE IF EXISTS "+ MovieContract.PopularMovieEntry.TABLE_NAME);
        getWritableDatabase().execSQL("DROP TABLE IF EXISTS "+ MovieContract.TopRatedMovieEntry.TABLE_NAME);
        getWritableDatabase().execSQL("DROP TABLE IF EXISTS "+ MovieContract.FavouriteMovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
