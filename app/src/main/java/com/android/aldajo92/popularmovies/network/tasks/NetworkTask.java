package com.android.aldajo92.popularmovies.network.tasks;

import android.os.AsyncTask;

import com.android.aldajo92.popularmovies.network.NetworkListener;
import com.android.aldajo92.popularmovies.network.NetworkManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;

public class NetworkTask extends AsyncTask<URL, Void, String> implements CheckConnectionTask.Consumer {

    private NetworkListener listener;
    private URL url;

    public NetworkTask(URL url, NetworkListener listener) {
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
