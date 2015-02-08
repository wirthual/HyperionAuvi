package com.wirthual.hyperionauvi;

import android.content.SharedPreferences;

import java.net.URI;

/**
 * Created by devbuntu on 27.01.15.
 */
public class HyperionConfig implements SharedPreferences.OnSharedPreferenceChangeListener{

    final String TAG = "HyperionConfig";
    private static HyperionConfig instance;

    String ip;
    String port;

    int topBottomLeds;
    int leftRightLeds;

    int prio;
    int offset;
    int rate;

    //Effekt 3 Zones
    int bass_min = 20;
    int bass_max = 90;
    int middle_min = 10;
    int middle_max = 70;
    int high_min = 10;
    int high_max = 60;

    //Effect Rainbow
    int rainbow_min = 10;
    int rainbow_max = 180;

    //Effect change color
    int changecolor_min = 20;
    int changecolor_max = 120;

    public int getChangecolor_min() {
        return changecolor_min;
    }

    public void setChangecolor_min(int changecolor_min) {
        this.changecolor_min = changecolor_min;
    }

    public int getChangecolor_max() {
        return changecolor_max;
    }

    public void setChangecolor_max(int changecolor_max) {
        this.changecolor_max = changecolor_max;
    }

    public int getRainbow_min() {
        return rainbow_min;
    }

    public void setRainbow_min(int rainbow_min) {
        this.rainbow_min = rainbow_min;
    }

    public int getRainbow_max() {
        return rainbow_max;
    }

    public void setRainbow_max(int rainbow_max) {
        this.rainbow_max = rainbow_max;
    }

    private HyperionConfig(){

    }

    public static HyperionConfig getInstance() {
        if (instance == null) {
            instance = new HyperionConfig();
        }
        return instance;
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip=ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public int getTopBottomLeds() {
        return topBottomLeds;
    }

    public int getBass_min() {
        return bass_min;
    }

    public void setBass_min(int bass_min) {
        this.bass_min = bass_min;
    }

    public int getBass_max() {
        return bass_max;
    }

    public void setBass_max(int bass_max) {
        this.bass_max = bass_max;
    }

    public int getMiddle_min() {
        return middle_min;
    }

    public void setMiddle_min(int middle_min) {
        this.middle_min = middle_min;
    }

    public int getMiddle_max() {
        return middle_max;
    }

    public void setMiddle_max(int middle_max) {
        this.middle_max = middle_max;
    }

    public int getHigh_min() {
        return high_min;
    }

    public void setHigh_min(int high_min) {
        this.high_min = high_min;
    }

    public int getHigh_max() {
        return high_max;
    }

    public void setHigh_max(int high_max) {
        this.high_max = high_max;
    }

    public void setTopBottomLeds(int topBottomLeds) {
        this.topBottomLeds = topBottomLeds;
    }

    public int getLeftRightLeds() {
        return leftRightLeds;
    }

    public void setLeftRightLeds(int leftRightLeds) {
        this.leftRightLeds = leftRightLeds;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }


    public int getPrio() {
        return prio;
    }

    public void setPrio(int prio) {
        this.prio = prio;
    }


    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public URI getUri(){
        String temp = "ws://" + this.ip +":" + this.port;
        return URI.create(temp);
    }

    public String getTAG() {
        return TAG;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key){
            case "ip":
                String newIP = sharedPreferences.getString(key,"0.0.0.0");
                if(!"".equalsIgnoreCase(newIP))this.setIp(newIP);
                break;
            case "port":
                String newPort = sharedPreferences.getString(key,"19444");
                if(!"".equalsIgnoreCase(newPort)) this.setPort(newPort);
                break;
            case "topBottom":
                String newTopBottom = sharedPreferences.getString(key,"40");
                if(!"".equalsIgnoreCase(newTopBottom)) this.setTopBottomLeds(Integer.valueOf(newTopBottom));
                break;
            case "leftRight":
                String newLeftRight = sharedPreferences.getString(key,"24");
                if(!"".equalsIgnoreCase(newLeftRight)) this.setLeftRightLeds(Integer.valueOf(newLeftRight));
                break;
            case "offset":
                String newOffset = sharedPreferences.getString(key,"0");
                if(!"".equalsIgnoreCase(newOffset)) this.setOffset(Integer.valueOf(newOffset)) ;
                break;
            case "rate":
                String newRate= sharedPreferences.getString(key,"0");
                if(!"".equalsIgnoreCase(newRate)) this.setRate(Integer.valueOf(newRate));
                break;
            case "prio":
                String newPrio= sharedPreferences.getString(key,"0");
                if(!"".equalsIgnoreCase(newPrio)) this.setPrio(Integer.valueOf(newPrio));
                break;
            case "bass_min":
                int newMinBass = sharedPreferences.getInt(key,20);
                this.setBass_min(newMinBass);
                break;
            case "bass_max":
                int newMaxBass = sharedPreferences.getInt(key,90);
                this.setBass_max(Integer.valueOf(newMaxBass));
                break;
            case "middle_min":
                int newMinMiddle= sharedPreferences.getInt(key,10);
                this.setMiddle_min(Integer.valueOf(newMinMiddle));
                break;
            case "middle_max":
                int newMaxMiddle = sharedPreferences.getInt(key,70);
                this.setMiddle_max(Integer.valueOf(newMaxMiddle));
                break;
            case "high_min":
                int newMinHigh = sharedPreferences.getInt(key,10);
                this.setHigh_min(Integer.valueOf(newMinHigh));
                break;
            case "high_max":
                int newMaxHigh = sharedPreferences.getInt(key,60);
                this.setHigh_max(Integer.valueOf(newMaxHigh));
                break;
            case "rainbow_max":
                int newRainbowMax = sharedPreferences.getInt(key,200);
                this.setRainbow_max(Integer.valueOf(newRainbowMax));
                break;
            case "rainbow_min":
                int newRainbowMin = sharedPreferences.getInt(key,10);
                this.setRainbow_min(Integer.valueOf(newRainbowMin));
                break;
            case "changecolor_max":
                int newchangeColorMax = sharedPreferences.getInt(key,180);
                this.setChangecolor_max(Integer.valueOf(newchangeColorMax));
                break;
            case "changecolor_min":
                int newChangeColorMin = sharedPreferences.getInt(key,10);
                this.setChangecolor_min(Integer.valueOf(newChangeColorMin));
                break;
        }
    }
}
