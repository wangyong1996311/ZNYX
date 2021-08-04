package com.mingrisoft.yysb;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mingrisoft.yysb.util.OkHttpUtil;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener, ViewTreeObserver.OnGlobalLayoutListener, TextWatcher {

    private String TAG = "ifu25";

    private ImageButton mIbNavigationBack;
    private LinearLayout mLlLoginPull;
    private View mLlLoginLayer;
    private LinearLayout mLlLoginOptions;
    private EditText mEtLoginUsername;
    private EditText mEtLoginPwd;
    private LinearLayout mLlLoginUsername;
    private ImageView mIvLoginUsernameDel;
    private Button mBtLoginSubmit;
    private LinearLayout mLlLoginPwd;
    private ImageView mIvLoginPwdDel;
    //private ImageView mIvLoginLogo;
    //private LinearLayout mLayBackBar;
    private TextView mTvLoginForgetPwd;
    private Button mBtLoginRegister;
    private  String username;
    private String pwd;
    private CheckBox rememberPsw;
    OkHttpUtil okHttpUtil = new OkHttpUtil();
    private String Token;
    private SharedPreferences.Editor editor;
    //全局Toast
    private Toast mToast;
    private int mLogoHeight;
    private int mLogoWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        initView();
        initCheckBox();
    }

    //初始化视图
    private void initView() {
        //登录层、下拉层、其它登录方式层
        //mLlLoginLayer = findViewById(R.id.ll_login_layer);
        //mIvLoginLogo = findViewById(R.id.iv_login_logo);
        mLlLoginUsername = findViewById(R.id.ll_login_username);
        mEtLoginUsername = findViewById(R.id.et_login_username);
        mIvLoginUsernameDel = findViewById(R.id.iv_login_username_del);
        //passwd
        mLlLoginPwd = findViewById(R.id.ll_login_pwd);
        mEtLoginPwd = findViewById(R.id.et_login_pwd);
        mIvLoginPwdDel = findViewById(R.id.iv_login_pwd_del);

        //提交、注册
        mBtLoginSubmit = findViewById(R.id.bt_login_submit);
        //mBtLoginRegister = findViewById(R.id.bt_login_register);

        //记住密码
        rememberPsw = findViewById(R.id.cb_remember_login);

        //忘记密码
        //mTvLoginForgetPwd = findViewById(R.id.tv_login_forget_pwd);
        //mTvLoginForgetPwd.setOnClickListener(this);

        //注册点击事件
        //mLlLoginPull.setOnClickListener(this);
        //mIbNavigationBack.setOnClickListener(this);
        mEtLoginUsername.setOnClickListener(this);
        mIvLoginUsernameDel.setOnClickListener(this);
        mBtLoginSubmit.setOnClickListener(this);
        //mBtLoginRegister.setOnClickListener(this);
        mEtLoginPwd.setOnClickListener(this);
        mIvLoginPwdDel.setOnClickListener(this);
        /*findViewById(R.id.ib_login_weibo).setOnClickListener(this);
        findViewById(R.id.ib_login_qq).setOnClickListener(this);
        findViewById(R.id.ib_login_wx).setOnClickListener(this);*/

        //注册其它事件
        //mLayBackBar.getViewTreeObserver().addOnGlobalLayoutListener(this);
        mEtLoginUsername.setOnFocusChangeListener(this);
        mEtLoginUsername.addTextChangedListener(this);
        mEtLoginPwd.setOnFocusChangeListener(this);
        mEtLoginPwd.addTextChangedListener(this);
    }
    private void initCheckBox(){
        // 读取密码
        SharedPreferences pref = getSharedPreferences("checkBoxStates", Context.MODE_PRIVATE);
        boolean isRemember = pref.getBoolean("remember_box", false);
        if (isRemember) {
            String username = pref.getString("user_name", "");
            String password = pref.getString("pass_word", "");
            //boolean rememberbox = pref.getBoolean("remember_box",true);
            rememberPsw.setChecked(true);
            mEtLoginUsername.setText(username);
            mEtLoginPwd.setText(password);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.ib_navigation_back:
//                //返回
//                finish();
//                break;
            case R.id.et_login_username:
                mEtLoginPwd.clearFocus();
                mEtLoginUsername.setFocusableInTouchMode(true);
                mEtLoginUsername.requestFocus();
                break;
            case R.id.et_login_pwd:
                mEtLoginUsername.clearFocus();
                mEtLoginPwd.setFocusableInTouchMode(true);
                mEtLoginPwd.requestFocus();
                break;
            case R.id.iv_login_username_del:
                //清空用户名
                mEtLoginUsername.setText(null);
                break;
            case R.id.iv_login_pwd_del:
                //清空密码
                mEtLoginPwd.setText(null);
                break;
            case R.id.bt_login_submit:
                //登录
                loginRequest();
                break;
            default:
                break;
        }
    }

    //用户名密码焦点改变
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        int id = v.getId();

        if (id == R.id.et_login_username) {
            if (hasFocus) {
                mLlLoginUsername.setActivated(true);
                mLlLoginPwd.setActivated(false);
            }
        } else {
            if (hasFocus) {
                mLlLoginPwd.setActivated(true);
                mLlLoginUsername.setActivated(false);
            }
        }
    }


    /*private void glide(int height, float progress, int time) {
        mLlLoginLayer.animate()
                .alphaBy(1 * progress)
                .alpha(0)
                .setDuration(time)
                .setListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        if (animation instanceof ValueAnimator) {
                            mLlLoginLayer.setTag(((ValueAnimator) animation).getAnimatedValue());
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (animation instanceof ValueAnimator) {
                            mLlLoginLayer.setTag(((ValueAnimator) animation).getAnimatedValue());
                        }
                        mLlLoginLayer.setVisibility(View.GONE);
                    }
                })
                .start();
    }
*/

  /*  private void upGlide(int height, float progress, int time) {
        mLlLoginPull.animate()
                .translationYBy(height * progress)
                .translationY(0)
                .setDuration(time)
                .start();
        mLlLoginLayer.animate()
                .alphaBy(1 - progress)
                .alpha(1)
                .setDuration(time)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        mLlLoginLayer.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        if (animation instanceof ValueAnimator) {
                            mLlLoginLayer.setTag(((ValueAnimator) animation).getAnimatedValue());
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (animation instanceof ValueAnimator) {
                            mLlLoginLayer.setTag(((ValueAnimator) animation).getAnimatedValue());
                        }
                    }
                })
                .start();
    }*/
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    //用户名密码输入事件
    @Override
    public void afterTextChanged(Editable s) {
        username = mEtLoginUsername.getText().toString().trim();//删除字符串两端空白字符
        pwd = mEtLoginPwd.getText().toString().trim();

        //是否显示清除按钮
        if (username.length() > 0) {
            mIvLoginUsernameDel.setVisibility(View.VISIBLE);
        } else {
            mIvLoginUsernameDel.setVisibility(View.INVISIBLE);
        }
        if (pwd.length() > 0) {
            mIvLoginPwdDel.setVisibility(View.VISIBLE);
        } else {
            mIvLoginPwdDel.setVisibility(View.INVISIBLE);
        }

        //登录按钮是否可用
        if (!TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(username)) {
            mBtLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit);
            mBtLoginSubmit.setTextColor(getResources().getColor(R.color.white));
        } else {
            mBtLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
            mBtLoginSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));
        }
    }

    //登录
    private void loginRequest() {
        final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        //************向服务器请求登陆***************
        LoginData loginData = new LoginData();
        loginData.setUsername(username);
        loginData.setPassword(pwd);
        Gson gson = new Gson();
        String jsonData = gson.toJson(loginData);
        okHttpUtil.LoginPost1("http://36.7.135.171:18080/yx/bind", jsonData, new OkHttpUtil.ReqCallBack() {
            @Override
            public void onReqSuccess(Object result) {
                SharedPreferences pref = getSharedPreferences("checkBoxStates",Context.MODE_PRIVATE);
                editor = pref.edit();
                if (rememberPsw.isChecked()) {
                    editor.putBoolean("remember_box", true);
                    editor.putString("user_name",username);
                    editor.putString("pass_word",pwd);

                } else {
                    editor.putBoolean("remember_box", false);
                    //editor.clear();
                }
                editor.apply();
                //跳转
                //intent.putExtra("Token",Token);
                startActivity(intent);
            }


            @Override
            public void onReqFailed(String errorMsg) {
                showToast(errorMsg);
            }
        });
        //****************登陆结束*********************
    }
    /*//微博登录
    private void weiboLogin() {
    }
    //QQ登录
    private void qqLogin() {
    }
    //微信登录
    private void weixinLogin() {
    }*/
    /**
     * 显示Toast
     *
     * @param msg 提示信息内容
     */
    private void showToast(String msg) {
        if (null != mToast) {
            mToast.setText(msg);
        } else {
            mToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    @Override
    public void onGlobalLayout() {

    }
}
