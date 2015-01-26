package com.wirthual.hyperionauvi.effects;

import android.content.Context;
import android.graphics.Color;
import android.media.audiofx.Visualizer;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by devbuntu on 23.01.15.
 */
public class LevelEffect extends Effect implements Visualizer.OnDataCaptureListener {

    final String TAG = "LevelEffect";

    int topBottomLeds = 0;
    int leftRightLeds = 0;
    int totalLeds = 0;

    List<Integer> colors = new ArrayList<Integer>();

    public LevelEffect(Context con,int topBottom,int leftRight){
        super(con);
        topBottomLeds = topBottom;
        leftRightLeds = leftRight;
        totalLeds = topBottomLeds *2 + leftRightLeds * 2;

        int stepSize = 360/leftRightLeds;
        for(int i=0;i<leftRightLeds;i++){
            float hue = i*stepSize;
            int rgb = Color.HSVToColor(new float[]{hue,1.0f,1.0f});
            int red = Color.red(rgb);
            int green = Color.green(rgb);
            int blue = Color.blue(rgb);
            colors.add(red);
            colors.add(green);
            colors.add(blue);
        }
    }

    @Override
    public String getName() {
        return TAG;
    }

    @Override
    public boolean isWaveformEffect() {
        return false;
    }

    @Override
    public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {

    }

    @Override
    public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {

        List<Float> testList=new ArrayList();

        for(int i=2;i<fft.length;i=i+2){
            int rfk = fft[i];
            String real = String.valueOf(rfk);
            int ifk = fft[i+1];
            String img = String.valueOf(ifk);

            float magnitude = (float) Math.sqrt((double) (rfk * rfk + ifk * ifk));
            int dbValue = (int) (10 * Math.log10(magnitude));
            testList.add(magnitude);
        }

        Collections.sort(testList);
        Collections.reverse(testList);
        int highestMagnitude = (int)Math.round(testList.get(0));
        Log.i("Highest Magnitude: " +highestMagnitude,TAG);




        int magnitude_min = 10;
        int magnitude_max = 190;

        int norm_val = normalize(testList.get(0),magnitude_min,magnitude_max);
        Log.i(String.valueOf(norm_val),TAG);

        int area1End = topBottomLeds;
        int area2End = topBottomLeds + leftRightLeds;
        int area3End = 2* topBottomLeds + leftRightLeds;


        JSONArray colorJson = new JSONArray();
        for (int i = 0; i < totalLeds; i++) {
            colorJson.put(0);
            colorJson.put(0);
            colorJson.put(0);
        }
     /*   for (int i = 0; i < topBottomLeds; i++) {
            try {
            colorJson.put(i*3,255);
            colorJson.put(i*3+1,0);
            colorJson.put(i*3+2,0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } */
        for (int i = 0;i<norm_val; i++){
            int red = colors.get(i*3);
            int green = colors.get(i * 3 + 1);
            int blue = colors.get(i * 3 + 2);
            try {
            int index1 = (topBottomLeds*3)+3*i;
            int index2 = (topBottomLeds*3)+3*i+1;
            int index3 = (topBottomLeds*3)+3*i+2;

            colorJson.put(index1,red);
            colorJson.put(index2, green);
            colorJson.put(index3,blue);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0;i<norm_val; i++){
            int red = colors.get(i*3);
            int green = colors.get(i*3+1);
            int blue = colors.get(i*3+2);
            try {
                int index1 = ((totalLeds)*3-1)-(3*i+2);
                int index2 = ((totalLeds)*3-1)-(3*i+1);
                int index3 = ((totalLeds)*3-1)-3*i;



                colorJson.put(index1,red);
                colorJson.put(index2,green);
                colorJson.put(index3,blue);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        Log.i(String.valueOf(colorJson.length()), TAG);

        JSONObject commandJson = new JSONObject();
        try {
            commandJson.put("command", "color");
            commandJson.put("color", colorJson);
            commandJson.put("priority", 100);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String commandString = commandJson.toString() + "\n";
        sendData(commandString);

    }

    private void print(String s) {
        Log.i(s, TAG);
    }


    private void print(String s, int i) {
        Log.i(s + String.valueOf(i), TAG);
    }

    private int calculateFrequency(int k, int samplingrate){
        return Math.abs((k * samplingrate)/64)/1000;
    }
    private int normalize(float value, int min, int max) {
        if (value < min) {
            return 0;
        }
        if (value > max) {
            return leftRightLeds;
        } else {
            return (int) (((value - min) / (max - min)) * leftRightLeds);
        }
    }


}
