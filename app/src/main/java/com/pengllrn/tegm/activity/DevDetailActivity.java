package com.pengllrn.tegm.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pengllrn.tegm.R;
import com.pengllrn.tegm.bean.DevDetail;
import com.pengllrn.tegm.bean.UseRecord;
import com.pengllrn.tegm.constant.Constant;
import com.pengllrn.tegm.gson.ParseJson;
import com.pengllrn.tegm.internet.OkHttp;
import com.pengllrn.tegm.views.BrokenLineChartView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class DevDetailActivity extends AppCompatActivity {
    private List<String> date = new ArrayList<>();
    private List<Double> score = new ArrayList<>();
    private TextView dev_type;
    private TextView dev_num;
    private TextView dev_useflag;

    private TextView dev_schoolname;
    private TextView dev_roominfo;
    private TextView dev_order_in_room;
    private TextView dev_usedepart;

    private TextView dev_use_record;
    private BrokenLineChartView line_chart;
    private TextView tv_more;

    private TextView dev_kind;
    private TextView dev_configureinfo;
    private TextView dev_description;

    private Button apply_damage;

    private String path = Constant.URL_DEVICE_DETAIL;
    private ParseJson mParseJson = new ParseJson();

    private String deviceid;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case 0x2017:
                    String responseData = (msg.obj).toString();
                    DevDetail devDetail = mParseJson.JsonToDevDetail(responseData);
                    initChartData(devDetail);
                    setview(devDetail);
                    break;
                case 0x22:
                    Toast.makeText(DevDetailActivity.this, "数据更新失败！", Toast.LENGTH_SHORT).show();
                    //line_chart.drawBrokenLine(date, score);
                    break;
            }
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.back);
            actionBar.setTitle("");
        }

        initview();
        Intent intent = getIntent();
        deviceid = intent.getStringExtra("device_id");
        //自动从网络更新数据
        RequestBody requestBody = new FormBody.Builder().add("deviceid", deviceid).build();
        OkHttp okHttp = new OkHttp(this, mHandler);
        okHttp.postDataFromInternet(path, requestBody);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initview() {
        dev_type = (TextView) findViewById(R.id.dev_type);
        dev_num = (TextView) findViewById(R.id.dev_num);
        dev_useflag = (TextView) findViewById(R.id.dev_useflag);

        dev_schoolname = (TextView) findViewById(R.id.dev_schoolname);
        dev_roominfo = (TextView) findViewById(R.id.dev_roominfo);
        dev_order_in_room = (TextView) findViewById(R.id.dev_order_in_room);
        dev_usedepart = (TextView) findViewById(R.id.dev_usedepart);

        dev_use_record = (TextView) findViewById(R.id.dev_use_record);
        tv_more = (TextView) findViewById(R.id.tv_more);
        line_chart = (BrokenLineChartView) findViewById(R.id.line_chart);

        dev_kind = (TextView) findViewById(R.id.dev_kind);
        dev_description = (TextView) findViewById(R.id.dev_description);
        dev_configureinfo = (TextView) findViewById(R.id.dev_configureinfo);

        apply_damage = (Button) findViewById(R.id.btn_apply_damage);
    }

    private void setview(final DevDetail devDetail) {
        dev_type.setText("设备名称：" + devDetail.getTypename());
        dev_num.setText("设备编号：" + devDetail.getDeviceNum());
        if(devDetail.getUseFlag().equals("true")){
            dev_useflag.setText("正在使用");
            dev_useflag.setTextColor(Color.parseColor("#048f39"));
        }
        dev_schoolname.setText("所属学校：" + devDetail.getSchoolname());
        dev_roominfo.setText("所属信息：" + devDetail.getRoom_Building() + "  "
                + devDetail.getRoom_Roomname());
        dev_order_in_room.setText("间内序号：" + devDetail.getOrderNum());
        dev_usedepart.setText("使用部门：" + devDetail.getUsedepart());

        dev_kind.setText("设备类型：" + devDetail.getDevicekind());
        dev_description.setText("设备介绍：" + devDetail.getDescription());
        dev_configureinfo.setText("配置信息：" + devDetail.getConfigureinfo());
        if (date.size() > 0 && score.size() > 0) {
            line_chart.drawBrokenLine(date, score);//设置图标
        }
        if (devDetail.getStatus() == 2) {
            apply_damage.setClickable(false);
            apply_damage.setBackgroundColor(Color.GRAY);
            apply_damage.setText("已报废");
        } else {
            apply_damage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DevDetailActivity.this, DamageApllyActivity.class);
                    intent.putExtra("deviceid", deviceid);
                    intent.putExtra("devicenum", devDetail.getDeviceNum());
                    intent.putExtra("status", devDetail.getStatus());
                    intent.putExtra("devicename", devDetail.getTypename());
                    startActivity(intent);
                }
            });
        }
        tv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date.clear();
                score.clear();
                List<UseRecord> useRates = devDetail.getDeviceUseRecord();
                if(useRates.size()>7){
                    for (int i = 0; i < useRates.size(); i++) {
                        date.add(useRates.get(i).getDate());
                        score.add(useRates.get(i).getTotal_time());
                    }
                    line_chart.drawBrokenLine(date, score);
                }
                dev_use_record.setText("近30天使用记录如下（小时）: ");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
        }
        return true;
    }

    private void initChartData(DevDetail devDetail) {
        date.clear();
        score.clear();
        List<UseRecord> useRates = devDetail.getDeviceUseRecord();
        if (useRates.size() > 7) {
            for (int i = useRates.size() - 7; i < useRates.size(); i++) {
                date.add(useRates.get(i).getDate());
                score.add(useRates.get(i).getTotal_time());
            }
        } else {
            for (int i = 0; i < useRates.size(); i++) {
                date.add(useRates.get(i).getDate());
                score.add(useRates.get(i).getTotal_time());
            }
        }
    }

}
