package com.wirthual.hyperionauvi.settings;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.widget.Toast;

import com.wirthual.hyperionauvi.LicenceAcitvity;
import com.wirthual.hyperionauvi.R;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceClickListener{
    /**
     * Determines whether to always show the simplified settings UI, where
     * settings are presented in a single list. When false, settings are shown
     * as a master/detail two-pane view on tablets. When true, a single pane is
     * shown on tablets.
     */
    private static final boolean ALWAYS_SIMPLE_PREFS = false;


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_general);

        Preference dialogPreference = (Preference) getPreferenceScreen().findPreference("dialog_preference");
        dialogPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getApplicationContext(), LicenceAcitvity.class);
                startActivity(intent);
                return true;
            }
        });

        Preference about= findPreference("version");
        about.setOnPreferenceClickListener(this);

    }

    int clickCounter = 0;

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (clickCounter>4) {
            Toast.makeText(this,"Got it :)",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(clickCounter==0){
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                clickCounter=0;
            }
        }, 3000);
        }
        clickCounter ++;
        return false;
    }

}
