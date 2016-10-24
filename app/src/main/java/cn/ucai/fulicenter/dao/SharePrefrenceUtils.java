package cn.ucai.fulicenter.dao;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/10/24.
 */

public class SharePrefrenceUtils {
    private static final String SHARE_NAME = "saveUserInfo";
    private static SharePrefrenceUtils instance;
    private SharedPreferences msharedPreferences;
    private SharedPreferences.Editor mEditor;
    private static final String SHARE_KEY_USER_NAME = "share_key_user_name";

    public SharePrefrenceUtils(Context context){
        msharedPreferences = context.getSharedPreferences(SHARE_NAME,Context.MODE_PRIVATE);
        mEditor = msharedPreferences.edit();
    }

    public static SharePrefrenceUtils getInstance(Context context){
        if(instance==null){
            instance = new SharePrefrenceUtils(context);
        }
        return instance;
    }

    public void saveUser(String username){
        mEditor.putString(SHARE_KEY_USER_NAME,username);
        mEditor.commit();
    }

    public String getUser(){
        return msharedPreferences.getString(SHARE_KEY_USER_NAME,null);
    }
}
