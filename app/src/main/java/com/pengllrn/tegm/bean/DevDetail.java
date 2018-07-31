package com.pengllrn.tegm.bean;

import java.util.List;

/**
 * @author Administrator
 * @version $Rev$
 * @des ${UTODO}
 * @updateAuthor ${Author}$
 * @updateDate2017/10/5.
 */

public class DevDetail {
    private String DeviceId;
    private String typename;
    private String DeviceNum;
    private String UseFlag;

    private String schoolname;
    private String Room_Building;
    private String Room_Roomname;
    private String OrderNum;
    private String usedepart;

    private int status;

    private List<UseRecord> deviceUseRecord;

    private String devicekind;
    private String description;
    private String configureinfo;

    public String getDeviceId() {
        return DeviceId;
    }

    public String getTypename() {
        return typename;
    }

    public String getDeviceNum() {
        return DeviceNum;
    }

    public String getUseFlag() {
        return UseFlag;
    }

    public String getSchoolname() {
        return schoolname;
    }

    public String getRoom_Building() {
        return Room_Building;
    }

    public String getRoom_Roomname() {
        return Room_Roomname;
    }

    public String getOrderNum() {
        return OrderNum;
    }

    public String getUsedepart() {
        return usedepart;
    }

    public int getStatus() {
        return status;
    }

    public List<UseRecord> getDeviceUseRecord() {
        return deviceUseRecord;
    }

    public String getDevicekind() {
        return devicekind;
    }

    public String getDescription() {
        return description;
    }

    public String getConfigureinfo() {
        return configureinfo;
    }
}
