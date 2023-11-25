package com.example.citywalk.util;

import androidx.annotation.Nullable;
public class EntryDiary {
    String position;
    String text;
    String picture_path;

    public EntryDiary(String position) {
        this(position, null, null);
    }

    public EntryDiary(String position, String text) {
        this(position, text, null);
    }

    public EntryDiary(String position, String text, String picture_path) {
        if (null == position) {
            throw new IllegalArgumentException("Position can not be null.");
        }
        this.position = position;
        this.text = text;
        this.picture_path = picture_path;
    }

    public void setPosition(String position) {
        if (null == position) {
            throw new IllegalArgumentException("Position can not be null.");
        }
        this.position = position;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPicture_path(String picture_path) {
        this.picture_path = picture_path;
    }

    public String getPosition() {
        return position;
    }

    public String getText() {
        return text;
    }

    public String getPicture_path() {
        return picture_path;
    }
}
