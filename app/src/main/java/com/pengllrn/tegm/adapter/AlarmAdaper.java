package com.pengllrn.tegm.adapter;

import android.content.Context;

import com.pengllrn.tegm.R;
import com.pengllrn.tegm.base.ListViewAdapter;
import com.pengllrn.tegm.base.ViewHolder;
import com.pengllrn.tegm.bean.AlarmList;

import java.util.List;

/**
 * Author：Pengllrn
 * Date: 2018/6/12
 * Contact 897198177@qq.com
 * https://github.com/pengllrn
 */

public class AlarmAdaper extends ListViewAdapter<AlarmList> {
    public AlarmAdaper(Context context, List<AlarmList> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, AlarmList alarmList) {
        holder.setText(R.id.dev_order,alarmList.getOder());
        holder.setText(R.id.tv_alarm,alarmList.getAlarmtype());
        holder.setText(R.id.tv_room,alarmList.getRoom());
        holder.setText(R.id.tv_name,alarmList.getTypename());
        holder.setText(R.id.last_update_time,alarmList.getLastupdate());
        holder.setText(R.id.tv_schoolname,alarmList.getSchoolname());
        holder.setText(R.id.tv_num,alarmList.getDevnum());
        holder.setItemListener(R.id.cb_more,R.id.lv_detail);
    }
}
