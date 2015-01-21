package wirthual.com.visualizer;

import android.media.audiofx.Visualizer;

/**
 * Created by devbuntu on 20.01.15.
 */
public class AudioDataListener implements Visualizer.OnDataCaptureListener {

    AudioFttProcessor processor = null;

    public AudioDataListener(AudioFttProcessor audioFttProcessor) {
        processor = audioFttProcessor;
    }

    @Override
    public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {

    }

    @Override
    public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
        processor.processData(fft);
    }
}
