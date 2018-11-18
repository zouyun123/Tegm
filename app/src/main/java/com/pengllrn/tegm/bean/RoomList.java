package com.pengllrn.tegm.bean;

/**
 * @author Administrator
 * @version $Rev$
 * @des ${UTODO}
 * @updateAuthor ${Author}$
 * @updateDate2017/11/20.
 */

public class RoomList {
    private String roomname;//实际存储楼栋名
    private String totaldevice;//实际存储房间名
    private String usingdevice;//实际存储房间号

    public RoomList(String schoolname, String buildingname, String room){
        roomname = schoolname;
        totaldevice = buildingname;
        usingdevice = room;
    }


    public String getRoomname() {
        return roomname;
    }

    public String getTotaldevice() {
        return totaldevice;
    }

    public String getUsingdevice() {
        return usingdevice;
    }
}
