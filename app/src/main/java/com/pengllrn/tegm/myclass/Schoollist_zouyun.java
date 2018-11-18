package com.pengllrn.tegm.myclass;

/**
 * Created by 0hasee on 2018/11/9.
 */

import java.util.List;
//gson解析schoollist时需用到
public class Schoollist_zouyun {
    private int status;
    private List<School_zouyun>[] schoollist_zouyun;

    public int getStatus_zouyun(){
        return status;
    }
    public void setStatus_zouyun(int status){
        this.status = status;
    }

    public List<School_zouyun>[] getSchoollist_zouyun(){
        return schoollist_zouyun;
    }
    public void setSchoollist_zouyun(List<School_zouyun>[] schoollist_zouyun){
        this.schoollist_zouyun = schoollist_zouyun;
    }
}
