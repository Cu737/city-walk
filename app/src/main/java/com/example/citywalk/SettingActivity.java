package com.example.citywalk;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.citywalk.util.ButtonChoose;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageButton useMethod;
    private ImageButton aboutUs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);



        ButtonChoose.initButton(this);

        //获取关于我们和使用说明按钮
        useMethod = this.findViewById(R.id.use_method);
        aboutUs = this.findViewById(R.id.about_us);
        useMethod.setOnClickListener(this);
        aboutUs.setOnClickListener(this);

        //隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //设置顶部状态栏为透明
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
    }

    @Override
    public void onClick(View view) {
        ButtonChoose.chooseButton(this,view);
        if (view.getId()==R.id.use_method)
        {
            Intent intent = new Intent(this, UseMethodActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            this.startActivity(intent);
        }
        else if (view.getId() == R.id.about_us)
        {
            Intent intent = new Intent(this, AboutUsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            this.startActivity(intent);
        }
    }
}