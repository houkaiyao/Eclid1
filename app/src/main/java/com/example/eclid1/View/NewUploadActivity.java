package com.example.eclid1.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.eclid1.Bean.Info;
import com.example.eclid1.Entity.User;
import com.example.eclid1.R;
import com.example.eclid1.Utlis.UploadUtlis;
import com.example.eclid1.Utlis.Utlis;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewUploadActivity extends AppCompatActivity implements View.OnClickListener {

    private String Upload_url;
    private TextView title_text;
    private CheckBox checkbox;
    private Button Choose_Btn;
    private Button upload_btn;
    private ImageView back_icon;
    private RelativeLayout title_rela;
    private ImageView image;
    private TextView textView1;
    private View view1;
    private EditText item_name;
    private TextView textView2;
    private View view2;
    private EditText item_site;
    private EditText add_more;
    private ScrollView scrollView;
    int PICK_IMAGE_REQUEST = 111;
    private String base64;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utlis.initState(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_upload);
        initView();
        init();
    }

    private void init() {
        Upload_url = Utlis.getUploadUrl(Info.Lost_Or_found);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Choose_Btn:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);

                break;
            case R.id.image:
                Toast.makeText(this, Upload_url, Toast.LENGTH_SHORT).show();
                break;
            case R.id.upload_btn:
                submit();
                break;
        }
    }

    private void VollerHepler() {
        StringRequest stringRequest=new StringRequest(
                Request.Method.POST,
                Upload_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Toast.makeText(getApplication(), s, Toast.LENGTH_SHORT).show();
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
                hashMap.put("image",base64);
                hashMap.put("username",User.username);
                hashMap.put("phonenumber", User.phone);
                hashMap.put("itemname",item_name.getText().toString());
                hashMap.put("datatime",UploadUtlis.getDate());
                hashMap.put("place",item_site.getText().toString());
                hashMap.put("remark",add_more.getText().toString());
                return hashMap;
            }};
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(200000, 1, 2));
        Volley.newRequestQueue(getApplication()).add(stringRequest);

    }

    private void initView() {
        title_text = (TextView) findViewById(R.id.title_text);
        if(Info.Lost_Or_found==0){
            title_text.setText("失物登记");
        } else if (Info.Lost_Or_found==1){
            title_text.setText("招领登记");
        }
        checkbox = (CheckBox) findViewById(R.id.checkbox);
        Choose_Btn = (Button) findViewById(R.id.Choose_Btn);
        Choose_Btn.setOnClickListener(this);
        upload_btn = (Button) findViewById(R.id.upload_btn);
        upload_btn.setOnClickListener(this);
        back_icon = (ImageView) findViewById(R.id.back_icon);
        title_rela = (RelativeLayout) findViewById(R.id.title_rela);
        image = (ImageView) findViewById(R.id.image);
        image.setOnClickListener(this);
        textView1 = (TextView) findViewById(R.id.textView1);
        view1 = (View) findViewById(R.id.view1);
        item_name = (EditText) findViewById(R.id.item_name);
        textView2 = (TextView) findViewById(R.id.textView2);
        view2 = (View) findViewById(R.id.view2);
        item_site = (EditText) findViewById(R.id.item_site);
        add_more = (EditText) findViewById(R.id.add_more);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
    }

    private void submit() {
        // validate

        if (TextUtils.isEmpty(base64)){
            Toast.makeText(this,"请补充物品图片",Toast.LENGTH_SHORT).show();
            return;
        }
        String name = item_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "请输入物品名称", Toast.LENGTH_SHORT).show();
            return;
        }

        String site = item_site.getText().toString().trim();
        if (TextUtils.isEmpty(site)) {
            Toast.makeText(this, "请输入丢失地点", Toast.LENGTH_SHORT).show();
            return;
        }

        String more = add_more.getText().toString().trim();
        if (TextUtils.isEmpty(more)) {
            Toast.makeText(this, "请补充更多详细信息或其他联系方式", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!checkbox.isChecked()){
            Toast.makeText(this,"请勾选用户需知",Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something
        VollerHepler();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();

            try {
                //getting image from gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Log.d("a",bitmap.toString());
                //Setting image to ImageView
                Glide.with(this).load(bitmap).into(image);
                base64=UploadUtlis.BitmapToBase64(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}