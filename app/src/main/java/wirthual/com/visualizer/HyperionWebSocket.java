package wirthual.com.visualizer;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Created by devbuntu on 19.01.15.
 */
public class HyperionWebSocket extends WebSocketClient {

    String TAG = "wirthual.com.visualizer.HyperionWebSocket";

    Context context = null;

    boolean isOpen = false;

    SharedPreferences prefs;
    SharedPreferences.Editor e;

    public HyperionWebSocket(Context con,URI uri) {
        super(uri);
        context = con;

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        e = prefs.edit();
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor e = prefs.edit();
        e.putBoolean("websocket_connected",true);
        e.commit();
        Log.i("WEBSOCKET OPENED", TAG);
        makeNotification();
    }

    @Override
    public void onMessage(String message) {
        Log.i("Message Received: " + message, TAG);
    }


    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.i("WEBSOCKET CLOSED", TAG);
        e.putBoolean("websocket_connected",false);
        e.commit();
    }

    @Override
    public void onError(Exception ex) {
        Log.i(ex.getMessage(), TAG);
        e.putString("websocket_error",ex.getLocalizedMessage());
        e.commit();

    }

    private void makeNotification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle(context.getText(R.string.notificationTitle))
                .setSmallIcon(R.drawable.ic_launcher).setContentText(context.getText(R.string.notificationText))
                .setAutoCancel(false).setOngoing(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify("Running",0,mBuilder.build());
    }
}
