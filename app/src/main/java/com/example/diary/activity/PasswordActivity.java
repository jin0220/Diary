package com.example.diary.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.diary.R;

public class PasswordActivity extends AppCompatActivity {

    EditText password1,password2, password3, password4;
    private String oldPasscode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        SharedPreferences sharedPreferences = getSharedPreferences("com.example.diary_preferences",MODE_PRIVATE);
//
//        if(sharedPreferences.getBoolean("mode",false) == true){
//            setTheme(R.style.DarkTheme);
//        }else{
//            setTheme(R.style.LightTheme); //비밀번호 입력란 밑줄이 사라짐
//        }
        setContentView(R.layout.activity_password);

        password1 = findViewById(R.id.password1);
        password2 = findViewById(R.id.password2);
        password3 = findViewById(R.id.password3);
        password4 = findViewById(R.id.password4);

        // setup the keyboard
        ((Button) findViewById(R.id.num0)).setOnClickListener(btnListener);
        ((Button) findViewById(R.id.num1)).setOnClickListener(btnListener);
        ((Button) findViewById(R.id.num2)).setOnClickListener(btnListener);
        ((Button) findViewById(R.id.num3)).setOnClickListener(btnListener);
        ((Button) findViewById(R.id.num4)).setOnClickListener(btnListener);
        ((Button) findViewById(R.id.num5)).setOnClickListener(btnListener);
        ((Button) findViewById(R.id.num6)).setOnClickListener(btnListener);
        ((Button) findViewById(R.id.num7)).setOnClickListener(btnListener);
        ((Button) findViewById(R.id.num8)).setOnClickListener(btnListener);
        ((Button) findViewById(R.id.num9)).setOnClickListener(btnListener);

        ((Button) findViewById(R.id.clear))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clearFields();
                    }
                });

        ((Button) findViewById(R.id.delete_key))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onDeleteKey();
                    }
                });

    }

    protected void onPasscodeInputed(int type) {
        String passLock = password1.getText().toString()
                + password2.getText().toString()
                + password3.getText().toString()
                + password4.getText();

        password1.setText("");
        password2.setText("");
        password3.setText("");
        password4.setText("");
        password1.requestFocus();

        AppLock appLock = new AppLock(this);
        switch (type) {
            case AppLockConst.ENABLE_PASSLOCK: //잠금 설정
                if (oldPasscode == null) {
//                    tvMessage.setText(R.string.reenter_passcode);
                    oldPasscode = passLock;
                    clearFields();
                    TextView text = findViewById(R.id.again);
                    text.setVisibility(View.VISIBLE);
                } else {
                    if (passLock.equals(oldPasscode)) {
                        setResult(RESULT_OK);
//                        LockManager.getInstance().getAppLock()
//                                .setPasscode(passLock);

                        appLock.setPassLock(passLock);
                        finish();
                    } else {
                        oldPasscode = null;
//                        tvMessage.setText(R.string.enter_passcode);
//                        onPasscodeError();
                    }
                }
                break;

            case AppLockConst.DISABLE_PASSLOCK: //잠금 삭제
                if(appLock.isPassLockSet()){
                    if(appLock.checkPassLock(passLock)){
                        appLock.removePassLock();
                        setResult(RESULT_OK);
                        finish();
                    }
                }
                break;
//
//            case AppLockConst.CHANGE_PASSWORD:
//                if (LockManager.getInstance().getAppLock().checkPasscode(passLock)) {
//                    tvMessage.setText(R.string.enter_passcode);
//                    type = AppLockConst.ENABLE_PASSLOCK;
//                } else {
//                    onPasscodeError();
//                }
//                break;

            case AppLockConst.UNLOCK_PASSWORD:
                if (appLock.checkPassLock(passLock)) {
                    setResult(RESULT_OK);
                    finish();
                } else {
//                    onPasscodeError();
                    Toast.makeText(this, "비밀번호가 틀립니다.",Toast.LENGTH_LONG).show();
                }
                break;

            default:
                break;
        }
    }


    private View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int currentValue = -1;
            int id = view.getId();
            if (id == R.id.num0) {
                currentValue = 0;
            } else if (id == R.id.num1) {
                currentValue = 1;
            } else if (id == R.id.num2) {
                currentValue = 2;
            } else if (id == R.id.num3) {
                currentValue = 3;
            } else if (id == R.id.num4) {
                currentValue = 4;
            } else if (id == R.id.num5) {
                currentValue = 5;
            } else if (id == R.id.num6) {
                currentValue = 6;
            } else if (id == R.id.num7) {
                currentValue = 7;
            } else if (id == R.id.num8) {
                currentValue = 8;
            } else if (id == R.id.num9) {
                currentValue = 9;
            } else {
            }

            // 현재 입력된 번호를 String으로 변경 후 포커스 이동
            String currentValueString = String.valueOf(currentValue);
            if (password1.isFocused()) {
                password1.setText(currentValueString);
                password2.requestFocus();
                password2.setText("");
            } else if (password2.isFocused()) {
                password2.setText(currentValueString);
                password3.requestFocus();
                password3.setText("");
            } else if (password3.isFocused()) {
                password3.setText(currentValueString);
                password4.requestFocus();
                password4.setText("");
            } else if (password4.isFocused()) {
                password4.setText(currentValueString);
            }

            //비밀번호 4자리 모두 입력
            if (password4.getText().toString().length() > 0
                    && password3.getText().toString().length() > 0
                    && password2.getText().toString().length() > 0
                    && password1.getText().toString().length() > 0) {
                onPasscodeInputed(getIntent().getIntExtra("type",0));
            }
        }
    };

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            v.performClick();
            clearFields();
            return false;
        }
    };

    private void clearFields() { //비밀번호 전체 지우기
        password1.setText("");
        password2.setText("");
        password3.setText("");
        password4.setText("");

        password1.postDelayed(new Runnable() {

            @Override
            public void run() {
                password1.requestFocus();
            }
        }, 200);
    }

    private void onDeleteKey() { //비밀번호 하나씩 지우기
        if (password1.isFocused()) {

        } else if (password2.isFocused()) {
            password1.requestFocus();
            password1.setText("");
        } else if (password3.isFocused()) {
            password2.requestFocus();
            password2.setText("");
        } else if (password4.isFocused()) {
            password3.requestFocus();
            password3.setText("");
        }
    }
}