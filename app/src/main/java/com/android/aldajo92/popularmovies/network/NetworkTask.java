package com.android.aldajo92.popularmovies.network;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.URL;

public class NetworkTask extends AsyncTask<URL, Void, String> {

    private NetworkListener listener;

    public NetworkTask(NetworkListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.showLoader();
    }

    @Override
    protected String doInBackground(URL... urls) {
        URL url = urls[0];
        String result = "";
        try {
            result = NetworkManager.getResponseFromHttpUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        listener.hideLoader();
        listener.onResponse(response);
    }
}
