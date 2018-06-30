package com.android.aldajo92.popularmovies.network.tasks;

import android.os.AsyncTask;

import com.android.aldajo92.popularmovies.network.interfaces.ConnectionListener;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class CheckConnectionTask extends AsyncTask<Void, Void, Boolean> {

    private ConnectionListener mConnectionListener;

    public CheckConnectionTask(ConnectionListener connectionListener) {
        mConnectionListener = connectionListener;
        execute();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            Socket sock = new Socket();
            sock.connect(new InetSocketAddress("8.8.8.8", 53), 1500);
            sock.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean isConnected) {
        mConnectionListener.connectionEvent(isConnected);
    }
}
