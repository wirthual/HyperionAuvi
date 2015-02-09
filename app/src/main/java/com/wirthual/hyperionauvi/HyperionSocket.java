package com.wirthual.hyperionauvi;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Created by devbuntu on 28.01.15.
 */
public class HyperionSocket extends WebSocketClient {

    final String TAG = "OpenWebsocketTask";
    public final static String WS_OPEN = "OPEN";
    public final static String WS_CLOSED = "CLOSED";
    public final static String WS_REMOTE_CLOSED = "REMOTE_CLOSED";
    public final static String WS_ERROR = "ERROR";
    public final static String WS_MESSAGE ="MESSAGE";
    public final static String WS_TIMEOUT = "TIMEOUT";
    public final static String WS_CLOSED_LOCAL = "LOCAL_CLOSED";

    public final static int TIMEOUT = 800;


    Context context;
    URI uri;
    boolean connected = false;

    public HyperionSocket(Context context,URI uri){
        super(uri);
        this.uri = uri;
        this.context = context;
    }
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.i("OPEN "+uri.toString() , TAG);
        Intent intent = new Intent();
        intent.setAction(WS_OPEN);
        context.sendBroadcast(intent);
        connected = true;
    }

    @Override
    public void onMessage(String message) {
        Log.i("Message", TAG);
        Intent intent = new Intent();
        intent.setAction(WS_MESSAGE);
        context.sendBroadcast(intent);

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.i( TAG,"Close "+uri.toString() + " Remote: "+ String.valueOf(remote));
        Intent intent = new Intent();
        if(remote){
            if(connected)intent.setAction(WS_REMOTE_CLOSED);
        }else{
            if(connected)intent.setAction(WS_CLOSED_LOCAL);
        }
        context.sendBroadcast(intent);
        connected = false;
    }

    @Override
    public void onError(Exception ex) {
        Log.i(ex.getLocalizedMessage()+uri.toString(), TAG);
        Intent intent = new Intent();
        intent.setAction(WS_ERROR);
        context.sendBroadcast(intent);
        connected = false;
    }
}
