package com.pengllrn.tegm.adapter;

import android.content.Context;

import com.pengllrn.tegm.R;
import com.pengllrn.tegm.base.ListViewAdapter;
import com.pengllrn.tegm.base.ViewHolder;
import com.pengllrn.tegm.myclass.DeviceInRoom_zouyun;

import java.util.List;

/**
 * Created by 0hasee on 2018/11/15.
 */

public class DeviceinRoomAdapter extends ListViewAdapter<DeviceInRoom_zouyun>{


    public DeviceinRoomAdapter(Context context, List<DeviceInRoom_zouyun> datas, int layoutId) {
        super(context, datas, layoutId);
    }


    @Override
    public void convert(ViewHolder holder, DeviceInRoom_zouyun deviceInRoom_zouyun) {
        holder.setText(R.id.item1,deviceInRoom_zouyun.getDevicenum());
        holder.setText(R.id.item2,deviceInRoom_zouyun.getTypename());
        if(deviceInRoom_zouyun.getUseflag()){
            holder.setText(R.id.item3, "使用中");
            holder.setColor(R.id.item3,"#32CD32");
        }else{
            holder.setText(R.id.item3, "未使用");
            holder.setColor(R.id.item3,"#787878");
        }

        holder.setText(R.id.item4, deviceInRoom_zouyun.getDevicekind());
    }
}
