package com.example.citywalk;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.tencentmap.mapsdk.maps.*;
import com.tencent.tencentmap.mapsdk.maps.model.*;
import com.example.citywalk.util.ButtonChoose;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.TencentMapInitializer;
import com.tencent.tencentmap.mapsdk.maps.TextureMapView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LocationSource, TencentLocationListener {
    private TextureMapView mapView;
    private TencentMap tencentMap;
    private TencentLocationManager locationManager;
    private TencentLocationRequest locationRequest;
    private OnLocationChangedListener locationChangedListener;
    private Location mylocation;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private List<LatLng> latLngs = new ArrayList<LatLng>();
    private static double lat;
    private static double lgt;
    private static double old_lat = -1.0000;
    private static double old_lgt = -1.0000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TencentMapInitializer.setAgreePrivacy(true);
        TencentLocationManager.setUserAgreePrivacy(true);

        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // 权限未被授予
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            System.out.println("当前已授权");
        }
        mapView = findViewById(R.id.mapView);
        tencentMap = mapView.getMap();
        locationManager = TencentLocationManager.getInstance(this);
        //创建定位请求
        locationRequest = TencentLocationRequest.create();
        //设置定位周期（位置监听器回调周期）为3s
        locationRequest.setInterval(3000);

        tencentMap.setLocationSource(this);
        //设置当前位置可见
        tencentMap.setMyLocationEnabled(true);

        ButtonChoose.initButton(this);

        //隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //设置顶部状态栏为透明
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("执行成功");
               draw_road();
            }
        }, 0, 5000);
    }

    /**
     * mapview的生命周期管理
     */
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mapView.onRestart();
    }


    @Override
    public void onClick(View view) {
        ButtonChoose.chooseButton(this,view);



    }
    private List<LatLng> getLatlons(double lat,double lgt){

        latLngs.add(new LatLng(lat,lgt));
        return latLngs;
    }
    private void initLocation(){
        //用于访问腾讯定位服务的类, 周期性向客户端提供位置更新
        locationManager = TencentLocationManager.getInstance(this);
        //创建定位请求
        locationRequest = TencentLocationRequest.create();
        //设置定位周期（位置监听器回调周期）为3s
        locationRequest.setInterval(3000);
    }

    /**
     * 腾讯定位SDK位置变化回调
     */
    public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
        //其中 locationChangeListener 为 LocationSource.active 返回给用户的位置监听器
        //用户通过这个监听器就可以设置地图的定位点位置
        if(i == TencentLocation.ERROR_OK && locationChangedListener != null){
            System.out.println("定位成功");
            lat = tencentLocation.getLatitude();
            lgt = tencentLocation.getLongitude();
            System.out.println(lat);
            System.out.println(lgt);
            if(old_lgt == -1.0000)
            {
                old_lgt = lgt;
                old_lat = lat;
                latLngs.add(new LatLng(lat,lgt));
            }
            Location location = new Location(tencentLocation.getProvider());
            //设置经纬度
            location.setLatitude(tencentLocation.getLatitude());
            location.setLongitude(tencentLocation.getLongitude());
            //设置精度，这个值会被设置为定位点上表示精度的圆形半径
            location.setAccuracy(tencentLocation.getAccuracy());

            //设置定位标的旋转角度，注意 tencentLocation.getBearing() 只有在 gps 时才有可能获取
            //location.setBearing((float) tencentLocation.getBearing());
            //设置定位标的旋转角度，注意 tencentLocation.getDirection() 返回的方向，仅来自传感器方向，如果是gps，则直接获取gps方向
            location.setBearing((float) tencentLocation.getDirection());
            //将位置信息返回给地图
            locationChangedListener.onLocationChanged(location);
        }
        else {
            System.out.println(i);
            System.out.println(TencentLocation.ERROR_OK);
            System.out.println("定位失败");
        }
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {

    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        //这里我们将地图返回的位置监听保存为当前 Activity 的成员变量
        locationChangedListener = onLocationChangedListener;
        //开启定位
        int err = locationManager.requestLocationUpdates(
                locationRequest, this, Looper.myLooper());
        switch (err) {
            case 1:
                Toast.makeText(this,
                        "设备缺少使用腾讯定位服务需要的基本条件",
                        Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(this,
                        "manifest 中配置的 key 不正确", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(this,
                        "自动加载libtencentloc.so失败", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
    public void draw_road(){
        if(old_lgt == -1.0000)
        {
            System.out.println("初始化中");
        }
        else if(old_lgt!=lgt||old_lat!=lat)
        {
            old_lat = lat;
            old_lgt = lgt;
            System.out.println("位置更新");
            latLngs.add(new LatLng(lat,lgt));
// 构造 PolylineOpitons
            PolylineOptions polylineOptions = new PolylineOptions()
                    .addAll(latLngs)
                    // 折线设置圆形线头
                    .lineCap(true)
                    // 折线的颜色为绿色
                    .color(0xff00ff00)
                    // 折线宽度为25像素
                    .width(25)
                    // 还可以添加描边颜色
                    .borderColor(0xffff0000)
                    // 描边颜色的宽度，线宽还是 25 像素，不过填充的部分宽度为 `width` - 2 * `borderWidth`
                    .borderWidth(5);

// 绘制折线
            Polyline polyline = tencentMap.addPolyline(polylineOptions);
//        CameraUpdate cameraSigma = CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(39.026403, 117.712015), //新的中心点坐标  x为latitude，y为longitude
//                17,  //新的缩放级别
//                0, //俯仰角 0~45° (垂直地图时为0)
//                0)); //偏航角 0~360° (正北方为0)
////移动地图
//        tencentMap.moveCamera(cameraSigma);
// 构造 PolylineOpitons
//        PolylineOptions polylineOptions = new PolylineOptions()
//                .addAll(latLngs)
//                // 折线设置圆形线头
//                .lineCap(true)
//                // 纹理颜色
//                .color(PolylineOptions.Colors.GRAYBLUE)
//                .width(25);
//
//// 绘制折线
//        Polyline polyline = tencentMap.addPolyline(polylineOptions);

// 将地图视野移动到折线所在区域(指定西南坐标和东北坐标)，设置四周填充的像素
            tencentMap.moveCamera(CameraUpdateFactory.newLatLngBounds(
                    new LatLngBounds.Builder()
                            .include(latLngs).build(),
                    100));
        }
        else {
            System.out.println("位置不变");
        }


    }
    @Override
    public void deactivate() {
        //当不需要展示定位点时，需要停止定位并释放相关资源
        locationManager.removeUpdates(this);
        locationManager = null;
        locationRequest = null;
        locationChangedListener=null;
    }

}
