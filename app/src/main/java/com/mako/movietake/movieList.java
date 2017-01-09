package com.mako.movietake;

/**
 * Created by mako on 10/11/16.
 */

public class movieList {
    private String[] movieId;
    private String[] movieImg;
    private String[] movieTitle;

    private boolean response=false;

    public  movieList(String id[],String img[],String title[],boolean res){
        movieId=id;
        movieImg=img;
        response=true;
        movieTitle=title;
    }

    public String[] getMovieTitle() {return movieTitle;}
    public String[] getMovieId(){
        return movieId;
    }
    public  String[] getMovieImg(){
        return movieImg;
    }
    public boolean getStatus(){
        return response;
    }
}

