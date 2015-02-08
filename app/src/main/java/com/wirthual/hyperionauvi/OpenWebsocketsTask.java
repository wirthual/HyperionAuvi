package com.wirthual.hyperionauvi;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import org.java_websocket.WebSocket;

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
            final HyperionSocket s = params[0];
            s.connect();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (!(s.getReadyState() == WebSocket.READYSTATE.OPEN)) {
                    Log.i(TAG, "Closed socket because of timeout");
                    s.close();
                }
            }
        }, 2000);
        return null;
    }

}
