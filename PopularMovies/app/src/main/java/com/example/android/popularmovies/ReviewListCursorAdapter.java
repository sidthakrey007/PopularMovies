package com.example.android.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.db.MovieContract;

/**
 * Created by siddharth.thakrey on 29-09-2016.
 */
public class ReviewListCursorAdapter extends CursorRecyclerViewAdapter<ReviewListCursorAdapter.ViewHolder> {


    public ReviewListCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public TextView rTextView;

        public ViewHolder(View view) {
            super(view);

            mTextView = (TextView) view.findViewById(R.id.author);
            rTextView = (TextView) view.findViewById(R.id.review);

        }
    }


        @Override
        public int getItemCount() {
            return super.getItemCount();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.review_list_item_view, parent, false);
            ViewHolder vh = new ViewHolder(itemView);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {

            int idx = cursor.getColumnIndex(MovieContract.ReviewsEntry.AUTHOR);
            viewHolder.mTextView.setText(cursor.getString(idx));

            idx = cursor.getColumnIndex(MovieContract.ReviewsEntry.REVIEW);
            viewHolder.rTextView.setText(cursor.getString(idx));

        }
    }
