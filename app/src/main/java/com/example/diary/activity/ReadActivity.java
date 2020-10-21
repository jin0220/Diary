package com.example.diary.activity;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.diary.R;
import com.example.diary.data.DiaryDBHelper;

import java.io.File;
import java.io.InputStream;

public class ReadActivity extends AppCompatActivity {

    Toolbar toolbar;
    DiaryDBHelper diaryDBHelper;
    String date;
    TextView title, content;
    ImageView photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);//액션바가 없을 시 툴바를 액션바로 대체
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        photo = findViewById(R.id.photo);

        //데이터 조회
        diaryDBHelper = new DiaryDBHelper(this);

        Intent intent = getIntent();
        date = intent.getExtras().getString("date");

        Cursor cursor = diaryDBHelper.read(date);
        if(cursor.moveToNext()){
            String i = cursor.getString(cursor.getColumnIndexOrThrow(diaryDBHelper.IMAGE));
            String t = cursor.getString(cursor.getColumnIndexOrThrow(diaryDBHelper.TITLE));
            String c = cursor.getString(cursor.getColumnIndexOrThrow(diaryDBHelper.CONTENT));
//            Log.d("확인","바꾸기 전");
//            Uri uri = new Uri.Builder().build().parse(i);
//            InputStream in = getContentResolver().openInputStream(uri);
//            Bitmap bitmap = BitmapFactory.decodeStream(in);
//            Log.d("확인","바꾸기 후");


//            Log.d("확인","image : "+ i);
//            Log.d("확인","title : "+ t);
//            Log.d("확인","content : "+ c);

//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inSampleSize = 8; // 1/8 로 크기를 줄임
//            Bitmap bitmap = BitmapFactory.decodeFile(new File(i).getAbsolutePath(), options);
//            photo.setImageBitmap(bitmap);

//            Bitmap bitmap = BitmapFactory.decodeByteArray(i, 0, i.length);
            Uri uriImage = getUriFromPath(i);
            Log.d("확인", "uriImage : " + uriImage);
////            Uri photoUri = Uri.fromFile();
            photo.setImageURI(uriImage);


//            photo.setImageURI(uri);
            title.setText(t);
            content.setText(c);

        }

        cursor.close();

    }

//path를 uri로 바꾸기
    public Uri getUriFromPath(String filePath) {
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, "_data = '" + filePath + "'", null, null);

        cursor.moveToNext();
        int id = cursor.getInt(cursor.getColumnIndex("_id"));
        Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

        return uri;
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
