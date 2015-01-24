package wirthual.com.visualizer.effects;

import android.media.audiofx.Visualizer;
import android.util.Log;

import java.util.Map;
import java.util.TreeMap;

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

        TreeMap<Integer,Integer> map = new TreeMap<Integer,Integer>();

        print("Realteil 0: " + fft[0]);
        int freq = calculateFrequency(0,samplingRate);
        print("Realteil 64: " + fft[1]);
        int freqk = calculateFrequency(64,samplingRate);

        map.put(freq,(int)fft[0]);
        map.put(freqk,(int)fft[1]);


        for(int i=2;i<fft.length;i=i+2){
            int rfk = fft[i];
            String real = String.valueOf(rfk);
            int ifk = fft[i+1];
            String img = String.valueOf(ifk);

            int freqency = calculateFrequency(i/2,samplingRate);

            float magnitude = (float) Math.sqrt((double) (rfk * rfk + ifk * ifk));
            int dbValue = (int) (10 * Math.log10(magnitude));

            //String output ="Nummber: " + String.valueOf(i/2) + " Realteil: " + real + " Imaginartteil: " + img + " Frequenz: " + String.valueOf(freqency) + " DBValue: " + String.valueOf(dbValue);
            //print(output);
            map.put(freqency,(int)magnitude);
        }

        for(Map.Entry<Integer,Integer> entry : map.entrySet()) {
            System.out.println(String.valueOf(entry.getKey()) + " => " + String.valueOf(entry.getValue()));
        }





    }

    private void print(String s) {
        Log.i(s, TAG);
    }


    private void print(String s, int i) {
        Log.i(s + String.valueOf(i), TAG);
    }

    private int calculateFrequency(int k, int samplingrate){
        return Math.abs((k * samplingrate)/64)/1000;
    }
}
