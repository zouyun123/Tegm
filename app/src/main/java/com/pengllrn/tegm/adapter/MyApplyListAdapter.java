package com.pengllrn.tegm.adapter;

import android.content.Context;

import com.pengllrn.tegm.R;
import com.pengllrn.tegm.base.ListViewAdapter;
import com.pengllrn.tegm.base.ViewHolder;
import com.pengllrn.tegm.bean.ApplyCenterBean;


import java.util.List;

/**
 * @author Administrator
 * @version $Rev$
 * @des ${UTODO}
 * @updateAuthor ${Author}$
 * @updateDate2017/11/1.
 */

public class MyApplyListAdapter extends ListViewAdapter<ApplyCenterBean> {
    public MyApplyListAdapter(Context context, List<ApplyCenterBean> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, ApplyCenterBean applyCenterBean) {
        holder.setText(R.id.item1,applyCenterBean.getType());
        holder.setText(R.id.item2,applyCenterBean.getDevicenum());
        holder.setText(R.id.item3,applyCenterBean.getDatetime());
        holder.setText(R.id.item4,applyCenterBean.getDeal_status());
    }
}
