package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    public static List<MusicResponse> mList;
    public static Context context;
    private int position_hightLight = -1;
    private OnClickItem onClickItem;

    public MyAdapter(Context context, List<MusicResponse> listData) {
        this.context = context;
        this.mList = listData;
    }

    public void setOnClickItem(OnClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    public static List<MusicResponse> getmList() {
        return mList;
    }

    public void setPosition_hightLight(int position_hightLight) {
        this.position_hightLight = position_hightLight;
    }

    public void setmList(List<MusicResponse> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_lyric, viewGroup, false));
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        final MusicResponse fileAttach = mList.get(position);
        if (!TextUtils.isEmpty(fileAttach.getName())) {
            viewHolder.tvTitle.setText(fileAttach.getName());
        }
//        if (fileAttach.isSelected()) {
//            viewHolder.layoutDownload.setBackgroundResource(R.color.colorPrimaryDark);
//        } else {
//            viewHolder.layoutDownload.setBackgroundResource(R.color.white);
//        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickItem.onClicktem(position, fileAttach);
                Log.d("VinhLT:", "onClick" +"item_Adapter");
            }
        });

        if (position == position_hightLight) {
            viewHolder.layoutDownload.setBackgroundResource(R.color.colorPrimaryDark);
        } else {
            viewHolder.layoutDownload.setBackgroundResource(R.color.white);
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        LinearLayout layoutDownload;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_lyric);
            layoutDownload = itemView.findViewById(R.id.ln_list);
        }
    }

    public void setData(ArrayList<MusicResponse> mList) {
        this.mList = mList;
    }

    public interface OnClickItem {
        void onClicktem(int position, MusicResponse musicResponse);
    }
}
