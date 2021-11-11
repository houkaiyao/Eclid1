package com.example.eclid1.Adapter;

import com.example.eclid1.Entity.GlidItem;
import com.example.eclid1.R;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;

public class BannerAdapter {
    public static Banner BannerAdapter(Banner banner, ArrayList<String> arrayList) {

        banner.setImageLoader(new GlideImageLoader());
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
        banner.setBannerAnimation(Transformer.DepthPage);
        banner.setImages(arrayList);
        banner.setDelayTime(2500);

        return banner;
    }

}
