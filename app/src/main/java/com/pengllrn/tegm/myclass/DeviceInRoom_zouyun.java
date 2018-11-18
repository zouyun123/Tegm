package com.pengllrn.tegm.myclass;

/**
 * Created by 0hasee on 2018/11/15.
 */

public class DeviceInRoom_zouyun {
    private String Devicenum;
    private String Typename;
    private boolean Useflag;
    private String Devicekind;

    public DeviceInRoom_zouyun(String Devicenum, String Typename, boolean Useflag, String Devicekind){
        this.Devicenum = Devicenum;
        this.Typename = Typename;
        this.Useflag = Useflag;
        this.Devicekind = Devicekind;
    }


    public String getDevicenum(){
        return Devicenum;
    }
    public String getTypename(){
        return Typename;
    }
    public boolean getUseflag(){
        return Useflag;
    }
    public String getDevicekind(){
        return Devicekind;
    }


}
