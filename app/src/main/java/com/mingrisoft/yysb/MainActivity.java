
package com.mingrisoft.yysb;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.mingrisoft.yysb.bean.TextBean;
import com.mingrisoft.yysb.util.DetailActivity;
import com.mingrisoft.yysb.util.OkHttpUtil;
import com.mingrisoft.yysb.util.ParsonActivity;
import com.mingrisoft.yysb.util.PinyinSimilarity;
import com.mingrisoft.yysb.util.Utility;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private com.google.android.material.navigation.NavigationView navigationview;//导航视图
    private androidx.drawerlayout.widget.DrawerLayout drawerlayout;//抽屉
    private ImageView left;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private ImageView person_pic;//侧滑菜单头部的图片
    private TextView companyText;//侧滑菜单头部的文字
    private ListView iv_list;
    OkHttpUtil okHttpUtil = new OkHttpUtil();
    StringBuffer mBuffer = new StringBuffer();
    public ArrayList<TextBean> mListBean = new ArrayList<TextBean>(); //把文字对象放在容器中
    private MyAdapter myAdapter;
    public String[] UnKnow = new String[]{/*"不好意思，我注入的词汇不够！", "我还是个孩子啊！别问那么多问题！",
            "我听不懂！", "你还是别知道太多了！"*/"没听清，请您再说一遍"};
    FloatingActionButton imageButton;
    private Button mBtn;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SpeechUtility.createUtility(MainActivity.this, SpeechConstant.APPID + "=5e9bff4b");
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.RECORD_AUDIO},MY_PERMISSIONS_REQUEST_READ_CONTACTS
            );
        }else{
            //
        }

        iv_list = (ListView) findViewById(R.id.iv_list);
        left=findViewById(R.id.leftimgview);
        imageButton = (FloatingActionButton) findViewById(R.id.fb);
        myAdapter = new MyAdapter(MainActivity.this,mListBean);
        //initnewdata();
        initFindId();
        initView();
        if(navigationview.getHeaderCount() > 0){
            View headerLayout = navigationview.getHeaderView(0);
            person_pic = headerLayout.findViewById(R.id.person_pic);
            companyText=headerLayout.findViewById(R.id.companyText);
        }
        else {
            View headerLayout = navigationview.inflateHeaderView(R.layout.layout_head);
            person_pic = headerLayout.findViewById(R.id.person_pic);
            companyText=headerLayout.findViewById(R.id.companyText);
        }
        Utility.handlenew();
        Utility.initdayhistorydata();
        Utility.initweekhistorydata();
        iv_list.setAdapter(myAdapter);
        left.setOnClickListener(this);

    }

    // 语音听写UI
    public void startListenUI(View view) {
        // 1.创建RecognizerDialog对象
        RecognizerDialog mDialog = new RecognizerDialog(MainActivity.this,null);//初始化有交互动画的语音识别器
        // 2.设置accent、language等参数
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");

        // 3.设置回调接口
        mDialog.setListener(new RecognizerDialogListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResult(RecognizerResult results, boolean isLast) {

                String parseResult = ParsonActivity.parseData(results.getResultString());
//                  System.out.println(parseResult);
                mBuffer.append(parseResult);
//                  System.out.println("isLast:" + isLast);
                if(isLast){
                    String finalResult = mBuffer.toString();
                    System.out.println();

                    mBuffer = new StringBuffer();   //相当于StrinBuffer清空
                    //把说的文字进行封装
                    //提问finalResult
                    /*TextBean askBean = new TextBean(finalResult, true);
                    mListBean.add(askBean);*/
                    Random random = new Random();
                    String answer = UnKnow[random.nextInt(UnKnow.length)];
                    finalResult=changeToOurWords(finalResult);
                    TextBean askBean = new TextBean(finalResult, true);
                    mListBean.add(askBean);
                    String str= finalResult;
                    String str2 = "";
                    for(int i=0;i<str.length();i++){
                        if(str.charAt(i)>=48 && str.charAt(i)<=57){
                            str2+=str.charAt(i);
                        }
                    }
                    try {
                   //关于询问粮仓单日信息的问题
                       if(finalResult.contains("多少") || finalResult.contains("几个")){
                           if(finalResult.contains("粮仓") || finalResult.contains("仓库")){
                               if(finalResult.contains("正常")){
                                   if(Utility.dataList!=null && Utility.dataList.size()>0){
                                       if(Utility.dataList.get(0).getHasdepots()!=null){
                                           String string = "";
                                           for(int i = 0; i < Utility.dataList.get(0).getHasdepots().size();i++){
                                               string += Utility.dataList.get(0).getHasdepots().get(i).getDepotname()+"号，";
                                           }
                                            answer = "粮站现在有" + Utility.dataList.get(0).getHasdepots().size() +"个正常测量的仓库"+"分别是"+string+"这"+Utility.dataList.get(0).getHasdepots().size()+"个仓库";
                                       }else {
                                           answer = "粮站不存在有温度正常测量的仓库";
                                       }
                                   }
                               }else if(finalResult.contains("关机")){
                                   if(Utility.dataList!=null && Utility.dataList.size()>0){
                                       if(Utility.dataList.get(0).getOffdepots()!=null){
                                           String string = "";
                                           for(int i = 0; i < Utility.dataList.get(0).getOffdepots().size();i++){
                                               string += Utility.dataList.get(0).getOffdepots().get(i)+"号";
                                           }
                                           answer = "粮站现在有" + Utility.dataList.get(0).getOffdepots().size() +"个正常测量的仓库"+"分别是"+string+"这"+Utility.dataList.get(0).getHasdepots().size()+"个仓库";
                                       }else {
                                           answer = "粮站不存在关机的仓库";
                                       }
                                   }
                               }else if(finalResult.contains("故障")){
                                   if(Utility.dataList!=null && Utility.dataList.size()>0){
                                       if(Utility.dataList.get(0).getBaddepots()!=null){
                                           String string = "";
                                           for(int i = 0; i < Utility.dataList.get(0).getBaddepots().size();i++){
                                               string += Utility.dataList.get(0).getBaddepots().get(i)+"号";
                                           }
                                           answer = "粮站现在有" + Utility.dataList.get(0).getBaddepots().size() +"个故障的仓库"+"是"+string+"这"+Utility.dataList.get(0).getBaddepots().size()+"个仓库";
                                       }else {
                                           answer = "粮站未存在有温度正常测量的仓库";
                                       }
                                   }
                               }else if(finalResult.contains("没有测量") || finalResult.contains("没有回数据")){
                                   if(Utility.dataList!=null && Utility.dataList.size()>0){
                                       if(Utility.dataList.get(0).getHstdepots()!=null){
                                           String string = "";
                                           for(int i = 0; i < Utility.dataList.get(0).getHstdepots().size();i++){
                                               string += Utility.dataList.get(0).getHstdepots().get(i)+"号，";
                                           }
                                           answer = "粮站现在有" + Utility.dataList.get(0).getHstdepots().size() +"没有测量的仓库"+"分别是"+string+"仓库";
                                       }else {
                                           answer = "粮站不存在没有测量的仓库";
                                       }
                                   }
                               }else if(finalResult.contains("一共")){
                                  if(Utility.dataList!=null&&Utility.dataList.size()>0){
                                      if(Utility.dataList.get(0).getPushmessage()!=null){
                                          String string = Utility.dataList.get(0).getPushmessage().substring(0,8);
                                          answer = string;
                                      }
                                  }
                               }
                           }
                       }else if(finalResult.contains("温度测控详情")){
                           if(Utility.dataList!=null && Utility.dataList.size()>0){
                               if(Utility.dataList.get(0).getPushmessage() != null){
                                   String string = Utility.dataList.get(0).getPushmessage().substring(0,24);
                                   answer = string;
                               }else {
                                   answer = "粮站不存在仓库";
                               }
                           }
                   }else if(finalResult.contains("正常测量")  || finalResult.contains("温度正常测量")||finalResult.contains("正常")){
                    if(finalResult.contains("粮仓")||finalResult.contains("仓库")||finalResult.contains("仓")){
                        if(Utility.dataList.size()>0 && Utility.dataList != null){
                            String string = "";
                                for(int i = 0; i < Utility.dataList.get(0).getHasdepots().size();i++){
                                   string += Utility.dataList.get(0).getHasdepots().get(i).getDepotname()+"号，";
                                }
                                answer = "粮站温度正常测量的仓库有" + string +"共"+ Utility.dataList.get(0).getHasdepots().size()+"个仓库";
                            }else {
                                answer = "未存在正常测量的仓库";
                            }
                        }

                    }else if(finalResult.contains("未回数据")|| finalResult.contains("温度没有测量")||finalResult.contains("今日未回数据")||finalResult.contains("今日温度没有测量")||finalResult.contains("今日温度未回数据")||finalResult.contains("没有测量")||finalResult.contains("没有数据")){
                        if(finalResult.contains("仓库")||finalResult.contains("粮仓")||finalResult.contains("仓")){
                            if(Utility.dataList!=null && Utility.dataList.size()>0){
                                String string = "";
                                if(Utility.dataList.get(0).getHstdepots() != null){
                                    for(int i = 0; i < Utility.dataList.get(0).getHstdepots().size();i++){
                                        string += Utility.dataList.get(0).getHstdepots().get(i) +"号，";
                                    }
                                    answer = "未回数据的仓库有" + string+"共"+Utility.dataList.get(0).getHstdepots().size()+"个仓库";
                                }else {
                                    answer = "不存在温度没有测量，未回数据的仓库";
                                }
                            }
                        }
                    }else if(finalResult.contains("关机")||finalResult.contains("温度采集系统关机")||finalResult.contains("采集系统关机")){
                        if(finalResult.contains("仓库")||finalResult.contains("粮仓")||finalResult.contains("仓")){
                            if(Utility.dataList!=null && Utility.dataList.size()>0){
                                String string = "";
                                if(Utility.dataList.get(0).getOffdepots() != null){
                                    for(int i = 0; i < Utility.dataList.get(0).getHstdepots().size();i++){
                                        string += Utility.dataList.get(0).getHstdepots().get(i) +"号，";
                                    }
                                    answer = "关机的仓库有" + string + "共"+Utility.dataList.get(0).getOffdepots().size()+"个仓库";
                                }else {
                                    answer = "不存在关机的仓库";
                                }
                            }
                        }
                    }else if(finalResult.contains("测控故障") || finalResult.contains("采集点开路")||finalResult.contains("温度采集系统开路")||finalResult.contains("开路")||finalResult.contains("故障")){
                        if(finalResult.contains("仓库")||finalResult.contains("粮仓")||finalResult.contains("仓")){
                            if(Utility.dataList.size()>0 && Utility.dataList != null){
                                String string = "";
                                if(Utility.dataList.get(0).getBaddepots() != null){
                                    for(int i = 0; i < Utility.dataList.get(0).getBaddepots().size();i++){
                                        string += Utility.dataList.get(0).getBaddepots().get(i) + "号，";
                                    }
                                    answer = "温度采集系统开路，测控故障的仓库有" + string+"共"+ Utility.dataList.get(0).getBaddepots().size()+"个仓库";
                                }else {
                                    answer = "不存在测控故障的仓库";
                                }
                            }
                        }
                    }else if(finalResult.contains("仓温度")||finalResult.contains("粮仓温度")||finalResult.contains("仓库温度")){
                           int nb = Integer.parseInt(str2);
                            if(nb>0&&nb<=10){
                                if(Utility.dataList!=null && Utility.dataList.size()>0){
                                    if(Utility.dataList.get(0).getHasdepots()!=null&&Utility.dataList.get(0).getHasdepots().size()>0){
                                        for(int i = 0; i < Utility.dataList.get(0).getHasdepots().size();i++){
                                            if(Utility.dataList.get(0).getHasdepots().get(i).getDepotname().equals(str2)) {
                                                answer = Utility.dataList.get(0).getHasdepots().get(i).getDepotname() + "号仓温度情况:最高温度为" + Utility.dataList.get(0).getHasdepots().get(i).getMax() +"度,"+
                                                        "平均温度为" + Utility.dataList.get(0).getHasdepots().get(i).getMean()+"度," + "最低温度为" + Utility.dataList.get(0).getHasdepots().get(i).getMin()+"度,"+"最新一次测量在" +
                                                        Utility.dataList.get(0).getHasdepots().get(i).getMeasuringtime()+"。";
                                            break;
                                            }else {
                                                answer = "该仓温度尚未测量或未回数据";
                                            }
                                        }
                                    }
                                }
                            }else {
                                answer = "不存在"+str2+"号仓";
                            }
                        /*}*/
                    }else if(finalResult.contains("你好") || finalResult.contains("你是")){
                        answer = "你好，我是您的语音助手，很高兴为您服务，想要查询到粮仓温度信息，回复“最新数据”或“历史信息”，我将向您推荐相关问题查询";
                    }else if(finalResult.contains("最新数据")||finalResult.contains("最新温度")){
                        answer = "您可以回复“粮站温度测控详情”或“仓库工作状态”或“温度情况”进行相关问题查询";
                    }else if (finalResult.contains("温度情况")){
                        answer = "您可以通过仓库名加温度询问该仓库的温度信息，如“8号仓温度”";
                    }else if(finalResult.contains("仓库工作状态")||finalResult.contains("仓库工作情况")){
                        answer = "您可以了解粮站每个状态对应的仓库，可以向我提问：“粮站有几个正常测量的仓库”或“正常测量的仓库”？仓库的状态还有“未回数据”，“关机”，“测控故障”";
                    }else if(finalResult.contains("历史信息")||finalResult.contains("历史记录")){
                        answer = "通过询问历史信息，您可以查询到粮仓历史温度或温度变化情况，回复“历史温度”或“温度变化”，我将向您推荐相关问题查询";
                    }else if(finalResult.contains("最高温度变化")){
                        if(finalResult.contains("近两天")||finalResult.contains("这两天")||finalResult.contains("两天来")||finalResult.contains("两天")){
                            int nb = Integer.parseInt(str2);
                            if(nb>0&&nb<=10){
                                if(Utility.historydaylist !=null&&Utility.historydaylist.size()>0){
                                    if(Utility.historydaylist.get(Utility.historydaylist.size()-1)!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-1)!=null){
                                        if(Utility.historydaylist.get(Utility.historydaylist.size()-1).getHasdepots()!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-1).getHasdepots().size()>0&&Utility.historydaylist.get(Utility.historydaylist.size()-2).getHasdepots()!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-2).getHasdepots().size()>0){
                                            for(int i =0;i<Utility.historydaylist.get(Utility.historydaylist.size()-1).getHasdepots().size();i++){
                                                for(int j =0;j<Utility.historydaylist.get(Utility.historydaylist.size()-2).getHasdepots().size();j++){
                                                    if(Utility.historydaylist.get(Utility.historydaylist.size()-1).getHasdepots().get(i).getDepotname().equals(str2)&&Utility.historydaylist.get(Utility.historydaylist.size()-2).getHasdepots().get(j).getDepotname().equals(str2)){
                                                        NumberFormat nf = NumberFormat.getNumberInstance();
                                                        nf.setMaximumFractionDigits(2);
                                                        Float wd1 = Float.parseFloat(Utility.historydaylist.get(Utility.historydaylist.size() - 1).getHasdepots().get(i).getMax());
                                                        Float wd2 = Float.parseFloat(Utility.historydaylist.get(Utility.historydaylist.size() - 2).getHasdepots().get(i).getMax());
                                                        String t2 = Utility.historydaylist.get(Utility.historydaylist.size() - 2).getHasdepots().get(j).getMeasuringtime();
                                                        String t1 = Utility.historydaylist.get(Utility.historydaylist.size() - 1).getHasdepots().get(j).getMeasuringtime();
                                                        String string = "";
                                                        if(wd1 > wd2){
                                                            float count = Float.parseFloat(Utility.historydaylist.get(Utility.historydaylist.size() - 1).getHasdepots().get(i).getMax()) - Float.parseFloat(Utility.historydaylist.get(Utility.historydaylist.size() - 2).getHasdepots().get(j).getMax());
                                                            string = nf.format(count);
                                                            answer = str2+"号仓"+"在"+ t2+"至"+t1 + "的时间段内，测得最高温度上升了" + string
                                                                    + "度" + "最高温度分别为" + wd2 + "度" +"和"+ wd1 + "度。";
                                                            break;
                                                        }else if(wd1 < wd2 ){
                                                            float count = Float.parseFloat(Utility.historydaylist.get(Utility.historydaylist.size() - 2).getHasdepots().get(i).getMax()) - Float.parseFloat(Utility.historydaylist.get(Utility.historydaylist.size() - 1).getHasdepots().get(j).getMax());
                                                            string =nf.format(count);
                                                            answer =str2+ "号仓在"+ t2+"至"+t1+ "的时间段内，测得最高温度下降了" + string
                                                                    + "度" + "最高温度分别为" + wd2 + "度" +"和"+wd1 + "度。";
                                                            break;
                                                        }else if (wd1.equals(wd2)) {
                                                            answer = str2+"号仓在"+ t2+"至"+t1+"的时间段内，"+"测得最高温度持平，" + "并测得最高温度为" + wd1 + "度。";
                                                        }
                                                    }else if(Utility.historydaylist.get(Utility.historydaylist.size()-1).getBaddepots()!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-1).getBaddepots().size()>0||(Utility.historydaylist.get(Utility.historydaylist.size()-2).getBaddepots()!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-2).getBaddepots().size()>0)){
                                                        for(int m=0;m<Utility.historydaylist.get(Utility.historydaylist.size()-1).getBaddepots().size();m++) {
                                                            for(int n=0;n<Utility.historydaylist.get(Utility.historydaylist.size()-2).getBaddepots().size();n++)
                                                                if (Utility.historydaylist.get(Utility.historydaylist.size()-1).getBaddepots().get(m).equals(str2)||Utility.historydaylist.get(Utility.historydaylist.size()-2).getBaddepots().get(n).equals(str2)) {
                                                                    answer = "因温度采集系统开路，未返回"+str2+"号仓最高温度历史数据。";
                                                                }
                                                        }
                                                    }else if(Utility.historydaylist.get(Utility.historydaylist.size()-1).getOffdepots()!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-1).getOffdepots().size()>0||(Utility.historydaylist.get(Utility.historydaylist.size()-2).getOffdepots()!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-2).getOffdepots().size()>0)){
                                                        for(int m=0;m<Utility.historydaylist.get(Utility.historydaylist.size()-1).getOffdepots().size();m++) {
                                                            for(int n=0;n<Utility.historydaylist.get(Utility.historydaylist.size()-2).getOffdepots().size();n++)
                                                                if (Utility.historydaylist.get(Utility.historydaylist.size()-1).getOffdepots().get(m).equals(str2)||Utility.historydaylist.get(Utility.historydaylist.size()-2).getOffdepots().get(n).equals(str2)) {
                                                                    answer = str2+"号仓近期温度测量系统关机。";
                                                                }
                                                        }
                                                    }else if(Utility.historydaylist.get(Utility.historydaylist.size()-1).getHstdepots()!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-1).getHstdepots().size()>0||(Utility.historydaylist.get(Utility.historydaylist.size()-2).getHstdepots()!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-2).getHstdepots().size()>0)){
                                                        for(int m=0;m<Utility.historydaylist.get(Utility.historydaylist.size()-1).getHstdepots().size();m++) {
                                                            for(int n=0;n<Utility.historydaylist.get(Utility.historydaylist.size()-2).getHstdepots().size();n++)
                                                                if (Utility.historydaylist.get(Utility.historydaylist.size()-1).getHstdepots().get(m).equals(str2)||Utility.historydaylist.get(Utility.historydaylist.size()-2).getHstdepots().get(n).equals(str2)) {
                                                                    answer =str2+ "号仓近两天历史温度信息未能及时返回。";
                                                                }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }else {
                                    answer = "该段时间温度历史数据为空。";
                                }
                            }else {
                                answer = "不存在"+str2+"号仓。";
                            }
                       }else if(finalResult.contains("一周来")||finalResult.contains("近一周")||finalResult.contains("一周")){
                            int nb = Integer.parseInt(str2);
                            if(nb>0&&nb<=10){
                                if(Utility.historydaylist !=null&&Utility.historydaylist.size()>0){
                                    if(Utility.historydaylist.get(Utility.historydaylist.size()-1)!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-7)!=null){
                                        if(Utility.historydaylist.get(Utility.historydaylist.size()-1).getHasdepots()!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-1).getHasdepots().size()>0&&Utility.historydaylist.get(Utility.historydaylist.size()-7).getHasdepots()!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-7).getHasdepots().size()>0){
                                            for(int i =0;i<Utility.historydaylist.get(Utility.historydaylist.size()-1).getHasdepots().size();i++){
                                                for(int j =0;j<Utility.historydaylist.get(Utility.historydaylist.size()-7).getHasdepots().size();j++){
                                                    if(Utility.historydaylist.get(Utility.historydaylist.size()-1).getHasdepots().get(i).getDepotname().equals(str2)&&Utility.historydaylist.get(Utility.historydaylist.size()-7).getHasdepots().get(j).getDepotname().equals(str2)){
                                                        NumberFormat nf = NumberFormat.getNumberInstance();
                                                        nf.setMaximumFractionDigits(2);
                                                        String string = "";
                                                        Float wd1 = Float.parseFloat(Utility.historydaylist.get(Utility.historydaylist.size() - 1).getHasdepots().get(i).getMax());
                                                        Float wd2 = Float.parseFloat(Utility.historydaylist.get(Utility.historydaylist.size() - 7).getHasdepots().get(i).getMax());
                                                        String t2 = Utility.historydaylist.get(Utility.historydaylist.size() - 7).getHasdepots().get(j).getMeasuringtime();
                                                        String t1 = Utility.historydaylist.get(Utility.historydaylist.size() - 1).getHasdepots().get(j).getMeasuringtime();
                                                        if(wd1>wd2){
                                                            float count = Float.parseFloat(Utility.historydaylist.get(Utility.historydaylist.size() - 1).getHasdepots().get(i).getMax()) - Float.parseFloat(Utility.historydaylist.get(Utility.historydaylist.size() - 7).getHasdepots().get(j).getMax());
                                                            string =nf.format(count);
                                                            answer = str2+"号仓"+"在"+ t2+"至"+t1+ "的时间段内，测得最高温度上升了" + string
                                                                    + "度" + "最高温度分别为" + wd2 + "度" +"和"+ wd1 + "度。";
                                                            break;
                                                        }else if(wd1<wd2){
                                                            float count = Float.parseFloat(Utility.historydaylist.get(Utility.historydaylist.size() - 7).getHasdepots().get(i).getMax()) - Float.parseFloat(Utility.historydaylist.get(Utility.historydaylist.size() - 1).getHasdepots().get(j).getMax());
                                                            string =nf.format(count);
                                                            answer = str2+"号仓"+"在"+ t2+"至"+t1+ "的时间段内，测得最高温度下降了" + string
                                                                    + "度" + "最高温度分别为" + wd2 + "度" +"和"+ wd1 + "度。";
                                                            break;
                                                        }else if (wd1.equals(wd2)) {
                                                            answer = str2+"号仓"+"在"+ t2+"至"+t1+ "的时间段内，测得最高温度持平，"  + "并测得最高温度分别为" + wd2 + "度" +"和"+ wd1 + "度。";
                                                        }
                                                    }else if(Utility.historydaylist.get(Utility.historydaylist.size()-1).getBaddepots()!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-1).getBaddepots().size()>0||(Utility.historydaylist.get(Utility.historydaylist.size()-7).getBaddepots()!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-7).getBaddepots().size()>0)){
                                                        for(int m=0;m<Utility.historydaylist.get(Utility.historydaylist.size()-1).getBaddepots().size();m++) {
                                                            for(int n=0;n<Utility.historydaylist.get(Utility.historydaylist.size()-2).getBaddepots().size();n++)
                                                                if (Utility.historydaylist.get(Utility.historydaylist.size()-1).getBaddepots().get(m).equals(str2)||Utility.historydaylist.get(Utility.historydaylist.size()-2).getBaddepots().get(n).equals(str2)) {
                                                                    answer = "因温度采集系统开路，未返回"+str2+"号仓最高温度历史数据";
                                                                }
                                                        }
                                                    }else if(Utility.historydaylist.get(Utility.historydaylist.size()-1).getOffdepots()!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-1).getOffdepots().size()>0||(Utility.historydaylist.get(Utility.historydaylist.size()-7).getOffdepots()!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-7).getOffdepots().size()>0)){
                                                        for(int m=0;m<Utility.historydaylist.get(Utility.historydaylist.size()-1).getOffdepots().size();m++) {
                                                            for(int n=0;n<Utility.historydaylist.get(Utility.historydaylist.size()-7).getOffdepots().size();n++)
                                                                if (Utility.historydaylist.get(Utility.historydaylist.size()-1).getOffdepots().get(m).equals(str2)||Utility.historydaylist.get(Utility.historydaylist.size()-7).getOffdepots().get(n).equals(str2)) {
                                                                    answer = str2+"号仓近期温度测量系统关机";
                                                                }
                                                        }
                                                    }else if(Utility.historydaylist.get(Utility.historydaylist.size()-1).getHstdepots()!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-1).getHstdepots().size()>0||(Utility.historydaylist.get(Utility.historydaylist.size()-7).getHstdepots()!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-7).getHstdepots().size()>0)){
                                                        for(int m=0;m<Utility.historydaylist.get(Utility.historydaylist.size()-1).getHstdepots().size();m++) {
                                                            for(int n=0;n<Utility.historydaylist.get(Utility.historydaylist.size()-7).getHstdepots().size();n++)
                                                                if (Utility.historydaylist.get(Utility.historydaylist.size()-1).getHstdepots().get(m).equals(str2)||Utility.historydaylist.get(Utility.historydaylist.size()-7).getHstdepots().get(n).equals(str2)) {
                                                                    answer =str2+ "号仓一周历史温度信息未能及时返回";
                                                                }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }else {
                                    answer = "该段时间温度历史数据为空";
                                }
                            }else {
                                answer = "不存在"+str2+"号仓";
                            }
                        }
                    }else if(finalResult.contains("平均温度变化") ){
                        if(finalResult.contains("近两天")||finalResult.contains("这两天")||finalResult.contains("两天来")||finalResult.contains("两天")){
                            int nb = Integer.parseInt(str2);
                            if(nb>0&&nb<=10){
                                if(Utility.historydaylist !=null&&Utility.historydaylist.size()>0){
                                    if(Utility.historydaylist.get(Utility.historydaylist.size()-1)!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-1)!=null){
                                        if(Utility.historydaylist.get(Utility.historydaylist.size()-1).getHasdepots()!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-1).getHasdepots().size()>0&&Utility.historydaylist.get(Utility.historydaylist.size()-2).getHasdepots()!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-2).getHasdepots().size()>0){
                                            for(int i =0;i<Utility.historydaylist.get(Utility.historydaylist.size()-1).getHasdepots().size();i++){
                                                for(int j =0;j<Utility.historydaylist.get(Utility.historydaylist.size()-2).getHasdepots().size();j++){
                                                    if(Utility.historydaylist.get(Utility.historydaylist.size()-1).getHasdepots().get(i).getDepotname().equals(str2)&&Utility.historydaylist.get(Utility.historydaylist.size()-2).getHasdepots().get(j).getDepotname().equals(str2)){
                                                        NumberFormat nf = NumberFormat.getNumberInstance();
                                                        nf.setMaximumFractionDigits(2);
                                                        String string = "";
                                                        Float wd1 = Float.parseFloat(Utility.historydaylist.get(Utility.historydaylist.size() - 1).getHasdepots().get(i).getMean());
                                                        Float wd2 = Float.parseFloat(Utility.historydaylist.get(Utility.historydaylist.size() - 2).getHasdepots().get(i).getMean());
                                                        String t2 = Utility.historydaylist.get(Utility.historydaylist.size() - 2).getHasdepots().get(j).getMeasuringtime();
                                                        String t1 = Utility.historydaylist.get(Utility.historydaylist.size() - 1).getHasdepots().get(j).getMeasuringtime();
                                                        if(wd1>wd2){
                                                            float count = Float.parseFloat(Utility.historydaylist.get(Utility.historydaylist.size() - 1).getHasdepots().get(i).getMean()) - Float.parseFloat(Utility.historydaylist.get(Utility.historydaylist.size() - 2).getHasdepots().get(j).getMean());
                                                            string =nf.format(count);
                                                            answer = str2+"号仓"+"在"+ t2+"至"+t1+ "的时间段内，测得平均温度上升了" + string
                                                                    + "度，" + "平均温度分别为" + wd2 + "度" +"和"+ wd1 + "度。";
                                                            break;
                                                        }else if(wd1<wd2){
                                                            float count = Float.parseFloat(Utility.historydaylist.get(Utility.historydaylist.size() - 2).getHasdepots().get(i).getMean()) - Float.parseFloat(Utility.historydaylist.get(Utility.historydaylist.size() - 1).getHasdepots().get(j).getMean());
                                                            string =nf.format(count);
                                                            answer = str2+"号仓"+"在"+ t2+"至"+t1+ "的时间段内，测得平均温度下降了" + string
                                                                    + "度，" + "平均温度分别为" + wd2 + "度" +"和"+ wd1 + "度。";
                                                            break;
                                                        }else if (wd1.equals(wd2)) {
                                                            answer = str2+"号仓在"+ t2+"至"+t1+"时间段内，测得"+"平均温度持平，" + "并测得平均温度为" +wd2 + "度。";
                                                        }
                                                    }else if(Utility.historydaylist.get(Utility.historydaylist.size()-1).getBaddepots()!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-1).getBaddepots().size()>0||(Utility.historydaylist.get(Utility.historydaylist.size()-2).getBaddepots()!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-2).getBaddepots().size()>0)){
                                                        for(int m=0;m<Utility.historydaylist.get(Utility.historydaylist.size()-1).getBaddepots().size();m++) {
                                                            for(int n=0;n<Utility.historydaylist.get(Utility.historydaylist.size()-2).getBaddepots().size();n++)
                                                                if (Utility.historydaylist.get(Utility.historydaylist.size()-1).getBaddepots().get(m).equals(str2)||Utility.historydaylist.get(Utility.historydaylist.size()-2).getBaddepots().get(n).equals(str2)) {
                                                                    answer = "因温度采集系统开路，未返回"+str2+"号仓平均温度历史数据";
                                                                }
                                                        }
                                                    }else if(Utility.historydaylist.get(Utility.historydaylist.size()-1).getOffdepots()!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-1).getOffdepots().size()>0||(Utility.historydaylist.get(Utility.historydaylist.size()-2).getOffdepots()!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-2).getOffdepots().size()>0)){
                                                        for(int m=0;m<Utility.historydaylist.get(Utility.historydaylist.size()-1).getOffdepots().size();m++) {
                                                            for(int n=0;n<Utility.historydaylist.get(Utility.historydaylist.size()-2).getOffdepots().size();n++)
                                                                if (Utility.historydaylist.get(Utility.historydaylist.size()-1).getOffdepots().get(m).equals(str2)||Utility.historydaylist.get(Utility.historydaylist.size()-2).getOffdepots().get(n).equals(str2)) {
                                                                    answer = str2+"号仓近期温度测量系统关机。";
                                                                }
                                                        }
                                                    }else if(Utility.historydaylist.get(Utility.historydaylist.size()-1).getHstdepots()!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-1).getHstdepots().size()>0||(Utility.historydaylist.get(Utility.historydaylist.size()-2).getHstdepots()!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-2).getHstdepots().size()>0)){
                                                        for(int m=0;m<Utility.historydaylist.get(Utility.historydaylist.size()-1).getHstdepots().size();m++) {
                                                            for(int n=0;n<Utility.historydaylist.get(Utility.historydaylist.size()-2).getHstdepots().size();n++)
                                                                if (Utility.historydaylist.get(Utility.historydaylist.size()-1).getHstdepots().get(m).equals(str2)||Utility.historydaylist.get(Utility.historydaylist.size()-2).getHstdepots().get(n).equals(str2)) {
                                                                    answer =str2+ "号仓近两天历史温度信息未能及时返回。";
                                                                }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }else {
                                    answer = "该段时间温度历史数据为空。";
                                }
                            }else {
                                answer = "不存在"+str2+"号仓。";
                            }
                        }else if(finalResult.contains("一周来")||finalResult.contains("近一周")||finalResult.contains("一周")){
                            int nb = Integer.parseInt(str2);
                            if(nb>0&&nb<=10){
                                if(Utility.historydaylist !=null&&Utility.historydaylist.size()>0){
                                    if(Utility.historydaylist.get(Utility.historydaylist.size()-1)!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-7)!=null){
                                        if(Utility.historydaylist.get(Utility.historydaylist.size()-1).getHasdepots()!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-1).getHasdepots().size()>0&&Utility.historydaylist.get(Utility.historydaylist.size()-7).getHasdepots()!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-7).getHasdepots().size()>0){
                                            for(int i =0;i<Utility.historydaylist.get(Utility.historydaylist.size()-1).getHasdepots().size();i++){
                                                for(int j =0;j<Utility.historydaylist.get(Utility.historydaylist.size()-7).getHasdepots().size();j++){
                                                    if(Utility.historydaylist.get(Utility.historydaylist.size()-1).getHasdepots().get(i).getDepotname().equals(str2)&&Utility.historydaylist.get(Utility.historydaylist.size()-7).getHasdepots().get(j).getDepotname().equals(str2)){
                                                        NumberFormat nf = NumberFormat.getNumberInstance();
                                                        nf.setMaximumFractionDigits(2);
                                                        String string = "";
                                                        Float wd1 = Float.parseFloat(Utility.historydaylist.get(Utility.historydaylist.size() - 1).getHasdepots().get(i).getMean());
                                                        Float wd2 = Float.parseFloat(Utility.historydaylist.get(Utility.historydaylist.size() - 7).getHasdepots().get(i).getMean());
                                                        String t2 = Utility.historydaylist.get(Utility.historydaylist.size() - 7).getHasdepots().get(j).getMeasuringtime();
                                                        String t1 = Utility.historydaylist.get(Utility.historydaylist.size() - 1).getHasdepots().get(j).getMeasuringtime();
                                                        if(wd1>wd2){
                                                            float count = Float.parseFloat(Utility.historydaylist.get(Utility.historydaylist.size() - 1).getHasdepots().get(i).getMean()) - Float.parseFloat(Utility.historydaylist.get(Utility.historydaylist.size() - 7).getHasdepots().get(j).getMean());
                                                            string =nf.format(count);
                                                            answer = str2+"号仓"+"在"+ t2+"至"+t1+ "的时间段内，测得平均温度上升了" + string
                                                                    + "度，" + "平均温度分别为" + wd2 + "度" +"和"+ wd1 + "度。";
                                                            break;
                                                        }else if( wd1<wd2){
                                                            float count = Float.parseFloat(Utility.historydaylist.get(Utility.historydaylist.size() - 7).getHasdepots().get(i).getMean()) - Float.parseFloat(Utility.historydaylist.get(Utility.historydaylist.size() - 1).getHasdepots().get(j).getMean());
                                                            string =nf.format(count);
                                                            answer = str2+"号仓"+"在"+ t2+"至"+t1+ "的时间段内，测得平均温度下降了" + string
                                                                    + "度，" + "平均温度分别为" + wd2 + "度" +"和"+ wd1 + "度。";
                                                            break;
                                                        }else if (wd1.equals(wd2)) {
                                                            answer = str2+"号仓在"+ t2+"至"+t1+"的时间段内，测得"+"平均温度持平，" + "并测得平均温度为" + wd2 + "度。";
                                                        }
                                                    }else if(Utility.historydaylist.get(Utility.historydaylist.size()-1).getBaddepots()!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-1).getBaddepots().size()>0||(Utility.historydaylist.get(Utility.historydaylist.size()-7).getBaddepots()!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-7).getBaddepots().size()>0)){
                                                        for(int m=0;m<Utility.historydaylist.get(Utility.historydaylist.size()-1).getBaddepots().size();m++) {
                                                            for(int n=0;n<Utility.historydaylist.get(Utility.historydaylist.size()-7).getBaddepots().size();n++)
                                                                if (Utility.historydaylist.get(Utility.historydaylist.size()-1).getBaddepots().get(m).equals(str2)||Utility.historydaylist.get(Utility.historydaylist.size()-7).getBaddepots().get(n).equals(str2)) {
                                                                    answer = "因温度采集系统开路，未返回"+str2+"号仓平均温度历史数据。";
                                                                }
                                                        }
                                                    }else if(Utility.historydaylist.get(Utility.historydaylist.size()-1).getOffdepots()!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-1).getOffdepots().size()>0||(Utility.historydaylist.get(Utility.historydaylist.size()-7).getOffdepots()!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-7).getOffdepots().size()>0)){
                                                        for(int m=0;m<Utility.historydaylist.get(Utility.historydaylist.size()-1).getOffdepots().size();m++) {
                                                            for(int n=0;n<Utility.historydaylist.get(Utility.historydaylist.size()-7).getOffdepots().size();n++)
                                                                if (Utility.historydaylist.get(Utility.historydaylist.size()-1).getOffdepots().get(m).equals(str2)||Utility.historydaylist.get(Utility.historydaylist.size()-7).getOffdepots().get(n).equals(str2)) {
                                                                    answer = str2+"号仓近期温度测量系统关机。";
                                                                }
                                                        }
                                                    }else if(Utility.historydaylist.get(Utility.historydaylist.size()-1).getHstdepots()!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-1).getHstdepots().size()>0||(Utility.historydaylist.get(Utility.historydaylist.size()-7).getHstdepots()!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-7).getHstdepots().size()>0)){
                                                        for(int m=0;m<Utility.historydaylist.get(Utility.historydaylist.size()-1).getHstdepots().size();m++) {
                                                            for(int n=0;n<Utility.historydaylist.get(Utility.historydaylist.size()-7).getHstdepots().size();n++)
                                                                if (Utility.historydaylist.get(Utility.historydaylist.size()-1).getHstdepots().get(m).equals(str2)||Utility.historydaylist.get(Utility.historydaylist.size()-7).getHstdepots().get(n).equals(str2)) {
                                                                    answer =str2+ "号仓近两天历史温度信息未能及时返回。";
                                                                }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }else {
                                    answer = "该段时间温度历史数据为空。";
                                }
                            }else {
                                answer = "不存在"+str2+"号仓。";
                            }

                        }
                    }else if(finalResult.contains("最低温度变化") ){
                        if(finalResult.contains("近两天")||finalResult.contains("这两天")||finalResult.contains("两天来")||finalResult.contains("两天")) {
                            int nb = Integer.parseInt(str2);
                            if (nb>0&&nb<=10) {
                                if (Utility.historydaylist != null && Utility.historydaylist.size() > 0) {
                                    if (Utility.historydaylist.get(Utility.historydaylist.size() - 1) != null && Utility.historydaylist.get(Utility.historydaylist.size() - 2) != null) {
                                        if (Utility.historydaylist.get(Utility.historydaylist.size() - 1).getHasdepots() != null && Utility.historydaylist.get(Utility.historydaylist.size() - 1).getHasdepots().size() > 0 && Utility.historydaylist.get(Utility.historydaylist.size() - 2).getHasdepots() != null && Utility.historydaylist.get(Utility.historydaylist.size() - 2).getHasdepots().size() > 0) {
                                            for (int i = 0; i < Utility.historydaylist.get(Utility.historydaylist.size() - 1).getHasdepots().size(); i++) {
                                                for (int j = 0; j < Utility.historydaylist.get(Utility.historydaylist.size() - 2).getHasdepots().size(); j++) {
                                                    if (Utility.historydaylist.get(Utility.historydaylist.size() - 1).getHasdepots().get(i).getDepotname().equals(str2) && Utility.historydaylist.get(Utility.historydaylist.size() - 2).getHasdepots().get(j).getDepotname().equals(str2)) {
                                                        NumberFormat nf = NumberFormat.getNumberInstance();
                                                        nf.setMaximumFractionDigits(2);
                                                        String string = "";
                                                        Float wd1 = Float.parseFloat(Utility.historydaylist.get(Utility.historydaylist.size() - 1).getHasdepots().get(i).getMin());
                                                        Float wd2 = Float.parseFloat(Utility.historydaylist.get(Utility.historydaylist.size() - 2).getHasdepots().get(i).getMin());
                                                        String t2 = Utility.historydaylist.get(Utility.historydaylist.size() - 2).getHasdepots().get(j).getMeasuringtime();
                                                        String t1 = Utility.historydaylist.get(Utility.historydaylist.size() - 1).getHasdepots().get(j).getMeasuringtime();
                                                        if (wd1>wd2) {
                                                            float count = Float.parseFloat(Utility.historydaylist.get(Utility.historydaylist.size() - 1).getHasdepots().get(i).getMin()) - Float.parseFloat(Utility.historydaylist.get(Utility.historydaylist.size() - 2).getHasdepots().get(j).getMin());
                                                            string =nf.format(count);
                                                            answer = str2 + "号仓" + "在" + t2 + "至" + t1 + "的时间段内，测量的最低温度上升了" + string
                                                                    + "度，" + "最低温度分别为" +wd2 + "度" + "和" + wd1 + "度。";
                                                            break;
                                                        } else if (wd1<wd2) {
                                                            float count = Float.parseFloat(Utility.historydaylist.get(Utility.historydaylist.size() - 2).getHasdepots().get(i).getMin()) - Float.parseFloat(Utility.historydaylist.get(Utility.historydaylist.size() - 1).getHasdepots().get(j).getMin());
                                                            string =nf.format(count);
                                                            answer = str2 + "号仓" + "在" + t2 + "至" + t1 + "的时间段内，测量的最低温度下降了" + string
                                                                    + "度，" + "最低温度分别为" +wd2 + "度" + "和" + wd1 + "度。";
                                                            break;
                                                        } else if (wd1.equals(wd2)) {
                                                            answer = str2 + "号仓在" + t2 + "至" + t1 + "的时间段内，测得" + "最低温度持平，" + "并测得最低温度为" + wd2 + "度。";
                                                            break;
                                                        }
                                                    } else if (Utility.historydaylist.get(Utility.historydaylist.size() - 1).getBaddepots() != null && Utility.historydaylist.get(Utility.historydaylist.size() - 1).getBaddepots().size() > 0 ||(Utility.historydaylist.get(Utility.historydaylist.size() - 2).getBaddepots() != null && Utility.historydaylist.get(Utility.historydaylist.size() - 2).getBaddepots().size() > 0)){
                                                        for (int m = 0; m < Utility.historydaylist.get(Utility.historydaylist.size() - 1).getBaddepots().size(); m++) {
                                                            for (int n = 0; n < Utility.historydaylist.get(Utility.historydaylist.size() - 2).getBaddepots().size(); n++)
                                                                if (Utility.historydaylist.get(Utility.historydaylist.size() - 1).getBaddepots().get(m).equals(str2) || Utility.historydaylist.get(Utility.historydaylist.size() - 2).getBaddepots().get(n).equals(str2)) {
                                                                    answer = "因温度采集系统开路，未返回" + str2 + "号仓最低温度历史数据。";
                                                                }
                                                        }
                                                    } else if (Utility.historydaylist.get(Utility.historydaylist.size() - 1).getOffdepots() != null && Utility.historydaylist.get(Utility.historydaylist.size() - 1).getOffdepots().size() > 0 || (Utility.historydaylist.get(Utility.historydaylist.size() - 2).getOffdepots() != null && Utility.historydaylist.get(Utility.historydaylist.size() - 2).getOffdepots().size() > 0)) {
                                                        for (int m = 0; m < Utility.historydaylist.get(Utility.historydaylist.size() - 1).getOffdepots().size(); m++) {
                                                            for (int n = 0; n < Utility.historydaylist.get(Utility.historydaylist.size() - 2).getOffdepots().size(); n++)
                                                                if (Utility.historydaylist.get(Utility.historydaylist.size() - 1).getOffdepots().get(m).equals(str2) || Utility.historydaylist.get(Utility.historydaylist.size() - 2).getOffdepots().get(n).equals(str2)) {
                                                                    answer = str2 + "号仓近期温度测量系统关机。";
                                                                }
                                                        }
                                                    } else if (Utility.historydaylist.get(Utility.historydaylist.size() - 1).getHstdepots() != null && Utility.historydaylist.get(Utility.historydaylist.size() - 1).getHstdepots().size() > 0 || (Utility.historydaylist.get(Utility.historydaylist.size() - 2).getHstdepots() != null && Utility.historydaylist.get(Utility.historydaylist.size() - 2).getHstdepots().size() > 0)) {
                                                        for (int m = 0; m < Utility.historydaylist.get(Utility.historydaylist.size() - 1).getHstdepots().size(); m++) {
                                                            for (int n = 0; n < Utility.historydaylist.get(Utility.historydaylist.size() - 2).getHstdepots().size(); n++)
                                                                if (Utility.historydaylist.get(Utility.historydaylist.size() - 1).getHstdepots().get(m).equals(str2) || Utility.historydaylist.get(Utility.historydaylist.size() - 2).getHstdepots().get(n).equals(str2)) {
                                                                    answer = str2 + "号仓近两天历史温度信息未能及时返回。";
                                                                }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    answer = "该段时间温度历史数据为空。";
                                }
                            } else {
                                answer = "不存在" + str2 + "号仓";
                            }/****************************************************************************访问一周温度变化趋势***************************************************************************************************************************************************************************************/
                        }else if(finalResult.contains("一周来")||finalResult.contains("近一周")||finalResult.contains("一周")){
                            int nb = Integer.parseInt(str2);
                            if(nb>0&&nb<=10){
                                if(Utility.historydaylist !=null&&Utility.historydaylist.size()>0){
                                    if(Utility.historydaylist.get(Utility.historydaylist.size()-1)!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-7)!=null){
                                        if(Utility.historydaylist.get(Utility.historydaylist.size()-1).getHasdepots()!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-1).getHasdepots().size()>0&&Utility.historydaylist.get(Utility.historydaylist.size()-7).getHasdepots()!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-7).getHasdepots().size()>0){
                                            for(int i =0;i<Utility.historydaylist.get(Utility.historydaylist.size()-1).getHasdepots().size();i++){
                                                for(int j =0;j<Utility.historydaylist.get(Utility.historydaylist.size()-7).getHasdepots().size();j++){
                                                    if(Utility.historydaylist.get(Utility.historydaylist.size()-1).getHasdepots().get(i).getDepotname().equals(str2)&&Utility.historydaylist.get(Utility.historydaylist.size()-7).getHasdepots().get(j).getDepotname().equals(str2)){
                                                        NumberFormat nf = NumberFormat.getNumberInstance();
                                                        nf.setMaximumFractionDigits(2);
                                                        String string = "";
                                                        Float wd1 = Float.parseFloat(Utility.historydaylist.get(Utility.historydaylist.size() - 1).getHasdepots().get(i).getMin());
                                                        Float wd2 = Float.parseFloat(Utility.historydaylist.get(Utility.historydaylist.size() - 7).getHasdepots().get(i).getMin());
                                                        String t2 = Utility.historydaylist.get(Utility.historydaylist.size() - 7).getHasdepots().get(j).getMeasuringtime();
                                                        String t1 = Utility.historydaylist.get(Utility.historydaylist.size() - 1).getHasdepots().get(j).getMeasuringtime();
                                                        if(wd1>wd2){
                                                            float count = Float.parseFloat(Utility.historydaylist.get(Utility.historydaylist.size() - 1).getHasdepots().get(i).getMin()) - Float.parseFloat(Utility.historydaylist.get(Utility.historydaylist.size() - 7).getHasdepots().get(j).getMin());
                                                            string =nf.format(count);
                                                            answer = str2+"号仓"+"在"+ t2+"至"+t1+ "的时间段内，测量的最低温度上升了" + string
                                                                    + "度，" + "最低温度分别为" + wd2 + "度" +"和"+ wd1 + "度。";
                                                            break;
                                                        }else if(wd1<wd2){
                                                            float count = Float.parseFloat(Utility.historydaylist.get(Utility.historydaylist.size() - 7).getHasdepots().get(i).getMin()) - Float.parseFloat(Utility.historydaylist.get(Utility.historydaylist.size() - 1).getHasdepots().get(j).getMin());
                                                            string =nf.format(count);
                                                            answer = str2+"号仓"+"在"+ t2+"至"+t1+ "的时间段内，测量的最低温度下降了" + string
                                                                    + "度，" + "最低温度分别为" + wd2 + "度" +"和"+ wd1 + "度。";
                                                            break;
                                                        }else if (wd1.equals(wd2)) {
                                                            answer = str2+"号仓在"+ t2+"至"+t1+"的时间段内，"+"测得最低温度持平，" + "并测得最低温度为" + wd2 + "度。";
                                                        }
                                                    }else if(Utility.historydaylist.get(Utility.historydaylist.size()-1).getBaddepots()!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-1).getBaddepots().size()>0||(Utility.historydaylist.get(Utility.historydaylist.size()-7).getBaddepots()!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-7).getBaddepots().size()>0)){
                                                        for(int m=0;m<Utility.historydaylist.get(Utility.historydaylist.size()-1).getBaddepots().size();m++) {
                                                            for(int n=0;n<Utility.historydaylist.get(Utility.historydaylist.size()-7).getBaddepots().size();n++)
                                                                if (Utility.historydaylist.get(Utility.historydaylist.size()-1).getBaddepots().get(m).equals(str2)||Utility.historydaylist.get(Utility.historydaylist.size()-7).getBaddepots().get(n).equals(str2)) {
                                                                    answer = "因温度采集系统开路，未返回"+str2+"号仓最低温度历史数据。";
                                                                }
                                                        }
                                                    }else if(Utility.historydaylist.get(Utility.historydaylist.size()-1).getOffdepots()!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-1).getOffdepots().size()>0||(Utility.historydaylist.get(Utility.historydaylist.size()-7).getOffdepots()!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-7).getOffdepots().size()>0)){
                                                        for(int m=0;m<Utility.historydaylist.get(Utility.historydaylist.size()-1).getOffdepots().size();m++) {
                                                            for(int n=0;n<Utility.historydaylist.get(Utility.historydaylist.size()-7).getOffdepots().size();n++)
                                                                if (Utility.historydaylist.get(Utility.historydaylist.size()-1).getOffdepots().get(m).equals(str2)||Utility.historydaylist.get(Utility.historydaylist.size()-7).getOffdepots().get(n).equals(str2)) {
                                                                    answer = str2+"号仓近期温度测量系统关机。";
                                                                }
                                                        }
                                                    }else if(Utility.historydaylist.get(Utility.historydaylist.size()-1).getHstdepots()!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-1).getHstdepots().size()>0||(Utility.historydaylist.get(Utility.historydaylist.size()-7).getHstdepots()!=null&&Utility.historydaylist.get(Utility.historydaylist.size()-7).getHstdepots().size()>0)){
                                                        for(int m=0;m<Utility.historydaylist.get(Utility.historydaylist.size()-1).getHstdepots().size();m++) {
                                                            for(int n=0;n<Utility.historydaylist.get(Utility.historydaylist.size()-7).getHstdepots().size();n++)
                                                                if (Utility.historydaylist.get(Utility.historydaylist.size()-1).getHstdepots().get(m).equals(str2)||Utility.historydaylist.get(Utility.historydaylist.size()-7).getHstdepots().get(n).equals(str2)) {
                                                                    answer =str2+ "号仓近两天历史温度信息未能及时返回。";
                                                                }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }else {
                                    answer = "该段时间温度历史数据为空。";
                                }
                            }else {
                                answer = "不存在"+str2+"号仓";
                            }
                        }
                    }else if(finalResult.contains("昨天温度")||finalResult.contains("昨日温度")){
                        int nb = Integer.parseInt(str2);
                        if(nb>0&&nb<=10){
                            if(Utility.weekdatalist!=null&&Utility.weekdatalist.size()>0){
                                if(Utility.weekdatalist.get(Utility.weekdatalist.size()-1).getHasdepots()!=null&&Utility.weekdatalist.get(Utility.weekdatalist.size()-1).getHasdepots().size()>0){
                                    for(int i =0;i<Utility.weekdatalist.get(Utility.weekdatalist.size()-1).getHasdepots().size();i++){
                                        if(Utility.weekdatalist.get(Utility.weekdatalist.size()-1).getHasdepots().get(i).getDepotname().equals(str2)){
                                            answer ="昨日"+ Utility.weekdatalist.get(Utility.weekdatalist.size()-1).getHasdepots().get(i).getDepotname() + "号仓温度情况:最高温度为" + Utility.weekdatalist.get(Utility.weekdatalist.size()-1).getHasdepots().get(i).getMax() +"度，"+
                                                    "平均温度为" + Utility.weekdatalist.get(Utility.weekdatalist.size()-1).getHasdepots().get(i).getMean()+"度，" + "最低温度为" + Utility.weekdatalist.get(Utility.weekdatalist.size()-1).getHasdepots().get(i).getMin()+"度。";
                                            break;
                                        }else{
                                            answer = "昨日"+str2+"号仓温度未得到及时测量。";
                                        }
                                    }
                                }
                            }

                        }else {
                            answer = "不存在"+str2+"号仓。";
                        }
                    }else if(finalResult.contains("前天温度")||finalResult.contains("前日温度")){
                        int nb = Integer.parseInt(str2);
                        if(nb>0&&nb<=10){
                            if(Utility.weekdatalist!=null&&Utility.weekdatalist.size()>0){
                                if(Utility.weekdatalist.get(Utility.weekdatalist.size()-2).getHasdepots()!=null&&Utility.weekdatalist.get(Utility.weekdatalist.size()-2).getHasdepots().size()>0){
                                    for(int i =0;i<Utility.weekdatalist.get(Utility.weekdatalist.size()-2).getHasdepots().size();i++){
                                        if(Utility.weekdatalist.get(Utility.weekdatalist.size()-2).getHasdepots().get(i).getDepotname().equals(str2)){
                                            answer = "前天"+ Utility.weekdatalist.get(Utility.weekdatalist.size()-2).getHasdepots().get(i).getDepotname() + "号仓温度情况:最高温度为" + Utility.weekdatalist.get(Utility.weekdatalist.size()-2).getHasdepots().get(i).getMax() +"度，"+
                                                    "平均温度为" +Utility.weekdatalist.get(Utility.weekdatalist.size()-2).getHasdepots().get(i).getMean() +"度，" + "最低温度为" + Utility.weekdatalist.get(Utility.weekdatalist.size()-2).getHasdepots().get(i).getMin()+"度。";
                                            break;
                                        }else{
                                            answer = "前天"+str2+"号仓温度未得到及时测量。";
                                        }
                                    }
                                }
                            }

                        }else {
                            answer = "不存在"+str2+"号仓。";
                        }
                    }else if(finalResult.contains("三天前温度")||finalResult.contains("3天前温度")){
                        int nb = Integer.parseInt(str2);
                        if(nb>0&&nb<=10){
                            if(Utility.weekdatalist!=null&&Utility.weekdatalist.size()>0){
                                if(Utility.weekdatalist.get(Utility.weekdatalist.size()-3).getHasdepots()!=null&&Utility.weekdatalist.get(Utility.weekdatalist.size()-3).getHasdepots().size()>0){
                                    for(int i =0;i<Utility.weekdatalist.get(Utility.weekdatalist.size()-3).getHasdepots().size();i++){
                                        if(Utility.weekdatalist.get(Utility.weekdatalist.size()-3).getHasdepots().get(i).getDepotname().equals(str2)){
                                            answer ="三天前"+ Utility.weekdatalist.get(Utility.weekdatalist.size()-3).getHasdepots().get(i).getDepotname() + "号仓温度情况:最高温度为" + Utility.weekdatalist.get(Utility.weekdatalist.size()-3).getHasdepots().get(i).getMax() +"度，"+
                                                    "平均温度为" + Utility.weekdatalist.get(Utility.weekdatalist.size()-3).getHasdepots().get(i).getMean()+"度，" + "最低温度为" + Utility.weekdatalist.get(Utility.weekdatalist.size()-3).getHasdepots().get(i).getMin()+"度。";
                                            break;
                                        }else{
                                            answer = "三天前"+str2+"号仓温度未得到及时测量。";
                                        }
                                    }
                                }
                            }

                        }else {
                            answer = "不存在"+str2+"号仓。";
                        }
                    }else if(finalResult.contains("4天前温度")||finalResult.contains("四天前温度")){
                           int nb = Integer.parseInt(str2);
                           String str3= "";
                           str3 = str2.substring(0,1);
                           int nb1 = Integer.parseInt(str3);
                           if(nb1>0&&nb1<=10){
                               if(Utility.weekdatalist!=null&&Utility.weekdatalist.size()>0){
                                   if(Utility.weekdatalist.get(Utility.weekdatalist.size()-4).getHasdepots()!=null&&Utility.weekdatalist.get(Utility.weekdatalist.size()-4).getHasdepots().size()>0){
                                       for(int i =0;i<Utility.weekdatalist.get(Utility.weekdatalist.size()-4).getHasdepots().size();i++){
                                           if(Utility.weekdatalist.get(Utility.weekdatalist.size()-4).getHasdepots().get(i).getDepotname().equals(str3)){
                                               answer ="四天前"+ Utility.weekdatalist.get(Utility.weekdatalist.size()-4).getHasdepots().get(i).getDepotname() + "号仓温度情况:最高温度为" + Utility.weekdatalist.get(Utility.weekdatalist.size()-4).getHasdepots().get(i).getMax() +"度，"+
                                                       "平均温度为" + Utility.weekdatalist.get(Utility.weekdatalist.size()-4).getHasdepots().get(i).getMean()+"度，" + "最低温度为" + Utility.weekdatalist.get(Utility.weekdatalist.size()-4).getHasdepots().get(i).getMin()+"度。";
                                               break;
                                           }else{
                                               answer = "四天前"+str3+"号仓温度未得到及时测量。";
                                           }
                                       }
                                   }
                               }

                           }else {
                               answer = "不存在"+str3+"号仓。";
                           }
                       }else if(finalResult.contains("5天前温度")||finalResult.contains("五天前温度")){
                           String str3= "";
                           str3 = str2.substring(0,1);
                           int nb1 = Integer.parseInt(str3);
                           if(nb1>0&&nb1<=10){
                               if(Utility.weekdatalist!=null&&Utility.weekdatalist.size()>0){
                                   if(Utility.weekdatalist.get(Utility.weekdatalist.size()-5).getHasdepots()!=null&&Utility.weekdatalist.get(Utility.weekdatalist.size()-5).getHasdepots().size()>0){
                                       for(int i =0;i<Utility.weekdatalist.get(Utility.weekdatalist.size()-5).getHasdepots().size();i++){
                                           if(Utility.weekdatalist.get(Utility.weekdatalist.size()-5).getHasdepots().get(i).getDepotname().equals(str3)){
                                               answer ="五天前"+ Utility.weekdatalist.get(Utility.weekdatalist.size()-5).getHasdepots().get(i).getDepotname() + "号仓温度情况:最高温度为" + Utility.weekdatalist.get(Utility.weekdatalist.size()-5).getHasdepots().get(i).getMax() +"度，"+
                                                       "平均温度为" + Utility.weekdatalist.get(Utility.weekdatalist.size()-5).getHasdepots().get(i).getMean()+"度，" + "最低温度为" + Utility.weekdatalist.get(Utility.weekdatalist.size()-5).getHasdepots().get(i).getMin()+"度。";
                                               break;
                                           }else{
                                               answer = "五天前"+str3+"号仓温度未得到及时测量。";
                                           }
                                       }
                                   }
                               }

                           }else {
                               answer = "不存在"+str3+"号仓。";
                           }
                       }else if(finalResult.contains("6天前温度")||finalResult.contains("六天前温度")){
                           String str3= "";
                           str3 = str2.substring(0,1);
                           int nb1 = Integer.parseInt(str3);
                           if(nb1>0&&nb1<=10){
                               if(Utility.weekdatalist!=null&&Utility.weekdatalist.size()>0){
                                   if(Utility.weekdatalist.get(Utility.weekdatalist.size()-6).getHasdepots()!=null&&Utility.weekdatalist.get(Utility.weekdatalist.size()-6).getHasdepots().size()>0){
                                       for(int i =0;i<Utility.weekdatalist.get(Utility.weekdatalist.size()-6).getHasdepots().size();i++){
                                           if(Utility.weekdatalist.get(Utility.weekdatalist.size()-6).getHasdepots().get(i).getDepotname().equals(str3)){
                                               answer ="六天前"+ Utility.weekdatalist.get(Utility.weekdatalist.size()-6).getHasdepots().get(i).getDepotname() + "号仓温度情况:最高温度为" + Utility.weekdatalist.get(Utility.weekdatalist.size()-6).getHasdepots().get(i).getMax() +"度，"+
                                                       "平均温度为" + Utility.weekdatalist.get(Utility.weekdatalist.size()-6).getHasdepots().get(i).getMean()+"度，" + "最低温度为" + Utility.weekdatalist.get(Utility.weekdatalist.size()-6).getHasdepots().get(i).getMin()+"度。";
                                               break;
                                           }else{
                                               answer = "六天前"+str3+"号仓温度未得到及时测量。";
                                           }
                                       }
                                   }
                               }

                           }else {
                               answer = "不存在"+str3+"号仓。";
                           }
                       }else if(finalResult.contains("一周前温度")||finalResult.contains("前一周温度")||finalResult.contains("上一周温度")||finalResult.contains("一周前号仓温度")){
                        if(Integer.parseInt(str2)>0&&Integer.parseInt(str2)<=10){
                            if(Utility.weekdatalist!=null&&Utility.weekdatalist.size()>0){
                                if(Utility.weekdatalist.get(Utility.weekdatalist.size()-7).getHasdepots()!=null&&Utility.weekdatalist.get(Utility.weekdatalist.size()-7).getHasdepots().size()>0){
                                    for(int i =0;i<Utility.weekdatalist.get(Utility.weekdatalist.size()-7).getHasdepots().size();i++){
                                        if(Utility.weekdatalist.get(Utility.weekdatalist.size()-7).getHasdepots().get(i).getDepotname().equals(str2)){
                                            answer ="一周前"+ Utility.weekdatalist.get(Utility.weekdatalist.size()-7).getHasdepots().get(i).getDepotname() + "号仓温度情况:最高温度为" + Utility.weekdatalist.get(Utility.weekdatalist.size()-7).getHasdepots().get(i).getMax() +"度，"+
                                                    "平均温度为" + Utility.weekdatalist.get(Utility.weekdatalist.size()-7).getHasdepots().get(i).getMean()+"度，" + "最低温度为" + Utility.weekdatalist.get(Utility.weekdatalist.size()-7).getHasdepots().get(i).getMin()+"度。";
                                            break;
                                        }else{
                                            answer = "一周前"+str2+"号仓温度未得到及时测量。";
                                        }
                                    }
                                }
                            }

                        }else {
                            answer = "不存在"+str2+"号仓。";
                        }
                    }else if(finalResult.contains("历史温度")){
                        answer = "您可以了解粮仓昨日，前日，三天前乃至一周前的温度，通过仓库名加日期加温度来询问，例如询问“1号仓昨日温度”";
                    }else if(finalResult.contains("温度变化")){
                        answer = "你可以了解到粮仓近期温度变化，通过仓库名加时间段加温度来询问，例如“1号仓近两天平均温度变化”，时间段关键词有“两天”，“一周”,对应温度关键词有“最高温度”，“最低温度”，平均温度";
                    }else  {
                        answer = "没听清，请您再说一遍";
                    }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    /*}*/
                    //回答把回答的文字进行封装
                    TextBean answerBean = new TextBean(answer, false);
                    mListBean.add(answerBean);
                    myAdapter.notifyDataSetChanged();
                    iv_list.setSelection(mListBean.size()-1);
                    startSpeak(answer);
                }
            }

            @Override
            public void onError(SpeechError arg0) {

            }
    });
        // 4.显示dialog，接收语音输入
        mDialog.show();
    }

    // 语音合成
    public void startSpeak(String text) {
        // 1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        SpeechSynthesizer mTts = SpeechSynthesizer
                .createSynthesizer(this, null);

        // 2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
        // 设置发音人（更多在线发音人，用户可参见 附录12.2
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoqi"); // 设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");// 设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");// 设置音量，范围0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); // 设置云端
        // mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH,
        // "./sdcard/iflytek.pcm");
        // 3.开始合成
        mTts.startSpeaking(text, null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.leftimgview:
                if(drawerlayout.isDrawerOpen(navigationview)){
                    drawerlayout.closeDrawer(navigationview);//关闭抽屉
                }else {
                    drawerlayout.openDrawer(navigationview);//打开抽屉
                }
                break;
        }
    }
    private void initView() {
//监听侧滑菜单按钮点击
       navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
               switch (menuItem.getItemId()){
                   case R.id.item_1:
                       Intent intent = new Intent(MainActivity.this,HelpActivity.class);
                       startActivity(intent);
                       break;
                   case R.id.item_2:
                       Intent intent1 = new Intent(MainActivity.this,DetailActivity.class);
                       startActivity(intent1);
                       break;
               }
               //drawerlayout.closeDrawer(GravityCompat.START);
               return true;
           }
       });
    }


    class MyAdapter extends BaseAdapter {
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
                //Context context = null;
                convertView = LayoutInflater.from(context).inflate(R.layout.item_pager, null);
                mHolder = new viewHodler();
                mHolder.leftLayout = convertView.findViewById(R.id.msg_left);
                mHolder.rightLayout = convertView.findViewById(R.id.msg_right);
                mHolder.leftTextView = convertView.findViewById(R.id.left_tv);
                mHolder.rightTextView = convertView.findViewById(R.id.right_tv);
                convertView.setTag(mHolder);
            }else{
                mHolder = (viewHodler) convertView.getTag();
            }

            TextBean item = getItem(position);
            if(item.isAsk){
                mHolder.rightLayout.setVisibility(View.VISIBLE);
                mHolder.leftLayout.setVisibility(View.GONE);
                mHolder.rightTextView.setText(item.text);
            }else{
                mHolder.rightLayout.setVisibility(View.GONE);
                mHolder.leftLayout.setVisibility(View.VISIBLE);
                mHolder.leftTextView.setText(item.text);
            }

            return convertView;
        }

    }
    static class viewHodler{
        LinearLayout leftLayout;
        LinearLayout rightLayout;

        TextView leftTextView;
        TextView rightTextView;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dialog();
        }
        return false;
    }
    //退出登录
    private void dialog() {
        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setMessage("您确定要退出登录吗！")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finish();
                    }
                }).create();
        dialog.show();
    }
    private void initFindId() {
        navigationview=findViewById(R.id.nav);
        drawerlayout=findViewById(R.id.mdw);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);

    }
    String changeToOurWords(String input){
        String output=input;

        output = new PinyinSimilarity(true).changeOurWordsWithPinyin(output);

        return output;
    }
}









