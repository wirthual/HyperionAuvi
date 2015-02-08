package com.wirthual.hyperionauvi.effects;

import android.media.audiofx.Visualizer;

import com.wirthual.hyperionauvi.HyperionConfig;
import com.wirthual.hyperionauvi.HyperionSocket;

import org.json.JSONArray;

/**
 * Created by devbuntu on 20.01.15.
 */
public class ThreeZonesEffect extends com.wirthual.hyperionauvi.effects.Effect implements Visualizer.OnDataCaptureListener {

    public static final String TAG = "ThreeZonesEffect";

    HyperionConfig config;
    ThreeZonesProcessor processor;


    public ThreeZonesEffect(HyperionSocket socket) {
        super(socket);
        config = HyperionConfig.getInstance();
        processor = new ThreeZonesProcessor(config.getTopBottomLeds(),config.getLeftRightLeds());
    }

    @Override
    public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
            //Do nothing
    }

    @Override
    public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
            JSONArray result = processor.processData(fft,samplingRate);
            super.sendData(result,config.getPrio());
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean isWaveformEffect() {
        return false;
    }

    @Override
    public boolean isFFTEffect() {
        return true;
    }

}
