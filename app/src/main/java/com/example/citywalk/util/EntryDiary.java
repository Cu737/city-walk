package com.example.citywalk.util;

import androidx.annotation.Nullable;
public class EntryDiary {
    double latitude;
    double longitude;
    String text;
    String picture_path;

    public EntryDiary(double latitude, double longitude, String text, String picture_path) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.text = text;
        this.picture_path = picture_path;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPicture_path() {
        return picture_path;
    }

    public void setPicture_path(String picture_path) {
        this.picture_path = picture_path;
    }
}
