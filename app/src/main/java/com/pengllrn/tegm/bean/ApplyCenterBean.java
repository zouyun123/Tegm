package com.pengllrn.tegm.bean;

/**
 * Created by pengl on 2018/1/21.
 */

public class ApplyCenterBean {

    private String deviceid;
    private String devicenum;
    private String appliername;
    private String type;
    private String datetime;
    private String deal_status;

    public ApplyCenterBean() {
    }

    public ApplyCenterBean(String deviceid, String devicenum, String appliername, String type, String datetime, String deal_status) {
        this.deviceid = deviceid;
        this.devicenum = devicenum;
        this.appliername = appliername;
        this.type = type;
        this.datetime = datetime;
        this.deal_status = deal_status;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public String getDevicenum() {
        return devicenum;
    }

    public String getAppliername() {
        return appliername;
    }

    public String getType() {
        return type;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getDeal_status() {
        return deal_status;
    }
}
