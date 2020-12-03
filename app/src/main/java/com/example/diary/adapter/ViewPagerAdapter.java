package com.example.diary.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.diary.R;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {

    Context context;
    ArrayList<Uri> photos;

    public ViewPagerAdapter (ArrayList<Uri> photos, Context context){
        this.photos = photos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.viewpager,container,false);
        ImageView imageView = (ImageView)view.findViewById(R.id.photo);
        //이미지 용량 줄이기
//        Bitmap bm = null;
//        try {
//            bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), photos.get(position));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Bitmap resized = Bitmap.createScaledBitmap(bm, 200, 250, true);
//        imageView.setImageBitmap(resized);

        imageView.setImageURI(photos.get(position));
        container.addView(view);

        return view;
    }
}
