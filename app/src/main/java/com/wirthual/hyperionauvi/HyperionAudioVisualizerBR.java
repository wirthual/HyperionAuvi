package com.wirthual.hyperionauvi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by devbuntu on 28.01.15.
 */
public class HyperionAudioVisualizerBR extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action){
            case HyperionSocket.WS_OPEN:
                ((HyperionAudioVisualizer)context).updateUI(HyperionSocket.WS_OPEN);
                Crouton.makeText((android.app.Activity) context,context.getText(R.string.connected), Style.CONFIRM).show();
                break;
            case HyperionSocket.WS_CLOSED_LOCAL:
                ((HyperionAudioVisualizer)context).updateUI(HyperionSocket.WS_CLOSED);
                Crouton.makeText((android.app.Activity) context,context.getText(R.string.closedconnection), Style.INFO).show();
                break;
            case HyperionSocket.WS_ERROR:
                ((HyperionAudioVisualizer)context).updateUI(HyperionSocket.WS_CLOSED);
                Crouton.makeText((android.app.Activity) context,context.getText(R.string.wserror).toString(), Style.ALERT).show();
                break;
            case HyperionSocket.WS_MESSAGE:
                break;
            case HyperionSocket.WS_REMOTE_CLOSED:
                ((HyperionAudioVisualizer)context).updateUI(HyperionSocket.WS_CLOSED);
                Crouton.makeText((android.app.Activity) context,context.getText(R.string.gotclosed).toString(), Style.INFO).show();
                break;
        }


    }
}
