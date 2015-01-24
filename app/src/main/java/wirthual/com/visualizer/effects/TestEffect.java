package wirthual.com.visualizer.effects;

import android.media.audiofx.Visualizer;
import android.util.Log;

/**
 * Created by devbuntu on 23.01.15.
 */
public class TestEffect extends Effect implements Visualizer.OnDataCaptureListener {

    final String TAG = "TestEffect";

    @Override
    public String getName() {
        return TAG;
    }

    @Override
    public boolean isWaveformEffect() {
        return false;
    }

    @Override
    public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {

    }

    @Override
    public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
        print("Sampling Rate: " + samplingRate);
        print("Length of byte array: ", fft.length);

        print("Realteil 0: " + fft[0]);

        print("");

    }

    private void print(String s) {
        Log.i(s, TAG);
    }


    private void print(String s, int i) {
        Log.i(s + String.valueOf(i), TAG);
    }

    private void calculateFrequency(int n, int samplingrate){

    }
}
