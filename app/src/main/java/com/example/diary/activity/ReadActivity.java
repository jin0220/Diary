package com.example.diary.activity;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.diary.R;
import com.example.diary.adapter.SpinnerAdapter;
import com.example.diary.adapter.ViewPagerAdapter;
import com.example.diary.data.DiaryDBHelper;
import com.example.diary.fragment.MainFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class ReadActivity extends AppCompatActivity {

    Toolbar toolbar;
    DiaryDBHelper diaryDBHelper;
    String date, image;
    TextView title, content;
    ViewPager viewPager;
    String id;
    int count;
    ViewPagerAdapter viewPagerAdapter;
    ArrayList<Uri> photos;
    ArrayList<String> im;
    TabLayout tabLayout;
    String image_code;
    int position;
    int MODIFY_REQUEST = 1;

    public static Activity readActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("com.example.diary_preferences",MODE_PRIVATE);

        if(sharedPreferences.getBoolean("mode",false) == true){
            setTheme(R.style.DarkTheme);
        }else{
            setTheme(R.style.LightTheme);
        }

        setContentView(R.layout.activity_read);

        readActivity = ReadActivity.this;

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);//액션바가 없을 시 툴바를 액션바로 대체
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        viewPager = findViewById(R.id.viewPager);


        //데이터 조회
        diaryDBHelper = new DiaryDBHelper(this);

        Intent intent = getIntent();
        id = intent.getExtras().getString("id");
        position = intent.getExtras().getInt("position");

        String sql = "select * from " + diaryDBHelper.TABLE_DIARY + " d left outer join " + diaryDBHelper.TABLE_IMAGE
                + " i on d." + diaryDBHelper.IMAGE_CODE + " = i." + diaryDBHelper.IMAGE_CODE + " where _id = " + "'" + id + "'";

        Cursor cursor = diaryDBHelper.select_sql(sql);
        if(cursor.moveToNext()){
            image_code = cursor.getString(cursor.getColumnIndexOrThrow(diaryDBHelper.IMAGE_CODE));
            String t = cursor.getString(cursor.getColumnIndexOrThrow(diaryDBHelper.TITLE));
            String c = cursor.getString(cursor.getColumnIndexOrThrow(diaryDBHelper.CONTENT));
            date = cursor.getString(cursor.getColumnIndexOrThrow(diaryDBHelper.DATE));
            count = cursor.getInt(cursor.getColumnIndexOrThrow(diaryDBHelper.FAVORITE));

            im = new ArrayList<>();
            int j = 1;
            while (j <= 10) {
                if (cursor.getString(cursor.getColumnIndexOrThrow(diaryDBHelper.IMAGE + j)) != null)
                    im.add(cursor.getString(cursor.getColumnIndexOrThrow(diaryDBHelper.IMAGE + j)));
                else break;
                j++;
            }

            photos = new ArrayList<>();
            int k = 0;
            while (k < im.size()) {
                if (im.get(k) != null)
                    photos.add(getUriFromPath(im.get(k)));
                else photos.remove(k);
                k++;
            }

            title.setText(t);
            content.setText(c);
        }

        viewPager(photos);

        cursor.close();

        Spinner spinner = findViewById(R.id.spinner);

        Cursor cursor_spinner = diaryDBHelper.select();
        ArrayList<String> spinner_date = new ArrayList<>();

        while(cursor_spinner.moveToNext()){
            String d = cursor_spinner.getString(cursor_spinner.getColumnIndexOrThrow(diaryDBHelper.DATE));
            spinner_date.add(d);
        }

        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this, spinner_date, date);
        spinner.setAdapter(spinnerAdapter);

    }

//path를 uri로 바꾸기
    public Uri getUriFromPath(String filePath) {
        if(filePath.contains("files/Pictures")){
            return Uri.parse(filePath);
        }
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, "_data = '" + filePath + "'", null, null);

        cursor.moveToNext();
        int id = cursor.getInt(cursor.getColumnIndex("_id"));
        Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

        return uri;
    }

    public void viewPager(ArrayList<Uri> photos){
        //사진인 없을 경우 사진영역을 없앰
        if(image_code == null) {
            viewPager.setVisibility(View.GONE);
        }else {
            viewPager.setOffscreenPageLimit(10); //ViewPager가 상태를 유지할 페이지의 최대 갯수를 변경할 수 있게 해준다.(limit를 넘어간 page에 대해서는 죽이고, 선택되었을 때 다시 create한다)

            viewPagerAdapter = new ViewPagerAdapter(photos, this);
            viewPager.setAdapter(viewPagerAdapter);

            tabLayout = findViewById(R.id.indicator);
            tabLayout.setupWithViewPager(viewPager, true); //viewpager와 tabLayout 연결
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.read,menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//뒤로가기 버튼
        //좋아요 버튼 상태
        if(count == 0){
            menu.findItem(R.id.favorite).setIcon(R.drawable.ic_favorite);
        }else {
            menu.findItem(R.id.favorite).setIcon(R.drawable.ic_favorite_press);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.favorite:
                if(count == 0){
                    item.setIcon(R.drawable.ic_favorite_press);
                    count++;
                    diaryDBHelper.favorit_update(id, count);
                }else{
                    item.setIcon(R.drawable.ic_favorite);
                    count--;
                    diaryDBHelper.favorit_update(id, count);
                }
                break;
            case R.id.delete:
                diaryDBHelper.delete(id);
                diaryDBHelper.image_delete(image_code);
                intent = new Intent(getApplicationContext(), MainFragment.class);
                intent.putExtra("position", position);
                intent.putExtra("state", "remove");
                setResult(RESULT_OK, intent);
//                startActivity(intent);
//                MainActivity.mainActivity.finish();
                finish();
                return true;
            case R.id.modify:
                intent = new Intent(this, WriteActivity.class);
                intent.putExtra("modify",true);
                intent.putExtra("id",id);
                startActivityForResult(intent, MODIFY_REQUEST);
//                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == MODIFY_REQUEST && resultCode == RESULT_OK){

            String t = intent.getExtras().getString("title");
            String c = intent.getExtras().getString("content");
            photos = new ArrayList<>();
            im = new ArrayList<>();
            im = intent.getExtras().getStringArrayList("photo");

            int k = 0;
            while (k < im.size()) {
                if (im.get(k) != null)
                    photos.add(getUriFromPath(im.get(k)));
                else break;
                k++;
            }

            if(im.get(0) != null){
                image_code = im.get(0);
            }
            viewPager(photos); //오류
            title.setText(t);
            content.setText(c);
        }
    }
}
