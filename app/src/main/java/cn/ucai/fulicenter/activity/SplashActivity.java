package cn.ucai.fulicenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.ucai.fulicenter.Bean.UserAvatar;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.dao.SharePrefrenceUtils;
import cn.ucai.fulicenter.dao.UserDao;
import cn.ucai.fulicenter.utils.FuLiCenterApplication;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;

/**
 * Created by Administrator on 2016/10/14.
 */

public class SplashActivity extends AppCompatActivity {

    private final long sleepTime=2000;
    SplashActivity mContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);
        mContext = this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                long costTime = System.currentTimeMillis() - start;
                if(sleepTime-costTime>0){
                    try{
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                UserAvatar user = FuLiCenterApplication.getUser();
                String username = SharePrefrenceUtils.getInstance(mContext).getUser();
                if(user==null&&username!=null){
                    UserDao dao = new UserDao(mContext);
                    user = dao.getUser(username);
                    L.i("SplashActivity  user="+user);
                    if(user!=null){
                        FuLiCenterApplication.setUser(user);
                    }
                }
               MFGT.gotoMainActivity(SplashActivity.this);
            }
        }).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MFGT.finish(SplashActivity.this);
    }
}
