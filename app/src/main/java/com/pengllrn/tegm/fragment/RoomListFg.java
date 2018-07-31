package com.pengllrn.tegm.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pengllrn.tegm.R;
import com.pengllrn.tegm.activity.DeviceInRoom;
import com.pengllrn.tegm.activity.LookDevice;
import com.pengllrn.tegm.adapter.RoomListAdapter;
import com.pengllrn.tegm.bean.RoomList;
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

public class RoomListFg extends Fragment {
    private String applyUrl = Constant.URL_GIS;
    private LookDevice loolDeviceActivity;
    private ListView list_gis;

    private ParseJson mParseJson = new ParseJson();

    private String schoolid;
    private String buildingname;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case 0x2017:
                    String responseData = (msg.obj).toString();
                    final List<RoomList> listRoom = mParseJson.Json2Gis(responseData).getRoomLists();
                    if(listRoom!=null) {
                        list_gis.setAdapter(new RoomListAdapter(loolDeviceActivity,
                                listRoom, R.layout.base_list_item));
                        list_gis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                Intent intent = new Intent(loolDeviceActivity, DeviceInRoom.class);
                                intent.putExtra("schoolid",schoolid);
                                intent.putExtra("roomname", listRoom.get(position).getRoomname());
                                intent.putExtra("buildingname",buildingname);
                                startActivity(intent);
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
        LookDevice lookDevice=(LookDevice)getActivity();
        schoolid=getArguments().getString("schoolid");
        buildingname=getArguments().getString("buildingname");
        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list_gis = (ListView) view.findViewById(R.id.list_gis);
        OkHttp okHttp = new OkHttp(loolDeviceActivity, mHandler);

        RequestBody requestBody = new FormBody.Builder()
                .add("type", "3")
                .add("schoolid",schoolid)
                .add("buildingname",buildingname)
                .build();
        okHttp.postDataFromInternet(applyUrl, requestBody);
    }

}
