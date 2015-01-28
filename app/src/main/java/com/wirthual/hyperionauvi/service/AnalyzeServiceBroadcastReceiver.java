package com.wirthual.hyperionauvi.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.wirthual.hyperionauvi.HyperionSocket;

/**
 * Created by devbuntu on 28.01.15.
 */
public class AnalyzeServiceBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action){
            case HyperionSocket.WS_OPEN:
                ((AnalyzeService)context).startAnalyzing();
                break;
            case HyperionSocket.WS_CLOSED:
                ((AnalyzeService)context).stopSelf();
                break;
            case HyperionSocket.WS_ERROR:
                ((AnalyzeService)context).stopSelf();
                break;
            case HyperionSocket.WS_MESSAGE:
                break;
        }

    }
}
