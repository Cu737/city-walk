package com.example.citywalk;


import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.LinearLayoutCompat;
import com.example.citywalk.util.ButtonChoose;




public class DiaryActivity extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout bigbox;
    private ImageView bigbox_cancel;
    private LinearLayoutCompat item_1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        ButtonChoose.initButton(this);

        System.out.println("diary page begin");
        bigbox = (LinearLayout)this.findViewById(R.id.big_box);
        bigbox.setVisibility(View.VISIBLE);

        bigbox_cancel=(ImageView)this.findViewById(R.id.bigbox_cancel);
        bigbox_cancel.setOnClickListener(this);

        item_1 = (LinearLayoutCompat)this.findViewById(R.id.item_1);
        item_1.setOnClickListener(this);


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
//        System.out.println("diary:click bn"+view.getId());
        if(view.getId() == R.id.bigbox_cancel)
        {
            bigbox.setVisibility(View.INVISIBLE);
            System.out.println("diary:click bigbox_cancel");
        }
        else if (view.getId() == R.id.item_1) {
            bigbox.setVisibility(View.VISIBLE);
            System.out.println("diary:click item_1");
        }
    }
}