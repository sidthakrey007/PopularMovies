package com.example.android.popularmovies;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

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
import android.support.v4.app.Fragment;

public class MoviesFragment extends Fragment  {
    private ImageAdapter posterImageAdapter = null;
    private ArrayList<MovieDetails> movieDetailsList ;
    //class variable to determine weather to load main movies again or not
    boolean isPreferenceChanged ;


    public MoviesFragment() {
        // Required empty public constructor
    }

    /*method which created fragment and initializes the image adapter */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        if(savedInstanceState!=null) {

            movieDetailsList = savedInstanceState.getParcelableArrayList(getString(R.string.movie_details));
            posterImageAdapter = new ImageAdapter(getActivity(), movieDetailsList);
            posterImageAdapter.notifyDataSetChanged();
            posterImageAdapter.setNotifyOnChange(true);

        }
        else
        {
            movieDetailsList = new ArrayList<MovieDetails>();
            posterImageAdapter = new ImageAdapter(getActivity(), movieDetailsList);
            posterImageAdapter.setNotifyOnChange(true);

        }
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movie_details, menu);
        super.onCreateOptionsMenu(menu, inflater);
        super.onCreateOptionsMenu(menu, inflater);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings)
        {
            Intent preferenceActivityIntent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(preferenceActivityIntent);
        }
        if(id == R.id.action_refresh)
        {
            updateMovies();
        }

        return super.onOptionsItemSelected(item);
    }

    /* Method to create the alert Dialogue in case of internat connection is not available */
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


    @Override
    public void onResume() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        isPreferenceChanged = pref.getBoolean(getString(R.string.preference_change_key), true);
        if(isPreferenceChanged)
            updateMovies();
        pref.edit().putBoolean(getString(R.string.preference_change_key), false).commit();
        super.onResume();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final String POSTER_PATH = getString(R.string.poster);
        final String TITLE = getString(R.string.title);
        final String RATING = getString(R.string.rating);
        final String OVERVIEW = getString(R.string.overview);
        final String RELEASE_DATE = getString(R.string.releaseDate);
        View movieFragmentView = inflater.inflate(R.layout.fragment_movies, container, false);
        GridView posterGridView = (GridView) movieFragmentView.findViewById(R.id.movie_thumbnail_view);
        posterGridView.setAdapter(posterImageAdapter);
        posterGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent movieDetailIntent = new Intent(getActivity(), MovieDetailsActivity.class);
                ImageAdapter adapter= (ImageAdapter) parent.getAdapter();
                MovieDetails obj  = adapter.getItem(position);
                movieDetailIntent.putExtra(TITLE, obj.title);
                movieDetailIntent.putExtra(RATING,obj.UserRating);
                movieDetailIntent.putExtra(OVERVIEW, obj.overview);
                movieDetailIntent.putExtra(RELEASE_DATE,obj.ReleaseDate);
                movieDetailIntent.putExtra(POSTER_PATH, obj.ImageUrl);
                startActivity(movieDetailIntent);
            }
        });

        return movieFragmentView;
    }

/* method  to update movies in ui before updating it will check the availablity of internet connection*/
    void updateMovies()
    {
        if(isDeviceOnline()==false) {
            createNetworkErrorDialogue();
        }
        else {
            FetchMoviesTask moviesTask = new FetchMoviesTask();
            //movieDetailsList.clear();
            moviesTask.execute();
        }

    }

    // method to check if device has access to internet connection
    boolean isDeviceOnline()
    {
        ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork= connManager.getActiveNetworkInfo();
        return (activeNetwork!=null && activeNetwork.isConnected());
    }

    public void updateImages(JSONArray jsonArray)
    {
            extractMovieDetails(jsonArray);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(getString(R.string.movie_details), movieDetailsList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        onSaveInstanceState(new Bundle());
        super.onPause();
    }

    public class MovieDetails implements Parcelable {
        String title;
        String overview;
        String ReleaseDate;
        String UserRating;
        String ImageUrl;

        MovieDetails()
        {

        }

        protected MovieDetails(Parcel in) {
            title = in.readString();
            overview = in.readString();
            ReleaseDate = in.readString();
            UserRating = in.readString();
            ImageUrl = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(title);
            dest.writeString(overview);
            dest.writeString(ReleaseDate);
            dest.writeString(UserRating);
            dest.writeString(ImageUrl);
        }


        public final Parcelable.Creator<MovieDetails> CREATOR = new Parcelable.Creator<MovieDetails>() {
            @Override
            public MovieDetails createFromParcel(Parcel in) {
                return new MovieDetails(in);
            }

            @Override
            public MovieDetails[] newArray(int size) {
                return new MovieDetails[size];
            }
        };
    }

    MovieDetails[] extractMovieDetails(JSONArray jsonArray)
    {
        int length = jsonArray.length();
        MovieDetails[] movie_details = new MovieDetails[length];
        final String LOG_TAG = getString(R.string.extract_movie_log_tag);

        try {
            final String POSTER_PATH = getString(R.string.poster);
            final String TITLE = getString(R.string.title);
            final String RATING = getString(R.string.rating);
            final String OVERVIEW = getString(R.string.overview);
            final String RELEASE_DATE = getString(R.string.releaseDate);
            final String BaseUrl = BuildConfig.IMAGE_BASE_URL;
            for (int i = 0; i < length; i++) {

                JSONObject json_movie_details = jsonArray.getJSONObject(i);
                movie_details[i]= new MovieDetails();
                movie_details[i].ImageUrl =BaseUrl+json_movie_details.getString(POSTER_PATH);
                movie_details[i].title = json_movie_details.getString(TITLE);
                movie_details[i].overview = json_movie_details.getString(OVERVIEW);
                movie_details[i].UserRating = json_movie_details.getString(RATING);
                movie_details[i].ReleaseDate = json_movie_details.getString(RELEASE_DATE);
                movieDetailsList.add(movie_details[i]);
            }
        }
        catch(JSONException e)
            {
              Log.e(LOG_TAG, e.getMessage());
            }
        return movie_details;
    }


    class FetchMoviesTask extends AsyncTask<Void, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(Void... params) {
            URL url = null;
            final String RESULTS = getString(R.string.result_key);
            final String API_KEY = getString(R.string.apiKeyQueryStringParameter);
            final String LOG_KEY = getString(R.string.asyncTask_log_tag);

            String SORT_ORDER_PREFERENCE = getString(R.string.sort_preference_key);
            URLConnection connection = null;
            JSONArray movie_details=null;
            try {

                Uri.Builder uri = Uri.parse(BuildConfig.MOVIE_DB_API).buildUpon();
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String category = pref.getString(SORT_ORDER_PREFERENCE, getString(R.string.popular_preference_key));
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
           posterImageAdapter.clear();
            updateImages(jsonArray);
            posterImageAdapter.notifyDataSetChanged();
            super.onPostExecute(jsonArray);
        }

    }

    public class PosterViewHolder
    {
        ImageView posterImage;
    }

    public class ImageAdapter extends ArrayAdapter<MovieDetails> {
        ArrayList<MovieDetails> movie_details = null;
        private Context context;

        public ImageAdapter(Context c,  ArrayList<MovieDetails> movie_details) {
            super(c, R.layout.movie_poster_view, movie_details);
            this.movie_details = movie_details;
            this.context = c;
        }

        public int getCount()
        {
            return movie_details.toArray().length;
        }

        @Override
        public MovieDetails getItem(int position) {
            return super.getItem(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public ImageView getView(int position, View convertView, ViewGroup parent) {
            MovieDetails mDetails = getItem(position);
            ImageView imageView = (ImageView) convertView;
            PosterViewHolder holder;

            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = inflater.inflate(R.layout.movie_poster_view, parent, false);
                imageView = (ImageView) v.findViewById(R.id.movie_poster);
                Picasso.with(getActivity()).load(movie_details.get(position).ImageUrl).into(imageView);
                holder = new PosterViewHolder();
                holder.posterImage = imageView;
                imageView.setTag(holder);

            } else {
                holder = (PosterViewHolder)convertView.getTag();
            }
            if(mDetails!=null)
                Picasso.with(getActivity()).load(mDetails.ImageUrl).into(holder.posterImage);
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(0,0,0,0);
            return imageView;
        }
    }



}


















