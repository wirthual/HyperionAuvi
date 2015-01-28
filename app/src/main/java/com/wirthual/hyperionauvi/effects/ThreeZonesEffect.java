package com.wirthual.hyperionauvi.effects;

import android.media.audiofx.Visualizer;

import com.wirthual.hyperionauvi.HyperionSocket;

/**
 * Created by devbuntu on 20.01.15.
 */
public class ThreeZonesEffect extends com.wirthual.hyperionauvi.effects.Effect implements Visualizer.OnDataCaptureListener {

    String TAG = "AudioDataListener";

    ThreeZonesProcessor currentEffect;


    public ThreeZonesEffect(HyperionSocket socket,int topbottom, int leftRight) {
        super(socket);
        currentEffect = new ThreeZonesProcessor(topbottom,leftRight);
    }

    @Override
    public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
            //Do nothing
    }

    @Override
    public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
            String result = currentEffect.processData(fft);
            sendData(result);
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean isWaveformEffect() {
        return false;
    }

}
