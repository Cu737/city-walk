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
import java.text.SimpleDateFormat;
import java.util.*;

public class AllActivty extends AppCompatActivity implements View.OnClickListener{

    private TencentMap tencentMap;
    private TrailOverlayProvider trailOverlayProvider;
    private TextureMapView mapView;

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
        //添加轨迹数据到集合
        List<TrailLatLng> startLats = new ArrayList<>();
        try {
            //trailDataAll.dat文件格式（39.792151	116.397607	14	,）
            System.out.println("执行到这里1");
            br = new BufferedReader(new InputStreamReader(getResources().getAssets().open("trailDataAll.dat")));
            String line = null;
            System.out.println("执行到这里");
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                String[] oneDot = line.split(",");
                //轨迹图单点数据类型, 包含经纬度 + 时间戳
                TimeLatLng[] timeLatLngs = new TimeLatLng[oneDot.length];
                int i = 0;
                for (String timeLatLng : oneDot) {
                    String[] values = timeLatLng.split("\t");
                    LatLng latlng = new LatLng((Double.parseDouble(values[0])), (Double.parseDouble(values[1])));
                    int timeStamp = Integer.parseInt(values[2]);
                    timeLatLngs[i++] = new TimeLatLng(latlng, timeStamp);
                }
                //TrailLatLng轨迹图数据类型, 由TimeLatLng数组组成，输入数据需保证按时间戳增序
                TrailLatLng tmp = new TrailLatLng(timeLatLngs);
                startLats.add(tmp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //配置轨迹线数据
        System.out.println("执行到这里2");
        trailOverlayProvider = new TrailOverlayProvider().data(startLats);
        tencentMap.addVectorOverlay(trailOverlayProvider);
        LatLng latLng1 = new LatLng(39.984104, 116.307503);
        LatLng latLng2 = new LatLng(39.984198, 116.307296);
        LatLng latLng3 = new LatLng(39.984221, 116.307296);
//        Bitmap bitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        Paint paint = new Paint();
//        paint.setColor(Color.BLUE);
//        canvas.drawCircle(5, 5, 50, paint);
// 创建并添加标记
//        MarkerOptions markerOptions1 = new MarkerOptions()
//                .position(latLng1)
//                .icon(BitmapDescriptorFactory.fromBitmap(bitmap));
//        MarkerOptions markerOptions2 = new MarkerOptions()
//                .position(latLng2)
//                .icon(BitmapDescriptorFactory.fromBitmap(bitmap));
//        MarkerOptions markerOptions3 = new MarkerOptions()
//                .position(latLng3)
//                .icon(BitmapDescriptorFactory.fromBitmap(bitmap));
        MarkerOptions markerOptions1 = new MarkerOptions()
                .position(latLng1)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        MarkerOptions markerOptions2 = new MarkerOptions()
                .position(latLng2)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        MarkerOptions markerOptions3 = new MarkerOptions()
                .position(latLng3)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        Marker marker1 = tencentMap.addMarker(markerOptions1);
        Marker marker2 = tencentMap.addMarker(markerOptions2);
        Marker marker3 = tencentMap.addMarker(markerOptions3);
        int[] colors = {Color.argb((int) (255), 0xff, 0xca, 0x1f), Color.argb((int) (255 * 0.9), 0xcc, 0x18, 0x5d)};
        //设置轨迹线渐变颜色
        trailOverlayProvider.gradient(colors);
        trailOverlayProvider.displayLevel(OverlayLevel.OverlayLevelAboveLabels);


    }

    @Override
    public void onClick(View view) {
        ButtonChoose.chooseButton(this,view);
    }
}