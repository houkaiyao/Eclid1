package com.example.eclid1.View;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.functions.Consumer;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eclid1.Bean.Info;
import com.example.eclid1.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.eclid1.Utlis.UploadUtlis;
import com.example.eclid1.Utlis.Utlis;
import com.google.gson.JsonObject;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONException;
import org.json.JSONObject;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class RegistActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText input_user;
    private LinearLayout input_layout_user;
    private EditText user_phone;
    private LinearLayout input_layout_name;
    private Button yzm_btn,regist_btn;
    private EditText input_yzm;
    private EditText user_psw;
    private RxPermissions mRxPermissions;
    private String yzm_num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utlis.initState(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        initView();
    }

    private void initView() {
        input_user = (EditText) findViewById(R.id.input_user);
        input_layout_user = (LinearLayout) findViewById(R.id.input_layout_user);
        user_phone = (EditText) findViewById(R.id.user_phone);
        input_layout_name = (LinearLayout) findViewById(R.id.input_layout_name);
        yzm_btn = (Button) findViewById(R.id.yzm_btn);
        input_yzm = (EditText) findViewById(R.id.input_yzm);
        user_psw = (EditText) findViewById(R.id.user_psw);
        regist_btn=(Button)findViewById(R.id.regist_btn);
        regist_btn.setOnClickListener(this);
        yzm_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yzm_btn:
                if (user_phone.getText().length()==0){
                    Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    try {
                        requestPermissions();
                        int numcode = (int) ((Math.random() * 9 + 1) * 100000);
                        yzm_num = Integer.toString(numcode);
                        PendingIntent pi = PendingIntent.getBroadcast(RegistActivity.this, 0, new Intent(), 0);
                        SmsManager smsManagfer = SmsManager.getDefault();
                        List<String> smsList = smsManagfer.divideMessage("[国务院]您的验证码是：" + yzm_num);
                        for (String message : smsList) {
                            smsManagfer.sendTextMessage(user_phone.getText().toString(), null, message, pi, null);
                        }
                    }catch (Exception e){
                        requestPermissions();
                    }

                }
                break;
            case R.id.regist_btn:
                if (submit()) {
                    if (!input_yzm.getText().toString().equals(yzm_num)) {
                        Toast.makeText(RegistActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                    } else {
                        VolleryHelp();

                    }
                }
                break;
        }
    }

    private Boolean submit() {
        // validate
        String user = input_user.getText().toString().trim();
        if (TextUtils.isEmpty(user)) {
            Toast.makeText(this, "姓名", Toast.LENGTH_SHORT).show();
            return false;
        }

        String phone = user_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "手机号", Toast.LENGTH_SHORT).show();
            return false;
        }

        String yzm = input_yzm.getText().toString().trim();
        if (TextUtils.isEmpty(yzm)) {
            Toast.makeText(this, "验证码", Toast.LENGTH_SHORT).show();
            return false;
        }

        String psw = user_psw.getText().toString().trim();
        if (TextUtils.isEmpty(psw)) {
            Toast.makeText(this, "密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
        // TODO validate success, do something

    }
    private void VolleryHelp(){
        StringRequest stringRequest=new StringRequest(
                Request.Method.POST,
                Info.RegistUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String  msg = null;
                        Toast.makeText(getApplication(),"注册成功", Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            msg= (String) jsonObject.get("msg");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (msg.equals("OK")){
                            startActivity(new Intent(RegistActivity.this,LoginActivity.class));
                    }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), volleyError.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("phone",user_phone.getText().toString());
                hashMap.put("username",input_user.getText().toString());
                hashMap.put("password",user_psw.getText().toString());
                hashMap.put("datejoined",UploadUtlis.getDate());
                return hashMap;
            }};
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 1, 2));
        Volley.newRequestQueue(getApplication()).add(stringRequest);

    }
    private void requestPermissions() {
        mRxPermissions = new RxPermissions(RegistActivity.this);
        mRxPermissions.requestEach(Manifest.permission.SEND_SMS,
                Manifest.permission.READ_SMS)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            Log.d(TAG, permission.name + " is granted.");
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，弹出提示
                            showPermissions();
                        } else {
                            // 用户拒绝了该权限，弹出提示
                            showPermissions();
                        }
                    }
                });
    }
    public void showPermissions() {
        if (!mRxPermissions.isGranted(Manifest.permission.SEND_SMS)) {
            showPermissionsDialog("此应用需要获取发送短信,是否打开应用设置手动授予");
        } else if (!mRxPermissions.isGranted(Manifest.permission.READ_SMS)) {
            showPermissionsDialog("此应用需要获取收取短信,是否打开应用设置手动授予");
        }
    }
    public void showPermissionsDialog(String msg) {
        new AlertDialog.Builder(RegistActivity.this)
                .setTitle("请注意")
                .setMessage(msg)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.fromParts("package", getPackageName(), null));
                        startActivity(intent);
                    }
                }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        }).show();
    }
}