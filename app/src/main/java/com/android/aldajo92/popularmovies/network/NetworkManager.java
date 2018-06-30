package com.android.aldajo92.popularmovies.network;

import android.net.Uri;

import com.android.aldajo92.popularmovies.utils.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.Scanner;

public final class NetworkManager {

    private static final String BASE_URL = "api.themoviedb.org";
    private static final String API_KEY_PARAM = "api_key";
    public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185";
    public static final String IMAGE_HD_BASE_URL = "http://image.tmdb.org/t/p/w500";

    public static final String MOVIE_PARAM = "popular";
    public static final String TOP_RATED_PARAM = "top_rated";

    public static URL getPopularMovieURL(String filter) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority(BASE_URL)
                .appendPath("3")
                .appendPath("movie")
                .appendPath(filter)
                .appendQueryParameter(API_KEY_PARAM, Constants.API_KEY_MOVIE_DB);

        Uri buildUri = builder.build();
        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = urlConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
