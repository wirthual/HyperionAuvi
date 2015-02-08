package com.wirthual.hyperionauvi.utils;

/**
 * Created by devbuntu on 29.01.15.
 */
public class AudioUtils {

    public static int getMangnitude(byte real,byte imag){
        return  (int)Math.sqrt((double) (real * real + imag * imag));
    }

    public static int getFrequency(int k,int samplingrate){
        return Math.abs((k * samplingrate)/64);
    }

    public static int getDBValue(byte real,byte imag){
        return  (int) (10 * Math.log10(getMangnitude(real,imag)));
    }
}
