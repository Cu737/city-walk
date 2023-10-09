package com.example.citywalk;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class DiaryActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

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

        //隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //设置顶部状态栏为透明
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
    }

    @Override
    public void onClick(View view) {
        int btn_id = view.getId();
        //转入日记界面
        if(btn_id == R.id.add_btn)
        {
            Intent diary_intent = new Intent(this, MainActivity.class);
            diary_intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(diary_intent);
        }

    }
}