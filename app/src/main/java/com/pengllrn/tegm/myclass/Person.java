package com.pengllrn.tegm.myclass;

/**
 * Created by 0hasee on 2018/11/6.
 */

public class Person {
    private int status;
    private int usertype;
    private String username;
    private String loginid;

    public int getStatus(){
        return status;
    }
    public int getUsertype(){
        return usertype;
    }
    public String getUsername(){
        return username;
    }

    public void setStatus(int status){
        this.status = status;
    }
    public void setUsertype(int usertype){
        this.usertype = usertype;
    }
    public void setUsername(String username){
        this.username = username;
    }

    public String getLoginid(){
        return loginid;
    }
    public void setLoginid(String loginid){
        this.loginid = loginid;
    }

}
