package wirthual.com.visualizer;

import android.content.Context;
import android.util.Log;

import org.java_websocket.WebSocket;
import org.json.JSONObject;

import java.net.URI;

/**
 * Created by devbuntu on 20.01.15.
 */
public class AudioFttProcessor {

    String TAG = "AudioFttProcessor";

    Effect currentEffect;

    Context con = null;

    HyperionWebSocket webSocketClient = null;

    int topBottomLeds = 0;
    int leftRightLeds = 0;
    int totalLeds = 0;

    public AudioFttProcessor(int topBottomLeds, int leftRightLeds) {
        this.con = con;
        this.topBottomLeds = topBottomLeds;
        this.leftRightLeds = leftRightLeds;
        this.totalLeds = 2 * topBottomLeds + 2 * leftRightLeds;
        Log.i("Number Leds Left/Right: " + String.valueOf(leftRightLeds), TAG);

        currentEffect = new ThreeZonesEffect(topBottomLeds,leftRightLeds);

    }

    public void openWebSocket(String ip, String port) {
        Log.i("Websocket trys to connect to IP: " + ip + " Port: " + port, TAG);

        String ws = "ws://" + ip + ":" + port;
        URI uri = URI.create(ws);
        webSocketClient = new HyperionWebSocket(uri);
        webSocketClient.connect();
    }

    public void closeWebSocket() {
        if (webSocketClient != null && webSocketClient.getReadyState() == WebSocket.READYSTATE.OPEN) {
            webSocketClient.close();
        }
    }



    public void processData(byte[] bytes) {

        if (webSocketClient != null && webSocketClient.getReadyState() == WebSocket.READYSTATE.OPEN) {
            JSONObject result = currentEffect.processData(bytes);

            String withNewLine = result.toString() + "\n";

            webSocketClient.send(withNewLine);
        }
    }
}
