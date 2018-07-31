package com.pengllrn.tegm.bean;

/**
 * Author：Pengllrn
 * Date: 2018/6/12
 * Contact 897198177@qq.com
 * https://github.com/pengllrn
 */

public class AlarmList {
    private String oder;
    private String alarmtype;
    private String typename;
    private String room;
    private String lastupdate;
    private String schoolname;
    private String devnum;

    public AlarmList() {
    }

    public AlarmList(String oder, String alarmtype, String typename, String room, String lastupdate, String schoolname, String devnum) {
        this.oder = oder;
        this.alarmtype = alarmtype;
        this.typename = typename;
        this.room = room;
        this.lastupdate = lastupdate;
        this.schoolname = schoolname;
        this.devnum = devnum;
    }

    public String getOder() {
        return oder;
    }

    public String getAlarmtype() {
        return alarmtype;
    }

    public String getTypename() {
        return typename;
    }

    public String getRoom() {
        return room;
    }

    public String getLastupdate() {
        return lastupdate;
    }

    public String getSchoolname() {
        return schoolname;
    }

    public String getDevnum() {
        return devnum;
    }
}
