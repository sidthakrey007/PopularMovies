package com.example.android.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.db.MovieContract;

/**
 * Created by siddharth.thakrey on 12-10-2016.
 */
    public class MovieImageCursorAdapter extends CursorRecyclerViewAdapter<MovieImageCursorAdapter.ViewHolder> {


    Context mContext  =null;
        public MovieImageCursorAdapter(Context context, Cursor cursor) {

            super(context, cursor);
            this.mContext = context;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView i ;

            public ViewHolder(View view) {
                super(view);

                i = (ImageView) view.findViewById(R.id.movie_poster);
            }
        }


        @Override
        public int getItemCount() {
            return super.getItemCount();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_poster_view, parent, false);
            ViewHolder vh = new ViewHolder(itemView);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {

            int imageurl_index = cursor.getColumnIndex(MovieContract.PopularMovieEntry.IMAGE_URL);
            Bitmap image=null;
            String setting = Utility.getSortedSetting(mContext);
            if(mContext.getResources().getBoolean(R.bool.isTab)==true)
                Utility.getImageBitmap(mContext, mContext.getString(R.string.image_size) +cursor.getString(imageurl_index).substring(1), setting, viewHolder.i);
            else
                Utility.getImageBitmap(mContext, mContext.getString(R.string.image_size)+cursor.getString(imageurl_index).substring(1), setting, viewHolder.i);
            //viewHolder.i.setImageBitmap(image);
            viewHolder.i.setAdjustViewBounds(true);
            viewHolder.i.setScaleType(ImageView.ScaleType.FIT_XY);
            viewHolder.i.setPadding(0,0,0,0);

        }

    }


