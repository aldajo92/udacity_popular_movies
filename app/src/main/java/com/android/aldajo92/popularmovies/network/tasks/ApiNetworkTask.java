package com.android.aldajo92.popularmovies.network.tasks;

import android.os.AsyncTask;

import com.android.aldajo92.popularmovies.network.NetworkManager;
import com.android.aldajo92.popularmovies.network.interfaces.ApiNetworkListener;
import com.android.aldajo92.popularmovies.network.interfaces.ConnectionListener;

import java.io.IOException;
import java.net.URL;

public class ApiNetworkTask extends AsyncTask<URL, Void, String> implements ConnectionListener {

    private ApiNetworkListener listener;
    private URL url;

    public ApiNetworkTask(URL url, ApiNetworkListener listener) {
        this.listener = listener;
        this.url = url;
        validateConnection();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.showLoader();
    }

    @Override
    protected String doInBackground(URL... urls) {
        String result = "";
        URL url = urls[0];
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

    @Override
    public void connectionEvent(Boolean isConnected) {
        if (isConnected) {
            execute(url);
        } else {
            listener.showNetworkError();
        }
    }

    public void validateConnection() {
        new CheckConnectionTask(this);
    }
}
