package com.mingrisoft.yysb.util;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.gson.Gson;
import com.mingrisoft.yysb.R;
import com.mingrisoft.yysb.bean.Lastbean;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity2 extends AppCompatActivity {
    private List<CardViewData> dataList = new ArrayList<>();
    OkHttpUtil okHttpUtil = new OkHttpUtil();
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail2);
        //initData();
        recyclerView = findViewById(R.id.recycler_view);
    }
    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DetailAdapter adapter = new DetailAdapter(dataList);
        recyclerView.setAdapter(adapter);
    }

}
