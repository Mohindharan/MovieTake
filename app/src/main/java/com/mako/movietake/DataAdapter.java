package com.mako.movietake;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import static android.content.ContentValues.TAG;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder>   {

    private String imgs[];
    private String movieid[];
    private String image_base_url="http://image.tmdb.org/t/p/w500/";
    private Context context;

    public DataAdapter(Context context, String[] im, String[] movieId) {
        this.imgs = im;
        this.movieid=movieId;
        this.context = context;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.showcards, viewGroup, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder viewHolder, int i) {

        Picasso.with(context).load(image_base_url+imgs[i]).into(viewHolder.imageView);

    }

    @Override
    public int getItemCount() {
        return imgs.length;
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            imageView = (ImageView) view.findViewById(R.id.photo);
        }

        @Override
        public void onClick(View view) {
            Intent intent =new Intent(context,detailedActivity.class);
            intent.putExtra("id",movieid[getLayoutPosition()]);
            view.getContext().startActivity(intent);

        }
    }

}