package com.pengllrn.tegm.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pengllrn.tegm.R;
import com.pengllrn.tegm.constant.Constant;
import com.pengllrn.tegm.gson.ParseJson;
import com.pengllrn.tegm.myclass.Person;
import com.pengllrn.tegm.utils.ActivityCollector;
import com.pengllrn.tegm.utils.SharedHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText user_id;
    private EditText passward;
    private Button btn_login;
    private TextView forget;
    private String applyUrl = Constant.URL_LOGIN;
    private ParseJson mParseJson = new ParseJson();
    private SharedHelper sharedHelper;
    private OkHttpClient client = new OkHttpClient();
    private String myloginid;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO 完成信息的存储
            //TODO 完成登录成功后的操作
            switch (msg.what) {
                case 0x2017:
                    String responseData = (msg.obj).toString();
//                    System.out.println(responseData);
                    Gson gson = new Gson();
                    Person person = gson.fromJson(responseData, Person.class);
                    int status = person.getStatus();//得到status
                    int usertype = person.getUsertype();//得到usertype
                    String username = person.getUsername();//得到username

                    if (status == 0) {
//                        User user = mParseJson.Json2User(responseData);
//                        sharedHelper = new SharedHelper(getApplicationContext());
//                        sharedHelper.save(user);
                        sharedHelper = new SharedHelper(getApplicationContext());
                        sharedHelper.savePerson2(status, usertype, username, myloginid);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);

                        Toast.makeText(getApplicationContext(), "欢迎您，" + username, Toast.LENGTH_SHORT).show();
                        user_id.setText("");
                        passward.setText("");
                    } else {
                        Toast.makeText(getApplicationContext(), "账号或密码错误", Toast.LENGTH_SHORT).show();
                    }
                default:
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedHelper sharedHelper = new SharedHelper(this);
        if (sharedHelper.readStatus("status") == 0) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
        setContentView(R.layout.activity_login);
        ActivityCollector.addActivity(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permission = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(LoginActivity.this, permission, 1);
        }
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user_id.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "请输入登录账号", Toast.LENGTH_SHORT).show();
                    user_id.setFocusable(true);
                } else if (passward.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "请输入登录密码", Toast.LENGTH_SHORT).show();
                    passward.setFocusable(true);
                } else {
                    //TODO 密码加密
//                    OkHttp okHttp = new OkHttp(getApplicationContext(), mHandler);
                    RequestBody requestBody = new FormBody.Builder()
                            .add("loginid", user_id.getText().toString())
                            .add("password",passward.getText().toString())
                            .build();
                    myloginid = user_id.getText().toString();
                    login("http://47.107.37.50:8000/login/", requestBody);
                }
            }
        });
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        user_id = (EditText) findViewById(R.id.user_id);
        passward = (EditText) findViewById(R.id.passward);
        btn_login = (Button) findViewById(R.id.btn_login);
        forget = (TextView) findViewById(R.id.forget);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    //zouyun登陆并存储cookie
    public void login(final String url, final RequestBody requestBody){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    client = new OkHttpClient.Builder()
                            .cookieJar(new CookieJar() {
                                private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
                                @Override
                                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                                    cookieStore.put(url.host(), cookies);
                                    System.out.println(cookieStore.get(url.host()));
                                    System.out.println(cookieStore.get(url.host()).get(0));
                                    System.out.println(String.valueOf(cookieStore.get(url.host()).get(0)));
                                    System.out.println("cookieStore: " + cookieStore);
                                    SharedPreferences.Editor editor = getSharedPreferences("mycookie", MODE_PRIVATE).edit();
                                    editor.putString("sessionid", String.valueOf(cookieStore.get(url.host()).get(0)));
                                    editor.apply();
                                }

                                @Override
                                public List<Cookie> loadForRequest(HttpUrl url) {
                                    List<Cookie> cookies = cookieStore.get(url.host());
                                    System.out.println("列表cookies: " + cookies);
                                    return cookies != null ? cookies : new ArrayList<Cookie>();
                                }
                            }).build();
                    Request request = new Request.Builder().url(url).post(requestBody).build();
                    Call call = client.newCall(request);
                    Response response = call.execute();
                    String responsedata = response.body().string();
                    if(response.isSuccessful()){
                        System.out.println("登陆成功" + "   " + responsedata);
                        Message message = new Message();
                        message.what = 0x2017;
                        message.obj = responsedata;
                        mHandler.sendMessage(message);
                    }else{
                        System.out.println("登陆失败");
                    }
                }catch(Exception e){

                }
            }
        }).start();
    }


}
