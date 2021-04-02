package com.example.diary.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.diary.R;
import com.example.diary.activity.AppLock;
import com.example.diary.activity.AppLockConst;
import com.example.diary.activity.PasswordActivity;

public class PWSettingFragment extends Fragment {
    RadioGroup pw_select;
    RadioButton pw_null, pw;
    LinearLayout pw_select_2;
    Switch bio;
    TextView pw_change;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String pw_state;
    boolean biopassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_pwsetting, container, false);

        sharedPreferences = getActivity().getSharedPreferences("com.example.diary_preferences", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        pw_select = rootView.findViewById(R.id.pw_select);
        pw_null = rootView.findViewById(R.id.pw_null);
        pw = rootView.findViewById(R.id.pw);
        pw_select_2 = rootView.findViewById(R.id.pw_select_2);
        bio = rootView.findViewById(R.id.bio);
        pw_change = rootView.findViewById(R.id.pw_change);

        pw_state = sharedPreferences.getString("pw_state",null);
        biopassword = sharedPreferences.getBoolean("biopassword",false);

        if(pw_state.equals("pw_null") || pw_state == null){
            pw_select_2.setVisibility(View.GONE);
        }else if(sharedPreferences.getString("pw_state",null).equals("pw")){
            pw_select_2.setVisibility(View.VISIBLE);
            pw.setChecked(true);
        }

        if(biopassword == true){
            bio.setChecked(true);
        }else{
            bio.setChecked(false);
        }

        pw_select.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                Intent intent = new Intent(getContext(), PasswordActivity.class);

                if(pw_null.isChecked()) {
                    pw_select_2.setVisibility(View.GONE);
                    setPw_state("pw_null");
                    new AppLock(getContext()).removePassLock(); //비밀번호 삭제
                    new AppLock(getContext()).removeBio();
                    bio.setChecked(false);
                }
                else if (pw.isChecked()) {
                    pw_select_2.setVisibility(View.VISIBLE);
                    setPw_state("pw");
                    intent.putExtra("type", AppLockConst.ENABLE_PASSLOCK);
                    startActivityForResult(intent, AppLockConst.ENABLE_PASSLOCK);
                }
            }
        });

        bio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked)
                    new AppLock(getContext()).setBio();
                else
                    new AppLock(getContext()).removeBio();
            }
        });

        pw_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PasswordActivity.class);
                intent.putExtra("type", AppLockConst.CHANGE_PASSWORD);
                startActivityForResult(intent, AppLockConst.CHANGE_PASSWORD);
            }
        });

        return rootView;

    }

    public void setPw_state(String pw_state){
        editor.putString("pw_state",pw_state);
        editor.apply(); //저장
    }
}
