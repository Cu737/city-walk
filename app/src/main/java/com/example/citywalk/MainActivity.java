package com.example.citywalk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.TencentMapInitializer;
import com.tencent.tencentmap.mapsdk.maps.TextureMapView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextureMapView mapView;
    private TencentMap tencentMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TencentMapInitializer.setAgreePrivacy(true);

        mapView = findViewById(R.id.mapView);

        tencentMap = mapView.getMap();

        //获取五个按钮
        ImageButton add_btn = findViewById(R.id.add_btn);
        ImageButton all_btn = findViewById(R.id.all_btn);
        ImageButton diary_btn = findViewById(R.id.dairy_btn);
        ImageButton analyse_btn = findViewById(R.id.analyse_btn);
        ImageButton setting_btn = findViewById(R.id.setting_btn);
        //设置监听事件
        add_btn.setOnClickListener(this);
        all_btn.setOnClickListener(this);
        diary_btn.setOnClickListener(this);
        analyse_btn.setOnClickListener(this);
        setting_btn.setOnClickListener(this);

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
        int btn_id = view.getId();
        //转入日记界面
        if(btn_id == R.id.dairy_btn)
        {
            Intent diary_intent = new Intent(this, DiaryActivity.class);
            diary_intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(diary_intent);
        }



    }
}
