package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.android.popularmovies.db.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by siddharth.thakrey on 25-09-2016.
 */
class FetchMoviesTask extends AsyncTask<Void, Void, JSONArray> {


    Context c ;

    FetchMoviesTask(Context x)
    {
        c = x;
    }

    @Override
    protected JSONArray doInBackground(Void... params) {
        URL url = null;
        final String RESULTS = c.getString(R.string.result_key);
        final String API_KEY = c.getString(R.string.apiKeyQueryStringParameter);
        final String LOG_KEY = c.getString(R.string.asyncTask_log_tag);

        String SORT_ORDER_PREFERENCE = c.getString(R.string.sort_preference_key);
        URLConnection connection = null;
        JSONArray movie_details=null;
        try {

            Uri.Builder uri = Uri.parse(BuildConfig.MOVIE_DB_API).buildUpon();
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(c);
            String category = pref.getString(SORT_ORDER_PREFERENCE, c.getString(R.string.popular_preference_key));
            uri.appendPath(category);
            uri.appendQueryParameter(API_KEY, BuildConfig.MOVIE_DB_API_KEY);
            String url_string = uri.build().toString();
            Log.v("final_url", url_string);
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

        } catch (JSONException e) {
            Log.e(LOG_KEY, e.getMessage());
        } catch (MalformedURLException e) {
            Log.e(LOG_KEY, e.getMessage());
        } catch (IOException e) {
            Log.e(LOG_KEY, e.getMessage());
        }

        return movie_details;
    }

    @Override
    protected void onPostExecute(JSONArray jsonArray) {
        super.onPostExecute(jsonArray);
    }





}