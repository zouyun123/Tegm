package com.pengllrn.tegm.fragment;

import android.content.Context;
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
import com.pengllrn.tegm.adapter.BuildingListAdapter;
import com.pengllrn.tegm.bean.BuildingList;
import com.pengllrn.tegm.constant.Constant;
import com.pengllrn.tegm.gson.ParseJson;
import com.pengllrn.tegm.internet.OkHttp;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * @author Administrator
 * @version $Rev$
 * @des ${UTODO}
 * @updateAuthor ${Author}$
 * @updateDate2017/11/23.
 */

public class BuildingListFg extends Fragment {
    private String applyUrl = Constant.URL_GIS;
    private LookDevice loolDeviceActivity;
    private ListView list_gis;

    private ParseJson mParseJson = new ParseJson();
    private String schoolid;
    private String schoolname;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case 0x2017:
                    String responseData = (msg.obj).toString();
                    final List<BuildingList> listBuilding = mParseJson.Json2Gis(responseData).getBuildingLists();
                    if(listBuilding!=null) {
                        list_gis.setAdapter(new BuildingListAdapter(loolDeviceActivity,
                                listBuilding, R.layout.base_list_item));
                        list_gis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                String buildingname = listBuilding.get(position).getBuildingname();
                                Bundle bundle = new Bundle();
                                bundle.putString("schoolid",schoolid);
                                bundle.putString("buildingname",buildingname);
                                RoomListFg roomListFg=new RoomListFg();
                                roomListFg.setArguments(bundle);
                                FragmentManager fragmentManager = loolDeviceActivity.getSupportFragmentManager();
                                FragmentTransaction transaction = fragmentManager.beginTransaction();
                                transaction.add(R.id.fragment_list, roomListFg);
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }
                        });
                    }
                    break;
                default:
            }
            super.handleMessage(msg);
        }
    };
    private TextView tv_school;

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
        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list_gis = (ListView) view.findViewById(R.id.list_gis);

        OkHttp okHttp = new OkHttp(loolDeviceActivity, mHandler);
        RequestBody requestBody = new FormBody.Builder()
                .add("type", "2")
                .add("schoolid",schoolid)
                .build();
        okHttp.postDataFromInternet(applyUrl, requestBody);
    }
}
