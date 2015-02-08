package com.wirthual.hyperionauvi.effects;

import android.media.audiofx.Visualizer;

import com.wirthual.hyperionauvi.HyperionSocket;

import org.java_websocket.WebSocket;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by devbuntu on 21.01.15.
 */
public abstract class Effect implements Visualizer.OnDataCaptureListener {

    public static final String TAG = "Effect";

    HyperionSocket webSocketClient;

    public Effect(){}

    public Effect(HyperionSocket socket){
        webSocketClient = socket;
    }

    /**
     * Returns the name of the effects. Typically the TAG of the Class
     * @return Name of Effect
     */
    public abstract String getName();

    /**
     * Returns if this effect uses WaveformData
     * @return waveform
     */
    public abstract boolean isWaveformEffect();

    /**
     * Returns if this effect uses FFTData
     * @return fft
     */
    public abstract boolean isFFTEffect();



    /**
     * Creates the String to send to Hyperion from Color-Array and Priority
     * @param colorJson JSONArray with Color-Values (lenght totalLeds *3)
     * @param priority Priority as int to send command with
     */
    public void sendData(JSONArray colorJson, int priority){
        if (webSocketClient != null && webSocketClient.getReadyState() == WebSocket.READYSTATE.OPEN) {

            JSONObject commandJson = new JSONObject();
            try {
                commandJson.put("command", "color");
                commandJson.put("color", colorJson);
                commandJson.put("priority", priority);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String commandString = commandJson.toString() + "\n"; //Important, command has to end with newline

            webSocketClient.send(commandString);
        }
    }


}
