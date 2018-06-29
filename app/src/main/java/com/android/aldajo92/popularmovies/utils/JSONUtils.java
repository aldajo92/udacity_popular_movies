package com.android.aldajo92.popularmovies.utils;

import com.android.aldajo92.popularmovies.models.MovieModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class JSONUtils {

    private JSONUtils(){}

    public static List<MovieModel> JSONToMovieModel(String jsonString) {

        List<MovieModel> movieModelList = new ArrayList<>();

        try {
            JSONObject object = new JSONObject(jsonString);
            JSONArray resultsArrayObject = object.getJSONArray("results");

            for (int i = 0; i < resultsArrayObject.length(); i++) {
                JSONObject jsonobject = resultsArrayObject.getJSONObject(i);
                String name = jsonobject.getString("title");
                String vote = jsonobject.getString("vote_average");
                String imageUrl = jsonobject.getString("poster_path");
                String overview = jsonobject.getString("overview");
                String releaseDate = jsonobject.getString("release_date");
                movieModelList.add(new MovieModel(name, vote, imageUrl, overview, releaseDate));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movieModelList;
    }

}
