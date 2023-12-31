package com.example.citywalk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.tencent.map.geolocation.s;

import java.io.File;
import java.util.List;
import java.util.SimpleTimeZone;


//编写适配器
public class GridAdapter extends RecyclerView.Adapter {
    //子控件设置监听
    public OnItemClickListener mOnItemClickListener;
    public interface OnItemClickListener{
        void onClick(int position);
    }

    public void setOnItemListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener=mOnItemClickListener;
    }





    private Context mContext;
    private List<Diary> mData;

    //构造方法，传入上下文和数据源
    public GridAdapter(Context context,List<Diary> data){
        mContext=context;
        mData=data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item,null);
        return new MyViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
        MyViewHolder holder2=(MyViewHolder) holder;
        Diary diary=mData.get(position);

        Uri uri = Uri.parse(diary.m);
        holder2.item_m.setImageURI(uri);///////////////////////////////////此处需要检查图片显示

        //holder2.item_m.setImageURI(Uri.fromFile(new File(diary.m)));
        holder2.item_t.setText(diary.t);
        holder2.item_b.setText(diary.b);

        if (mOnItemClickListener != null){
            holder2.item_l.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mData != null){
            return mData.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView item_t;
        public ImageView item_m;
        public TextView item_b;
        public LinearLayoutCompat item_l;
        public MyViewHolder(View itemView) {
            super(itemView);
            item_t = (TextView) itemView.findViewById(R.id.item_t);
            item_m = (ImageView) itemView.findViewById(R.id.item_m);
            item_b = (TextView) itemView.findViewById(R.id.item_b);

            item_l=(LinearLayoutCompat) itemView.findViewById(R.id.item_l);
        }
    }

}
