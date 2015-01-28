package com.wirthual.hyperionauvi;

import android.content.SharedPreferences;

import java.io.Serializable;
import java.net.URI;

/**
 * Created by devbuntu on 27.01.15.
 */
public class HyperionConfig implements SharedPreferences.OnSharedPreferenceChangeListener,Serializable{

    final String TAG = "HyperionConfig";

    String ip;
    String port;

    int topBottomLeds;
    int leftRightLeds;

    int rate;



    public HyperionConfig(String ip,String port,int leftRightLeds,int topBottomLeds,int rate){
        this.ip = ip;
        this.port = port;
        this.leftRightLeds = leftRightLeds;
        this.topBottomLeds = topBottomLeds;
        this.rate = rate;
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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

    public void setTopBottomLeds(int topBottomLeds) {
        this.topBottomLeds = topBottomLeds;
    }

    public int getLeftRightLeds() {
        return leftRightLeds;
    }

    public void setLeftRightLeds(int leftRightLeds) {
        this.leftRightLeds = leftRightLeds;
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key){
            case "ip":
                String newIP = sharedPreferences.getString(key,"0.0.0.0");
                this.setIp(newIP);
                break;
            case "port":
                String newPort = sharedPreferences.getString(key,"19444");
                this.setPort(newPort);
                break;
            case "topBottom":
                String newTopBottom = sharedPreferences.getString(key,"40");
                this.setTopBottomLeds(Integer.valueOf(newTopBottom));
                break;
            case "leftRight":
                String newLeftRight = sharedPreferences.getString(key,"24");
                this.setLeftRightLeds(Integer.valueOf(newLeftRight));
                break;
        }
    }
}
