package com.mako.movietake;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class getMovieDb extends AsyncTask<String,Void,movieList> {


    public movieList m;
    public String[] movieId;
    public String[] movieImg;
    public String[] movieName;
    public boolean result=false;
    public AsyncResponse delegate = null;
    final String APPID_PARAM = "api_key";
    public String LOG_TAG=getMovieDb.class.getSimpleName();


    @Override
    protected movieList doInBackground(String... urls) {

        if(urls.length==0)
            return null;
        HttpURLConnection urlConnection=null;
        BufferedReader reader;

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
            // If the code didn't successfully get the movie data, there's no point in attemping
            // to parse it.
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

        }
        return m;
    }

    public void jsonParser(String moviedbStr) throws JSONException {
        final String OWM_LIST = "results";
        final String IMAGE_PATH="poster_path";
        final String original_title="title";
        final String ID_PATH="id";

        JSONObject movieJson = new JSONObject(moviedbStr);
        JSONArray movieArray = movieJson.getJSONArray(OWM_LIST);
        movieId=new String[movieArray.length()];
        movieImg=new String[movieArray.length()];
        movieName=new String[movieArray.length()];

        for(int i = 0; i < movieArray.length(); i++) {
            JSONObject movieObj= movieArray.getJSONObject(i);
            movieImg[i] = movieObj.getString(IMAGE_PATH);
            movieId[i] = movieObj.getString(ID_PATH);
            movieName[i]= movieObj.getString(original_title);

        }
        m=new movieList(movieId,movieImg,movieName,true);

    }
    @Override
    protected void onPostExecute(movieList movieLists) {
        delegate.processFinish(movieLists);
    }
}
