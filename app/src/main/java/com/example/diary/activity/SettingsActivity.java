package com.example.diary.activity;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import com.example.diary.R;

public class SettingsActivity extends AppCompatActivity implements PreferenceFragmentCompat.OnPreferenceStartFragmentCallback{

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.diary_preferences",MODE_PRIVATE);

//        Log.d("확인","모드" + sharedPreferences.getBoolean("mode",false));
        if(sharedPreferences.getBoolean("mode",false) == true){
            setTheme(R.style.DarkTheme);
//            Log.d("확인","모드1" + sharedPreferences.getBoolean("mode",false));
        }else{
            setTheme(R.style.AppTheme);
//            Log.d("확인","모드2" + sharedPreferences.getBoolean("mode",false));
        }

        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

//뒤로가기 버튼
    @Override
    public boolean onSupportNavigateUp() {

        if(getSupportFragmentManager().getBackStackEntryCount() == 0) {
            finish();
        }else {
            getSupportFragmentManager().popBackStack();
        }

        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference pref) {
        // Instantiate the new Fragment
        final Bundle args = pref.getExtras();
        final Fragment fragment = getSupportFragmentManager().getFragmentFactory().instantiate(
                getClassLoader(),
                pref.getFragment());
        fragment.setArguments(args);
        fragment.setTargetFragment(caller, 0);
        // Replace the existing Fragment with the new Fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.settings, fragment)
                .addToBackStack(null)
                .commit();
        return true;
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        SwitchPreference mode, alarm;
        SharedPreferences pref;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            mode = (SwitchPreference) findPreference("mode");
            alarm = (SwitchPreference) findPreference("alarm");


            //SharedPreference객체를 참조하여 설정상태에 대한 제어 가능..
            pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            pref.registerOnSharedPreferenceChangeListener(listener);
        }

        SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals("mode")) {
                    if (pref.getBoolean("mode", true)) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); //안됨

                    }else{
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    }
//                getActivity().finish();
//                startActivity(getActivity().getIntent());
                }else if(key.equals("alarm")){

                }else if (key.equals("pw")){

                }else if (key.equals("pdf")){

                }
            }
        };
    }
}