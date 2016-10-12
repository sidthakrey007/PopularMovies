package com.example.android.popularmovies.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.popularmovies.R;

/**
 * Created by siddharth.thakrey on 22-09-2016.
 */
public class MovieContentProvider extends ContentProvider {

    public static final int POPULAR_MOVIE = 1;
    public static final int FAVOURITE_MOVIE =2 ;
    public static final int TOPRATED_MOVIE = 3;
    public static final int POPULAR_MOVIE_DETAILS = 4;
    public static final int TOPRATED_MOVIE_DETAILS =5;
    public static final int FAVOURITE_MOVIE_DETAILS =6;
    public static final int VIDEOS = 7;
    public static final int VIDEOS_DETAILS =8;
    public static final int REVIEWS = 9;
    public static final int REVIEWS_DETAILS = 10;

    UriMatcher matcher;

    static final String PopularmoviebyID = MovieContract.PopularMovieEntry.MOVIE_ID +"= ?";
    static final String TopRatedmoviebyID = MovieContract.TopRatedMovieEntry.MOVIE_ID+"=?";
    static final String VideoMovieById = MovieContract.VideosEntry.MOVIE_ID+"=?";
    static final String ReviewById= MovieContract.ReviewsEntry.MOVIE_ID+ "=?";

    static  private MovieDBHelper dbHelper=null;

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        Uri insertUri=null;
        int code = matcher.match(uri);
        long id;
        switch (code)
        {
            case POPULAR_MOVIE:
                 id = dbHelper.getWritableDatabase().insert(MovieContract.PopularMovieEntry.TABLE_NAME, null, values);
                 insertUri = MovieContract.PopularMovieEntry.buildMovieUriWithId(id);
                 break;
            case TOPRATED_MOVIE:
                 id = dbHelper.getWritableDatabase().insert(MovieContract.TopRatedMovieEntry.TABLE_NAME,null, values);
                 insertUri = MovieContract.TopRatedMovieEntry.buildMovieUriWithId(id);
                 break;
            case FAVOURITE_MOVIE:
                 id = dbHelper.getWritableDatabase().insert(MovieContract.FavouriteMovieEntry.TABLE_NAME, null, values);
                 insertUri = MovieContract.FavouriteMovieEntry.buildMovieUriWithId(id);
                 break;
            case VIDEOS:
                id = dbHelper.getWritableDatabase().insert(MovieContract.VideosEntry.TABLE_NAME, null, values);
                insertUri = MovieContract.VideosEntry.buildMovieUriWithId(id);
                break;
            case REVIEWS:
                id = dbHelper.getWritableDatabase().insert(MovieContract.ReviewsEntry.TABLE_NAME,null, values);
                insertUri = MovieContract.ReviewsEntry.buildMovieUriWithId(id);
                break;
            default:
                 throw new UnsupportedOperationException("Exception in insertion of record");

        }

        getContext().getContentResolver().notifyChange(uri, null);
        return insertUri;
    }


    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        int code ;
        code = matcher.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count =0;
        long id;
        switch (code) {
            case POPULAR_MOVIE:
                try {
                    db.beginTransaction();
                    for (int i = 0; i < values.length; i++) {
                        id = db.insert(MovieContract.PopularMovieEntry.TABLE_NAME, null, values[i]);
                        if(id != -1)
                            count++;
                    }
                    db.setTransactionSuccessful();
                }
                finally {
                   db.endTransaction();
                }
                break;
            case TOPRATED_MOVIE:
                try {
                    db.beginTransaction();
                    for (int i = 0; i < values.length; i++) {
                        id = db.insert(MovieContract.TopRatedMovieEntry.TABLE_NAME, null, values[i]);
                        if(id != -1)
                            count++;
                    }
                    db.setTransactionSuccessful();
                }
                finally {
                    db.endTransaction();
                }
                break;
            case VIDEOS:
                try {
                    db.beginTransaction();
                    for (int i = 0; i < values.length; i++) {
                        id = db.insert(MovieContract.VideosEntry.TABLE_NAME, null, values[i]);
                        if(id != -1)
                            count++;
                    }
                    db.setTransactionSuccessful();
                }
                finally {
                    db.endTransaction();
                }
                break;

            case REVIEWS:
                try {
                    db.beginTransaction();
                    for (int i = 0; i < values.length; i++) {
                        id = db.insert(MovieContract.ReviewsEntry.TABLE_NAME, null, values[i]);
                        if(id != -1)
                            count++;
                    }
                    db.setTransactionSuccessful();
                }
                finally {
                    db.endTransaction();
                }
                break;

            default:
                return super.bulkInsert(uri, values);


        }
          getContext().getContentResolver().notifyChange(uri, null);
          return count;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        int code = matcher.match(uri);
        String id=null;
        Cursor returned_cursor = null;
        switch (code)
        {
            case POPULAR_MOVIE:
                returned_cursor= dbHelper.getWritableDatabase().query(MovieContract.PopularMovieEntry.TABLE_NAME, projection,selection, selectionArgs, null, null, sortOrder);
                break;
            case POPULAR_MOVIE_DETAILS:
                id = uri.getLastPathSegment();
                returned_cursor= dbHelper.getWritableDatabase().query(MovieContract.PopularMovieEntry.TABLE_NAME, projection, PopularmoviebyID, new String[]{id}, null,null,sortOrder);
                break;
            case TOPRATED_MOVIE:
                returned_cursor= dbHelper.getWritableDatabase().query(MovieContract.TopRatedMovieEntry.TABLE_NAME, projection,selection, selectionArgs, null, null, sortOrder);
                break;
            case TOPRATED_MOVIE_DETAILS:
                id = uri.getLastPathSegment();
                returned_cursor= dbHelper.getWritableDatabase().query(MovieContract.TopRatedMovieEntry.TABLE_NAME, projection, TopRatedmoviebyID, new String[]{id}, null,null,sortOrder);
                break;
            case FAVOURITE_MOVIE:
                returned_cursor= dbHelper.getWritableDatabase().rawQuery("SELECT * FROM " + MovieContract.PopularMovieEntry.TABLE_NAME+" WHERE "+
                        MovieContract.PopularMovieEntry.MOVIE_ID+ " IN (SELECT "+ MovieContract.FavouriteMovieEntry.MOVIE_ID+" FROM "+
                        MovieContract.FavouriteMovieEntry.TABLE_NAME+ " WHERE "+ MovieContract.FavouriteMovieEntry.CATEGORY+"='popular') UNION "+"SELECT * FROM " + MovieContract.TopRatedMovieEntry.TABLE_NAME+" WHERE "+
                        MovieContract.TopRatedMovieEntry.MOVIE_ID+ " IN (SELECT "+ MovieContract.FavouriteMovieEntry.MOVIE_ID+" FROM "+
                        MovieContract.FavouriteMovieEntry.TABLE_NAME + " WHERE "+ MovieContract.FavouriteMovieEntry.CATEGORY+"='top_rated')", null);
                break;
            case VIDEOS:
                returned_cursor= dbHelper.getWritableDatabase().query(MovieContract.VideosEntry.TABLE_NAME, projection,selection, selectionArgs, null, null, sortOrder);
                break;
            case VIDEOS_DETAILS:
                id = uri.getLastPathSegment();
                returned_cursor= dbHelper.getWritableDatabase().query(MovieContract.VideosEntry.TABLE_NAME, projection, VideoMovieById, new String[]{id}, null,null,sortOrder);
                break;
            case FAVOURITE_MOVIE_DETAILS:
                id = uri.getLastPathSegment();
                returned_cursor= dbHelper.getWritableDatabase().rawQuery("SELECT * FROM "+ MovieContract.PopularMovieEntry.TABLE_NAME+" WHERE "+
                        MovieContract.PopularMovieEntry.MOVIE_ID+ "=? UNION SELECT * FROM "+ MovieContract.TopRatedMovieEntry.TABLE_NAME +
                        " WHERE "+ MovieContract.TopRatedMovieEntry.MOVIE_ID +"=?" ,new String[]{id, id});
                break;
            case REVIEWS_DETAILS:
                id = uri.getLastPathSegment();
                returned_cursor = dbHelper.getWritableDatabase().query(MovieContract.ReviewsEntry.TABLE_NAME, projection, ReviewById, new String[]{id},null,null,sortOrder);
                break;
        }
        returned_cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returned_cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int code = matcher.match(uri);
        int rows =0;
        switch (code)
        {
            case POPULAR_MOVIE:
                rows = dbHelper.getWritableDatabase().update(MovieContract.PopularMovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case TOPRATED_MOVIE:
                rows = dbHelper.getWritableDatabase().update(MovieContract.TopRatedMovieEntry.TABLE_NAME, values,selection, selectionArgs);
                break;
            case FAVOURITE_MOVIE:
                rows = dbHelper.getWritableDatabase().update(MovieContract.FavouriteMovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case VIDEOS:
                rows= dbHelper.getWritableDatabase().update(MovieContract.VideosEntry.TABLE_NAME, values,selection, selectionArgs);
                break;
            case REVIEWS:
                rows = dbHelper.getWritableDatabase().update(MovieContract.ReviewsEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Exception raised in update");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rows;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new MovieDBHelper(getContext());
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(MovieContract.AUTHORITY,"/toprated_movie" ,TOPRATED_MOVIE );
        matcher.addURI(MovieContract.AUTHORITY,"/favourite_movie" ,FAVOURITE_MOVIE);
        matcher.addURI(MovieContract.AUTHORITY,"/popular_movie" ,POPULAR_MOVIE);
        matcher.addURI(MovieContract.AUTHORITY,"/toprated_movie/#" ,TOPRATED_MOVIE_DETAILS );
        matcher.addURI(MovieContract.AUTHORITY,"/favourite_movie/#" ,FAVOURITE_MOVIE_DETAILS );
        matcher.addURI(MovieContract.AUTHORITY,"/popular_movie/#" ,POPULAR_MOVIE_DETAILS);
        matcher.addURI(MovieContract.AUTHORITY, "/videos", VIDEOS);
        matcher.addURI(MovieContract.AUTHORITY, "/videos/#", VIDEOS_DETAILS);
        matcher.addURI(MovieContract.AUTHORITY, "/reviews", REVIEWS);
        matcher.addURI(MovieContract.AUTHORITY, "/reviews/#", REVIEWS_DETAILS);
        return false;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int code = matcher.match(uri);
        int rows = 0;
        switch (code)
        {
            case POPULAR_MOVIE:
                dbHelper.getWritableDatabase().execSQL("DELETE FROM "+ MovieContract.PopularMovieEntry.TABLE_NAME+" WHERE "+ MovieContract.PopularMovieEntry.MOVIE_ID+
                " NOT IN (SELECT "+ MovieContract.FavouriteMovieEntry.MOVIE_ID +" FROM "+ MovieContract.FavouriteMovieEntry.TABLE_NAME+" " +
                        "WHERE " + MovieContract.FavouriteMovieEntry.CATEGORY+"='popular');");
                break;
            case TOPRATED_MOVIE:
                 dbHelper.getWritableDatabase().execSQL("DELETE FROM "+ MovieContract.TopRatedMovieEntry.TABLE_NAME+" WHERE "+ MovieContract.TopRatedMovieEntry.MOVIE_ID+
                        " NOT IN (SELECT "+ MovieContract.FavouriteMovieEntry.MOVIE_ID +" FROM "+ MovieContract.FavouriteMovieEntry.TABLE_NAME+
                        " WHERE "+MovieContract.FavouriteMovieEntry.CATEGORY+"='top_rated');");
                break;
            case FAVOURITE_MOVIE:
                rows = dbHelper.getWritableDatabase().delete(MovieContract.FavouriteMovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case VIDEOS:
                rows = dbHelper.getWritableDatabase().delete(MovieContract.VideosEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case REVIEWS:
                rows = dbHelper.getWritableDatabase().delete(MovieContract.ReviewsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.cp_delete_exception_msg));
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rows;
    }
}
