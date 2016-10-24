package cn.ucai.fulicenter.utils;

import android.app.Application;
import android.content.Context;

import cn.ucai.fulicenter.Bean.UserAvatar;

/**
 * Created by Administrator on 2016/10/17.
 */

public class FuLiCenterApplication extends Application{
    public static FuLiCenterApplication applicationContext ;
    private static  FuLiCenterApplication instance;
    public  static UserAvatar user ;

    public static UserAvatar getUser() {
        return user;
    }

    public static void setUser(UserAvatar user) {
        FuLiCenterApplication.user = user;
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
