package com.pengllrn.tegm.bean;

/**
 * @author Administrator
 * @version $Rev$
 * @des ${UTODO}
 * @updateAuthor ${Author}$
 * @updateDate2017/9/19.
 */

public class Device {
    private String DeviceId = "";
    private String DeviceNum = "";
    private String typename = "";
    private String BuildName = "";
    private String RoomName = "";
    private String OrderNum = "";
    private String UseFlag = "";
    private String imgUrl = "";

    public Device(String deviceNum, String deviceType, String roomName, String orderNum, String useFlag,String imgUrl,String deviceId) {
        this.DeviceNum = deviceNum;
        this.typename = deviceType;
        RoomName = roomName;
        OrderNum = orderNum;
        UseFlag = useFlag;
        this.imgUrl = imgUrl;
        this.DeviceId = deviceId;
    }

    public String getDeviceNum() {
        return DeviceNum;
    }

    public String getDeviceType() {
        return typename;
    }

    public String getRoomName() {
        return RoomName;
    }

    public String getOrderNum() {
        return OrderNum;
    }

    public String getUseFlag() {
        return UseFlag;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public String getBuildName() {
        return BuildName;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }
}
