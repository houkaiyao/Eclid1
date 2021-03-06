package com.example.eclid1.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.util.Util;
import com.example.eclid1.Bean.Bus_Ground;
import com.example.eclid1.Bean.Info;
import com.example.eclid1.Entity.NowBusLines;
import com.example.eclid1.R;
import com.example.eclid1.Utlis.Utlis;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BusActivity extends Activity implements View.OnClickListener {
    private String Lng, Lat;
    private ListView listview, busline_listview;
    private ImageView back_icon, daohang;
    private SmartRefreshLayout refreshLayout;
    List<HashMap<String, Object>> hashMapList;
    HashMap<String, Object> hashMap;
    List<Bus_Ground.ReturlListDTO> returlListDTOS;
    Bus_Ground.ReturlListDTO returlListDTO;
    private TextView title_text,around_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utlis.initState(this);
        getLocation();
        setContentView(R.layout.activity_bus);
        init();
        VolleyHelper();
    }

    private void init() {
        listview = (ListView) findViewById(R.id.listview);
        OnClickItem();
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        Refresh();
        busline_listview = (ListView) findViewById(R.id.busline_listview);
        title_text = (TextView) findViewById(R.id.title_text);
        around_title = (TextView) findViewById(R.id.around_title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.daohang:
                Utlis.goGaodeMap(BusActivity.this);
                break;
        }
    }

    // ????????????-????????????
    private void VolleyHelper() {
        hashMapList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                Info.Bus_around + "xylat=" + Lat + "&xylng=" + Lng,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Gson gson = new Gson();
                        //Toast.makeText(getApplicationContext(),jsonObject.toString(),Toast.LENGTH_SHORT).show();
                        Bus_Ground bus_ground = gson.fromJson(jsonObject.toString(), Bus_Ground.class);
                        int ListSize = bus_ground.getReturl_list().size();
                        returlListDTOS = bus_ground.getReturl_list();
                        for (int i = 0; i < ListSize; i++) {
                            returlListDTO = returlListDTOS.get(i);
                            hashMap = new HashMap<>();
                            hashMap.put("title", returlListDTO.getTitle());
                            hashMap.put("range", returlListDTO.getXydistance() + "???");
                            hashMap.put("buslist", returlListDTO.getBuslist());
                            hashMapList.add(hashMap);
                        }
                        SimpleAdapter simpleAdapter = new SimpleAdapter(
                                getApplication(),
                                hashMapList,
                                R.layout.bus_item,
                                new String[]{"title", "range", "buslist"},
                                new int[]{R.id.title, R.id.range, R.id.bus_lines});
                        listview.setAdapter(simpleAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), volleyError.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    //????????????location
    private void getLocation() {
        //???????????????LocationManager??????
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //??????????????????
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //???????????????????????????location??????
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,      //GPS???????????????
                1000,       //?????????????????????1???
                1,      //???????????????1???
                //???????????????
                new LocationListener() {  //GPS????????????????????????????????????????????????????????????

                    @Override
                    public void onLocationChanged(Location location) {
                        //GPS????????????????????????????????????
                        Lat = String.valueOf(location.getLatitude());
                        Lng = String.valueOf(location.getLongitude());
                    }

                    @Override
                    //?????????????????????????????????
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }

                    @Override
                    //??????????????????????????????
                    public void onProviderEnabled(String provider) {
                    }

                    @Override
                    //??????????????????????????????
                    public void onProviderDisabled(String provider) {
                    }
                });
        //???GPS???????????????????????????
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Lat = String.valueOf(location.getLatitude());
        Lng = String.valueOf(location.getLongitude());
    }

    //list????????????(????????????)
    private void Refresh() {
        RefreshLayout refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        //????????????

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000);
                VolleyHelper();
            }
        });
        //????????????
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(2000);
                //VolleyHelper();
            }
        });
    }

    // listview????????????
    private void OnClickItem() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //parent ??????listView View ?????? ????????????????????? position ??????????????? id ??????????????????
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                returlListDTO = returlListDTOS.get(position);
                NowBusLines.BusSite = returlListDTO.getTitle();
                NowBusLines.SiteLat = returlListDTO.getLat();
                NowBusLines.SiteLng = returlListDTO.getLng();
                NowBusLines.range = returlListDTO.getXydistance();
                NowBusLines.bus_list = returlListDTO.getBuslist();
                PosiOnclick();
            }
        });
    }

    HashMap<String, Object> hashMap2;
    List<HashMap<String, Object>> hashMapList2;

    private void PosiOnclick() {
        refreshLayout.setVisibility(View.GONE);
        busline_listview.setVisibility(View.VISIBLE);
        //Utlis.goGaodeMap(BusActivity.this,Lat,Lng,"????????????");
        title_text.setText(NowBusLines.BusSite);
        around_title.setText("??????????????????");
        daohang = (ImageView) findViewById(R.id.daohang);
        Bitmap gameStatusBitmap2 = BitmapFactory.decodeResource(getResources(), R.mipmap.daohang);
        daohang.setImageBitmap(gameStatusBitmap2);
        daohang.setOnClickListener(this);
        int lenth = NowBusLines.bus_list.size();
        hashMapList2 = new ArrayList<>();
        for (int i = 0; i < lenth; i++) {
            hashMap2 = new HashMap<>();
            hashMap2.put("lines", NowBusLines.bus_list.get(i));
            hashMapList2.add(hashMap2);
            Log.d("??????????????????????????????????????????????????????????????????", NowBusLines.bus_list.get(i));
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(
                getApplication(),
                hashMapList2,
                R.layout.nowlines_item,
                new String[]{"lines"},
                new int[]{R.id.bus_lines_itemtext});
        busline_listview.setAdapter(simpleAdapter);

        busline_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //parent ??????listView View ?????? ????????????????????? position ??????????????? id ??????????????????
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String posi=NowBusLines.bus_list.get(position);
                Toast.makeText(BusActivity.this,posi,Toast.LENGTH_SHORT).show();
            }
        });
    }


//  -----------??????-------------
}


