package com.mingrisoft.yysb.util;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mingrisoft.yysb.R;
import java.util.List;



public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.MyViewHolder> {
    private Context mContext;//上下文
    private List<CardViewData> mCardViewData;

    public DetailAdapter(List<CardViewData> dataList) {
        mCardViewData = dataList;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_depotname;
        TextView tv_measuringtime;
        TextView tv_min;
        TextView tv_mean;
        TextView tv_max;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_depotname = itemView.findViewById(R.id.tv_depotname);
            tv_measuringtime = itemView.findViewById(R.id.tv_measuringtime);
            tv_min = itemView.findViewById(R.id.tv_min);
            tv_mean = itemView.findViewById(R.id.tv_mean);
            tv_max = itemView.findViewById(R.id.tv_max);
        }
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_adapter, parent, false);
        MyViewHolder holder = new MyViewHolder(view);//将布局加载进来，创建实例，将布局加载到构造函数中
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CardViewData cardViewData = mCardViewData.get(position);
        holder.tv_depotname.setText(cardViewData.getDepotname());
        holder.tv_measuringtime.setText((CharSequence) cardViewData.getMeasuringtime());
        holder.tv_min.setText(cardViewData.getMin());
        holder.tv_max.setText(cardViewData.getMax());
        holder.tv_mean.setText(cardViewData.getMean());
    }

    @Override
    public int getItemCount() {
        return mCardViewData.size();
    }


}



