package wirthual.com.visualizer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class HyperionAudioVisualizer extends ActionBarActivity {

    String TAG = "HyperionAudioVisualizer";

    Visualizer mVisualizer;
    AudioDataListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_fx_demo);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_audio_fx_demo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

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

         listener = new AudioDataListener(topBottomLeds,leftRightLeds);


        mVisualizer.setDataCaptureListener(listener, Visualizer.getMaxCaptureRate() / intRate, false, true);
        listener.openWebSocket(ip,port);

        mVisualizer.setEnabled(true);

    }

    @Override
    protected void onPause() {
        super.onPause();
        listener.closeWebSocket();
        mVisualizer.setEnabled(false);
    }
}
