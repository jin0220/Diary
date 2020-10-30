package com.example.diary.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import androidx.annotation.Nullable;

import com.example.diary.R;

public class SettingPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings_preference); //별도의 레이아웃 파일을 만들지 않고 xml 문서를 화면으로 생성

    }
}
