package com.pengllrn.tegm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pengllrn.tegm.R;
import com.pengllrn.tegm.adapter.PhotoAdapter;
import com.pengllrn.tegm.constant.Constant;
import com.pengllrn.tegm.internet.OkHttp;
import com.pengllrn.tegm.listener.RecyclerItemClickListener;
import com.pengllrn.tegm.utils.CompressImage;
import com.pengllrn.tegm.utils.SharedHelper;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

public class DamageApllyActivity extends AppCompatActivity {
    String applyUrl= Constant.URL_DAMAGE_APPLY;

    private PhotoAdapter photoAdapter;
    private SharedHelper sharedHelper ;

    private ArrayList<String> selectedPhotos = new ArrayList<>();
    private RecyclerView recylerview;
    private TextView tv_device_type;
    private TextView tv_device_status;
    private TextView tv_person;
    private TextView action_sure;
    private TextView action_cancel;
    private TextView tv_device_num;
    private EditText et_damage_depict;

    private String devicename;
    private int status;
    private String devicenum;
    String deviceid=null;
    String applierid="";
    String appliername="";
    String damagedepict="";
    String datetime="";
    JSONObject jsonObject=null;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case 0x2018:
                    String responseMes = (msg.obj).toString();
                    jsonObject=null;
                    try {
                        jsonObject = new JSONObject(responseMes);
                        int code=jsonObject.getInt("code");
                        switch (code){
                            case 0:
                                deviceid = null;
                                tv_device_status.setText("设备已提交报废申请");
                                et_damage_depict.setText("");
                                Toast.makeText(DamageApllyActivity.this,"设备报废申请已提交，请勿重试！", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                deviceid = null;
                                tv_device_status.setText("设备已提交报废申请");
                                et_damage_depict.setText("");
                                Toast.makeText(DamageApllyActivity.this,"设备报废申请提交成功！", Toast.LENGTH_SHORT).show();
                                break;
                            case -1:
                                Toast.makeText(DamageApllyActivity.this,"系统错误！", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                        }
                    }catch (Exception e){
                        e.printStackTrace();
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
        setContentView(R.layout.activity_damage_apply);

        Intent intent=getIntent();
        deviceid=intent.getStringExtra("deviceid");
        devicename=intent.getStringExtra("devicename");
        status=intent.getIntExtra("status",3);
        devicenum =intent.getStringExtra("devicenum");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.back);
            actionBar.setTitle("");
        }

        initviews();
        setRecyclerview();
        sharedHelper = new SharedHelper(getApplicationContext());
        applierid = sharedHelper.readbykey("userid");
        appliername = sharedHelper.readbykey("username");
        tv_person.setText(appliername);
        tv_device_type.setText(devicename);
        tv_device_num.setText(devicenum);
        tv_device_status.setText("设备正常");
    }



    private void initviews() {
        tv_device_type = (TextView) findViewById(R.id.tv_device_type);
        tv_device_status = (TextView) findViewById(R.id.tv_device_status);
        tv_device_num = (TextView) findViewById(R.id.tv_device_num);
        tv_person = (TextView) findViewById(R.id.tv_person);
        et_damage_depict = (EditText) findViewById(R.id.et_damage_depict);
        recylerview = (RecyclerView) findViewById(R.id.recycler_view);
        action_sure = (TextView) findViewById(R.id.tv_action_sure1);
        action_cancel = (TextView) findViewById(R.id.tv_action_cancel1);
    }

    private void setRecyclerview() {
        photoAdapter = new PhotoAdapter(this, selectedPhotos);
        recylerview.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));
        recylerview.setAdapter(photoAdapter);
        recylerview.addOnItemTouchListener(new RecyclerItemClickListener(this,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (photoAdapter.getItemViewType(position) == PhotoAdapter.TYPE_ADD) {
                            PhotoPicker.builder()
                                    .setPhotoCount(6)
                                    .setShowCamera(true)
                                    .setPreviewEnabled(false)
                                    .setSelected(selectedPhotos)
                                    .start(DamageApllyActivity.this);
                        } else {
                            PhotoPreview.builder()
                                    .setPhotos(selectedPhotos)
                                    .setCurrentItem(position)
                                    .start(DamageApllyActivity.this);
                        }
                    }
                }));
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        setLisener();
    }

    private void setLisener() {
        action_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(deviceid == null) {
                    Toast.makeText(DamageApllyActivity.this, "设备报废申请已提交，请勿重试！", Toast.LENGTH_SHORT).show();
                    return;
                }
                setString();
                if(damagedepict.equals("")||damagedepict.length()<10){
                    Toast.makeText(DamageApllyActivity.this,"请填写设备问题描述，且字数大于10", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(selectedPhotos.size()==0){
                    Toast.makeText(DamageApllyActivity.this,"请上传设备图片！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(applierid.equals("")){
                    Toast.makeText(DamageApllyActivity.this,"用户错误！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(status == 2){
                    Toast.makeText(DamageApllyActivity.this,"设备已报废，请勿重试！", Toast.LENGTH_SHORT).show();
                    return;
                }
                // final String img = new String(Base64.encodeToString(bytes, Base64.DEFAULT));
                OkHttp okHttp = new OkHttp(DamageApllyActivity.this,mHandler);
                File[] file=new File[selectedPhotos.size()];
                File[] file_cp=new File[selectedPhotos.size()];
                for(int i=0;i<selectedPhotos.size();i++){
                    file[i] = new File(selectedPhotos.get(i));
                    file_cp[i] = CompressImage.scalFile(file[i],DamageApllyActivity.this.getCacheDir().getAbsolutePath()+i+".jpg");
                }
                if(file_cp.length>0) {
                    okHttp.uploadMultiFile(applyUrl, devicenum,
                            deviceid,applierid,appliername,damagedepict,datetime, file_cp);
                }
            }
        });

        action_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setString() {
        damagedepict = et_damage_depict.getText().toString();
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss ");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        datetime=formatter.format(curDate);
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

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK &&
                (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {

            List<String> photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            }
            selectedPhotos.clear();

            if (photos != null) {
                selectedPhotos.addAll(photos);
            }
            photoAdapter.notifyDataSetChanged();
        }
    }

}
