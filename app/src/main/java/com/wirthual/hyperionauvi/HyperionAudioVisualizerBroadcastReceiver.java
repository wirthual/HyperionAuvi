package com.wirthual.hyperionauvi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by devbuntu on 28.01.15.
 */
public class HyperionAudioVisualizerBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action){
            case HyperionSocket.WS_OPEN:
                ((HyperionAudioVisualizer)context).updateUI(HyperionSocket.WS_OPEN);
                break;
            case HyperionSocket.WS_CLOSED:
                ((HyperionAudioVisualizer)context).updateUI(HyperionSocket.WS_CLOSED);
                break;
            case HyperionSocket.WS_ERROR:
                Toast.makeText(context,"Error in connection. Check IP and Port",Toast.LENGTH_SHORT).show();
            case HyperionSocket.WS_MESSAGE:
                break;
        }


    }
}
