package com.example.android.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by siddharth.thakrey on 29-09-2016.
 */
public final class ListItemViewHolder extends RecyclerView.ViewHolder {
    ImageView label;
    TextView name;

    public ListItemViewHolder(View itemView) {
        super(itemView);
        label = (ImageView) itemView.findViewById(R.id.play_button);
        name = (TextView) itemView.findViewById(R.id.video_name);
    }

}