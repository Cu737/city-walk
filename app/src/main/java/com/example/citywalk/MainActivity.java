package com.example.citywalk;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.Transliterator;
import android.location.Location;

import android.graphics.Color;
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
import com.tencent.tencentmap.mapsdk.maps.*;
import com.tencent.tencentmap.mapsdk.maps.model.*;
import com.example.citywalk.util.ButtonChoose;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.TencentMapInitializer;
import com.tencent.tencentmap.mapsdk.maps.TextureMapView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LocationSource, TencentLocationListener {
    private TextureMapView mapView;
    private TencentMap tencentMap;

    private View add_dairy;
    private EditText edit_dairy;

    private static final String TAG = "图片";

    private static final int TAKE_PHOTO = 11;// 拍照
    private static final int CROP_PHOTO_TAKE = 12;// 裁剪图片
    private static final int LOCAL_CROP = 13;// 本地图库
    private static final int CROP_PHOTO_LOCAL = 14;// 裁剪图片
    private Uri imageUri;// 拍照时的图片uri


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

    private static UserDBHelper db1 = null;

    private Uri save_uri;



    Log Log;
    private View half_transparent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestPermission();
        TencentMapInitializer.setAgreePrivacy(true);
        TencentLocationManager.setUserAgreePrivacy(true);
        db1 = UserDBHelper.getInstance(this);
        db1.openReadLink();
        db1.openWriteLink();
        List<Position> lat1= db1.queryAllPosition();
        for (Position p1 : lat1) {
            System.out.println(p1.latitude);
            System.out.println(p1.longitude);
            latLngs.add(new LatLng(p1.latitude,p1.latitude));

        }
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
        add_dairy= findViewById(R.id.add_dairy);
        half_transparent = findViewById(R.id.half_transparent);

        //设置增加日记界面的弹出与收起
        add_dairy.setVisibility(View.GONE);
        half_transparent.setVisibility(View.GONE);
        ImageButton exit_dairy_button = findViewById(R.id.dairy_exit);
        exit_dairy_button.setOnClickListener(this);

        View add_image = findViewById(R.id.add_image);
        add_image.setOnClickListener(this);
        View save_dairy = findViewById(R.id.save_diary);
        save_dairy.setOnClickListener(this);
        edit_dairy = findViewById(R.id.edit_diary);
        edit_dairy.setOnClickListener(this);

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
    //动态请求权限
    private void requestPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //请求权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
        }
    }


    @Override
    public void onClick(View view) {
        ButtonChoose.chooseButton(this,view);
        if(view.getId() == R.id.dairy_exit)
        {

            add_dairy.setVisibility(View.GONE);
            half_transparent.setVisibility(View.GONE);

        }
        else if(view.getId() == R.id.save_diary)
        {

            android.view.animation.TranslateAnimation animation = new android.view.animation.TranslateAnimation(0,15,0,15);
            animation.setDuration(500);
            animation.setFillAfter(false);
            view.startAnimation(animation);

            String text = edit_dairy.getText().toString();

            Log.w("edit_message", "输入的内容: " +text);
            Log.w("edit_message",lat+" "+lgt);
            Log.w("edit_message", "图片: " +save_uri);


        } else if(view.getId() == R.id.add_image)
        {
            takePhotoOrSelectPicture();// 拍照或者调用图库
        }




    }

    private void takePhotoOrSelectPicture() {
        CharSequence[] items = {"拍照", "图库"};// 裁剪items选项

        // 弹出对话框提示用户拍照或者是通过本地图库选择图片
        new AlertDialog.Builder(MainActivity.this)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            // 选择了拍照
                            case 0:
                                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                                String imageFileName = "JPEG_" + timeStamp + "_";
                                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                                System.out.println(Environment.DIRECTORY_PICTURES);
                                System.out.println(storageDir);
                                File takePhotoImage = null;
                                try {
                                    takePhotoImage = File.createTempFile(
                                            imageFileName,  /* prefix */
                                            ".jpg",         /* suffix */
                                            storageDir      /* directory */
                                    );
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }


                                // 创建文件保存拍照的图片

                                try {
                                    // 文件存在，删除文件
                                    if (takePhotoImage.exists()) {
                                        takePhotoImage.delete();
                                    }
                                    // 根据路径名自动的创建一个新的空文件
                                    takePhotoImage.createNewFile();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                // 获取图片文件的uri对象
                                // imageUri = Uri.fromFile(takePhotoImage);
                                if (Build.VERSION.SDK_INT >= 24) {
                                    imageUri = FileProvider.getUriForFile(MainActivity.this,
                                            "com.example.citywalk", takePhotoImage);
                                } else {
                                    imageUri = Uri.fromFile(takePhotoImage);
                                }
                                save_uri = imageUri;

                                // 创建Intent，用于启动手机的照相机拍照
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                // 指定输出到文件uri中
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                // 启动intent开始拍照
                                startActivityForResult(intent, TAKE_PHOTO);
                                break;
                            // 调用系统图库
                            case 1:
                                System.out.println(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                // 创建Intent，用于打开手机本地图库选择图片
                                Intent intent1 = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                // 启动intent打开本地图库
                                startActivityForResult(intent1, LOCAL_CROP);
                                break;

                        }

                    }
                }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case TAKE_PHOTO:// 拍照

                if (resultCode == RESULT_OK) {
                    // 创建intent用于裁剪图片
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    // 设置数据为文件uri，类型为图片格式
                    intent.setDataAndType(imageUri, "image/*");
                    // 允许裁剪
                    intent.putExtra("scale", true);
                    // 指定输出到文件uri中
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    // 启动intent，开始裁剪
                    startActivityForResult(intent, CROP_PHOTO_TAKE);
                }
                break;
            case LOCAL_CROP:// 系统图库

                if (resultCode == RESULT_OK) {
//                    // 创建intent用于裁剪图片
//                    Intent intent1 = new Intent("com.android.camera.action.CROP");
//                    // 获取图库所选图片的uri
//                    Uri uri = data.getData();
//                    intent1.setDataAndType(uri, "image/*");
//                    //  设置裁剪图片的宽高
//                    intent1.putExtra("outputX", 300);
//                    intent1.putExtra("outputY", 300);
//                    // 裁剪后返回数据
//
//                    intent1.putExtra("return-data", true);
//                    // 启动intent，开始裁剪
//                    startActivityForResult(intent1, CROP_PHOTO_LOCAL);
                    Uri selectedImage = data.getData();
                    save_uri = selectedImage;
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    System.out.println(picturePath);
                    ImageButton imageView = (ImageButton) findViewById(R.id.add_image);
                    System.out.println("可以执行");
                    imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                    System.out.println("yes");
                }

                break;
            case CROP_PHOTO_TAKE:// 裁剪后展示图片
                if (resultCode == RESULT_OK) {
                    try {
                        // 展示拍照后裁剪的图片
                        if (imageUri != null) {
                            // 创建BitmapFactory.Options对象
                            //BitmapFactory.Options option = new BitmapFactory.Options();
                            // 属性设置，用于压缩bitmap对象
                            //option.inSampleSize = 2;
                            //option.inPreferredConfig = Bitmap.Config.RGB_565;
                            // 根据文件流解析生成Bitmap对象
                            //Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri), null, option);
                            //Log.w("edit_message", "输入的内容: " + imageUri);
                            // 展示图片
                            //iv_show_picture.setImageBitmap(bitmap);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CROP_PHOTO_LOCAL:// 裁剪后展示图片
                if (resultCode == RESULT_OK) {
                    try {
                        // 展示图库中选择裁剪后的图片
                        if (data != null) {
                            // 根据返回的data，获取Bitmap对象
                            //Bitmap bitmap = data.getExtras().getParcelable("data");
                            // 展示图片
                            //iv_show_picture.setImageBitmap(bitmap);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

        }

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
            db1.insertPosition(new Position(lat,lgt));
            System.out.println("数据库插入成功");
            latLngs.add(new LatLng(lat,lgt));
// 构造 PolylineOpitons
            PolylineOptions polylineOptions = new PolylineOptions()
                    .addAll(latLngs)
                    // 折线设置圆形线头
                    .lineCap(true)
                    // 折线的颜色为绿色
                    .color(0x8014734B)
                    // 折线宽度为25像素
                    .width(10)
                    // 还可以添加描边颜色
                    .borderColor(0x7DD9E248)
                    // 描边颜色的宽度，线宽还是 25 像素，不过填充的部分宽度为 `width` - 2 * `borderWidth`
                    .borderWidth(2);

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
//            tencentMap.moveCamera(CameraUpdateFactory.newLatLngBounds(
//                    new LatLngBounds.Builder()
//                            .include(latLngs).build(),
//                    100));
        }
        else {
            if(latLngs.size()<=2)
            {
                System.out.println(latLngs.size());
                System.out.println("无轨迹");
                return ;
            }
            PolylineOptions polylineOptions = new PolylineOptions()
                    .addAll(latLngs)
                    // 折线设置圆形线头
                    .lineCap(true)
                    // 折线的颜色为绿色
                    .color(0x8014734B)
                    // 折线宽度为25像素
                    .width(10)
                    // 还可以添加描边颜色
                    .borderColor(0x7DD9E248)
                    // 描边颜色的宽度，线宽还是 25 像素，不过填充的部分宽度为 `width` - 2 * `borderWidth`
                    .borderWidth(2);

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
//            tencentMap.moveCamera(CameraUpdateFactory.newLatLngBounds(
//                    new LatLngBounds.Builder()
//                            .include(latLngs).build(),
//                    100));
//            System.out.println("位置不变");
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