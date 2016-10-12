package com.example.android.popularmovies;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.CursorRecyclerViewAdapter;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.db.MovieContract;

public class MyListCursorAdapter extends CursorRecyclerViewAdapter<MyListCursorAdapter.ViewHolder> {


    public MyListCursorAdapter(Context context, Cursor cursor){
        super(context,cursor);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ImageButton b;
        public ViewHolder(View view) {
            super(view);

            mTextView = (TextView) view.findViewById(R.id.video_name);
            b = (ImageButton)view.findViewById(R.id.play_button);
        }
    }


    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_list_item_view, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {


        int idx= cursor.getColumnIndex(MovieContract.VideosEntry.NAME);
        viewHolder.mTextView.setText(cursor.getString(idx));
        idx= cursor.getColumnIndex(MovieContract.VideosEntry.SIZE);
        viewHolder.mTextView.append("("+cursor.getString(idx)+"p)");
        idx= cursor.getColumnIndex(MovieContract.VideosEntry.VIDEO_KEY);
        viewHolder.b.setTag(cursor.getString(idx));

        }
}

