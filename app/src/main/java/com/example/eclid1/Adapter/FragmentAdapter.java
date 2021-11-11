package com.example.eclid1.Adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.eclid1.Fragment.Fragment1;
import com.example.eclid1.Fragment.Fragment2;
import com.example.eclid1.Fragment.Fragment3;
import com.example.eclid1.MainActivity;
import com.example.eclid1.R;

/**
 * Created by Jay on 2015/8/31 0031.
 */
public class FragmentAdapter extends FragmentPagerAdapter {
    private final int PAGER_COUNT = 3;
    private Fragment1 fragment1 = null;
    private Fragment2 fragment2 = null;
    private Fragment3 fragment3 = null;

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();

    }


    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

    @Override
    public Object instantiateItem(ViewGroup vg, int position) {
        return super.instantiateItem(vg, position);

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        System.out.println("position Destory" + position);
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case MainActivity.PAGE_ONE:
                fragment = fragment1;

                break;
            case MainActivity.PAGE_TWO:
                fragment = fragment2;

                break;
            case MainActivity.PAGE_THREE:
                fragment = fragment3;
                break;
        }
        return fragment;
    }


}

