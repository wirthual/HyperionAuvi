package com.wirthual.hyperionauvi.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.audiofx.Visualizer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.wirthual.hyperionauvi.HyperionAudioVisualizer;
import com.wirthual.hyperionauvi.HyperionConfig;
import com.wirthual.hyperionauvi.HyperionSocket;
import com.wirthual.hyperionauvi.OpenWebsocketsTask;
import com.wirthual.hyperionauvi.R;
import com.wirthual.hyperionauvi.effects.Effect;
import com.wirthual.hyperionauvi.effects.LevelEffect;
import com.wirthual.hyperionauvi.effects.ThreeZonesEffect;

/**
 * Created by devbuntu on 28.01.15.
 */
public class AnalyzeService extends Service{

    final static String TAG = "AnalyzeService";
    public static final int THREEZONESEFFECT =1;
    public static final int LEVELEFFECT =2;


    HyperionSocket socket;
    BroadcastReceiver receiver;
    Visualizer mVisualizer;
    HyperionConfig config;
    Effect currentEffect;
    int eff;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        eff = intent.getIntExtra("effect",THREEZONESEFFECT);

         receiver = new AnalyzeServiceBroadcastReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(HyperionSocket.WS_OPEN);
        intentFilter.addAction(HyperionSocket.WS_CLOSED);
        intentFilter.addAction(HyperionSocket.WS_ERROR);
        intentFilter.addAction(HyperionSocket.WS_MESSAGE);

        this.registerReceiver(receiver,intentFilter);

        config = (HyperionConfig)intent.getSerializableExtra("config");
        socket = new HyperionSocket(this,config.getUri());

        OpenWebsocketsTask task = new OpenWebsocketsTask();
        task.execute(socket);

        return Service.START_NOT_STICKY;
    }

    public void startAnalyzing(){
        startForeground(99,makeRunningNotification()); //If service starts analyzing, bring it to foreground.
                                                       //then it doesnt stop if activity is destroyed.
        Log.i("Start analyzing", TAG);

        mVisualizer = new Visualizer(0); //Id= 0 for output mix. Works just with aePlayer and Google Play Music
        int minRange = Visualizer.getCaptureSizeRange()[0];
        Log.i(TAG, String.valueOf(minRange));
        int maxRange = Visualizer.getCaptureSizeRange()[1];
        Log.i(TAG, String.valueOf(maxRange));
        mVisualizer.setCaptureSize(minRange);


        if(eff == LEVELEFFECT) {
            currentEffect = new LevelEffect(socket, config.getTopBottomLeds(), config.getLeftRightLeds());
        }else if(eff == THREEZONESEFFECT){
            currentEffect = new ThreeZonesEffect(socket,config.getTopBottomLeds(),config.getLeftRightLeds());
        }

        mVisualizer.setDataCaptureListener(currentEffect, Visualizer.getMaxCaptureRate() / config.getRate(), false, true);
        mVisualizer.setCaptureSize(128);

        mVisualizer.setEnabled(true);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(receiver);
        if(mVisualizer!=null) {
            mVisualizer.setEnabled(false);
        }
        socket.close();
        Log.i("Service stopped", TAG);
    }


    private Notification makeRunningNotification() {
        Intent notifyIntent = new Intent(this, HyperionAudioVisualizer.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        // Creates the PendingIntent
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle(getText(R.string.notificationTitle))
                .setSmallIcon(R.drawable.ic_launcher).setContentText(getText(R.string.notificationText))
                .setAutoCancel(true).setOngoing(true).setContentIntent(pendingIntent);


        return mBuilder.build();
    }

}
