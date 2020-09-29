package com.example.diary.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.diary.data.MainGridData;
import com.example.diary.R;

import java.util.ArrayList;

public class MainGridAdapter extends BaseAdapter {
    TextView textView;
    ImageView imageView;

    ArrayList<MainGridData> items = new ArrayList<>();

    public MainGridAdapter() {

    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.activity_main_grid,viewGroup,false);

        textView = view.findViewById(R.id.textView);

        imageView = view.findViewById(R.id.imageView);

        MainGridData mainGridData = items.get(i);

        textView.setText(mainGridData.getText());

        imageView.setImageDrawable(mainGridData.getImage());

        return view;
    }

    public void addData(String text, Drawable image){
        MainGridData item = new MainGridData();

        item.setText(text);
        item.setImage(image);

        items.add(item);
    }
}
