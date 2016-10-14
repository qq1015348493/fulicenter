package cn.ucai.fulicenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.utils.MFGT;

/**
 * Created by Administrator on 2016/10/14.
 */

public class SplashActivity extends AppCompatActivity {

    private final long sleepTime=2000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);
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
