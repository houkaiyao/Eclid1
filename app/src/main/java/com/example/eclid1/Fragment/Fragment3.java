package com.example.eclid1.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.eclid1.Adapter.ConstomDialog;
import com.example.eclid1.Entity.User;
import com.example.eclid1.R;
import com.example.eclid1.Utlis.CacheDataManager;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Hou on 2021/9/27 0028.
 */
public class Fragment3 extends Fragment implements View.OnClickListener{
    private ImageView wxp;
    private ImageView wxd;
    private TextView user_phone;
    private LinearLayout daohang;
    private LinearLayout order;
    private TextView Cache_num;
    private LinearLayout clear_Cache;
    private LinearLayout shezhi;
    private TextView about;
    SharedPreferences sharedPreferences;
    public Fragment3() {
    }

    private View view;
    private ImageView user_pic;
    private TextView user_name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment3, container, false);
        bindViews();
        init();

        return view;
    }

    private void bindViews() {
        user_pic = (ImageView) view.findViewById(R.id.user_pic);
        user_name = (TextView) view.findViewById(R.id.user_name);
        clear_Cache= (LinearLayout)view.findViewById(R.id.clear_Cache);
        clear_Cache.setOnClickListener(this);
        user_name.setText(User.username);
        Cache_num=(TextView)view.findViewById(R.id.Cache_num);
        try {
            String cacheAllSize = CacheDataManager.getTotalCacheSize(getContext());
            Cache_num.setText(cacheAllSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        user_phone =(TextView) view.findViewById(R.id.user_phone);
        user_phone.setText(User.phone);
    }

    private void init() {
        Glide.with(getContext())
                .load(R.drawable.a)
                .centerCrop()
                .into(user_pic);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clear_Cache:
                showDialog();
                break;
        }
    }
    /**
     * 弹出对话框
     */
    public void showDialog() {
        //实例化自定义对话框
        final ConstomDialog mdialog = new ConstomDialog(getContext());
        //对话框中退出按钮事件
        mdialog.setOnExitListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果对话框处于显示状态
                if (mdialog.isShowing()) {
                    //关闭对话框
                    mdialog.dismiss();
                    //关闭当前界面
                    CacheDataManager.clearAllCache(getContext());
                    saveLoginInfo();
                    getActivity().finish();
                    System.exit(0);
                }

            }
        });
        //对话框中取消按钮事件
        mdialog.setOnCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mdialog != null && mdialog.isShowing()) {
                    //关闭对话框
                    mdialog.dismiss();
                }
            }
        });
        mdialog.show();
    }
    //  保存账号密码
    private void saveLoginInfo() {
        sharedPreferences = getActivity().getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username","");
        editor.putString("password", "");
        editor.commit();
    }

}
