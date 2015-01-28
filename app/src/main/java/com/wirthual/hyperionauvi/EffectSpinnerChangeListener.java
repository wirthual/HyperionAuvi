package com.wirthual.hyperionauvi;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.wirthual.hyperionauvi.service.AnalyzeService;

/**
 * Created by devbuntu on 26.01.15.
 */
public class EffectSpinnerChangeListener implements Spinner.OnItemSelectedListener {

    Context context;

    public EffectSpinnerChangeListener(Context context){
        super();
        this.context = context;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selected = parent.getItemAtPosition(position).toString();
        if(selected.equalsIgnoreCase("3 Zones")) {
            ((HyperionAudioVisualizer) context).setSelectedEffect(AnalyzeService.THREEZONESEFFECT);
        }else if(selected.equalsIgnoreCase("Level Rainbow")){
            ((HyperionAudioVisualizer) context).setSelectedEffect(AnalyzeService.LEVELEFFECT);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
