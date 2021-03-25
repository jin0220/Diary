package com.example.diary.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.example.diary.R;

import java.util.concurrent.Executor;

public class PasswordActivity extends AppCompatActivity {

    //비밀번호
    EditText password1,password2, password3, password4;
    TextView title;
    private String oldPasscode = null;
    boolean change_pw = false; //비밀번호 변경 여부

    //지문 인식
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.diary_preferences",MODE_PRIVATE);
//
//        if(sharedPreferences.getBoolean("mode",false) == true){
//            setTheme(R.style.DarkTheme);
//        }else{
//            setTheme(R.style.LightTheme); //비밀번호 입력란 밑줄이 사라짐
//        }
        setContentView(R.layout.activity_password);

    //비밀번호
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

        title = findViewById(R.id.title);

        AppLock appLock = new AppLock(this);

        if(getIntent().getIntExtra("bio", 0) == AppLockConst.BIO_PASSWORD){
            setBiometricPrompt(true); //지문 인식
            setResult(RESULT_OK);
        }
        if(sharedPreferences.getBoolean("biopassword",false) == true
                && getIntent().getIntExtra("type", 0) == AppLockConst.DISABLE_PASSLOCK){
            setBiometricPrompt(false);
            setResult(RESULT_OK);
        }
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

        //잠금 설정이 되어 있으면서 다시 잠금 설정을 할 경우
        if(type == AppLockConst.ENABLE_PASSLOCK && appLock.isPassLockSet()){
            type = AppLockConst.CHANGE_PASSWORD;
        }

        TextView text = findViewById(R.id.again);
        switch (type) {
            case AppLockConst.ENABLE_PASSLOCK: //잠금 설정
                if (oldPasscode == null) {
                    oldPasscode = passLock;
                    clearFields();
                    text.setVisibility(View.VISIBLE);
                } else {
                    if (passLock.equals(oldPasscode)) {
                        setResult(RESULT_OK);
                        appLock.setPassLock(passLock);
                        finish();
                    } else {
                        oldPasscode = null;
                        text.setVisibility(View.GONE);
                        onPasscodeError();
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

            case AppLockConst.CHANGE_PASSWORD: //비밀 번호 변경
                if (appLock.checkPassLock(passLock) && !change_pw) {
                    title.setText("비밀번호 변경");
                    change_pw = true;
                }
                else if(change_pw) {
                    if (oldPasscode == null) {
                        oldPasscode = passLock;
                        clearFields();
                        text.setVisibility(View.VISIBLE);
                    } else {
                        if (passLock.equals(oldPasscode)) {
                            setResult(RESULT_OK);
                            appLock.setPassLock(passLock);
                            finish();
                        } else {
                            oldPasscode = null;
                            text.setVisibility(View.GONE);
                            onPasscodeError();
                        }
                    }
                } else {
                    onPasscodeError();
                }
                break;

            case AppLockConst.UNLOCK_PASSWORD: //잠금 해제하기
                if (appLock.checkPassLock(passLock)) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    onPasscodeError();
                }
                break;

            default:
                break;
        }
    }

    protected void onPasscodeError() {
        Thread thread = new Thread() {
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(
                        PasswordActivity.this, R.anim.shake);
                findViewById(R.id.title).startAnimation(animation);
                password1.setText("");
                password2.setText("");
                password3.setText("");
                password4.setText("");
                password1.requestFocus();
            }
        };
        runOnUiThread(thread);
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

    protected void setBiometricPrompt(final boolean type){
        final AppLock appLock = new AppLock(this);

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
//                Toast.makeText(getApplicationContext(),
//                        "에러", Toast.LENGTH_SHORT)
//                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
//                Toast.makeText(getApplicationContext(),
//                        "성공", Toast.LENGTH_SHORT).show();
                if(type)
                    appLock.setBio();
                else
                    appLock.removeBio();
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
//                Toast.makeText(getApplicationContext(), "실패",
//                        Toast.LENGTH_SHORT)
//                        .show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("지문 인증")
                .setSubtitle("기기에 등록된 지문을 이용하여 지문을 인증해주세요.")
                .setNegativeButtonText("취소")
                .setDeviceCredentialAllowed(false)
                .build();

        //  사용자가 다른 인증을 이용하길 원할 때 추가하기

//        Button biometricLoginButton = findViewById(R.id.buttonAuthWithFingerprint);
//        biometricLoginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                biometricPrompt.authenticate(promptInfo);
//            }
//        });
        biometricPrompt.authenticate(promptInfo);
    }
}