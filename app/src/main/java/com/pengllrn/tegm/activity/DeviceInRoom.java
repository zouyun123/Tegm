package com.pengllrn.tegm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pengllrn.tegm.R;
import com.pengllrn.tegm.adapter.DeviceAdapter;
import com.pengllrn.tegm.bean.Device;
import com.pengllrn.tegm.constant.Constant;
import com.pengllrn.tegm.gson.ParseJson;
import com.pengllrn.tegm.internet.OkHttp;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class DeviceInRoom extends AppCompatActivity {
    private String applyUrl = Constant.URL_GIS;
    private String buildingname;
    private String roomname;
    private String schoolid;

    ParseJson mParseJson = new ParseJson();

    private LinearLayout lv_change;
    private LinearLayout lv_all;
    private LinearLayout lv_usingdevice;
    private TextView tv_buildingname;
    private TextView tv_roomnane;
    private TextView tv_device_using;
    private TextView tv_all;
    private ListView list_device;
    private SwipeRefreshLayout swipeRefresh;

    private List<Device> Device = new ArrayList<>();
    private List<Device> UsingDevice = new ArrayList<>();

    private boolean isFresh = false;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case 0x2017:
                    String responseData = (msg.obj).toString();
                    final List<Device> listDevice = mParseJson.JsonToAll(responseData).getDevice();
                    if(listDevice!=null){
                        Device = listDevice;
                        list_device.setAdapter(new DeviceAdapter(getApplicationContext(),listDevice,
                                R.layout.item_layout));
                        setTv(listDevice);
                        list_device.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                Intent intent = new Intent(getApplicationContext(), DevDetailActivity.class);
                                intent.putExtra("device_id", listDevice.get(position).getDeviceId());
                                startActivity(intent);
                            }
                        });
                    }
                    if(isFresh){
                        Toast.makeText(getApplicationContext(),"刷新成功!",Toast.LENGTH_SHORT).show();
                        if(swipeRefresh.isRefreshing()){
                            swipeRefresh.setRefreshing(false);
                        }
                    }
                    break;
                default:
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_in_room);

        Intent intent = getIntent();
        schoolid=intent.getStringExtra("schoolid");
        buildingname=intent.getStringExtra("buildingname");
        roomname=intent.getStringExtra("roomname");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.back);
            actionBar.setTitle("");
        }

        initView();

        tv_buildingname.setText(buildingname);
        tv_roomnane.setText(roomname);

        OkHttp okHttp = new OkHttp(getApplicationContext(), mHandler);
        RequestBody requestBody = new FormBody.Builder()
                .add("type", "4")
                .add("schoolid",schoolid)
                .add("buildingname",buildingname)
                .add("roomname",roomname)
                .build();
        okHttp.postDataFromInternet(applyUrl, requestBody);

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        setListener();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.refresh:
                OkHttp okHttp = new OkHttp(getApplicationContext(), mHandler);
                RequestBody requestBody = new FormBody.Builder()
                        .add("type", "4")
                        .add("schoolid",schoolid)
                        .add("buildingname",buildingname)
                        .add("roomname",roomname)
                        .build();
                okHttp.postDataFromInternet(applyUrl, requestBody);
                isFresh = true;
                break;
            default:
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.look_device,menu);
        return true;
    }

    private void setListener() {
        lv_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pass
            }
        });
        lv_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setList(Device);
            }
        });
        lv_usingdevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setList(UsingDevice);
            }
        });
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttp okHttp = new OkHttp(getApplicationContext(), mHandler);
                        RequestBody requestBody = new FormBody.Builder()
                                .add("type", "4")
                                .add("schoolid",schoolid)
                                .add("buildingname",buildingname)
                                .add("roomname",roomname)
                                .build();
                        okHttp.postDataFromInternet(applyUrl, requestBody);
                        isFresh = true;
                    }
                }).start();
            }
        });
    }

    private void initView() {
        lv_change = (LinearLayout) findViewById(R.id.lv_change);
        lv_all = (LinearLayout) findViewById(R.id.lv_all);
        lv_usingdevice = (LinearLayout) findViewById(R.id.lv_usingdevice);

        tv_buildingname = (TextView) findViewById(R.id.tv_buildingname);
        tv_roomnane = (TextView) findViewById(R.id.tv_roomnane);
        tv_device_using = (TextView) findViewById(R.id.tv_device_using);
        tv_all = (TextView) findViewById(R.id.tv_all);

        list_device = (ListView) findViewById(R.id.list_device);
    }

    private void setTv(List<Device> listDevice) {
        int count=0;
        UsingDevice.clear();
        for(int i=0;i<listDevice.size();i++){
            if(listDevice.get(i).getUseFlag().equals("正在使用")){
                UsingDevice.add(listDevice.get(i));
                count++;
            }
        }
        tv_all.setText(String.valueOf(listDevice.size()));
        tv_device_using.setText(String.valueOf(count));
    }

    private void setList(List<Device> listDevice){
        list_device.setAdapter(new DeviceAdapter(getApplicationContext(),listDevice,
                R.layout.item_layout));
    }
}
