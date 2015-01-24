package wirthual.com.visualizer.effects;

import android.util.Log;

import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by wirthual on 21.01.15.
 */
public class ThreeZonesProcessor {

    String TAG = "ThreeZonesEffect";
    boolean debug = false;

    int topBottomLeds = 0;
    int leftRightLeds = 0;
    int totalLeds = 0;

    public ThreeZonesProcessor(int topBottom, int leftRight) {
        topBottomLeds = topBottom;
        leftRightLeds = leftRight;
        totalLeds = topBottomLeds *2 + leftRightLeds * 2;
    }

    //Settings
    int t_bass_min = 30;
    int t_bass_max = 100;
    int t_middle_min = 5;
    int t_middle_max = 25;
    int t_high_min = 2;
    int t_high_max = 18;

    public String processData(byte[] bytes) {
        byte[] bass = ArrayUtils.subarray(bytes, 0, 20);
        byte[] middle = ArrayUtils.subarray(bytes, 21, 40);
        byte[] high = ArrayUtils.subarray(bytes, 41, 60);

        List bassList = Arrays.asList(ArrayUtils.toObject(bass));
        List middleList = Arrays.asList(ArrayUtils.toObject(middle));
        List highList = Arrays.asList(ArrayUtils.toObject(high));

        byte maxBass = (byte) Collections.max(bassList);
        byte maxMiddle = (byte) Collections.max(middleList);
        byte maxHigh = (byte) Collections.max(highList);

        int normalized_max_bass = normalize_to_rgb_value(maxBass, t_bass_min, t_bass_max);
        int normalized_max_middle = normalize_to_rgb_value(maxMiddle, t_middle_min, t_middle_max);
        int normalized_max_high = normalize_to_rgb_value(maxHigh, t_high_min, t_high_max);

        if (debug) {
            Log.d("Max(bass) = " + maxBass, TAG);
            Log.d("Max(middle) = " + maxMiddle, TAG);
            Log.d("Max(high) = " + maxHigh, TAG);
            Log.d("Normalized max(bass) to RGB is '" + normalized_max_bass + "'.", TAG);
            Log.d("Normalized max(middle) to RGB is '" + normalized_max_middle + "'.", TAG);
            Log.d("Normalized max(high) to RGB is '" + normalized_max_high + "'.", TAG);
        }


        return CreateCommandString(normalized_max_bass, normalized_max_middle, normalized_max_high);
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


    private String CreateCommandString(int normalized_max_bass, int normalized_max_middle, int normalized_max_high) {

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
        return commandString;
    }
}
