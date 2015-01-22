package wirthual.com.visualizer.effects;

/**
 * Created by devbuntu on 21.01.15.
 */
abstract class Effect {


    boolean waveform = false;
    int topBottomLeds = 0;
    int leftRightLeds = 0;
    int totalLeds = 0;

    public Effect(int topBottom, int leftRight){
        this.waveform = waveform;
        this.topBottomLeds = topBottom;
        this.leftRightLeds = leftRight;
        totalLeds = 2*topBottom + 2*leftRight;
    }

    abstract String processData(byte[] bytes);

    public abstract String getName();

    public abstract boolean isWaveformEffect();

}
