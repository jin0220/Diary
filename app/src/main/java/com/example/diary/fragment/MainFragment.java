package com.example.diary.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.diary.adapter.MainGridAdapter;
import com.example.diary.R;
import com.example.diary.activity.ReadActivity;
import com.example.diary.activity.WriteActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainFragment extends Fragment {

    GridView gridView;
    MainGridAdapter adapter;
    FloatingActionButton btn_write;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.main_fragment,container,false);

        adapter = new MainGridAdapter();


        gridView = rootView.findViewById(R.id.gridView);

        gridView.setAdapter(adapter);


        adapter.addData("Aug. 16",ContextCompat.getDrawable(getActivity(),R.drawable.button));
        adapter.addData("Aug. 16",ContextCompat.getDrawable(getActivity(),R.drawable.button));
        adapter.addData("Aug. 16",ContextCompat.getDrawable(getActivity(),R.drawable.button));
        adapter.addData("Aug. 16",ContextCompat.getDrawable(getActivity(),R.drawable.button));
        adapter.addData("Aug. 16",ContextCompat.getDrawable(getActivity(),R.drawable.button));
        adapter.addData("Aug. 16",ContextCompat.getDrawable(getActivity(),R.drawable.button));
        adapter.addData("Aug. 16",ContextCompat.getDrawable(getActivity(),R.drawable.button));
        adapter.addData("Aug. 16",ContextCompat.getDrawable(getActivity(),R.drawable.button));
        adapter.addData("Aug. 16",ContextCompat.getDrawable(getActivity(),R.drawable.button));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent =  new Intent(getActivity().getApplicationContext(), ReadActivity.class);//인텐트 안에서 getApplicationContext()가 에러 난다명 앞에 getActivity() 붙여줌
                startActivity(intent);
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

        return  rootView;
    }
}
