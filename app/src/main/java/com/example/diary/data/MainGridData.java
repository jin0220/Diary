package com.example.diary.data;

import android.graphics.drawable.Drawable;

public class MainGridData {
    String text;
    Drawable image;

    public String getText() {
        return text;
    }

    public Drawable getImage() {
        return image;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }
}
