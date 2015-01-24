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
import android.widget.Toast;

import wirthual.com.visualizer.effects.TestEffect;
import wirthual.com.visualizer.effects.ThreeZonesEffect;


public class HyperionAudioVisualizer extends ActionBarActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    String TAG = "HyperionAudioVisualizer";

    Visualizer mVisualizer;
    ThreeZonesEffect listener;

    SharedPreferences prefs;
    SharedPreferences.Editor e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_fx_demo);

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        e = prefs.edit();
        e.putBoolean("websocket_connected",false);
        e.putString("websocket_error","");

        prefs.registerOnSharedPreferenceChangeListener(this);

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

    @Override
    protected void onPause() {
        super.onPause();
        listener.closeWebSocket();
        mVisualizer.setEnabled(false);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key=="websocket_connected") {
            boolean connected = prefs.getBoolean(key, false);
            if(connected) {
                Toast.makeText(this, "Connected to Server", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Disconnect from Server", Toast.LENGTH_SHORT).show();
            }
        }
        if(key=="websocket_error"){
            String error = prefs.getString(key, "");
            if(error!="") {
                Toast.makeText(this, "Error on connecting " + error, Toast.LENGTH_LONG).show();
                e.putString("websocket_error", "");
                e.commit();
            }
        }
    }
}
