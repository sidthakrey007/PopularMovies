package com.example.android.popularmovies.service;

/**
 * Created by siddharth.thakrey on 05-10-2016.
 */

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.android.popularmovies.BuildConfig;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.db.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

public class SyncAdapter extends AbstractThreadedSyncAdapter {
    ContentResolver mContentResolver;
    Vector<Integer> Movieid_array =null;

    public static final int SYNC_INTERVAL = 720*60;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Movieid_array = new Vector<Integer>();
        URL url = null;
        final String RESULTS = getContext().getString(R.string.result_key);
        final String API_KEY = getContext().getString(R.string.apiKeyQueryStringParameter);
        URLConnection connection = null;
        JSONArray movie_details = null;

        try {


            Uri.Builder uri = Uri.parse(BuildConfig.MOVIE_DB_API).buildUpon();
            String category = getContext().getString(R.string.popular_preference_key);
            uri.appendPath(category);
            uri.appendQueryParameter(API_KEY, BuildConfig.MOVIE_DB_API_KEY);
            String url_string = uri.build().toString();
            url = new URL(url_string);
            connection = url.openConnection();
            connection.setDoInput(true);
            InputStream is = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            StringBuffer buffer = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }

            JSONObject json_response = new JSONObject(buffer.toString());
            movie_details = json_response.getJSONArray(RESULTS);
            extractMovieDetails(movie_details, getContext().getString(R.string.popular_preference_key));





            uri = Uri.parse(BuildConfig.MOVIE_DB_API).buildUpon();
            category = getContext().getString(R.string.toprated_preference_key);
            uri.appendPath(category);
            uri.appendQueryParameter(API_KEY, BuildConfig.MOVIE_DB_API_KEY);
            url_string = uri.build().toString();
            url = new URL(url_string);
            connection = url.openConnection();
            connection.setDoInput(true);
            is = connection.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            buffer = new StringBuffer();
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }

            json_response = new JSONObject(buffer.toString());
            movie_details = json_response.getJSONArray(RESULTS);
            extractMovieDetails(movie_details, getContext().getString(R.string.toprated_preference_key));

            for(int i=0; i<Movieid_array.size();i++)
            {

                uri = Uri.parse(BuildConfig.MOVIE_DB_API).buildUpon();
                uri.appendPath(String.valueOf(Movieid_array.toArray()[i]));
                uri.appendPath("videos");
                uri.appendQueryParameter(API_KEY, BuildConfig.MOVIE_DB_API_KEY);
                url_string = uri.build().toString();
                url = new URL(url_string);
                connection = url.openConnection();
                connection.setDoInput(true);
                is = connection.getInputStream();
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);
                buffer = new StringBuffer();
                while ((line = br.readLine()) != null) {
                    buffer.append(line);
                }

                json_response = new JSONObject(buffer.toString());
                movie_details = json_response.getJSONArray(RESULTS);
                Vector<ContentValues> cv_array = new Vector<ContentValues>();
                for(int j=0; j<movie_details.length();j++) {
                    ContentValues cv = new ContentValues();
                    JSONObject video_details = movie_details.getJSONObject(j);
                    cv.put(MovieContract.VideosEntry.MOVIE_ID, String.valueOf(Movieid_array.toArray()[i]));
                    cv.put(MovieContract.VideosEntry.VIDEO_KEY, video_details.getString("key"));
                    cv.put(MovieContract.VideosEntry.SIZE, video_details.getString("size"));
                    cv.put(MovieContract.VideosEntry.NAME,video_details.getString("name") );
                    cv.put(MovieContract.VideosEntry.TYPE, video_details.getString("type"));
                    cv_array.add(cv);
                }

                mContentResolver.delete(MovieContract.VideosEntry.CONTENT_URI, MovieContract.VideosEntry.MOVIE_ID+"=?",new String[]{String.valueOf(Movieid_array.toArray()[i])});
                mContentResolver.bulkInsert(MovieContract.VideosEntry.CONTENT_URI,cv_array.toArray(new ContentValues[cv_array.size()]));

                uri = Uri.parse(BuildConfig.MOVIE_DB_API).buildUpon();
                uri.appendPath(String.valueOf(Movieid_array.toArray()[i]));
                uri.appendPath("reviews");
                uri.appendQueryParameter(API_KEY, BuildConfig.MOVIE_DB_API_KEY);
                url_string = uri.build().toString();
                url = new URL(url_string);
                connection = url.openConnection();
                connection.setDoInput(true);
                is = connection.getInputStream();
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);
                buffer = new StringBuffer();
                while ((line = br.readLine()) != null) {
                    buffer.append(line);
                }

                json_response = new JSONObject(buffer.toString());
                movie_details = json_response.getJSONArray(RESULTS);
                cv_array = new Vector<ContentValues>();
                for(int j=0; j<movie_details.length();j++) {
                    ContentValues cv = new ContentValues();
                    JSONObject video_details = movie_details.getJSONObject(j);
                    cv.put(MovieContract.ReviewsEntry.MOVIE_ID, String.valueOf(Movieid_array.toArray()[i]));
                    cv.put(MovieContract.ReviewsEntry.AUTHOR, video_details.getString("author"));
                    cv.put(MovieContract.ReviewsEntry.REVIEW, video_details.getString("content"));
                    cv_array.add(cv);
                }

                mContentResolver.delete(MovieContract.ReviewsEntry.CONTENT_URI, MovieContract.ReviewsEntry.MOVIE_ID+"=?",new String[]{String.valueOf(Movieid_array.toArray()[i])});
                mContentResolver.bulkInsert(MovieContract.ReviewsEntry.CONTENT_URI,cv_array.toArray(new ContentValues[cv_array.size()]));


            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void extractMovieDetails(JSONArray jsonArray, String setting)
    {
        File m_applicationDir = null;//new File(getContext().getFilesDir() + "");
        File m_picturesDir = null;//new File(m_applicationDir + "/favourite");

        int length = jsonArray.length();
        final String LOG_TAG = getContext().getString(R.string.extract_movie_log_tag);
        try {
            final String POSTER_PATH = getContext().getString(R.string.poster);
            final String TITLE = getContext().getString(R.string.title);
            final String RATING = getContext().getString(R.string.rating);
            final String OVERVIEW = getContext().getString(R.string.overview);
            final String RELEASE_DATE = getContext().getString(R.string.releaseDate);
            final String BaseUrl = BuildConfig.IMAGE_BASE_URL;
            final String MOVIE_ID = "id";
            Vector<ContentValues> contentValuesVector = new Vector<ContentValues>() ;

            File root = getContext().getFilesDir();
            File[] Files = root.listFiles();
            if(Files != null) {
                int j;
                for(j = 0; j < Files.length; j++) {
                    System.out.println(Files[j].getAbsolutePath());
                    System.out.println(Files[j].delete());
                }
            }
            for (int i = 0; i < length; i++) {

                m_applicationDir = new File(getContext().getFilesDir() + "");
                m_picturesDir = new File(m_applicationDir + "/"+setting);

                ContentValues cv = new ContentValues();
                JSONObject json_movie_details = jsonArray.getJSONObject(i);
                String image_url = BaseUrl+"/w500"+json_movie_details.getString(POSTER_PATH);
                URL url = new URL(image_url);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                saveImage(getContext(), bmp, "w500"+json_movie_details.getString(POSTER_PATH).substring(1),"", setting);
                cv.put(MovieContract.PopularMovieEntry.IMAGE_URL, json_movie_details.getString(POSTER_PATH));
                String title = json_movie_details.getString(TITLE);
                cv.put(MovieContract.PopularMovieEntry.TITLE, title);
                String overview = json_movie_details.getString(OVERVIEW);
                cv.put(MovieContract.PopularMovieEntry.SYNOPSIS, overview);
                String UserRating = json_movie_details.getString(RATING);
                cv.put(MovieContract.PopularMovieEntry.RATING, UserRating);
                String ReleaseDate = json_movie_details.getString(RELEASE_DATE);
                cv.put(MovieContract.PopularMovieEntry.RELEASE_DATE, ReleaseDate);
                String  id = json_movie_details.getString(MOVIE_ID);
                cv.put(MovieContract.PopularMovieEntry.MOVIE_ID, id);
                Movieid_array.add(Integer.valueOf(id));
                contentValuesVector.add(cv);
            }

            Uri inserturi=null;
            switch (setting) {
                case "popular":
                    inserturi = MovieContract.PopularMovieEntry.CONTENT_URI;
                    mContentResolver.delete(MovieContract.PopularMovieEntry.CONTENT_URI, null,null);
                    mContentResolver.bulkInsert(MovieContract.PopularMovieEntry.CONTENT_URI, contentValuesVector.toArray(new ContentValues[length]));
                    break;
                case "top_rated":
                    inserturi = MovieContract.TopRatedMovieEntry.CONTENT_URI;
                    mContentResolver.delete(MovieContract.TopRatedMovieEntry.CONTENT_URI, null,null);
                    mContentResolver.bulkInsert(MovieContract.TopRatedMovieEntry.CONTENT_URI, contentValuesVector.toArray(new ContentValues[length]));
                    break;
            }


        }
        catch(JSONException e)
        {
            Log.e(LOG_TAG, e.getMessage());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ;
    }

    public void saveImage(Context context, Bitmap b,String name,String extension ,String setting){
        File path=new File(getContext().getFilesDir(),setting);

        File mypath=new File(path,name);
        mypath.getParentFile().mkdirs();
        try {
            mypath.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out;
        try {
            out = new FileOutputStream(mypath);
            b.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        SyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);

        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        ;
        return newAccount;
    }


}