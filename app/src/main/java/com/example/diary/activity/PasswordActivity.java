package com.example.diary.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.diary.R;

public class PasswordActivity extends AppCompatActivity {

    EditText password1,password2, password3, password4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    }

    protected void onPasscodeInputed() {
        String passLock = password1.getText().toString()
                + password2.getText().toString()
                + password3.getText().toString() + password4.getText();

        password1.setText("");
        password2.setText("");
        password3.setText("");
        password4.setText("");
        password1.requestFocus();

//        switch (type) {
//
//            case AppLock.DISABLE_PASSLOCK:
//                if (LockManager.getInstance().getAppLock().checkPasscode(passLock)) {
//                    setResult(RESULT_OK);
//                    LockManager.getInstance().getAppLock().setPasscode(null);
//                    finish();
//                } else {
//                    onPasscodeError();
//                }
//                break;
//
//            case AppLock.ENABLE_PASSLOCK:
//                if (oldPasscode == null) {
//                    tvMessage.setText(R.string.reenter_passcode);
//                    oldPasscode = passLock;
//                } else {
//                    if (passLock.equals(oldPasscode)) {
//                        setResult(RESULT_OK);
//                        LockManager.getInstance().getAppLock()
//                                .setPasscode(passLock);
//                        finish();
//                    } else {
//                        oldPasscode = null;
//                        tvMessage.setText(R.string.enter_passcode);
//                        onPasscodeError();
//                    }
//                }
//                break;
//
//            case AppLock.CHANGE_PASSWORD:
//                if (LockManager.getInstance().getAppLock().checkPasscode(passLock)) {
//                    tvMessage.setText(R.string.enter_passcode);
//                    type = AppLock.ENABLE_PASSLOCK;
//                } else {
//                    onPasscodeError();
//                }
//                break;
//
//            case AppLock.UNLOCK_PASSWORD:
//                if (LockManager.getInstance().getAppLock().checkPasscode(passLock)) {
//                    setResult(RESULT_OK);
//                    finish();
//                } else {
//                    onPasscodeError();
//                }
//                break;
//
//            default:
//                break;
//        }
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

            // set the value and move the focus
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

            if (password4.getText().toString().length() > 0
                    && password3.getText().toString().length() > 0
                    && password2.getText().toString().length() > 0
                    && password1.getText().toString().length() > 0) {
                onPasscodeInputed();
            }
        }
    };
}