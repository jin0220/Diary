package com.example.diary.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.diary.R;
import com.example.diary.data.DiaryDBHelper;

public class ReadActivity extends AppCompatActivity {

    Toolbar toolbar;
    DiaryDBHelper diaryDBHelper;
    String date;
    TextView title, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);//액션바가 없을 시 툴바를 액션바로 대체
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        title = findViewById(R.id.title);
        content = findViewById(R.id.content);

        //데이터 조회
        diaryDBHelper = new DiaryDBHelper(this);

        Intent intent = getIntent();
        date = intent.getExtras().getString("date");

        Cursor cursor = diaryDBHelper.read(date);
        if(cursor.moveToNext()){
            String i = cursor.getString(cursor.getColumnIndexOrThrow(diaryDBHelper.IMAGE));
            String t = cursor.getString(cursor.getColumnIndexOrThrow(diaryDBHelper.TITLE));
            String c = cursor.getString(cursor.getColumnIndexOrThrow(diaryDBHelper.CONTENT));
            Log.d("확인","image : "+ i);
            Log.d("확인","title : "+ t);
            Log.d("확인","content : "+ c);

            title.setText(t);
            content.setText(c);

        }

        cursor.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.read,menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//뒤로가기 버튼

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
