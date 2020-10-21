package com.example.diary.data;

import android.graphics.drawable.Drawable;
import android.net.Uri;

public class MainGridData {
    String text;
    Uri image;

    public String getText() {
        return text;
    }

    public Uri getImage() {
        return image;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setImage(Uri image) {
        this.image = image;
    }
}
