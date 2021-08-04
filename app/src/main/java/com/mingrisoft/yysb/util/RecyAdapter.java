package com.mingrisoft.yysb.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mingrisoft.yysb.R;
import com.mingrisoft.yysb.bean.TextBean;

import java.util.ArrayList;
import java.util.List;

/*
public class RecyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private List<TextBean> mListBean;
    private OnItemClickLitener mOnItemClickLitener;

    public RecyAdapter(Context context,List<TextBean> mListBean) {
        this.mListBean = mListBean;
        this.context = context;
    }

    public enum ITEM_TYPE {
        ITEM_TYPE_SEND,
        ITEM_TYPE_RECIVE,
        ITEM_TYPE_TIME
    }

    public void setData(List<TextBean> mListBean){
        this.mListBean = mListBean;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM_TYPE_SEND.ordinal()) {
            return new SendMsgViewHolder(LayoutInflater.from(context).inflate(R.layout.item_send_msg, parent, false));
        }else if (viewType == ITEM_TYPE.ITEM_TYPE_RECIVE.ordinal()){
            return new ReceiveMsgViewHolder(LayoutInflater.from(context).inflate(R.layout.item_receive_msg, parent, false));
        }else{
            return new MsgTimeViewHolder(LayoutInflater.from(context).inflate(R.layout.item_msg_time, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof SendMsgViewHolder) {
            ((SendMsgViewHolder) holder).tvSendContent.setText(mListBean.get(position).toString());
        } else if (holder instanceof ReceiveMsgViewHolder) {
            ((ReceiveMsgViewHolder) holder).tvReceiveContent.setText(mListBean.get(position).toString());
        }else {
            ((MsgTimeViewHolder) holder).tvMsgTime.setText(mListBean.get(position).toString());
        }

        //设置item的点击事件
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mListBean.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (mListBean.get(position).getType()){
            case "send":
                return ITEM_TYPE.ITEM_TYPE_SEND.ordinal();
            case "receive":
                return ITEM_TYPE.ITEM_TYPE_RECIVE.ordinal();
            case "time":
                return ITEM_TYPE.ITEM_TYPE_TIME.ordinal();
            default:
                break;
        }

        return super.getItemViewType(position);
    }

    public static class SendMsgViewHolder extends RecyclerView.ViewHolder {
        ImageView ivSendContent;
        TextView tvSendContent;

        SendMsgViewHolder(View itemView) {
            super(itemView);
            ivSendContent = itemView.findViewById(R.id.right_img);
            tvSendContent = itemView.findViewById(R.id.right_tv);
        }
    }

    public static class ReceiveMsgViewHolder extends RecyclerView.ViewHolder {
        ImageView ivReceiveContent;
        TextView tvReceiveContent;

        ReceiveMsgViewHolder(View itemView) {
            super(itemView);
            ivReceiveContent = itemView.findViewById(R.id.left_img);
            tvReceiveContent = itemView.findViewById(R.id.left_tv);
        }
    }

    public static class MsgTimeViewHolder extends RecyclerView.ViewHolder {

        TextView tvMsgTime;

        MsgTimeViewHolder(View itemView) {
            super(itemView);
            tvMsgTime = itemView.findViewById(R.id.msg_time);
        }
    }

    //设置item点击监听
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

}
*/
