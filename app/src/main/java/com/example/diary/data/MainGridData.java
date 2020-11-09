package com.example.diary.data;

import android.net.Uri;

public class MainGridData {
    String text;
    Uri image;
    int viewType;

    public String getText() {
        return text;
    }

    public Uri getImage() {
        return image;
    }

    public int getViewType() {
        return viewType;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public void setViewType(int viewType){
        this.viewType = viewType;
    }

}
