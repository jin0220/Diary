package com.example.diary.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.diary.R;
import com.example.diary.data.MainGridData;

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

        textView.setText(mainGridData.getText()); //텍스트 설정

        Uri image = mainGridData.getImage();
//        Log.d("확인", "어댑터 " + image);
//        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
//        Uri uriImage = Uri.parse(image);
//        Log.d("확인","uriimage : " + uriImage);
//        imageView.setImageURI(uriImage);
        imageView.setImageURI(image);

        return view;
    }

    public void addData(String text, Uri image){
        String[] date = text.split("-");
        String[] day = date[2].split(" ");
        switch (date[1]){
            case "01":
                date[1] = "Jan.";
                break;
            case "02":
                date[1] = "Feb.";
                break;
            case "03":
                date[1] = "Mar.";
                break;
            case "04":
                date[1] = "Apr.";
                break;
            case "05":
                date[1] = "May.";
                break;
            case "06":
                date[1] = "Jun.";
                break;
            case "07":
                date[1] = "Jul.";
                break;
            case "08":
                date[1] = "Aug.";
                break;
            case "09":
                date[1] = "Sep.";
                break;
            case "10":
                date[1] = "Oct.";
                break;
            case "11":
                date[1] = "Nov.";
                break;
            case "12":
                date[1] = "Dec.";
                break;
        }
        MainGridData item = new MainGridData();

//        item.setText(date[1] + "\n" + day[0]);
        item.setText(text);
        item.setImage(image);

        items.add(item);
    }
}
