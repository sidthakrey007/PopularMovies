package com.example.android.popularmovies;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */

public class MovieDetailsFragment extends Fragment {


    public MovieDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final String RATING = getString(R.string.rating);
        final String TITLE = getString(R.string.title);
        final String RELEASE_DATE = getString(R.string.releaseDate);
        final String POSTER = getString(R.string.poster);
        final String OVERVIEW= getString(R.string.overview);

        View movieDetailView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        Intent message  = getActivity().getIntent();
        TextView titleTextView = (TextView) movieDetailView.findViewById(R.id.title);
        titleTextView.setText(message.getStringExtra(TITLE));


        TextView ratingTextView =(TextView) movieDetailView.findViewById(R.id.rating);
        ratingTextView.setText(message.getStringExtra(RATING)+getString(R.string.rate_on_ten));

        TextView releaseDateTextView = (TextView) movieDetailView.findViewById(R.id.releasedate);
        releaseDateTextView.setText(message.getStringExtra(RELEASE_DATE));

        TextView overviewTextView = (TextView) movieDetailView.findViewById(R.id.overview);
        overviewTextView.setText(message.getStringExtra(OVERVIEW));

        ImageView posterImage =(ImageView)movieDetailView.findViewById(R.id.poster);
        Picasso.with(getActivity()).load(message.getStringExtra(POSTER)).into(posterImage);
        posterImage.setAdjustViewBounds(true);

        return movieDetailView;
    }


}
