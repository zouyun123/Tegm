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
import com.pengllrn.tegm.adapter.SchoolListAdapter;
import com.pengllrn.tegm.bean.School;
import com.pengllrn.tegm.constant.Constant;
import com.pengllrn.tegm.gson.ParseJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.R.attr.data;

/**
 * @author Administrator
 * @version $Rev$
 * @des ${UTODO}
 * @updateAuthor ${Author}$
 * @updateDate2017/11/23.
 */

public class SchoolListFg extends Fragment {
    private String applyUrl = Constant.URL_GIS;
    private LookDevice lookDeviceActivity;
    private ListView list_gis;
    private String a ;//存储responsedta
    private List<School> mylist = new LinkedList<>();
    private int num1 = 0;//存储学校个数
    private int num2;
    private List<Integer> usinglist = new ArrayList<>();
    private List<Integer> totallist = new ArrayList<>();
    private List<String> namelist = new ArrayList<>();
    private List<String> schoolidlist = new ArrayList<>();
    private String mydata;


    private ParseJson mParseJson = new ParseJson();

    //zouyun 设置顶端提示性语句
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch(msg.what) {

//                    String responseData = (msg.obj).toString();
//                    List<School> listSchool = mParseJson.Json2Gis(responseData).getSchoolLists();
                case 0X2017:
                    System.out.println("usinglist的值为：" + usinglist);
                    System.out.println("totallist的值为：" + totallist);
//                    String data = lookDeviceActivity.read("schoolList");


                        mylist.clear();
//
//                        JSONObject object = new JSONObject(mydata);
//                        JSONArray array = object.getJSONArray("schoolList");
                        for(int i = 0; i < num2; i++){
//                            JSONObject myobject = array.getJSONObject(i);
                            String schoolname = namelist.get(i);
                            String schoolid = schoolidlist.get(i);
                            String using_device = String.valueOf(usinglist.get(i));
                            String total_device = String.valueOf(totallist.get(i));
                            int rate = 0;
                            if(totallist.get(i) != 0){
                                double f1 = new BigDecimal((float)usinglist.get(i)/totallist.get(i)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                rate = (int) (f1 * 100);
                            }
                            School school = new School(schoolid, schoolname, using_device, total_device, rate);
                            mylist.add(school);
                        }

                    if(mylist!=null) {
                        list_gis.setAdapter(new SchoolListAdapter(lookDeviceActivity,
                                mylist, R.layout.base_list_item));
                        setListListener(mylist);
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context != null) {
            lookDeviceActivity = (LookDevice) context;
        }else {
            lookDeviceActivity = (LookDevice) getActivity();
        }
        System.out.println("Attach中mylist的长度：" + mylist.size());
        mylist.clear();
        usinglist.clear();
        totallist.clear();
        namelist.clear();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

//        textView1 = (TextView) lookDeviceActivity.findViewById(R.id.text1_zouyun);
//        textView2 = (TextView) lookDeviceActivity.findViewById(R.id.text2_zouyun);
//        textView3 = (TextView) lookDeviceActivity.findViewById(R.id.text3_zouyun);
//        textView4 = (TextView) lookDeviceActivity.findViewById(R.id.text4_zouyun);
//        textView1.setText("学校");
//        textView2.setText("设备总数");
//        textView3.setText("正在使用");
//        textView4.setText("使用率");

        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mylist.clear();
        usinglist.clear();
        totallist.clear();
        namelist.clear();
        schoolidlist.clear();
        list_gis = (ListView) view.findViewById(R.id.list_gis);
         mydata = lookDeviceActivity.read("schoolList");
        System.out.println("schoolList的data: " +data);
        System.out.println("schoolListFg的responsedata为: " + data);
            try {
                JSONObject object = new JSONObject(mydata);
                JSONArray array = object.getJSONArray("school_list");
                System.out.println("schoolListFg的array为: " + array);
                num2 = array.length();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject myobject = array.getJSONObject(i);
                    String myid = myobject.getString("schoolid");
                    System.out.println("schoolid为：" + myid);
                    String schoolname = myobject.getString("schoolname");
                    get_devices_usage(myid);

                    System.out.println("schoolid为：" + myid);
                    System.out.println("schoolname: " + namelist);
                    System.out.println("totallist: " + totallist);
                    System.out.println("usinglist: " + usinglist);
                }
                // System.out.println("usinglist的值为：" + usinglist);
            } catch (JSONException e) {
                e.printStackTrace();
            }

    }


//        if(mylist != null){
//            list_gis.setAdapter(new SchoolListAdapter(lookDeviceActivity,
//                    mylist, R.layout.base_list_item));
//            setListListener(mylist);
//        }




//
//        if (data != null && !data.equals("")) {
//            List<School> listSchool = mParseJson.SchoolPoint(data);
//            if(listSchool!=null) {
//                list_gis.setAdapter(new SchoolListAdapter(lookDeviceActivity,
//                        listSchool, R.layout.base_list_item));
//                setListListener(listSchool);
//            }
//        }else {
//            OkHttp okHttp = new OkHttp(lookDeviceActivity, mHandler);
//            RequestBody requestBody = new FormBody.Builder().add("type", "1").build();
//            okHttp.postDataFromInternet(applyUrl, requestBody);
//        }


    public void setListListener(final List<School> listSchool){
        list_gis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String schoolid=listSchool.get(position).getId();
                Bundle bundle = new Bundle();
                bundle.putString("schoolid",schoolid);

               System.out.println("mylist: " + mylist);


                //System.out.println("schoolid: " + schoolid);

                    BuildingListFg buildingListFg = new BuildingListFg();
                    buildingListFg.setArguments(bundle);
                    FragmentManager fragmentManager = lookDeviceActivity.getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragment_list, buildingListFg);
                    transaction.addToBackStack(null);
                    transaction.commit();
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        textView1 = (TextView) lookDeviceActivity.findViewById(R.id.text1_zouyun);
        textView2 = (TextView) lookDeviceActivity.findViewById(R.id.text2_zouyun);
        textView3 = (TextView) lookDeviceActivity.findViewById(R.id.text3_zouyun);
        textView4 = (TextView) lookDeviceActivity.findViewById(R.id.text4_zouyun);
        textView1.setText("名称");
        textView2.setText("设备总数");
        textView3.setText("正在使用");
        textView4.setText("使用率");
    }



    @Override
    public void onDestroyView(){
        super.onDestroyView();
        System.out.println("mylist的长度：" + mylist.size());
        mylist.clear();
        usinglist.clear();
        totallist.clear();
        System.out.println("mylist的长度：" + mylist.size());
        List<School> list_clear = new ArrayList<>();
        list_gis.setAdapter(new SchoolListAdapter(lookDeviceActivity,
                list_clear, R.layout.base_list_item));
    }



    // TODO: zouyun 获取学校中的设备使用情况
    public void get_devices_usage(final String schoolid){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    LookDevice activity = (LookDevice) getActivity();
                    SharedPreferences pref = activity.getSharedPreferences("mycookie", Context.MODE_PRIVATE);
                    String sessionid = pref.getString("sessionid", " ");

                    String url = getUrl2("http://47.107.37.50:8000/get_devices_usage/", getUrl1("schoolid", schoolid));
                    System.out.println("schoollist的url为：" + url);



                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().addHeader("cookie", sessionid).url(url).build();
                    Response response = client.newCall(request).execute();
                    String responsedata = response.body().string();
                    System.out.println("responsedata :" + responsedata);

                    deal(responsedata);

                    if(response.isSuccessful() && usinglist.size() == num2 && totallist.size() == num2){
                        System.out.println("获取设备使用请况成功：" + responsedata);
                        Message msg = new Message();
                        msg.what = 0x2017;
                        msg.obj = responsedata;
                        mHandler.sendMessage(msg);
                    }else{
                        System.out.println("获取设备使用请况失败");
                    }
                }catch(Exception e){
                    System.out.println("获取设备使用请况异常");
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

    //ToDo 处理数据
    public void deal(String jsondata){
        JSONObject object2;
        try {
            JSONObject object1 = new JSONObject(jsondata);
            object2 = (JSONObject) object1.get("device_usage");
            usinglist.add(object2.getInt("using_device"));
            totallist.add(object2.getInt("total_device"));
            namelist.add(object2.getString("schoolname"));
            schoolidlist.add(object2.getString("schoolid"));

            System.out.println("deal方法usinglist的值为：" + usinglist);
            System.out.println("deal方法totallist的值为：" + totallist);
            System.out.println("deal方法namelist的值为：" + namelist);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}
