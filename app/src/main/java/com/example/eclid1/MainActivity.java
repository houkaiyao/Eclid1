package com.example.eclid1;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.eclid1.Adapter.FragmentAdapter;
import com.example.eclid1.Utlis.Utlis;


public class MainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    private MainActivity mainActivity;
    private RadioGroup rg_tab_bar;
    private RadioButton rb_one;
    private RadioButton rb_two;
    private RadioButton rb_three;
    private ViewPager vpager;
    private FragmentAdapter mAdapter;

    //几个代表Fragment页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utlis.initState(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAdapter = new FragmentAdapter(getSupportFragmentManager());
        bindViews();
        rb_one.setChecked(true);
    }

    private void bindViews() {
        rg_tab_bar = (RadioGroup) findViewById(R.id.rg_tab_bar);
        rb_one = (RadioButton) findViewById(R.id.rb_one);
        rb_two = (RadioButton) findViewById(R.id.rb_two);
        rb_three = (RadioButton) findViewById(R.id.rb_three);

        rg_tab_bar.setOnCheckedChangeListener(this);

        vpager = (ViewPager) findViewById(R.id.vpager);
        vpager.setAdapter(mAdapter);
        vpager.setCurrentItem(0);
        vpager.addOnPageChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_one:
                vpager.setCurrentItem(PAGE_ONE);
                break;
            case R.id.rb_two:
                vpager.setCurrentItem(PAGE_TWO);
                break;
            case R.id.rb_three:
                vpager.setCurrentItem(PAGE_THREE);
                break;
        }
    }


    //重写ViewPager页面切换的处理方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
        if (state == 2) {
            switch (vpager.getCurrentItem()) {
                case PAGE_ONE:
                    rb_one.setChecked(true);
                    break;
                case PAGE_TWO:
                    rb_two.setChecked(true);
                    break;
                case PAGE_THREE:
                    rb_three.setChecked(true);
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
        mainActivity = null;
    }

}
