package wirthual.com.visualizer;

import android.media.audiofx.Visualizer;
import android.util.Log;

import org.java_websocket.WebSocket;

import java.net.URI;

import wirthual.com.visualizer.effects.ThreeZonesEffect;

/**
 * Created by devbuntu on 20.01.15.
 */
public class AudioDataListener implements Visualizer.OnDataCaptureListener {

    String TAG = "AudioDataListener";

    HyperionWebSocket webSocketClient = null;
    ThreeZonesEffect currentEffect;


    public AudioDataListener(int topbottom,int leftRight) {
        currentEffect = new ThreeZonesEffect(topbottom,leftRight);
    }

    @Override
    public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
            //Do nothing
    }

    @Override
    public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {

        if (webSocketClient != null && webSocketClient.getReadyState() == WebSocket.READYSTATE.OPEN) {
            String result = currentEffect.processData(fft);
            sendData(result);
        }

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

    private void sendData(String s){
        if (webSocketClient != null && webSocketClient.getReadyState() == WebSocket.READYSTATE.OPEN) {
            webSocketClient.send(s);
        }
    }

}
