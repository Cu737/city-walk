package com.example.citywalk;


import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.example.citywalk.util.ButtonChoose;

import java.util.*;


public class DiaryActivity extends AppCompatActivity implements View.OnClickListener{
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private GridAdapter mAdapter;


    public int item_num;
    public List<Diary> item_lst;


    //bigbox相关属性
    private LinearLayout bigbox;
    private TextView bbtop_text;
    private ImageView bbmid_img;
    private TextView bbbot_text;
    private ImageView bigbox_cancel;


    void init_diary_list(){
        item_lst.add(new Diary("lng.111 lat.123","drawable/diary_r1.webp","this is diary_contentthis is diary_contentthis is diary_content"));
        item_lst.add(new Diary("lng.222 lat.123","drawable/diary_r1.webp","this is diary_contentthis is diary_contentthis is diary_content"));
        item_lst.add(new Diary("lng.333 lat.123","drawable/diary_r1.webp","this is diary_contentthis is diary_contentthis is diary_content"));
        item_lst.add(new Diary("lng.444 lat.123","drawable/diary_r1.webp","this is diary_contentthis is diary_contentthis is diary_content"));
        item_lst.add(new Diary("lng.555 lat.123","D:\\Git_repository\\CityWalk\\app\\src\\main\\res\\drawable\\diary_r1.webp","this is diary_contentthis is diary_contentthis is diary_content"));
        item_lst.add(new Diary("lng.666 lat.123","D:\\Git_repository\\CityWalk\\app\\src\\main\\res\\drawable\\diary_r1.webp","this is diary_contentthis is diary_contentthis is diary_content"));
        item_lst.add(new Diary("lng.777 lat.123","D:\\Git_repository\\CityWalk\\app\\src\\main\\res\\drawable\\diary_r1.webp","this is diary_contentthis is diary_contentthis is diary_content"));
        item_lst.add(new Diary("lng.888 lat.123","D:\\Git_repository\\CityWalk\\app\\src\\main\\res\\drawable\\diary_r1.webp","this is diary_contentthis is diary_contentthis is diary_content"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        ButtonChoose.initButton(this);

        //初始化数据，此处需要ldm来调一下
        item_lst=new ArrayList<>();
        init_diary_list();
        item_num=item_lst.size();
        System.out.println("diary item_lst size:"+item_num);


        //设置布局管理和适配器
        System.out.println("diary page begin");
        mRecyclerView  = (RecyclerView) findViewById(R.id.recyclerview);
        mLayoutManager = new GridLayoutManager(this,2);
        mAdapter = new GridAdapter(this,item_lst);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        //引用bigbox相关并监听
        bigbox = (LinearLayout)this.findViewById(R.id.big_box);
        bbtop_text=(TextView)this.findViewById(R.id.bbtop_text);
        bbmid_img=(ImageView)this.findViewById(R.id.bbmid_img);
        bbbot_text=(TextView)this.findViewById(R.id.bbbot_text);
        bigbox_cancel=(ImageView)this.findViewById(R.id.bigbox_cancel);
        bigbox_cancel.setOnClickListener(this);



        //隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //设置顶部状态栏为透明
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

    }


    private void refreshBigboxContent(int x){
        switch (x) {
            case 1:
                bbtop_text.setText("lng.123 lat.123");
                bbmid_img.setImageDrawable(getResources().getDrawable(R.drawable.diary_r1));/////图片地址待修改
                bbbot_text.setText("this is diary_content only for item_1");
                break;
        }
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
//        else if (view.getId() == R.id.item_1) {
//            refreshBigboxContent(1);
//            bigbox.setVisibility(View.VISIBLE);
//            System.out.println("diary:click item_1");
//        }
    }
}