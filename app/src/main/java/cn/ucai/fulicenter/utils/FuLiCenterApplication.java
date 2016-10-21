package cn.ucai.fulicenter.utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2016/10/17.
 */

public class FuLiCenterApplication extends Application{
    public static FuLiCenterApplication applicationContext ;
    private static  FuLiCenterApplication instance;
    public static String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public FuLiCenterApplication() {
        super.onCreate();
        applicationContext=this;
        instance=this;
    }

    public static FuLiCenterApplication getInstance() {
        if(instance == null){
                if(instance == null){
                    instance = new FuLiCenterApplication();
                }
        }
        return instance;
    }

}
