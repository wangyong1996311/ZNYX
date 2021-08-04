package com.mingrisoft.yysb.util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.mingrisoft.yysb.MainActivity;
import com.mingrisoft.yysb.bean.Historybean;
import com.mingrisoft.yysb.bean.Lastbean;
import com.mingrisoft.yysb.body.Historybody;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.N)
public class Utility {
    public static List<NumberData> dataList = new ArrayList<NumberData>();
    public static List<HistoryData> historydaylist = new ArrayList<HistoryData>();
    public static List<HistoryData> weekdatalist = new ArrayList<>();
    //public static L
    OkHttpUtil okHttpUtil = new OkHttpUtil();
    //处理最新数据
    public static void handlenew(){
        OkHttpUtil okHttpUtil = new OkHttpUtil();
        okHttpUtil.Get1("http://wxmessage.honencloud.com/datainfo?username=怀宁省级粮食储备库",new OkHttpUtil.ReqCallBack() {
           @Override
           public void onReqSuccess(Object result) {
               Gson gson = new Gson();
               Lastbean lastbean = gson.fromJson((String) result, Lastbean.class);
               if (lastbean.getData()!=null){
                   NumberData numberData = new NumberData(lastbean.getData().getBaddepots(),lastbean.getData().getHasdepots(),
                           lastbean.getData().getHstdepots(),lastbean.getData().getOffdepots(),lastbean.getData().getPushmessage());
                   dataList.add(numberData);
               }
           }

           @Override
           public void onReqFailed(String errorMsg) {

           }
       });
    }
    //获取近一周的比较的历史数据
    public static void initdayhistorydata() {
        Calendar calendar = Calendar.getInstance();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.DAY_OF_YEAR,-30);
        Long dd = calendar.getTime().getTime()/1000;
        Long dd1 = calendar1.getTime().getTime()/1000;
        OkHttpUtil okHttpUtil = new OkHttpUtil();
        Gson gson = new Gson();
        Historybody historybody = new Historybody();
        historybody.setUsername("怀宁省级粮食储备库");
        historybody.setStarttime(dd1);
        historybody.setEndtime(dd);
        String jsonData = gson.toJson(historybody);
        okHttpUtil.HistoryPost1("http://wxmessage.honencloud.com/datainfo", jsonData, new OkHttpUtil.ReqCallBack() {
            @Override
            public void onReqSuccess(Object result) {
                Gson gson = new Gson();
                Historybean historybean = gson.fromJson((String) result, Historybean.class);
                if(historybean.getData().size()>0 && historybean.getData() !=null){
                    for(int i = 0; i < historybean.getData().size(); i++){
                        HistoryData historyData = new HistoryData(historybean.getData().get(i).getBaddepots(),historybean.getData().get(i).getHasdepots(),
                                historybean.getData().get(i).getHstdepots(),historybean.getData().get(i).getOffdepots(),historybean.getData().get(i).getPushmessage());
                        HistoryData historyData1 = new HistoryData(historybean.getData().get(i).getBaddepots(),historybean.getData().get(i).getHasdepots(),
                                historybean.getData().get(i).getHstdepots(),historybean.getData().get(i).getOffdepots(),historybean.getData().get(i).getPushmessage());
                        historydaylist.add(historyData1);
                    }
                }


            }

            @Override
            public void onReqFailed(String errorMsg) {

            }
        });
    }
    //获取一周往期历史信息（不包括今日）
    public static void initweekhistorydata() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),0,0,0);
        long tt = calendar.getTime().getTime()/1000;
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(calendar1.get(Calendar.YEAR),calendar1.get(Calendar.MONTH),calendar1.get(Calendar.DAY_OF_MONTH)-7,0,0,0);
        long tt1 = calendar1.getTime().getTime()/1000;
        OkHttpUtil okHttpUtil = new OkHttpUtil();
        Gson gson = new Gson();
        Historybody historybody = new Historybody();
        historybody.setUsername("怀宁省级粮食储备库");
        historybody.setStarttime(tt1);
        historybody.setEndtime(tt);
        String jsonData = gson.toJson(historybody);
        okHttpUtil.HistoryPost1("http://wxmessage.honencloud.com/datainfo", jsonData, new OkHttpUtil.ReqCallBack() {
            @Override
            public void onReqSuccess(Object result) {
                Gson gson = new Gson();
                Historybean historybean = gson.fromJson((String) result, Historybean.class);
                if(historybean.getData().size()>0 && historybean.getData() !=null){
                    for(int i = 0; i < historybean.getData().size(); i++){
                        HistoryData historyData = new HistoryData(historybean.getData().get(i).getBaddepots(),historybean.getData().get(i).getHasdepots(),
                                historybean.getData().get(i).getHstdepots(),historybean.getData().get(i).getOffdepots(),historybean.getData().get(i).getPushmessage());
                        HistoryData historyData1 = new HistoryData(historybean.getData().get(i).getBaddepots(),historybean.getData().get(i).getHasdepots(),
                                historybean.getData().get(i).getHstdepots(),historybean.getData().get(i).getOffdepots(),historybean.getData().get(i).getPushmessage());
                        weekdatalist.add(historyData1);
                    }
                }


            }

            @Override
            public void onReqFailed(String errorMsg) {

            }
        });
    }


}
