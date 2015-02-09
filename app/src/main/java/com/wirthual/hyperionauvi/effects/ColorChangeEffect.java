package com.wirthual.hyperionauvi.effects;

import android.graphics.Color;
import android.media.audiofx.Visualizer;
import android.os.Handler;

import com.wirthual.hyperionauvi.HyperionConfig;
import com.wirthual.hyperionauvi.HyperionSocket;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by devbuntu on 23.01.15.
 */
public class ColorChangeEffect extends BaseEffect implements Visualizer.OnDataCaptureListener {

    public static final String TAG = "ColorChangeEffect";

    HyperionConfig config;

    // COLORSTEPS * COLORCHANGE is the amount of time for one color-circle
    private final int COLORSTEPS = 360;
    private final int COLORCHANGE = 600; //ms

    private int currentRed = 255;
    private int currentGreen = 0;
    private int currentBlue = 0;

    int j = 0;
    int k = 1;


    int topBottomLeds = 0;
    int leftRightLeds = 0;
    int totalLeds = 0;
    int offset;
    int prio;

    List<Integer> colors = new ArrayList<Integer>();
    Handler handler = new Handler();

    public ColorChangeEffect(HyperionSocket socket){
        super(socket);
       config = HyperionConfig.getInstance();
        topBottomLeds = config.getTopBottomLeds();
        leftRightLeds = config.getLeftRightLeds();
        totalLeds = topBottomLeds *2 + leftRightLeds * 2;
        this.offset = config.getOffset();
        this.prio = config.getPrio();

        int stepSize = 360/COLORSTEPS;
        for(int i=0;i<COLORSTEPS;i++){
            float hue = i*stepSize;
            int rgb = Color.HSVToColor(new float[]{hue,1.0f,1.0f});
            int red = Color.red(rgb);
            int green = Color.green(rgb);
            int blue = Color.blue(rgb);
            colors.add(red);
            colors.add(green);
            colors.add(blue);
        }


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                 changeColor();
                 handler.postDelayed(this, COLORCHANGE);
            }
        };
        runnable.run();
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
    public boolean isFFTEffect() { return true; }

    @Override
    public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
        //Do nothing, this effect only uses waveform data
    }

    @Override
    public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {

        List<Float> magnitudesList=new ArrayList();

        for(int i=2;i<fft.length;i=i+2){
            int rfk = fft[i];
            int ifk = fft[i+1];

            float magnitude = (float) Math.sqrt((double) (rfk * rfk + ifk * ifk));
            magnitudesList.add(magnitude);
        }

        Collections.sort(magnitudesList);
        Collections.reverse(magnitudesList);
        int highestMagnitude = (int)Math.round(magnitudesList.get(0));
        //Log.i("Highest Magnitude: " +highestMagnitude,TAG);

        float norm_val = normalize(highestMagnitude,config.getChangecolor_min(),config.getChangecolor_max());
        //Log.i(String.valueOf(norm_val),TAG);

        JSONArray colorJson = new JSONArray();
        for (int i = 0; i < totalLeds; i++) {
            try {
                colorJson.put(norm_val*currentRed);
                colorJson.put(norm_val*currentBlue);
                colorJson.put(norm_val*currentGreen);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Log.i(String.valueOf(colorJson.length()), TAG);
        super.sendData(colorJson, config.getPrio());
    }

    /**
     * Normalize given value to a float value between 0 and 1
     * @param value Value to be normalized
     * @param min Lower bound. Everything below this value is 0
     * @param max Upper bound. Everything above this value is 1
     * @return normalized value
     */
    private float normalize(float value, int min, int max) {
        if (value < min) {
            return 0;
        }
        if (value > max) {
            return 1;
        } else {
            return (((value - min) / (max - min)));
        }
    }

    /**
     * Changes the Color-Values r,g,b to the next value in colors-Array. If end is reached, go backwards to
     * beginning of Array.
     */
    public void changeColor(){
        if(j==COLORSTEPS-1){
            k=-1;
        }
        if(j==0){
            k=1;
        }
        this.j=this.j+k;

        currentRed = colors.get(j*3);
        currentGreen = colors.get(j*3+1);
        currentBlue = colors.get(j*3+2);
    }

}
