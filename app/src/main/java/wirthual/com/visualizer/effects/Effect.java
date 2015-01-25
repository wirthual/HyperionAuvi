package wirthual.com.visualizer.effects;

import android.content.Context;
import android.util.Log;

import org.java_websocket.WebSocket;

import java.net.URI;

import wirthual.com.visualizer.HyperionWebSocket;

/**
 * Created by devbuntu on 21.01.15.
 */
public abstract class Effect {

    String TAG = "Effect";

    HyperionWebSocket webSocketClient;

    Context context;

    public Effect(){};

    public Effect(Context con){
        context = con;
    }

    public abstract String getName();

    public abstract boolean isWaveformEffect();

    public WebSocket.READYSTATE openWebSocket(String ip, String port) {
        Log.i("Websocket trys to connect to IP: " + ip + " Port: " + port, TAG);

        String ws = "ws://" + ip + ":" + port;
        URI uri = URI.create(ws);
        webSocketClient = new HyperionWebSocket(context,uri);
        try {
            webSocketClient.connectBlocking();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return webSocketClient.getReadyState();
    }

    public void closeWebSocket() {
        if (webSocketClient != null && webSocketClient.getReadyState() == WebSocket.READYSTATE.OPEN) {
            webSocketClient.close();
        }
    }

    public void sendData(String s){
        if (webSocketClient != null && webSocketClient.getReadyState() == WebSocket.READYSTATE.OPEN) {
            webSocketClient.send(s);
        }
    }

}
