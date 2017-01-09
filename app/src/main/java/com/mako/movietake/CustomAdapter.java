package com.mako.movietake;

import android.content.Context;
import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private String[] imagesList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageview;

        public MyViewHolder(View view) {
            super(view);
            imageview = (ImageView) view.findViewById(R.id.imageView);
        }
    }


    public CustomAdapter(Context context,String[] imageList) {
        this.imagesList = imageList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movielist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String movieim = imagesList[position];
        Picasso.with(context).load(imagesList[position]).into(holder.imageview);

    }

    @Override
    public int getItemCount() {
        return imagesList.length;
    }
}