package com.mingrisoft.yysb.util;


import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mingrisoft.yysb.R;
import com.mingrisoft.yysb.bean.Lastbean;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private List<CardViewData> dataList = new ArrayList<CardViewData>();
    OkHttpUtil okHttpUtil = new OkHttpUtil();
    private  RecyclerView recyclerView;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initData();
        recyclerView = findViewById(R.id.recycler_view);

    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DetailAdapter adapter = new DetailAdapter(dataList);
        recyclerView.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initData(){
        OkHttpUtil okHttpUtil = new OkHttpUtil();
        okHttpUtil.Get1("http://wxmessage.honencloud.com/datainfo?username=怀宁省级粮食储备库",new OkHttpUtil.ReqCallBack() {
            @Override
            public void onReqSuccess(Object result) {
                Gson gson = new Gson();
                Lastbean lastbean = gson.fromJson((String) result, Lastbean.class);
                if (lastbean.getData().getHasdepots().size()>0){
                    for(int i = 0; i<lastbean.getData().getHasdepots().size();i++){
                        CardViewData cardViewData = new CardViewData(lastbean.getData().getHasdepots().get(i).getDepotname(),lastbean.getData().getHasdepots().get(i).getMeasuringtime(),
                                lastbean.getData().getHasdepots().get(i).getMin(),lastbean.getData().getHasdepots().get(i).getMax(),
                                lastbean.getData().getHasdepots().get(i).getMean());
                        dataList.add(cardViewData);
                    }
                    initView();
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {

            }
        });
    }
}
