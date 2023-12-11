package com.example.citywalk;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.*;
import android.icu.text.Transliterator;
import android.location.Location;

import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import android.widget.*;
import android.widget.ImageButton;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import com.example.citywalk.database.UserDBHelper;
import com.example.citywalk.enity.Position;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.map.sdk.utilities.visualization.datamodels.TimeLatLng;
import com.tencent.map.sdk.utilities.visualization.datamodels.TrailLatLng;
import com.tencent.map.sdk.utilities.visualization.trails.TrailOverlayProvider;
import com.tencent.tencentmap.mapsdk.maps.*;
import com.tencent.tencentmap.mapsdk.maps.model.*;
import com.example.citywalk.util.ButtonChoose;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.TencentMapInitializer;
import com.tencent.tencentmap.mapsdk.maps.TextureMapView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;

public class AllActivty extends AppCompatActivity implements View.OnClickListener{

    private TencentMap tencentMap;
    private TrailOverlayProvider trailOverlayProvider;
    private TextureMapView mapView;
    private static UserDBHelper db1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_activty);
        ButtonChoose.initButton(this);
        TencentMapInitializer.setAgreePrivacy(true);
        TencentLocationManager.setUserAgreePrivacy(true);
        //设置顶部状态栏为透明
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mapView = findViewById(R.id.mapView);
        tencentMap = mapView.getMap();
        //隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        BufferedReader br = null;
        db1 = UserDBHelper.getInstance(this);
        db1.openReadLink();
        db1.openWriteLink();
        System.out.println("数据库插入成功");
        List<Position> lat1= db1.queryAllPosition();
        System.out.println(lat1.size());
        int i = 0;
        for (Position p1 : lat1) {
            System.out.println(p1.latitude);
            System.out.println(p1.longitude);
            LatLng latLngs1 = new LatLng(p1.latitude,p1.longitude);
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLngs1)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            Marker marker1 = tencentMap.addMarker(markerOptions);
            System.out.println(i);
        }
    }

    @Override
    public void onClick(View view) {
        ButtonChoose.chooseButton(this,view);
    }
}