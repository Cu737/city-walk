package com.example.citywalk;


import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import android.widget.ImageButton;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.citywalk.util.ButtonChoose;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.TencentMapInitializer;
import com.tencent.tencentmap.mapsdk.maps.TextureMapView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextureMapView mapView;
    private TencentMap tencentMap;

    private View add_dairy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TencentMapInitializer.setAgreePrivacy(true);

        mapView = findViewById(R.id.mapView);

        tencentMap = mapView.getMap();

        ButtonChoose.initButton(this);

        //隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //设置顶部状态栏为透明
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        add_dairy= findViewById(R.id.add_dairy);

        //设置增加日记界面的弹出与收起
        add_dairy.setVisibility(View.GONE);
        ImageButton exit_dairy_button = findViewById(R.id.dairy_exit);
        exit_dairy_button.setOnClickListener(this);
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
        if(view.getId() == R.id.dairy_exit)
        {
            add_dairy.setVisibility(View.GONE);

        }




    }
}
