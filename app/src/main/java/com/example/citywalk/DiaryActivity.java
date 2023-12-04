package com.example.citywalk;


import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.citywalk.util.ButtonChoose;




public class DiaryActivity extends AppCompatActivity implements View.OnClickListener{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        ButtonChoose.initButton(this);

//        LinearLayout item_1 = (LinearLayout)this.findViewById(R.id.item_1);

        LinearLayout bigbox = (LinearLayout)this.findViewById(R.id.big_box);
        ImageView cancel_bn=(ImageView)this.findViewById(R.id.bigbox_cancel);



//        item_1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bigbox.setVisibility(View.VISIBLE);
//            }
//        });

        cancel_bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bigbox.setVisibility(View.INVISIBLE);
            }
        });


        //隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //设置顶部状态栏为透明
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

    }


    @Override
    public void onClick(View view) {
        //调用关于各个按钮的监听
        ButtonChoose.chooseButton(this,view);

    }
}