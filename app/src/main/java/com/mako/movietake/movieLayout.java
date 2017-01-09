package com.mako.movietake;

import android.support.v4.app.Fragment;

/**
 * Created by Mako on 1/9/2017.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import static android.content.ContentValues.TAG;


public class movieLayout extends Fragment implements AsyncResponse {


    public movieList m;
    private String categoy;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    RecyclerView rv ;
    View view;

    public movieLayout(String category) {
        this.categoy=category;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.movie_grid_layout,container,false);
        rv = (RecyclerView)view.findViewById(R.id.gridView_cover);
        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(new SimpleRecycler());
        updateMovie();
        return view;
    }
    public void updateMovie() {

        final String baseurl = "http://api.themoviedb.org/3/movie/";
        final String url = baseurl + categoy + "?";
        getMovieDb mm = new getMovieDb();
        mm.delegate= (AsyncResponse) this;
        mm.execute(url);
    }


    @Override
    public void processFinish(movieList output) {


        String[] movieImg=output.getMovieImg();
        String[] movieId=output.getMovieId();
        boolean response=output.getStatus();
        Log.e(TAG, "processFinish: "+response);
        if(response==true){


            DataAdapter adapter = new DataAdapter(getActivity(),movieImg,movieId);
            rv.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),2);
            rv.setLayoutManager(layoutManager);
            rv.setAdapter(adapter);

        }
        else{
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }
}
