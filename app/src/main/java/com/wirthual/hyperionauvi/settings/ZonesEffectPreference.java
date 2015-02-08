package com.wirthual.hyperionauvi.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;

import com.wirthual.hyperionauvi.R;
import com.wirthual.hyperionauvi.utils.RangeSeekBar;

/**
 * Created by devbuntu on 01.02.15.
 */
public class ZonesEffectPreference extends DialogPreference implements RangeSeekBar.OnRangeSeekBarChangeListener<Integer>{

    RangeSeekBar<Integer> bassSlider;
    RangeSeekBar<Integer> middleSlider;
    RangeSeekBar<Integer> highSlider;

    SharedPreferences pref;


    public ZonesEffectPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.zoneseffect_dialog);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);

        setDialogIcon(null);
    }

    @Override
    protected void onBindDialogView(View view) {

        pref = PreferenceManager.getDefaultSharedPreferences(getContext());

        bassSlider = (RangeSeekBar) view.findViewById(R.id.bass_slider);
        bassSlider.setOnRangeSeekBarChangeListener(this);
        middleSlider = (RangeSeekBar) view.findViewById(R.id.middle_slider);
        middleSlider.setOnRangeSeekBarChangeListener(this);
        highSlider = (RangeSeekBar) view.findViewById(R.id.high_slider);
        highSlider.setOnRangeSeekBarChangeListener(this);

        bassSlider.setSelectedMinValue(pref.getInt("bass_min",20));
        bassSlider.setSelectedMaxValue(pref.getInt("bass_max",90));

        middleSlider.setSelectedMinValue(pref.getInt("middle_min",10));
        middleSlider.setSelectedMaxValue(pref.getInt("middle_max",70));

        highSlider.setSelectedMinValue(pref.getInt("high_min",10));
        highSlider.setSelectedMaxValue(pref.getInt("high_max",60));

        super.onBindDialogView(view);
    }


    @Override
    protected void onDialogClosed(boolean positiveResult) {
        // When the user selects "OK", persist the new value
        super.onDialogClosed(positiveResult);
    }

    @Override
    public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
        int id = bar.getId();
        SharedPreferences.Editor editor = pref.edit();
        switch (id){
            case R.id.bass_slider:
                editor.putInt("bass_min", (Integer) bar.getSelectedMinValue());
                editor.putInt("bass_max", (Integer) bar.getSelectedMaxValue());
                break;
            case R.id.middle_slider:
                editor.putInt("middle_min",(Integer) bar.getSelectedMinValue());
                editor.putInt("middle_max",(Integer) bar.getSelectedMaxValue());
                break;
            case R.id.high_slider:
                editor.putInt("high_min",(Integer) bar.getSelectedMinValue());
                editor.putInt("high_max",(Integer) bar.getSelectedMaxValue());
                break;
        };
        editor.commit();
    }
}
