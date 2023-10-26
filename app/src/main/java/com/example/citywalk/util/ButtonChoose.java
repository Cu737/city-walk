package com.example.citywalk.util;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import com.example.citywalk.*;

public class ButtonChoose {
    public static void initButton(Activity activity)
    {
        //封装设置初始化按钮并设置监听的代码
        ImageButton add_btn = activity.findViewById(R.id.add_btn);
        ImageButton all_btn = activity.findViewById(R.id.all_btn);
        ImageButton diary_btn = activity.findViewById(R.id.dairy_btn);
        ImageButton analyse_btn = activity.findViewById(R.id.analyse_btn);
        ImageButton setting_btn = activity.findViewById(R.id.setting_btn);

        add_btn.setOnClickListener((View.OnClickListener) activity);
        all_btn.setOnClickListener((View.OnClickListener) activity);
        diary_btn.setOnClickListener((View.OnClickListener) activity);
        analyse_btn.setOnClickListener((View.OnClickListener) activity);
        setting_btn.setOnClickListener((View.OnClickListener) activity);
    }
    public static void chooseButton(Activity activity,View view)
    {
        //封装按钮跳转代码
        int btn_id = view.getId();
        //转入日记界面
        if(btn_id == R.id.add_btn && activity.getLocalClassName().equals("MainActivity"))
        {
            View add_dairy = activity.findViewById(R.id.add_dairy);
            add_dairy.setVisibility(View.VISIBLE);
        }
        else if(btn_id == R.id.add_btn)
        {
            Intent intent = new Intent(activity, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            activity.startActivity(intent);
        }
        else if (btn_id == R.id.dairy_btn)
        {
            Intent intent = new Intent(activity, DiaryActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            activity.startActivity(intent);
        }
        else if (btn_id == R.id.setting_btn)
        {
            Intent intent = new Intent(activity, SettingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            activity.startActivity(intent);
        }
        else if (btn_id == R.id.analyse_btn)
        {
            Intent intent = new Intent(activity, AnalyseActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            activity.startActivity(intent);
        }
        else if (btn_id == R.id.all_btn)
        {
            Intent intent = new Intent(activity, AllActivty.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            activity.startActivity(intent);
        }

    }


}
