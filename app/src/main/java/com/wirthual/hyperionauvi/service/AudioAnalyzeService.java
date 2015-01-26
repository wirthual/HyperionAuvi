package com.wirthual.hyperionauvi.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.Visualizer;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.wirthual.hyperionauvi.R;
import com.wirthual.hyperionauvi.effects.Effect;
import com.wirthual.hyperionauvi.effects.LevelEffect;
import com.wirthual.hyperionauvi.effects.ThreeZonesEffect;

import org.java_websocket.WebSocket;

/**
 * Created by devbuntu on 24.01.15.
 */
public class AudioAnalyzeService extends Service {

    public static final String TAG ="AudioService";
    public static final int THREEZONESEFFECT =1;
    public static final int LEVELEFFECT =2;


    Visualizer mVisualizer;
    ThreeZonesEffect listener;
    Effect currentEffect;

    SharedPreferences prefs;
    SharedPreferences.Editor e;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Service started",TAG);

        int eff = intent.getIntExtra("effect",THREEZONESEFFECT);

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        e = prefs.edit();


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

        //listener = new ThreeZonesEffect(this,topBottomLeds,leftRightLeds);
        if(eff == LEVELEFFECT) {
            currentEffect = new LevelEffect(this, topBottomLeds, leftRightLeds);
        }else if(eff == THREEZONESEFFECT){
            currentEffect = new ThreeZonesEffect(this,topBottomLeds,leftRightLeds);
        }

        WebSocket.READYSTATE state = currentEffect.openWebSocket(ip, port);
        if(state == WebSocket.READYSTATE.OPEN) {
            Toast.makeText(this, getText(R.string.connected), Toast.LENGTH_SHORT).show();
            e.putBoolean("running",true);
            e.commit();
        }else {
            stopSelf();
        }

        mVisualizer.setDataCaptureListener(currentEffect, Visualizer.getMaxCaptureRate() / intRate, false, true);
        mVisualizer.setCaptureSize(128);

        mVisualizer.setEnabled(true);



        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Service stopped",TAG);
        e.putBoolean("running",false);
        e.commit();
        mVisualizer.setEnabled(false);
        currentEffect.closeWebSocket();
    }
}
