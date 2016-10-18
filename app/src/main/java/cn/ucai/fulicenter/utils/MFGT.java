package cn.ucai.fulicenter.utils;

import android.app.Activity;
import android.content.Intent;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.Goods_Details;
import cn.ucai.fulicenter.activity.MainActivity;


public class MFGT {
    public static void finish(Activity activity){
        activity.finish();
        activity.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
    public static void gotoMainActivity(Activity context){
        startActivity(context, MainActivity.class);
    }
    public static void startActivity(Activity context,Class<?> cls){
        Intent intent = new Intent();
        intent.setClass(context,cls);
        startActivity(context,intent);
    }
    public static void gotoGoodsDetails(Activity context,int goodsId){
        Intent intent = new Intent();
        intent.setClass(context, Goods_Details.class);
        intent.putExtra(I.GoodsDetails.KEY_GOODS_ID,goodsId);
        startActivity(context,intent);
    }

    public static void startActivity(Activity context,Intent intent){
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
}
