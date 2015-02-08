package com.wirthual.hyperionauvi;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.wirthual.hyperionauvi.service.AnalyzeService;
import com.wirthual.hyperionauvi.settings.SettingsActivity;

import de.keyboardsurfer.android.widget.crouton.Crouton;


public class HyperionAudioVisualizer extends ActionBarActivity implements View.OnClickListener{
    String TAG = "HyperionAudioVisualizer";

    SharedPreferences prefs;

    HyperionConfig config;

    Button b;
    Spinner spinner;

    HyperionAudioVisualizerBR receiver;
    //WifiStateChangedBR wifiReceiver;

    int selectedEffect = AnalyzeService.THREEZONESEFFECT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_fx_demo);


        receiver = new HyperionAudioVisualizerBR();
        //wifiReceiver = new WifiStateChangedBR();

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        this.loadPreferences();

        prefs.registerOnSharedPreferenceChangeListener(config);


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
        String playerIdx = prefs.getString("player_preference","com.google.android.music");

        if(id == R.id.play_music){
            startNewActivity(this,playerIdx);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!isMyServiceRunning(AnalyzeService.class)) {
            UpdateGui(false);
        }else{
           UpdateGui(true);
    }

    }

    @Override
    protected void onPause() {
        super.onPause();
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
        Intent intent = new Intent(this, AnalyzeService.class);
        intent.putExtra("effect",selectedEffect);
        switch (v.getId()) {
            case R.id.button:
                if(!isMyServiceRunning(AnalyzeService.class)) {
                    Log.i(TAG, "Start new Service from " + TAG);
                    this.getApplicationContext().startService(intent);
                }else{
                    Log.i(TAG, "Stop Service from " + TAG);
                    stopService(intent);
                }
        }
        final Button clicked = ((Button)v);
        clicked.setEnabled(false); //Disable Button till answer form websocket arrives
    }

    public void setSelectedEffect(int effect){
        selectedEffect = effect;
    }


    //http://stackoverflow.com/questions/3872063/launch-an-application-from-another-application-on-android
    public void startNewActivity(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
        /* We found the activity now start the activity */
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
        /* Bring user to the market or let them choose an app? */
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + packageName));
            context.startActivity(intent);
        }
    }

    public void updateUI(String message){
        switch (message){
            case HyperionSocket.WS_OPEN:
                UpdateGui(true);
                break;
            case HyperionSocket.WS_CLOSED:
                UpdateGui(false);
                break;
        }
    }

    private void loadPreferences(){

        config = HyperionConfig.getInstance();

        String ip   =  prefs.getString("ip", "0.0.0.0");
        config.setIp(ip);
        String port  = prefs.getString("port", "19444");
        config.setPort(port);
        String rate = prefs.getString("rate", "2");

        int intRate = Integer.valueOf(rate);
        if (intRate < 2 ) {
            intRate = 2;
        }
        if (intRate > 100 ) {
            intRate = 100;
        }
        config.setRate(intRate);

        int topBottomLeds = Integer.valueOf(prefs.getString("topBottom", "40"));
        config.setTopBottomLeds(topBottomLeds);
        int leftRightLeds = Integer.valueOf(prefs.getString("leftRight", "24"));
        config.setLeftRightLeds(leftRightLeds);
        int prio = Integer.valueOf(prefs.getString("prio", "100"));
        config.setPrio(prio);
        int offset = Integer.valueOf(prefs.getString("offset", "24"));
        config.setOffset(offset);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(HyperionSocket.WS_OPEN);
        intentFilter.addAction(HyperionSocket.WS_CLOSED);
        intentFilter.addAction(HyperionSocket.WS_ERROR);
        intentFilter.addAction(HyperionSocket.WS_REMOTE_CLOSED);
        intentFilter.addAction(HyperionSocket.WS_CLOSED_LOCAL);
        this.registerReceiver(receiver,intentFilter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        this.unregisterReceiver(receiver);
        //this.unregisterReceiver(wifiReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Crouton.cancelAllCroutons();
    }

    private void UpdateGui(boolean serviceRunning){
        if(serviceRunning){
            b.setText(getText(R.string.stop));
            b.setEnabled(true);
            spinner.setEnabled(false);
        }else{
            b.setText(getText(R.string.start));
            b.setEnabled(true);
            spinner.setEnabled(true);
        }
    }
}
