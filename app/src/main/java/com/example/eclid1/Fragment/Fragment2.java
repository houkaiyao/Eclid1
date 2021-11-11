package com.example.eclid1.Fragment;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.eclid1.R;
import com.example.eclid1.View.EuclidActivity;

/**
 * Created by Hou on 2021/9/27 0028.
 */
public class Fragment2 extends Fragment implements View.OnClickListener{
    public Fragment2() {
    }
    private View view;
    private TextView textView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment2,container,false);
         bindview();
         init();
         return view;
    }

    private void bindview() {
        textView = (TextView) view.findViewById(R.id.txt_content);
        textView.setOnClickListener(this);
        textView.setText("2");
    }
    private void init(){

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txt_content:
                startActivity(new Intent(getContext(), EuclidActivity.class));
        }

    }
}
