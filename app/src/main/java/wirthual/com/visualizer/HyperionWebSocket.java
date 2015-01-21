package wirthual.com.visualizer;

import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Created by devbuntu on 19.01.15.
 */
public class HyperionWebSocket extends WebSocketClient {

    String TAG = "wirthual.com.visualizer.HyperionWebSocket";

    public HyperionWebSocket(URI uri) {
        super(uri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.i("WEBSOCKET OPENED", TAG);
    }

    @Override
    public void onMessage(String message) {
        Log.i("Message Received: " + message, TAG);
    }


    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.i("WEBSOCKET CLOSED", TAG);
    }

    @Override
    public void onError(Exception ex) {
        Log.i(ex.getMessage(), TAG);
    }
}
