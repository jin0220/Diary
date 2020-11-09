package com.example.diary.activity;

import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.diary.R;
import com.example.diary.data.DiaryDBHelper;

public class ReadActivity extends AppCompatActivity {

    Toolbar toolbar;
    DiaryDBHelper diaryDBHelper;
    String date, image;
    TextView title, content;
    ImageView photo;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("com.example.diary_preferences",MODE_PRIVATE);

//        Log.d("확인","모드" + sharedPreferences.getBoolean("mode",false));
        if(sharedPreferences.getBoolean("mode",false) == true){
            setTheme(R.style.DarkTheme);
//            Log.d("확인","모드1" + sharedPreferences.getBoolean("mode",false));
        }else{
            setTheme(R.style.LightTheme);
//            Log.d("확인","모드2" + sharedPreferences.getBoolean("mode",false));
        }

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
//        image = intent.getExtras().getString("Image");

        Cursor cursor = diaryDBHelper.read(date);
        if(cursor.moveToNext()){
            String i = cursor.getString(cursor.getColumnIndexOrThrow(diaryDBHelper.IMAGE));
            String t = cursor.getString(cursor.getColumnIndexOrThrow(diaryDBHelper.TITLE));
            String c = cursor.getString(cursor.getColumnIndexOrThrow(diaryDBHelper.CONTENT));
            id = cursor.getString(cursor.getColumnIndexOrThrow(diaryDBHelper._ID));
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
//            Log.d("확인", "uriImage : " + uriImage);
////            Uri photoUri = Uri.fromFile();
            photo.setImageURI(uriImage);


//            photo.setImageURI(uri);
            title.setText(t);
            content.setText(c);

        }

        cursor.close();

//        Spinner spinner = (Spinner) findViewById(R.id.spinner);
//
//        Cursor cursor_spinner = diaryDBHelper.select();
//
//        while(cursor_spinner.moveToNext()){
//            String date = cursor.getString(cursor.getColumnIndexOrThrow(diaryDBHelper.DATE));
////            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, date);
//        }
//
//
//// Create an ArrayAdapter using the string array and a default spinner layout
//        String[] planets_array = {"2020-10-27","2020-10-28"};
////        ArrayAdapter<String> adapter = ArrayAdapter.createFromResource(this,
////                planets_array, android.R.layout.simple_spinner_item);
//
//// Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//// Apply the adapter to the spinner
//        spinner.setAdapter(adapter);



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
int count = 0;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.favorite:
                if(count == 0){
                    item.setIcon(R.drawable.ic_favorite_press);
                    count++;
                }else{
                    item.setIcon(R.drawable.ic_favorite);
                    count--;
                }
                break;
            case R.id.delete:
                diaryDBHelper.delete(id);
                finish();
                return true;
            case R.id.modify:
                Intent intent = new Intent(this, WriteActivity.class);
                intent.putExtra("modify",true);
                intent.putExtra("id",id);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
