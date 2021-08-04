package com.mingrisoft.yysb.util;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.mingrisoft.yysb.bean.Historybean;
import com.mingrisoft.yysb.bean.Lastbean;
import com.mingrisoft.yysb.bean.LoginBean;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



public class OkHttpUtil {
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");//mdiatype 这个需要和服务端保持一致
    // static final String BASE_URL = "http://117.78.27.178:18080";//请求接口根地址
    public static final int TYPE_GET = 0;//get请求
    public static final int TYPE_POST_JSON = 1;//post请求参数为json
    private OkHttpClient mOkHttpClient;//okHttpClient 实例
    private Handler okHttpHandler;//全局处理子线程和M主线程通信
    private static final String TAG = OkHttpUtil.class.getSimpleName();
    private static volatile OkHttpUtil mInstance;//单利引用

    /**
     * 初始化RequestManager
     */
    public OkHttpUtil() {
        //初始化OkHttpClient
        mOkHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(10, TimeUnit.SECONDS)//设置写入超时时间
                .build();
        //初始化Handler
        okHttpHandler = new Handler(Looper.getMainLooper());//表示放到主UI线程去处理
    }

    /**
     * 获取单例引用
     *
     * @return
     */
    //双重检查模式(DCL)优点：资源利用率高，缺点：第一次加载慢
    public static OkHttpUtil getInstance() {
        OkHttpUtil inst = mInstance;
        //第一次为了不必要的同步
        if (inst == null) {
            synchronized (OkHttpUtil.class) {
                inst = mInstance;
                //第二次是在OkHttpUtil等于null的时候创建实例
                if (inst == null) {
                    inst = new OkHttpUtil();
                    mInstance = inst;
                }
            }
        }
        return inst;
    }
    public void HistoryPost1(String Url, String Data1,final ReqCallBack callBack) {
        getInstance().HistoryPost(Url, Data1,callBack);
    }

    private void HistoryPost(String Url, String Data1,final ReqCallBack callBack) {
        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, Data1);
        //String requestUrl = String.format("%s/%s", BASE_URL, Url);
        final Request request = new Request.Builder()
                .url(Url).post(body).build();
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                failedCallBack("连接失败，请检查网络", callBack);
                Log.e(TAG, e.toString());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String string = response.body().string();
                    if (response.code() == 200) {
                        Log.e(TAG, "response ----->" + string);
                        Gson gson = new Gson();
                        Historybean javaBean = gson.fromJson(string, Historybean.class);
                        if (javaBean.getCode() == 200) {
                            successCallBack(string, callBack);
                        } else {
                            failedCallBack("未找到记录", callBack);
                        }

                    } else {
                        Log.e(TAG, "服务器错误");
                        failedCallBack("服务器错误", callBack);
                    }
                } catch (IOException e) {
                    failedCallBack(e.toString(), callBack);
                }
            }
        });
    }

    public void Get1(String Url, final ReqCallBack callBack) {
        getInstance().Get(Url,callBack);
    }

    private void Get(String Url, final ReqCallBack callBack) {
        //String requestUrl = String.format("%s/%s", BASE_URL, Url);
        final Request request = addHeaders().url(Url).build();
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                failedCallBack("访问失败", callBack);
                Log.e(TAG, e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String string = response.body().string();
                    if (response.code() == 200) {
                        Log.e(TAG, "response ----->" + string);
                        Gson gson = new Gson();
                        Lastbean javaBean  = gson.fromJson(string, Lastbean.class);
                        if (javaBean.getCode() == 200) {
                            successCallBack(string, callBack);
                        } else {
                            failedCallBack("未找到记录", callBack);
                        }

                    } else {
                        Log.e(TAG, "服务器错误");
                        failedCallBack("服务器错误", callBack);
                    }
                } catch (IOException e) {
                    failedCallBack(e.toString(), callBack);
                }
            }
        });
    }
    public void LoginPost1(String Url, String Data1, final ReqCallBack callBack) {
        getInstance().LoginPost(Url, Data1, callBack);
    }
    private void LoginPost(String Url, String Data1, final ReqCallBack callBack) {
        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, Data1);
        //String requestUrl = String.format("%s/%s", BASE_URL, Url);
        final Request request = new Request.Builder()
                .url(Url).post(body).build();
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                failedCallBack("连接失败，请检查网络", callBack);
                Log.e(TAG, e.toString());
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String string = response.body().string();
                    if (response.code() == 200) {
                        Log.e(TAG, "response ----->" + string);
                        Gson gson = new Gson();
                        LoginBean javaBean = gson.fromJson(string, LoginBean.class);
                        if (javaBean.getMsg().equals("绑定成功") || javaBean.getErr1() == "0") {
                            successCallBack(string, callBack);
                        } else {
                            failedCallBack("绑定失败：用户名或密码错误", callBack);
                        }

                    } else {
                        Log.e(TAG, "服务器错误");
                        failedCallBack("服务器错误", callBack);
                    }
                } catch (IOException e) {
                    failedCallBack(e.toString(), callBack);
                }
            }
        });
    }

    private Request.Builder addHeaders() {
        Request.Builder builder = new Request.Builder();
        return builder;
    }

    /**
     * 统一同意处理成功信息
     *
     * @param result
     * @param callBack
     * @param <T>
     */
    private <T> void successCallBack(final T result, final ReqCallBack<T> callBack) {
        okHttpHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null){
                    callBack.onReqSuccess(result);
                }
            }
        });
    }

    private <T> void failedCallBack(final String errorMsg, final ReqCallBack<T> callBack) {
        okHttpHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null){
                    callBack.onReqFailed(errorMsg);
                }
            }
        });
    }

    public interface ReqCallBack<T> {
        /**
         * 响应成功
         */
        void onReqSuccess(T result);

        /**
         * 响应失败
         */
        void onReqFailed(String errorMsg);
    }
}

