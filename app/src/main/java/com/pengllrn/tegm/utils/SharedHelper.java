package com.pengllrn.tegm.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.pengllrn.tegm.bean.User;
import com.pengllrn.tegm.myclass.Person;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by pengl on 2018/1/15.
 */

public class SharedHelper {

    private Context mContext;

    public SharedHelper(){}

    public SharedHelper(Context mContext){
        this.mContext=mContext;
    }

    //定义一个保存数据的方法
    /**
     *
     */
    public void save(User user){
        SharedPreferences sp = mContext.getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("userid",user.getUserid());
        editor.putString("username",user.getUsername());
        editor.putBoolean("isOfficial",user.getIsOfficial());
        editor.putString("usertype",user.getUsertype());
        editor.putString("school",user.getSchool());
        editor.putString("email",user.getEmail());
        editor.putString("wechat",user.getWechat());
        editor.putString("qq",user.getRegist_time());
        editor.putString("regist_time",user.getQq());
        editor.putString("job",user.getJob());
        editor.putString("status","fine");
        editor.apply();
    }

    public void save(String userid,String username,String usertype,String school,
        String email,String wechat,String qq){
        SharedPreferences sp = mContext.getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("userid",userid);
        editor.putString("username",username);
        editor.putString("usertype",usertype);
        editor.putString("school",school);
        editor.putString("email",email);
        editor.putString("wechat",wechat);
        editor.putString("qq",qq);
        editor.putString("status","fine");
        editor.apply();
    }

    public String readbykey(String key){
        SharedPreferences sp = mContext.getSharedPreferences("user", MODE_PRIVATE);
        return sp.getString(key,"");
    }

    public void clear(){
        SharedPreferences sp = mContext.getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    public Boolean readOfficial(){
        SharedPreferences sp = mContext.getSharedPreferences("user", MODE_PRIVATE);
        return sp.getBoolean("isOfficial",false);
    }

    //zouyun保存Person类数据方法一
    public void savePerson1(Person person){
        SharedPreferences sp = mContext.getSharedPreferences("person", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("status", person.getStatus());
        editor.putInt("usertype", person.getUsertype());
        editor.putString("username", person.getUsername());
        editor.putString("loginid", person.getLoginid());
        editor.apply();

    }

    //zouyun保存Person类数据方法一
    public void savePerson2(int status, int usertype, String username, String loginid){
        SharedPreferences sp = mContext.getSharedPreferences("person", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("status", status);
        editor.putInt("usertype", usertype);
        editor.putString("username", username);
        editor.putString("loginid", loginid);
        editor.apply();

    }


    //zouyun读取status
    public int readStatus(String key){
        SharedPreferences sp = mContext.getSharedPreferences("person", MODE_PRIVATE);
        int status = sp.getInt("status", 100);
        return status;
    }

    //zouyun清空person
    public void clearPerson(){
        SharedPreferences pref = mContext.getSharedPreferences("person", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
}
