package com.wirthual.hyperionauvi.effects;

import android.util.Log;

import com.wirthual.hyperionauvi.HyperionConfig;
import com.wirthual.hyperionauvi.utils.AudioUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by wirthual on 21.01.15.
 */
public class ThreeZonesProcessor {

    String TAG = "ThreeZonesEffect";
    boolean debug = false;
    HyperionConfig config;


    int topBottomLeds = 0;
    int leftRightLeds = 0;
    int totalLeds = 0;

    public ThreeZonesProcessor(int leftRightLeds,int topBottomLeds) {
        config = HyperionConfig.getInstance();
        this.topBottomLeds = topBottomLeds;
        this.leftRightLeds = leftRightLeds;
        totalLeds = topBottomLeds *2 + leftRightLeds * 2;
    }

    public JSONArray processData(byte[] bytes,int samplingRate) {

        TreeMap<Integer,Integer> map = new TreeMap<Integer,Integer>();


        //Byte 0 and 1 are special. See doku of Visualizer
        int freq0 = AudioUtils.getFrequency(0, samplingRate,bytes.length);
        int freqkhalf =  AudioUtils.getFrequency(bytes.length/2, samplingRate,bytes.length);

        int mag0 = AudioUtils.getMangnitude(bytes[0],(byte)0);
        int magk2half = AudioUtils.getMangnitude(bytes[1],(byte)0);

        map.put(freq0,mag0);
        map.put(freqkhalf,magk2half);


        for(int i=2;i<bytes.length/2;i=i+2){
            byte real = bytes[i];
            byte imag = bytes[i+1];

            int freqency = AudioUtils.getFrequency(i / 2, samplingRate,bytes.length);
            int magnitude = AudioUtils.getMangnitude(real,imag);

            //String output ="Nummber: " + String.valueOf(i/2) + " Realteil: " + real + " Imaginartteil: " + imag + " Frequenz: " + String.valueOf(freqency);
            //Log.i(TAG,output);

            map.put(freqency, (int) magnitude);
        }

        //for(Map.Entry<Integer,Integer> entry : map.entrySet()) {
        //   Log.i(TAG,String.valueOf(entry.getKey()) + " => " + String.valueOf(entry.getValue()));
        //}


        Collection sortedValues = map.values();
        ArrayList<Integer> sortedList = new ArrayList<Integer>(sortedValues );

        int newSize = map.size()/2;//take only half of the map
        int index1 = (newSize)/3; //First third is border between bass and middle
        int index2 = ((newSize)/3)*2; //Second third is border between middle and high

        List<Integer> bass = sortedList.subList(0,5);
        List<Integer> middle = sortedList.subList(6,15);
        List<Integer> high = sortedList.subList(16, newSize);


        int maxBass =    Collections.max(bass);
        int maxMiddle = Collections.max(middle);
        int maxHigh =   Collections.max(high);

        int normalized_max_bass = normalize_to_rgb_value(maxBass, config.getBass_min(), config.getBass_max());
        int normalized_max_middle = normalize_to_rgb_value(maxMiddle, config.getMiddle_min(),config.getMiddle_max());
        int normalized_max_high = normalize_to_rgb_value(maxHigh, config.getHigh_min(), config.getHigh_max());

        if (debug) {
            Log.d("Max(bass) = " + maxBass, TAG);
            Log.d("Max(middle) = " + maxMiddle, TAG);
            Log.d("Max(high) = " + maxHigh, TAG);
            Log.d("Normalized max(bass) to RGB is '" + normalized_max_bass + "'.", TAG);
            Log.d("Normalized max(middle) to RGB is '" + normalized_max_middle + "'.", TAG);
            Log.d("Normalized max(high) to RGB is '" + normalized_max_high + "'.", TAG);
        }

        return CreateColorArray(normalized_max_bass, normalized_max_middle, normalized_max_high);
    }

    public String getName() {
        return TAG;
    }

    public boolean isWaveformEffect() {
        return false;
    }

    private int normalize_to_rgb_value(float value, int min, int max) {
        if (value < min) {
            if (debug) {
                Log.d("The value recorded was lower than the given min value. Maybe think about decreasing the min value. Current value = "
                        + value + ", current min = " + min, TAG);
            }
            return 0;
        }
        if (value > max) {
            if (debug) {
                Log.d("The value recorded was higher than the given max. Maybe think about increasing the max value. Current value = "
                        + value + ", current max = " + max, TAG);
            }
            return 255;
        } else {
            return (int) (((value - min) / (max - min)) * 255);
        }
    }


    private JSONArray CreateColorArray(int normalized_max_bass, int normalized_max_middle, int normalized_max_high) {

        int edgeOverlap = leftRightLeds / 4;

        int area1End = topBottomLeds + edgeOverlap; //46
        int area2End = area1End + leftRightLeds / 2;  //46 + 12 = 58
        int area3End = area2End + edgeOverlap + topBottomLeds + edgeOverlap; //110d
        int area4End = totalLeds - edgeOverlap; //122


        JSONArray colorJson = new JSONArray();
        for (int i = 0; i < totalLeds; i++) {
            if (i < area1End || i >= area4End) {
                colorJson.put(normalized_max_bass);
                colorJson.put(0);
                colorJson.put(0);
            }
            if (i >= area1End && i < area2End) {
                colorJson.put(0);
                colorJson.put(normalized_max_middle);
                colorJson.put(0);
            }
            if (i >= area2End && i < area3End) {
                colorJson.put(0);
                colorJson.put(0);
                colorJson.put(normalized_max_high);
            }
            if (i >= area3End && i < area4End) {
                colorJson.put(0);
                colorJson.put(normalized_max_middle);
                colorJson.put(0);
            }
        }
        if(debug){
        Log.i(String.valueOf(colorJson.length()), TAG);
        }

        return colorJson;
    }

    //From http://stackoverflow.com/questions/13678387/how-to-split-array-list-into-equal-parts
    public static <T> ArrayList<T[]> chunks(ArrayList<T> bigList,int n){
        ArrayList<T[]> chunks = new ArrayList<T[]>();

        for (int i = 0; i < bigList.size(); i += n) {
            T[] chunk = (T[])bigList.subList(i, Math.min(bigList.size(), i + n)).toArray();
            chunks.add(chunk);
        }

        return chunks;
    }

}
