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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diary.R;
import com.example.diary.activity.WriteActivity;
import com.example.diary.adapter.MainGridAdapter;
import com.example.diary.data.DiaryDBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainFragment extends Fragment {

    RecyclerView recyclerView;
    MainGridAdapter adapter;
    FloatingActionButton btn_write;
    DiaryDBHelper diaryDBHelper;

    public static final int HEADER_VIEW = 0;
    public static final int ITEM_VIEW = 1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.main_fragment, container, false);

        diaryDBHelper = new DiaryDBHelper(getContext());

        recyclerView = rootView.findViewById(R.id.recyclerView);

        // 리사이클러뷰에 GridLayoutManager 객체 지정.
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 4); //spancount는 한 줄에 몇 개의 칸으로 나눌건지 결정
        recyclerView.setLayoutManager(layoutManager);

        // 리사이클러뷰에 MainGridAdapter 객체 지정.
        adapter = new MainGridAdapter();
        recyclerView.setAdapter(adapter);

        //데이터 조회
        Cursor cursor = diaryDBHelper.select();
        String oldDate = "0";
        while (cursor.moveToNext()) {
            String[] date1;
            String dateCombination;
            String image = cursor.getString(cursor.getColumnIndexOrThrow(diaryDBHelper.IMAGE));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(diaryDBHelper.DATE));
            Uri uriImage = getUriFromPath(image);
            date1 = date.split("-");
            dateCombination = date1[0] + "-" + date1[1];

            if(oldDate.equals(dateCombination)){
                adapter.addData(date, uriImage, ITEM_VIEW);
            }else{
                adapter.addData2(dateCombination,HEADER_VIEW);
                adapter.addData(date, uriImage, ITEM_VIEW);
            }
            oldDate = dateCombination;
        }

        cursor.close();

        //위치별로 차지할 폭을 결정한다
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(adapter.getItemViewType(position) == MainGridAdapter.HEADER_VIEW){
                    return 4; //4칸 다 차지
                }
                else return 1;
            }
        });

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
