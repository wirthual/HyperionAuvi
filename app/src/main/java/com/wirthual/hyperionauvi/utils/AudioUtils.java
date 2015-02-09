package com.wirthual.hyperionauvi.utils;

/**
 * Created by devbuntu on 29.01.15.
 */
public class AudioUtils {

    /**
     * Method for calculation magnitude
     * @param real real-part
     * @param imag imag-part
     * @return magnitude as int
     */
    public static int getMangnitude(byte real,byte imag){
        return  (int)Math.sqrt((double) (real * real + imag * imag));
    }

    /**
     * Method for calculation the Frequency for values with index k and samplingrate
     * @param k index of the values
     * @param samplingrate samplingrate of the received bytes
     * @return frequency
     */
    public static int getFrequency(int k,int samplingrate,int captureSize){
        return Math.abs((k * samplingrate)/captureSize);
    }

    /**
     * Method for calulation of decibel value
     * @param real
     * @param imag
     * @return decibel value
     */
    public static int getDBValue(byte real,byte imag){
        return  (int) (10 * Math.log10(getMangnitude(real,imag)));
    }
}
