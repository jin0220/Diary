package com.example.diary.activity;

import android.content.Context;
import android.content.SharedPreferences;


public class AppLock {
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public AppLock(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("com.example.diary_preferences", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setPassLock(String password){
        editor.putString("passcode",password);
        editor.apply(); //저장
    }

    public void removePassLock(){
        editor.remove("passcode");
        editor.apply();
    }

    //입력한 비밀번호가 맞는지 확인
    public boolean checkPassLock(String password){
        return sharedPreferences.getString("passcode","0").equals(password);
    }

    //잠금 설정이 되어있는지 확인
    public boolean isPassLockSet(){
        if(sharedPreferences.contains("passcode"))
            return true;
        return false;
    }
}
