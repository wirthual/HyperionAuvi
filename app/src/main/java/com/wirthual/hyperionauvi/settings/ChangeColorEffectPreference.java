package com.wirthual.hyperionauvi.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.wirthual.hyperionauvi.R;
import com.wirthual.hyperionauvi.utils.RangeSeekBar;

/**
 * Created by devbuntu on 01.02.15.
 */
public class ChangeColorEffectPreference extends DialogPreference implements RangeSeekBar.OnRangeSeekBarChangeListener<Integer>{

    public final static String TAG = "ChangeColorEffectPreference";

    RangeSeekBar<Integer> maxSlider;

    SharedPreferences pref;


    public ChangeColorEffectPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.changecolor_dialog);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);

        setDialogIcon(null);
    }

    @Override
    protected void onBindDialogView(View view) {

        pref = getSharedPreferences();

        maxSlider = (RangeSeekBar) view.findViewById(R.id.changecolorMinMax);
        maxSlider.setOnRangeSeekBarChangeListener(this);

        int min = pref.getInt("changecolor_min",20);
        int max = pref.getInt("changecolor_max",120);
        maxSlider.setSelectedMaxValue(max);
        maxSlider.setSelectedMinValue(min);

        super.onBindDialogView(view);
    }


    @Override
    protected void onDialogClosed(boolean positiveResult) {
        // When the user selects "OK", persist the new value
        super.onDialogClosed(positiveResult);
    }

    @Override
    public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
        Log.d(TAG,String.valueOf(minValue) + " " + String.valueOf(maxValue));
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt("changecolor_max",maxValue);
        editor.putInt("changecolor_min",minValue);

        editor.commit();
    }
}

