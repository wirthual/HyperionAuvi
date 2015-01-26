package com.wirthual.hyperionauvi;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.wirthual.hyperionauvi.effects.ThreeZonesEffect;
import com.wirthual.hyperionauvi.service.AudioAnalyzeService;


public class HyperionAudioVisualizer extends ActionBarActivity implements View.OnClickListener,SharedPreferences.OnSharedPreferenceChangeListener {

    String TAG = "HyperionAudioVisualizer";

    Visualizer mVisualizer;
    ThreeZonesEffect listener;

    SharedPreferences prefs;
    SharedPreferences.Editor e;

    Button b;
    Spinner spinner;

    int selectedEffect = AudioAnalyzeService.THREEZONESEFFECT;

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

        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.effects_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new EffectSpinnerChangeListener(this));

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
            spinner.setEnabled(false);
        }else{
            b.setText(getText(R.string.start));
            b.setEnabled(true);
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
                spinner.setEnabled(false);
            }else{
                b.setText(getText(R.string.start));
                spinner.setEnabled(true);
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
        intent.putExtra("effect",selectedEffect);
        switch (v.getId()) {
            case R.id.button:
                if(!isMyServiceRunning(AudioAnalyzeService.class)) {
                    this.getApplicationContext().startService(intent);
                }else{
                    stopService(intent);
                }

        }
    }

    public void setSelectedEffect(int effect){
        selectedEffect = effect;
    }
}
