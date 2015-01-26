package wirthual.com.visualizer;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import wirthual.com.visualizer.effects.ThreeZonesEffect;
import wirthual.com.visualizer.service.AudioAnalyzeService;


public class HyperionAudioVisualizer extends ActionBarActivity implements View.OnClickListener,SharedPreferences.OnSharedPreferenceChangeListener {

    String TAG = "HyperionAudioVisualizer";

    Visualizer mVisualizer;
    ThreeZonesEffect listener;

    SharedPreferences prefs;
    SharedPreferences.Editor e;

    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_fx_demo);

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        //e = prefs.edit();
        //e.putBoolean("running",false);
        //e.commit();
        prefs.registerOnSharedPreferenceChangeListener(this);


        b = (Button)findViewById(R.id.button);
        b.setOnClickListener(this);
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

        //Check if analyzing is running, if yes, set button text to stop ...
        boolean running = prefs.getBoolean("running",false);
        if(running){
            b.setText(getText(R.string.stop));
        }else{
            b.setText(getText(R.string.start));
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
       /* listener.closeWebSocket();
        mVisualizer.setEnabled(false);*/
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key=="running") {
            boolean running = prefs.getBoolean(key, false);
            if(running){
                b.setText(getText(R.string.stop));
            }else{
                b.setText(getText(R.string.start));
            }
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, AudioAnalyzeService.class);
        switch (v.getId()) {
            case R.id.button:
                if(!isMyServiceRunning(AudioAnalyzeService.class)) {
                    this.getApplicationContext().startService(intent);
                }else{
                    stopService(intent);
                }

        }

    }
}
