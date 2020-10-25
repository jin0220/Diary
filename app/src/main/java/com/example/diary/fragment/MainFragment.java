package com.example.diary.fragment;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.diary.R;
import com.example.diary.activity.ReadActivity;
import com.example.diary.activity.WriteActivity;
import com.example.diary.adapter.MainGridAdapter;
import com.example.diary.data.DiaryDBHelper;
import com.example.diary.data.MainGridData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainFragment extends Fragment {

    GridView gridView;
    MainGridAdapter adapter;
    FloatingActionButton btn_write;
    DiaryDBHelper diaryDBHelper;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.main_fragment, container, false);

        adapter = new MainGridAdapter();
        diaryDBHelper = new DiaryDBHelper(getContext());

        gridView = rootView.findViewById(R.id.gridView);

        gridView.setAdapter(adapter);

        //데이터 조회
        Cursor cursor = diaryDBHelper.select();


        while (cursor.moveToNext()) {
            String image = cursor.getString(cursor.getColumnIndexOrThrow(diaryDBHelper.IMAGE));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(diaryDBHelper.DATE));
            Uri uriImage = getUriFromPath(image);
            adapter.addData(date, uriImage);
        }


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Log.d("확인","실행 1");
                Intent intent = new Intent(getActivity().getApplicationContext(), ReadActivity.class);//인텐트 안에서 getApplicationContext()가 에러 난다명 앞에 getActivity() 붙여줌
//                Log.d("확인","실행 2");
                MainGridData data = (MainGridData) adapter.getItem(i);
//                Log.d("확인",  item.getText()); //날짜
//                String sql = "select * from "+ diaryDBHelper.TABLE_NAME + " where date = " + i;
//                Cursor cursor = diaryDBHelper.getReadableDatabase().rawQuery();
//                Log.d("확인","실행 3 : "+ da);
                intent.putExtra("date", data.getText());
//                intent.putExtra("image", data.getImage().toString());
                startActivity(intent);
            }
        });

        cursor.close();

        btn_write = rootView.findViewById(R.id.floatingActionButton);
        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), WriteActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    //path를 uri로 바꾸기
    public Uri getUriFromPath(String filePath) {
        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, "_data = '" + filePath + "'", null, null);

        cursor.moveToNext();
        int id = cursor.getInt(cursor.getColumnIndex("_id"));
        Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

        return uri;
    }
}
