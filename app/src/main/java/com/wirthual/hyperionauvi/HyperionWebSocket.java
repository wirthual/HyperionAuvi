package com.wirthual.hyperionauvi;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.wirthual.hyperionauvi.service.AudioAnalyzeService;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Created by devbuntu on 19.01.15.
 */
public class HyperionWebSocket extends WebSocketClient {

    String TAG = "com.wirthual.visualizer.HyperionWebSocket";

    final static int ID_ABORTNOTIFICATION = 808;
    final static int ID_RUNNINGNOTIFICATION = 809;

    final static int WS_TIMEOUT = 1; //s


    Context context = null;

    boolean isOpen = false;

    SharedPreferences prefs;
    SharedPreferences.Editor e;
    NotificationManager notificationManager;

    public HyperionWebSocket(Context con,URI uri) {
        super(uri,new Draft_17(),null,WS_TIMEOUT);
        context = con;

        notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        e = prefs.edit();



    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.i("WEBSOCKET OPENED", TAG);
        makeRunningNotification();
        isOpen = true;
    }

    @Override
    public void onMessage(String message) {
        Log.i("Message Received: " + message, TAG);
    }


    @Override
    public void onClose(int code, String reason, boolean remote) {
        Intent intent = new Intent(context, AudioAnalyzeService.class);
        if(isOpen==true){
            makeAbortNotification();
            context.stopService(intent);
            isOpen = false;
        }
    }

    @Override
    public void onError(Exception ex) {
        Log.i(ex.getMessage(), TAG);
        //Toast.makeText(context, "Cannot connect to hyperion.", Toast.LENGTH_SHORT).show();
        String msg = context.getText(R.string.cannotconnect).toString();
        new ToastMessageTask().execute(msg);
    }

    private void makeRunningNotification() {
        Intent notifyIntent = new Intent(context, HyperionAudioVisualizer.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        // Creates the PendingIntent
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle(context.getText(R.string.notificationTitle))
                .setSmallIcon(R.drawable.ic_launcher).setContentText(context.getText(R.string.notificationText))
                .setAutoCancel(true).setOngoing(true).setContentIntent(pendingIntent);


        notificationManager.cancel(ID_ABORTNOTIFICATION);
        notificationManager.notify(ID_RUNNINGNOTIFICATION,mBuilder.build());
    }

    private void makeAbortNotification() {
        Intent notifyIntent = new Intent(context, HyperionAudioVisualizer.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        // Creates the PendingIntent
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle(context.getText(R.string.notificationTitleAbort))
                .setSmallIcon(R.drawable.ic_launcher).setContentText(context.getText(R.string.notificationTextAbort))
                .setAutoCancel(false).setOngoing(false).setContentIntent(pendingIntent);

        notificationManager.cancel(ID_RUNNINGNOTIFICATION);
        notificationManager.notify(ID_ABORTNOTIFICATION,mBuilder.build());

        e.putBoolean("running",false);
        e.commit();
    }

    private class ToastMessageTask extends AsyncTask<String, String, String> {
        String toastMessage;

        @Override
        protected String doInBackground(String... params) {
            toastMessage = params[0];
            return toastMessage;
        }

        protected void OnProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
        // This is executed in the context of the main GUI thread
        protected void onPostExecute(String result){
            Toast toast = Toast.makeText(context.getApplicationContext(), result, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
