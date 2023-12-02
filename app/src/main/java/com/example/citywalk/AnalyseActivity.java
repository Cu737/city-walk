package com.example.citywalk;

import android.graphics.Color;
import android.view.View;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.citywalk.util.ButtonChoose;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.*;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.*;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.Arrays;

public class AnalyseActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyse);

        ButtonChoose.initButton(this);

        //隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //设置顶部状态栏为透明
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        LineChart lineChart = (LineChart)this.findViewById(R.id.line_chart);

//        数据部分
        ArrayList<Entry> meter_data = new ArrayList<Entry>();
        meter_data.add(new Entry(0, 10000));
        meter_data.add(new Entry(1, 12000));
        meter_data.add(new Entry(2, 42000));
        meter_data.add(new Entry(3, 20000));
        meter_data.add(new Entry(4, 15000));



        LineDataSet timeslineData = new LineDataSet(meter_data,"天数");
        LineData data = new LineData(timeslineData);
        lineChart.setData(data);
        timeslineData.setLineWidth(5f);

//      去除description
        Description description = new Description();
        description.setEnabled(false);
        lineChart.setDescription(description);

//      去除对图标的触摸操作，如果没有去除，可以通过触摸对折线图进行
//        放大，缩小等操作。
        lineChart.setTouchEnabled(false);
//        将折现转变位曲线
        timeslineData.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//        设置曲线颜色
        timeslineData.setColor(R.color.black);
//        不显示圆点(默认是在每个数据点为蓝色小圆圈)
        timeslineData.setDrawCircles(true);
        timeslineData.setCircleColor(R.color.black);
//        不显示数据点的数值
        timeslineData.setDrawValues(false);

//        X轴处理
        XAxis xAxis = lineChart.getXAxis();
//        设置X轴的位置为正下方
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        设置X轴坐标的刻度，即X轴上出现几个刻度
        xAxis.setLabelCount(5);
//        去除X轴上网格竖线
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1.0F);
//        避免“剪掉”在x轴上的图表或屏幕边缘的第一个和最后一个坐标轴标签项
        xAxis.setAvoidFirstLastClipping(true);
//        自定义X轴上的刻度显示
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                //System.out.println(Arrays.toString(axis.mEntries));
                //System.out.println(axis.mDecimals);
                return 4-(int) value + "天前";
            }
        });
        lineChart.invalidate();


//        Y轴处理
        YAxis ylAxis =lineChart.getAxis(YAxis.AxisDependency.LEFT);
        ylAxis.setAxisMinimum(0f);
//        设置Y轴的最大值
        ylAxis.setAxisMaximum(50000);
//        Y轴横线设置为虚线
        ylAxis.enableGridDashedLine(20, 15, 0);
//        自定义Y轴的刻度显示
        ylAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return value + "米";
            }
        });

        // 隐藏右侧Y轴（默认的折线图是左右两侧各一条Y轴）
        YAxis yrAxis = lineChart.getAxisRight();
        yrAxis.setEnabled(false);

        // 去掉 Legend
        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);

    }

    @Override
    public void onClick(View view) {
        ButtonChoose.chooseButton(this,view);
    }
}