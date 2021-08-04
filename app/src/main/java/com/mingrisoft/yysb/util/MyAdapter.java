package com.mingrisoft.yysb.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mingrisoft.yysb.MainActivity;
import com.mingrisoft.yysb.R;
import com.mingrisoft.yysb.bean.TextBean;

import java.util.List;

public class MyAdapter extends BaseAdapter {
    private List<TextBean> mListBean;
    Context context;

    public MyAdapter(Context context,List<TextBean> mListBean) {
        this.mListBean = mListBean;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mListBean.size();
    }

    @Override
    public TextBean getItem(int position) {
        return mListBean.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHodler mHolder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_pager, null);
            mHolder = new viewHodler();
            mHolder.leftLayout = convertView.findViewById(R.id.msg_left);
            mHolder.rightLayout = convertView.findViewById(R.id.msg_right);
            mHolder.leftTextView = convertView.findViewById(R.id.left_tv);
            mHolder.rightTextView = convertView.findViewById(R.id.right_tv);
            convertView.setTag(mHolder);
            /*mHolder.tvAsk =  convertView.findViewById(R.id.tv_ask);
            mHolder.tvAnswer = convertView.findViewById(R.id.tv_answer);
            mHolder.llAnswer = convertView.findViewById(R.id.ll_answer);*/
           // mHolder.ivAnswer = convertView.findViewById(R.id.iv_answer);

        }else{
            mHolder = (viewHodler) convertView.getTag();
        }

        TextBean item = getItem(position);
        if(item.isAsk){
            mHolder.leftLayout.setVisibility(View.VISIBLE);
            mHolder.rightLayout.setVisibility(View.GONE);
            mHolder.leftTextView.setText(item.text);
        }else{
            mHolder.leftLayout.setVisibility(View.GONE);
            mHolder.rightLayout.setVisibility(View.VISIBLE);
            mHolder.leftTextView.setText(item.text);

        }

        return convertView;
    }
    class viewHodler{
        LinearLayout leftLayout;
        LinearLayout rightLayout;

        TextView leftTextView;
        TextView rightTextView;
       /* public TextView tvAsk;
        public TextView tvAnswer;
        public LinearLayout llAnswer;
        //public ImageView ivAnswer;*/
    }
}

