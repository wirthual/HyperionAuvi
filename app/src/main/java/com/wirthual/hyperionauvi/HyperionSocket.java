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
    public final static String WS_ERROR = "ERROR";
    public final static String WS_MESSAGE ="MESSAGE";


    Context context;

    public HyperionSocket(Context context,URI uri){
        super(uri);
        this.context = context;
    }
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.i("OPEN" , TAG);
        Intent intent = new Intent();
        intent.setAction(WS_OPEN);
        context.sendBroadcast(intent);
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
        Log.i("Close", TAG);
        Intent intent = new Intent();
        intent.setAction(WS_CLOSED);
        context.sendBroadcast(intent);
    }

    @Override
    public void onError(Exception ex) {
        Log.i("Error", TAG);
        Intent intent = new Intent();
        intent.setAction(WS_ERROR);
        context.sendBroadcast(intent);
    }
}
