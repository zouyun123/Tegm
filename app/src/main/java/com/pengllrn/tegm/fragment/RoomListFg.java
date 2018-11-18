package com.pengllrn.tegm.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.pengllrn.tegm.R;
import com.pengllrn.tegm.activity.LookDevice;
import com.pengllrn.tegm.adapter.RoomListAdapter;
import com.pengllrn.tegm.bean.RoomList;
import com.pengllrn.tegm.constant.Constant;
import com.pengllrn.tegm.gson.ParseJson;

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

/**
 * @author Administrator
 * @version $Rev$
 * @des ${UTODO}
 * @updateAuthor ${Author}$
 * @updateDate2017/11/23.
 */

public class RoomListFg extends Fragment {
    private String applyUrl = Constant.URL_GIS;
    private LookDevice loolDeviceActivity;
    private ListView list_gis;

    private ParseJson mParseJson = new ParseJson();

    private String schoolid;
    private String buildingname;

    //zouyun 设置顶端提示性语句
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case 0x2017:
//                    String responseData = (msg.obj).toString();
//                    final List<RoomList> listRoom = mParseJson.Json2Gis(responseData).getRoomLists();

                    final List<RoomList> listRoom = new ArrayList<>();
                    String responsedata = (msg.obj).toString();
                    try {
                        JSONObject jsonObject = new JSONObject(responsedata);
                        JSONArray array = jsonObject.getJSONArray("room_list");
                        String schoolname_zouyun = jsonObject.getString("schoolname");
                        for(int i = 0; i < array.length(); i++){
                            JSONObject myobject = array.getJSONObject(i);
                            if(myobject.getString("buildingname").equals(buildingname)){
                                String room = myobject.getString("roomname");
                                String roomid = myobject.getString("roomid");
                                RoomList myroom = new RoomList(buildingname, room, roomid);
                                listRoom.add(myroom);

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(listRoom!=null) {
                        list_gis.setAdapter(new RoomListAdapter(loolDeviceActivity,
                                listRoom, R.layout.base_list_item));
                        list_gis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                                Intent intent = new Intent(loolDeviceActivity, DeviceInRoom.class);
//                                intent.putExtra("schoolid",schoolid);
//                                intent.putExtra("roomname", listRoom.get(position).getRoomname());
//                                intent.putExtra("buildingname",buildingname);
//                                startActivity(intent);

//                                String roomid = listRoom.get(position).getUsingdevice()

                                String roomid = listRoom.get(position).getUsingdevice();
                                Bundle bundle = new Bundle();
                                bundle.putString("roomid", roomid);
                                DeviceFg_zouyu deviceFg_zouyu = new DeviceFg_zouyu();
                                deviceFg_zouyu.setArguments(bundle);
                                FragmentManager fragmentManager = loolDeviceActivity.getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.fragment_list, deviceFg_zouyu);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }
                        });
                    }
                    break;
                default:
            }
            super.handleMessage(msg);
        }
    };


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context != null) {
            loolDeviceActivity = (LookDevice) context;
        }
        loolDeviceActivity = (LookDevice) getActivity();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        schoolid=getArguments().getString("schoolid");
        buildingname=getArguments().getString("buildingname");
        System.out.println("RoomListFg中的schoolid: " + schoolid);
        System.out.println("RoomListFg中的buildingname: " + buildingname);
        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView1 = (TextView) loolDeviceActivity.findViewById(R.id.text1_zouyun);
        textView2 = (TextView) loolDeviceActivity.findViewById(R.id.text2_zouyun);
        textView3 = (TextView) loolDeviceActivity.findViewById(R.id.text3_zouyun);
        textView4 = (TextView) loolDeviceActivity.findViewById(R.id.text4_zouyun);
        textView1.setText("教学楼");
        textView2.setText("房间");
        textView3.setText("房间编号");
        textView4.setText("");

        list_gis = (ListView) view.findViewById(R.id.list_gis);

        getroom(schoolid);

    }



    // TODO: zouyun 获取学校中房间
    public void getroom(final String schoolid){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    LookDevice activity = (LookDevice) getActivity();
                    SharedPreferences pref = activity.getSharedPreferences("mycookie", Context.MODE_PRIVATE);
                    String sessionid = pref.getString("sessionid", " ");

                    String url = getUrl2("http://47.107.37.50:8000/get_rooms_list/", getUrl1("schoolid", schoolid));
                    System.out.println("Roomlist的url为：" + url);

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().addHeader("cookie", sessionid).url(url).build();
                    Response response = client.newCall(request).execute();
                    String responsedata = response.body().string();
                    if(response.isSuccessful()){
                        System.out.println("获取学校中房间成功：" + responsedata);
                        Message msg = new Message();
                        msg.what = 0x2017;
                        msg.obj = responsedata;
                        mHandler.sendMessage(msg);
                    }else{
                        System.out.println("获取学校中房间失败");
                    }
                }catch(Exception e){
                    System.out.println("获取学校中房间异常");
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
