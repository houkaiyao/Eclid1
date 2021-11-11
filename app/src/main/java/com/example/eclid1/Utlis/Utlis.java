package com.example.eclid1.Utlis;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.eclid1.Bean.Info;
import com.example.eclid1.Entity.NowBusLines;

import java.util.List;

public class Utlis {

    //截取30字节字符串
    public static String Remark_SubString(String string) {
        if (string.length() > 30)
            return string.substring(0, 30) + "....";
        else
            return string;
    }

    //获取最后一行字符串
    public static String lastString(String s) {
        int pos = s.lastIndexOf("\n");
        String result = s.substring(pos + 1);
        return result;
    }

    public static void initState(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

    }

    // Toast
    public static void toastShow(final Activity activity, String message) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 拨打电话
    public static void tellPhone(Activity activity, String number) {
        //Toast.makeText(activity,number,Toast.LENGTH_SHORT).show();
        Uri uri = Uri.parse("tel:" + number);
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(uri);
        activity.startActivity(intent);
    }

    public static void ClearMemory(Activity activity) {
        activity = null;
        activity.finish();
    }

    //获取Euclid页面 Lost / Found   的URL接口（0:LOST  1:FONUD）
    public static String getItemUrl(int posi) {
        String url = null;
        if (posi == 0) {
            url = Info.LostURI;
        } else if (posi == 1) {
            url = Info.FoundURI;
        }
        return url;
    }

    // 上传页面的 URL 接口判断
    public static String getUploadUrl(int posi) {
        String url = null;
        if (posi == 0) {
            url = Info.LostUploadUrl;
        } else if (posi == 1) {
            url = Info.FoundUploadUrl;
        }
        return url;
    }

    // 隐藏键盘
    public static void showOrHide(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void goGaodeMap(Activity activity) {
        try {
            Uri uri = Uri.parse("amapuri://route/plan/?dlat=" + NowBusLines.SiteLat + "&dlon=" + NowBusLines.SiteLng + "&dname=" + NowBusLines.BusSite + "&dev=0&t=0");
            activity.startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } catch (Exception e) {
            Toast.makeText(activity, "您尚未安装高德地图", Toast.LENGTH_SHORT).show();
        }
    }


}
