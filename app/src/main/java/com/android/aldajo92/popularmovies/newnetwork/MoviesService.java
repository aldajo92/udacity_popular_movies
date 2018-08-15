package com.android.aldajo92.popularmovies.newnetwork;

import android.support.annotation.NonNull;

import com.android.aldajo92.popularmovies.utils.Constants;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.android.aldajo92.popularmovies.newnetwork.NetworkConstants.API_KEY_PARAM;
import static com.android.aldajo92.popularmovies.newnetwork.NetworkConstants.BASE_URL;

public class MoviesService {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request original = chain.request();

                                HttpUrl url = original
                                        .url()
                                        .newBuilder()
                                        .addQueryParameter(API_KEY_PARAM, Constants.API_KEY_MOVIE_DB)
                                        .build();

                                Request request = original
                                        .newBuilder()
                                        .url(url)
                                        .build();

                                return chain.proceed(request);
                            }
                        }
                ).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }

}
