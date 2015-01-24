package wirthual.com.visualizer.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.Visualizer;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import wirthual.com.visualizer.effects.TestEffect;
import wirthual.com.visualizer.effects.ThreeZonesEffect;

/**
 * Created by devbuntu on 24.01.15.
 */
public class AudioAnalyzeService extends IntentService {

    public static final String TAG ="AudioService";
    public static final int THREEZONESEFFECT =1;



    Visualizer mVisualizer;
    ThreeZonesEffect listener;

    SharedPreferences prefs;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AudioAnalyzeService(String name) {
        super(name);
        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onHandleIntent(Intent intent){
        String ip = prefs.getString("ip", "192.168.2.105");
        String port = prefs.getString("port", "19444");

        String rate = prefs.getString("rate", "2");
        int intRate = Integer.valueOf(rate);
        if (intRate < 2) {
            intRate = 2;
        }

        int topBottomLeds = Integer.valueOf(prefs.getString("topBottom", "40"));
        Log.i("Number Leds Top/Bottom: " + String.valueOf(topBottomLeds), TAG);
        int leftRightLeds = Integer.valueOf(prefs.getString("leftRight", "24"));
        Log.i("Number Leds Left/Right: " + String.valueOf(leftRightLeds), TAG);


        mVisualizer = new Visualizer(0);
        int minRange = Visualizer.getCaptureSizeRange()[0];
        Log.i(TAG, String.valueOf(minRange));
        int maxRange = Visualizer.getCaptureSizeRange()[1];
        Log.i(TAG, String.valueOf(maxRange));
        mVisualizer.setCaptureSize(minRange);

        //processor = new AudioFttProcessor(topBottomLeds, leftRightLeds);
        //processor.openWebSocket(ip, port);

        listener = new ThreeZonesEffect(this,topBottomLeds,leftRightLeds);
        listener.openWebSocket(ip,port);

        TestEffect eff = new TestEffect();

        mVisualizer.setDataCaptureListener(eff, Visualizer.getMaxCaptureRate() / 4, false, true);
        mVisualizer.setCaptureSize(128);

        mVisualizer.setEnabled(true);


    }
}
