package com.wirthual.hyperionauvi;

/**
 * Created by devbuntu on 27.01.15.
 */
public class HyperionConfig {

    String ip;
    String port;

    int topBottomLeds;
    int leftRightLeds;

    int rate;



    public HyperionConfig(String ip,String port,int leftRightLeds,int topBottomLeds){
        this.ip = ip;
        this.port = port;
        this.leftRightLeds = leftRightLeds;
        this.topBottomLeds = topBottomLeds;
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
}
