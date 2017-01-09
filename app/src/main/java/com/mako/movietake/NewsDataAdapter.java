package com.mako.movietake;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubePlayer;
import com.squareup.picasso.Picasso;
import com.thefinestartist.ytpa.YouTubePlayerActivity;
import com.thefinestartist.ytpa.enums.Orientation;

import static android.content.ContentValues.TAG;

public class NewsDataAdapter extends RecyclerView.Adapter<NewsDataAdapter.ViewHolder>   {

    private String imgs[];
    private String movieid[];
    private String source[];
    private String image_base_url="http://image.tmdb.org/t/p/w500/";
    private Context context;
    private boolean youtube;


    public NewsDataAdapter(Context context, String[] im,String[] source, String[] movieId, boolean youtube) {
        this.imgs = im;
        this.movieid=movieId;
        this.source=source;
        this.context = context;
        this.youtube=youtube;
    }


    @Override
    public NewsDataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.newscard, viewGroup, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(NewsDataAdapter.ViewHolder viewHolder, int i) {
        viewHolder.textView.setText(movieid[i]+" properties like name, number of songs and cover image inside it.");
        viewHolder.title.setText(source[i]);
        Picasso.with(context).load(image_base_url+imgs[i]).into(viewHolder.imageView);

    }

    @Override
    public int getItemCount() {
        return imgs.length;
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private TextView textView;
        private String trailerId;
        private TextView title;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            imageView = (ImageView) view.findViewById(R.id.photo);
            textView=(TextView)view.findViewById(R.id.title);
            title=(TextView)view.findViewById(R.id.source);
            trailerId="iS1g8G_njx8";
        }

        @Override
        public void onClick(View view) {
            Intent intent;
            if (youtube!=true) {
                intent = new Intent(context, detailedActivity.class);
                intent.putExtra("id", movieid[getLayoutPosition()]);
            }
            else{
                intent = new Intent(view.getContext(), YouTubePlayerActivity.class);
                intent.putExtra(YouTubePlayerActivity.EXTRA_VIDEO_ID, trailerId);
                intent.putExtra(YouTubePlayerActivity.EXTRA_PLAYER_STYLE, YouTubePlayer.PlayerStyle.MINIMAL);
                intent.putExtra(YouTubePlayerActivity.EXTRA_ORIENTATION, Orientation.AUTO);
                intent.putExtra(YouTubePlayerActivity.EXTRA_SHOW_AUDIO_UI, true);
                intent.putExtra(YouTubePlayerActivity.EXTRA_HANDLE_ERROR, true);
                intent.putExtra(YouTubePlayerActivity.EXTRA_ANIM_ENTER, R.anim.pull_in_right);
                intent.putExtra(YouTubePlayerActivity.EXTRA_ANIM_EXIT, R.anim.push_out_left);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            view.getContext().startActivity(intent);
        }
    }

}