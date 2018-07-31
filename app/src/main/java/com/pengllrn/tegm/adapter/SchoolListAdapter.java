package com.pengllrn.tegm.adapter;

import android.content.Context;

import com.pengllrn.tegm.R;
import com.pengllrn.tegm.base.ListViewAdapter;
import com.pengllrn.tegm.base.ViewHolder;
import com.pengllrn.tegm.bean.School;
import com.pengllrn.tegm.bean.SchoolList;

import java.util.List;

/**
 * @author Administrator
 * @version $Rev$
 * @des ${UTODO}
 * @updateAuthor ${Author}$
 * @updateDate2017/11/1.
 */

    public class SchoolListAdapter extends ListViewAdapter<School> {
    public SchoolListAdapter(Context context, List<School> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, School school) {
        holder.setText(R.id.item1,school.getSchoolname());
        holder.setText(R.id.item2,school.getTotaldevice());
        holder.setText(R.id.item3,school.getUsingdevice());
        holder.setText(R.id.item4,String.valueOf(school.getRate())+"%");
    }
}
