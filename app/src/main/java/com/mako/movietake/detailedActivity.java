package com.mako.movietake;

/**
 * Created by Mako on 1/2/2017.
 */
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubePlayer;
import com.squareup.picasso.Picasso;
import com.thefinestartist.ytpa.YouTubePlayerActivity;
import com.thefinestartist.ytpa.enums.Orientation;
import com.thefinestartist.ytpa.enums.Quality;
import com.thefinestartist.ytpa.utils.YouTubeThumbnail;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by mako on 15/8/16.
 */
public class detailedActivity extends AppCompatActivity {
    public String Base_URL="http://api.themoviedb.org/3/movie/";
    private String image_base_url="http://image.tmdb.org/t/p/w500/";
    public boolean result;
    ImageView imageView,tralier,playbtn;
    public String imagePath,title,overview,rating,r_date,runtime,lang,trailerId;
    TextView textView,overView,ratingView,releaseView,runtimeView,langView,genreView,castview;
    RecyclerView rv;

    ProgressBar p;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        setContentView(R.layout.detail_layout);
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#263238")));
        Intent intent=getIntent();
        String id=intent.getStringExtra("id");
        textView =(TextView)findViewById(R.id.title);
        overView=(TextView)findViewById(R.id.overview);
        imageView=(ImageView)findViewById(R.id.image);
        tralier=(ImageView)findViewById(R.id.tralier);
        ratingView=(TextView)findViewById(R.id.rating);
        releaseView=(TextView)findViewById(R.id.relesedate);
        runtimeView=(TextView)findViewById(R.id.runtime);
        langView=(TextView)findViewById(R.id.language);
        genreView=(TextView)findViewById(R.id.genre);
        castview=(TextView)findViewById(R.id.cast);
        rv=(RecyclerView)findViewById(R.id.photolist);
        playbtn=(ImageView)findViewById(R.id.inside_imageview);
        Base_URL=Base_URL+id+"?";
        getMoviedetail mm= new getMoviedetail();
        mm.execute(Base_URL);
        p=(ProgressBar)findViewById(R.id.progressbar);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,true);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(new SimpleRecycler());
    }


    public class getMoviedetail extends AsyncTask<String,Void,String[]> {

        final String APPID_PARAM = "api_key";
        public String LOG_TAG=getMoviedetail.class.getSimpleName();
        private String[] photos;


        @Override
        protected void onPostExecute(String[] strings) {
            p.setVisibility(View.GONE);
            textView.setText(title);
            overView.setText(overview);
            ratingView.setText("88");
            releaseView.setText(r_date);
            runtimeView.setText(runtime);
            langView.setText("Tamil");
            genreView.setText("Drama , Comedy");
            castview.setText("Amy Jackson , Amir Khan , Salman Khan & Katrina Kaif");
            Picasso.with(detailedActivity.this).load(image_base_url+imagePath).into(imageView);
            Picasso.with(detailedActivity.this).load(YouTubeThumbnail.getUrlFromVideoId(trailerId, Quality.HIGH)).into(tralier);
            playbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(detailedActivity.this, YouTubePlayerActivity.class);
                    intent.putExtra(YouTubePlayerActivity.EXTRA_VIDEO_ID, trailerId);
                    intent.putExtra(YouTubePlayerActivity.EXTRA_PLAYER_STYLE, YouTubePlayer.PlayerStyle.MINIMAL);
                    intent.putExtra(YouTubePlayerActivity.EXTRA_ORIENTATION, Orientation.AUTO);
                    intent.putExtra(YouTubePlayerActivity.EXTRA_SHOW_AUDIO_UI, true);
                    intent.putExtra(YouTubePlayerActivity.EXTRA_HANDLE_ERROR, true);
                    intent.putExtra(YouTubePlayerActivity.EXTRA_ANIM_ENTER, R.anim.pull_in_right);
                    intent.putExtra(YouTubePlayerActivity.EXTRA_ANIM_EXIT, R.anim.push_out_left);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
            rv.setAdapter(new CustomAdapter(detailedActivity.this,photos));
        }

        @Override
        protected String[] doInBackground(String... urls) {

            if(urls.length==0)
                return null;
            HttpURLConnection urlConnection=null;
            BufferedReader reader;
            String format="json";
            String moviedbStr=null;
            try{
                Uri builtUri = Uri.parse(urls[0]).buildUpon()
                        .appendQueryParameter(APPID_PARAM, BuildConfig.MOVIEDB_API_KEY)
                        .build();
                URL url = new URL(builtUri.toString());
                urlConnection =(HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream=urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader= new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                result=true;
                moviedbStr = buffer.toString();
                jsonParser(moviedbStr);
            }

            catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

            }
            return null;
        }

        public void jsonParser(String moviedbStr) throws JSONException {
            final String Overview_path = "overview";
            final String IMAGE_PATH="poster_path";
            final String original_title="original_title";
            final String RATING_PATH="vote_average";
            final String relese_date_path="release_date";
            final String runtime_path="runtime";
            final String language="original_language";
            final String[] photos={"http://d1f7f1axn40slq.cloudfront.net/30a5e724-4052-496f-a768-655d78dc7ecb",
                    "http://d1f7f1axn40slq.cloudfront.net/657206e1-8de0-4604-a286-d53d137f4606",
                    "http://d1f7f1axn40slq.cloudfront.net/d45eb888-c8f2-4f82-b40b-010744a64947",
                    "http://d1f7f1axn40slq.cloudfront.net/d0cbbba5-c3f7-459c-b724-40fc7badd09e"};
            JSONObject movieJson = new JSONObject(moviedbStr);

            imagePath = movieJson.getString(IMAGE_PATH);
            title=movieJson.getString(original_title);
            overview=movieJson.getString(Overview_path);
            rating=movieJson.getString(RATING_PATH);
            r_date=movieJson.getString(relese_date_path);
            String year=r_date.substring(0,4);
            this.photos=photos;
            String day=r_date.substring(8,10);
            String mon=theMonth(r_date.substring(5,7));
             r_date=mon+" "+day+", "+year;
            runtime=movieJson.getString(runtime_path);
            runtime=HourTime(runtime);
            trailerId="iS1g8G_njx8";
        }
    }
    public static String theMonth(String mon){
        int month=Integer.parseInt(mon)-1;
        String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return monthNames[month];
    }
    public String HourTime(String runtime){
        String value ;
        int mtime=Integer.parseInt(runtime);
        int hr=mtime/60;
        int min=mtime%60;
        if(min==0)
            value=value="Hr "+hr;
        else
          value=hr+" hr "+min+" mins";
        return value;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            return true;
        }
        return false;
    }
}
