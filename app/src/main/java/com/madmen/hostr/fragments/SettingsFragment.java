package com.madmen.hostr.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.madmen.hostr.R;

/**
 * Another class in ${PACKAGE}
 * Created by Jay Whaley on 7/26/2017.
 */

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

}