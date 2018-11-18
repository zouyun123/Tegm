package com.pengllrn.tegm.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.pengllrn.tegm.R;
import com.pengllrn.tegm.bean.School;
import com.pengllrn.tegm.constant.Constant;
import com.pengllrn.tegm.gson.ParseJson;
import com.pengllrn.tegm.utils.ActivityCollector;
import com.pengllrn.tegm.utils.FileCache;
import com.pengllrn.tegm.utils.SharedHelper;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private NavigationView navView;
    private ImageView menu_home;
    private MapView mapView;
    private TextView username;
    private LinearLayout lv_exit;

    private String applyUrl = Constant.URL_MAIN;

    public LocationClient mLocationClient;
    private BaiduMap baiduMap;

    private SharedHelper sharedHelper;
    private ParseJson parseJson;
    private List<School> listSchool;

    private long exitTime = 0;
    private boolean isFirstLocate = true;
    private boolean isFirstSearch = true;

    private double avg_latitude = 0;
    private double avg_longitude = 0;

    private String school;
    private String userid;
    private Boolean isOfficial;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case 0x2017:
                    String responseData = (msg.obj).toString();
                    parseJson = new ParseJson();
                    listSchool = parseJson.SchoolPoint(responseData);
                    School school;
                    for (int i = 0; i < listSchool.size(); i++) {
                        school = listSchool.get(i);
                        avg_latitude += school.getLatitude();
                        avg_longitude += school.getLongitude();
                        drawmarker(school.getId(), new LatLng(school.getLatitude(), school.getLongitude()), 50);
                    }
                    if (listSchool.size() > 0) {
                        save(responseData, "schoolList");
                    }
                    avg_latitude = avg_latitude / listSchool.size();
                    avg_longitude = avg_longitude / listSchool.size();
                    LatLng latLng = new LatLng(avg_latitude, avg_longitude);
                    MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latLng);
                    baiduMap.setMapStatus(update);
                    update = MapStatusUpdateFactory.zoomTo(13f);
                    baiduMap.setMapStatus(update);
                    isFirstLocate = false;
                    isFirstSearch = false;
                    //LatLng(v,v1),第一个参数是纬度值，第二个参数是经度值
                    break;
                default:
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());//地图
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.draw_layout);
        navView = (NavigationView) findViewById(R.id.nav_view);
        menu_home = (ImageView) findViewById(R.id.menu_home);
        lv_exit = (LinearLayout) findViewById(R.id.lv_exit);
        username = (TextView) navView.getHeaderView(0).findViewById(R.id.tv_username);
        mapView = (MapView) findViewById(R.id.bmapView);
        mapView.removeViewAt(1);//移除百度logo
        baiduMap = mapView.getMap();

        sharedHelper = new SharedHelper(getApplicationContext());
        username.setText(sharedHelper.readbykey("username"));

        school = sharedHelper.readbykey("school");
        userid = sharedHelper.readbykey("userid");
        isOfficial = sharedHelper.readOfficial();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocationClient = new LocationClient(getApplicationContext());//位置客户端
        mLocationClient.registerLocationListener(new MyLocationListener());//注册一个 位置监听器，并在匿名类对象onReceiveLocation函数中实现
        if (isFirstLocate) {
            navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_look_device:
                            Intent intent = new Intent(MainActivity.this, LookDevice.class);
                            startActivity(intent);
                            break;
                        case R.id.nav_search_device:
                            if (sharedHelper.readOfficial()) {
                                Toast.makeText(getApplicationContext(), "您没有权限查询", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent1 = new Intent(MainActivity.this, SearchActivity.class);
                                startActivity(intent1);
                            }
                            break;
                        case R.id.nav_damage_center:
                            Intent intent2 = new Intent(MainActivity.this, ApplyCenter.class);
                            startActivity(intent2);
                            break;
                        case R.id.nav_abnormal_warn:
                            Intent intent4 = new Intent(MainActivity.this,DeviceAlarmAct.class);
                            startActivity(intent4);
                            break;
                        case R.id.nav_property_statics:
                            Intent intent3 = new Intent(MainActivity.this, StatisticsAct.class);
                            startActivity(intent3);
                            break;
                        case R.id.nav_private_center:
                            Intent intent5 = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent5);
                            break;
                    }
                    return true;
                }
            });
            menu_home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });

            lv_exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    logout();
                    sharedHelper.clearPerson();
                    finish();
                }
            });

            //设置图标的点击事件
            baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Intent intent = new Intent(getApplicationContext(), LookDevice.class);
                    intent.putExtra("schoolid", marker.getTitle());
                    System.out.println("marker: " + marker.getTitle());
                    startActivity(intent);
                    return false;
                }
            });
        }
        if (isFirstSearch) {
//            OkHttp okHttp = new OkHttp(getApplicationContext(), mHandler);
//            if(isOfficial) school = "1";
//            RequestBody requestBody = new FormBody.Builder().add("school", school).add("userid", userid).build();
//            okHttp.postDataFromInternet(applyUrl, requestBody);

            SharedPreferences pref = getSharedPreferences("person", MODE_PRIVATE);
            String loginid = pref.getString("loginid", "");
            String url = getUrl2("http://47.107.37.50:8000/get_school_list/", getUrl1("loginid", loginid));
            System.out.println("schoollist的url为：" + url);

            getschool_list(url);



        }
        mapView.onResume();
        requestLocation();//开始定位
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawers();
            } else if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                FileCache fileCache = new FileCache(getApplicationContext());
                fileCache.clear("schoolList");
                ActivityCollector.finishAll();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void requestLocation() {
        mLocationClient.start();//开始定位，且只定位一次
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(10000);
        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (isFirstLocate) {
                LatLng latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latLng);
                baiduMap.setMapStatus(update);
                isFirstLocate = false;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();//结束定位
        mapView.onDestroy();
    }

    public void drawmarker(String id, LatLng point, int percent) {
        if (percent < 40 && percent >= 0) {
            BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                    .fromResource(R.drawable.gray_smile);
            MarkerOptions option = new MarkerOptions()
                    .position(point)
                    .icon(mCurrentMarker);
            option.title(id);
            baiduMap.addOverlay(option);
        } else if (percent <= 70 && percent >= 40) {
            BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                    .fromResource(R.drawable.blue_smile);
            MarkerOptions option = new MarkerOptions()
                    .position(point)
                    .icon(mCurrentMarker);
            option.title(id);
            baiduMap.addOverlay(option);
        } else if (percent <= 100 && percent > 70) {
            BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                    .fromResource(R.drawable.red_smile);
            MarkerOptions option = new MarkerOptions()
                    .position(point)
                    .icon(mCurrentMarker);
            option.title(id);
            baiduMap.addOverlay(option);
        }
    }

    public void save(String data, String filename) {
        FileCache fileCache = new FileCache(getApplicationContext());
        fileCache.saveInCacheDir(data, filename);
    }



    // ToDo zouyun登出
    public void logout(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    SharedPreferences pref = getSharedPreferences("mycookie", MODE_PRIVATE);
                    String sessionid = pref.getString("sessionid", " ");

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().addHeader("cookie", sessionid).url("http://47.107.37.50:8000/logout").build();
                    Response response = client.newCall(request).execute();
                    String responsedata = response.body().string();
                    System.out.println("登出：" + responsedata);
                }catch(Exception e){

                }
            }
        }).start();
    }

    // ToDo zouyun获取学校列表
    public void getschool_list(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    SharedPreferences pref = getSharedPreferences("mycookie", MODE_PRIVATE);
                    String sessionid = pref.getString("sessionid", " ");

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().addHeader("cookie",sessionid).url(url).build();
                    Response response = client.newCall(request).execute();
                    String responsedata = response.body().string();
                    System.out.println("学校列表：" + responsedata);
                    if(response.isSuccessful()){
                        Message message = new Message();
                        message.what = 0x2017;
                        message.obj = responsedata;
                        mHandler.sendMessage(message);
                    }else{

                    }
                }catch(Exception e){

                }
            }
        }).start();
    }

    // ToDo zouyun 携带信息get请求的url的转换方法1
    public static HashMap<String, String> getUrl1(String key1, String value1){
        HashMap hashmap = new HashMap();
        hashmap.put(key1, value1);
        return hashmap;
    }
    // ToDo zouyun携带信息get请求的url的转换方法2
    public static String getUrl2(String actionUrl, HashMap<String, String> paramsMap) {
        StringBuilder myBuilder = new StringBuilder();
        String a = null;
        try {
            //处理参数
            int pos = 0;
            for(String key : paramsMap.keySet()) {
                if (pos > 0) {
                    myBuilder.append("&");
                }
                //对参数进行URLEncoder
                myBuilder.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                pos++;
            }
            //补全请求地址
            String requestUrl = String.format("%s?%s", actionUrl, myBuilder.toString());
            a = requestUrl;
        }catch(Exception e) {
            e.printStackTrace();
        }
        return a;
    }
}