package com.android.aldajo92.popularmovies.network.tasks;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;

public class CheckConnectionTask extends AsyncTask<Void, Void, Boolean>{

    private Consumer mConsumer;

    public interface Consumer {
        void connectionEvent(Boolean isConnected);
    }

    public CheckConnectionTask(Consumer consumer) {
        mConsumer = consumer;
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
        mConsumer.connectionEvent(isConnected);
    }
}
