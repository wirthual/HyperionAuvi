package com.wirthual.hyperionauvi.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.wirthual.hyperionauvi.HyperionSocket;

/**
 * Created by devbuntu on 28.01.15.
 */
public class AnalyzeServiceBR extends BroadcastReceiver {

    final String TAG = "AnalyzeServiceBR";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action){
            case HyperionSocket.WS_OPEN:
                ((AnalyzeService)context).startAnalyzing();
                break;
            case HyperionSocket.WS_CLOSED:
                Log.i(TAG,"Service Received Brodcast: WS_CLOSE");
                ((AnalyzeService)context).stopSelf();
                break;
            case HyperionSocket.WS_ERROR:
                Log.i(TAG,"Service Received Brodcast: WS_ERROR");
                ((AnalyzeService)context).stopSelf();
                break;
            case HyperionSocket.WS_CLOSED_LOCAL:
                ((AnalyzeService)context).stopSelf();
                break;
            case HyperionSocket.WS_MESSAGE:
                break;
            case HyperionSocket.WS_REMOTE_CLOSED:
                ((AnalyzeService)context).stopSelf();
                break;
        }


    }
}
