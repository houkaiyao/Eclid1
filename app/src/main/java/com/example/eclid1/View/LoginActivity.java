package com.example.eclid1.View;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.eclid1.Adapter.JellyInterpolator;
import com.example.eclid1.Bean.Info;
import com.example.eclid1.Bean.LoginBean;
import com.example.eclid1.Bean.UserBean;
import com.example.eclid1.Entity.User;
import com.example.eclid1.MainActivity;
import com.example.eclid1.R;
import com.example.eclid1.Utlis.Utlis;
import com.google.gson.Gson;

import android.animation.Animator.AnimatorListener;

import android.animation.ValueAnimator.AnimatorUpdateListener;

import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Toast;

import org.json.JSONObject;


public class LoginActivity extends Activity implements View.OnClickListener {

    private LoginActivity loginActivity;
    private LinearLayout input_layout_name;
    private LinearLayout input_layout_psw;
    private ProgressBar progressBar2;
    private TextView main_btn_login, user_phone, user_psw, signup;
    private View mInputLayout;
    private float mWidth, mHeight;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utlis.initState(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        getLoginInfo();
    }

    private void initView() {
        input_layout_name = (LinearLayout) findViewById(R.id.input_layout_name);
        input_layout_psw = (LinearLayout) findViewById(R.id.input_layout_psw);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        main_btn_login = (TextView) findViewById(R.id.main_btn_login);
        main_btn_login.setOnClickListener(this);
        mInputLayout = findViewById(R.id.input_layout);
        user_phone = findViewById(R.id.user_phone);
        user_psw = findViewById(R.id.user_psw);
        signup = (TextView) findViewById(R.id.signup);
        signup.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_btn_login:
                submit();
                break;
            case R.id.signup:
                startActivity(new Intent(LoginActivity.this, RegistActivity.class));
        }
    }

    public void login() {
        mWidth = main_btn_login.getMeasuredWidth();
        mHeight = main_btn_login.getMeasuredHeight();
        // 隐藏输入框
        input_layout_name.setVisibility(View.INVISIBLE);
        input_layout_psw.setVisibility(View.INVISIBLE);

        inputAnimator(mInputLayout, mWidth, mHeight);
        Utlis.showOrHide(this);
        VolleyHelper();
    }

    private void VolleyHelper() {
        String phone = user_phone.getText().toString();
        String psw = user_psw.getText().toString();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                //参数一：网络请求方式
                Request.Method.POST,
                //参数二：网络请求地址URL
                Info.LoginUrl + "?phone=" + phone + "&psw=" + psw,
                "",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Gson gson = new Gson();
                        //Toast.makeText(LoginActivity.this,jsonObject.toString(),Toast.LENGTH_SHORT).show();
                        LoginBean loginBean = gson.fromJson(jsonObject.toString(), LoginBean.class);
                        if (loginBean.getCode() == 0) {
                            Utlis.toastShow(LoginActivity.this, "该账号未注册");
                        } else if (loginBean.getCode() == 100) {
                            Utlis.toastShow(LoginActivity.this, "密码错误");
                        } else if (loginBean.getCode() == 200) {
                            getUserHelper(phone);
                            saveLoginInfo();
                        }
                    }
                },
                //参数五：网络请求失败的操作
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplication(), "网络异常", Toast.LENGTH_SHORT).show();

                    }
                }
        );
        //发送网络请求
        Volley.newRequestQueue(getApplication()).add(jsonObjectRequest);

    }

    private void getUserHelper(String phone) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                //参数一：网络请求方式
                Request.Method.POST,
                //参数二：网络请求地址URL
                Info.GetUserInfo + "?phone=" + phone,
                "",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Gson gson = new Gson();
                        UserBean userBean = gson.fromJson(jsonObject.toString(), UserBean.class);
                        if (userBean.getCode() == 200) {
                            User.phone = userBean.getPhone();
                            User.username = userBean.getUsername();
                            User.datejoin = userBean.getDatejoin();
                            User.pic_Profile = userBean.getPic_Profile();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(getApplication(), "服务器繁忙", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                //参数五：网络请求失败的操作
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplication(), "网络异常", Toast.LENGTH_SHORT).show();

                    }
                }
        );
        //发送网络请求
        Volley.newRequestQueue(getApplication()).add(jsonObjectRequest);
    }

    private void inputAnimator(final View view, float w, float h) {

        AnimatorSet set = new AnimatorSet();

        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (MarginLayoutParams) view
                        .getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout,
                "scaleX", 1.f, 0.f);
        set.setDuration(600);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animator2);
        set.start();
        set.addListener(new AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                progressBar2.setVisibility(View.VISIBLE);
                progressAnimator(progressBar2);
                mInputLayout.setVisibility(View.INVISIBLE);
                main_btn_login.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void progressAnimator(final View view) {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view,
                animator, animator2);
        animator3.setDuration(1000);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();

    }

    private void submit() {
        // validate
        String name = user_phone.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "请输入物品名称", Toast.LENGTH_SHORT).show();
            return;
        }

        String site = user_psw.getText().toString().trim();
        if (TextUtils.isEmpty(site)) {
            Toast.makeText(this, "请输入丢失地点", Toast.LENGTH_SHORT).show();
            return;
        }
        login();

        // TODO validate success, do something

    }

    //  保存账号密码
    private void saveLoginInfo() {
        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", user_phone.getText().toString());
        editor.putString("password", user_psw.getText().toString());
        editor.commit();
    }

    //  查询账号密码
    private void getLoginInfo() {
        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        String usernames = sharedPreferences.getString("username", "");
        if (usernames != "") {
            user_phone.setText(usernames);
            String password = sharedPreferences.getString("password", "");
            user_psw.setText(password);
            login();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginActivity.finish();
    }
}