package com.wirthual.hyperionauvi;

import android.os.AsyncTask;

/**
 * Created by devbuntu on 27.01.15.
 */
public class OpenWebsocketsTask extends AsyncTask<HyperionSocket,Void,Void> {

    final String TAG = "OpenWebsocketTask";
    final String WS_OPEN = "OPEN";
    final String WS_CLOSED = "CLOSED";
    final String WS_ERROR = "ERROR";
    final String WS_MESSAGE ="MESSAGE";


    public OpenWebsocketsTask(){

    }

    @Override
    protected Void doInBackground(HyperionSocket... params) {
            HyperionSocket s = params[0];
            s.connect();
        return null;
    }

}
