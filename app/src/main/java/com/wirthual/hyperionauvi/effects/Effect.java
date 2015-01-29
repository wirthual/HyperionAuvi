package com.wirthual.hyperionauvi.effects;

import android.media.audiofx.Visualizer;

import com.wirthual.hyperionauvi.HyperionSocket;

import org.java_websocket.WebSocket;

/**
 * Created by devbuntu on 21.01.15.
 */
public abstract class Effect implements Visualizer.OnDataCaptureListener {

    String TAG = "Effect";

    HyperionSocket webSocketClient;

    public Effect(){}

    public Effect(HyperionSocket socket){
        webSocketClient = socket;
    }

    public abstract String getName();

    public abstract boolean isWaveformEffect();

    public void sendData(String s){
        if (webSocketClient != null && webSocketClient.getReadyState() == WebSocket.READYSTATE.OPEN) {
            webSocketClient.send(s);
        }
    }

}
