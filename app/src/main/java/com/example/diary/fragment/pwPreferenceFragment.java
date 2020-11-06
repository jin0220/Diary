package com.example.diary.fragment;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.diary.R;

public class pwPreferenceFragment extends PreferenceFragmentCompat {

    public pwPreferenceFragment(){

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pw_preference, rootKey);
    }

}
