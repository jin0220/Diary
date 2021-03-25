package com.example.diary.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.diary.R;
import com.example.diary.activity.AppLockConst;
import com.example.diary.activity.PasswordActivity;

public class pwPreferenceFragment extends PreferenceFragmentCompat {

    public pwPreferenceFragment(){

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pw_preference, rootKey);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        String key = preference.getKey();

        if(key.equals("pw")){
            Intent intent = new Intent(getContext(), PasswordActivity.class);
            intent.putExtra("type", AppLockConst.ENABLE_PASSLOCK);
            startActivityForResult(intent, AppLockConst.ENABLE_PASSLOCK);
            return true;
        }
        else if(key.equals("bio")){
            Intent intent = new Intent(getContext(), PasswordActivity.class);
            intent.putExtra("bio", AppLockConst.BIO_PASSWORD);
            startActivityForResult(intent, AppLockConst.BIO_PASSWORD);
            return true;
        }
        else if(key.equals("null")){
            Intent intent = new Intent(getContext(), PasswordActivity.class);
            intent.putExtra("type", AppLockConst.DISABLE_PASSLOCK);
            startActivityForResult(intent, AppLockConst.DISABLE_PASSLOCK);
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == AppLockConst.ENABLE_PASSLOCK && resultCode == Activity.RESULT_OK){
            Toast.makeText(getContext(),"설정",Toast.LENGTH_LONG).show();
        }
        if(requestCode == AppLockConst.DISABLE_PASSLOCK && resultCode == Activity.RESULT_OK){
            Toast.makeText(getContext(),"설정 안 함",Toast.LENGTH_LONG).show();
        }
        if(requestCode == AppLockConst.BIO_PASSWORD && resultCode == Activity.RESULT_OK){
            Toast.makeText(getContext(),"지문 인식 설정",Toast.LENGTH_LONG).show();
        }
    }
}
