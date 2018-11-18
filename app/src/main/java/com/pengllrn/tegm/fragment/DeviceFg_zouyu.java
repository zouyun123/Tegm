package com.pengllrn.tegm.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.pengllrn.tegm.R;
import com.pengllrn.tegm.activity.LookDevice;
import com.pengllrn.tegm.adapter.DeviceinRoomAdapter;
import com.pengllrn.tegm.myclass.DeviceInRoom_zouyun;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DeviceFg_zouyu extends Fragment {

    private LookDevice lookDevice;
    private ListView list_gis;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private String roomid;


    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
                case 0x2017:
                    String responsedata = (msg.obj).toString();
                    List<DeviceInRoom_zouyun> mylist = new ArrayList<>();

                    try {
                        JSONObject object = new JSONObject(responsedata);
                        JSONArray array = object.getJSONArray("device_list");
                        for(int i = 0; i < array.length(); i++){
                            JSONObject myobject = array.getJSONObject(i);
                            String Devicenum = myobject.getString("DeviceNum");
                            String Typename = myobject.getString("typename");
                            boolean Useflag = myobject.getBoolean("UseFlag");
                            String Devicekind = myobject.getString("devicekind");
                            DeviceInRoom_zouyun myclass = new DeviceInRoom_zouyun(Devicenum, Typename, Useflag, Devicekind);
                            mylist.add(myclass);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(mylist != null){
                        list_gis.setAdapter(new DeviceinRoomAdapter(lookDevice, mylist, R.layout.base_list_item));
                        list_gis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                            }
                        });
                    }

            }
        }
    };


    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context != null){
            lookDevice = (LookDevice) context;
        }
        lookDevice = (LookDevice) getActivity();
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        textView1 = (TextView) lookDevice.findViewById(R.id.text1_zouyun);
        textView2 = (TextView) lookDevice.findViewById(R.id.text2_zouyun);
        textView3 = (TextView) lookDevice.findViewById(R.id.text3_zouyun);
        textView4 = (TextView) lookDevice.findViewById(R.id.text4_zouyun);
        textView1.setText("设备编号");
        textView2.setText("设备种类");
        textView3.setText("是否使用");
        textView4.setText("所属种类");
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        list_gis = (ListView) view.findViewById(R.id.list_gis);
        roomid = getArguments().getString("roomid");
        System.out.println("DeviceFg_zouyu中的roomid为：" + roomid);
        get_devices_in_room(roomid);
    }


    // TODO: zouyun 获取房间中的设备
    public void get_devices_in_room(final String roomid){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    LookDevice activity = (LookDevice) getActivity();
                    SharedPreferences pref = activity.getSharedPreferences("mycookie", Context.MODE_PRIVATE);
                    String sessionid = pref.getString("sessionid", " ");

                    String url = getUrl2("http://47.107.37.50:8000/get_devices_in_room/", getUrl1("roomid", roomid));
                    System.out.println("deviceinroom的url为：" + url);

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().addHeader("cookie", sessionid).url(url).build();
                    Response response = client.newCall(request).execute();
                    String responsedata = response.body().string();
                    System.out.println("房间中的设备信息为：" + responsedata);
                    if(response.isSuccessful()){
                        System.out.println("获取房间中设备信息成功：" + responsedata);
                        Message msg = new Message();
                        msg.what = 0x2017;
                        msg.obj = responsedata;
                        mHandler.sendMessage(msg);
                    }else{
                        System.out.println("获取房间中设备信息失败");
                    }
                }catch(Exception e){
                    System.out.println("获取房间中设备信息异常");
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
