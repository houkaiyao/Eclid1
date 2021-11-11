package com.example.eclid1.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.eclid1.Adapter.BannerAdapter;
import com.example.eclid1.Adapter.GlidAdapter;
import com.example.eclid1.Bean.Info;
import com.example.eclid1.Entity.GlidItem;
import com.example.eclid1.R;
import com.example.eclid1.Utlis.Utlis;
import com.example.eclid1.View.BusActivity;
import com.example.eclid1.View.EuclidActivity;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.ArrayList;

/**
 * Created by Hou on 2021/9/27 0028.
 */
public class Fragment1 extends Fragment implements View.OnClickListener {

    private TextView title_text;
    private ArrayList<String> list_path;
    private Banner banner;
    private View view;
    private GridView gridView;

    public Fragment1() {
    }

    private ArrayList<GlidItem> mData = null;
    private BaseAdapter mAdapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment1, container, false);
        viewbind();
        init();
        return view;
    }

    private void viewbind() {
        title_text = (TextView) view.findViewById(R.id.title_text);
        title_text.setText("1");
        banner = (Banner) view.findViewById(R.id.banner);
        SetBanner();
        gridView = (GridView) view.findViewById(R.id.gridView);
        SetGlid();

    }

    private void init() {

    }

    // 单机事件
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_content:
                startActivity(new Intent(getContext(), EuclidActivity.class));
                break;
        }
    }

    private void SetBanner() {
        //放图片地址的集合
        list_path = new ArrayList<>();
        list_path.add("https://img-blog.csdnimg.cn/2021090414551221.gif");
        list_path.add(Info.IP + "/image/banner/b.jpg");
        list_path.add(Info.IP + "/image/banner/a.jpg");
        BannerAdapter.BannerAdapter(banner, list_path);
        banner.start();
        banner.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void OnBannerClick(int position) {
                Utlis.toastShow(getActivity(), position + "");
            }
        });
    }

    private void SetGlid() {
        mData = new ArrayList<GlidItem>();
        mData.add(new GlidItem(R.mipmap.logo, "Lost"));
        mData.add(new GlidItem(R.mipmap.logo, "Found"));
        mData.add(new GlidItem(R.mipmap.logo, "图标3"));
        mData.add(new GlidItem(R.mipmap.logo, "图标4"));
        mData.add(new GlidItem(R.mipmap.logo, "图标5"));
        mData.add(new GlidItem(R.mipmap.logo, "图标6"));

        mAdapter = new GlidAdapter<GlidItem>(mData, R.layout.glid_item) {
            @Override
            public void bindView(ViewHolder holder, GlidItem glidItem) {
                holder.setImageResource(R.id.glid_image, glidItem.getiId());
                holder.setText(R.id.glid_text, glidItem.getiName());
            }
        };
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position){
                    case 0:
                        ToEuclid(position);
                        break;
                    case 1:
                        ToEuclid(position);
                        break;
                    case 2:
                        startActivity(new Intent(getContext(), BusActivity.class));
                        break;
                    case 3:
                        //startActivity(new Intent(getContext(), SelfFoundActivity.class));
                        break;
                }
            }
        });
    }
    public void ToEuclid(int posi){
        Info.Lost_Or_found=posi;
        startActivity(new Intent(getContext(),EuclidActivity.class));
    }
}
