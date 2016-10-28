package cn.ucai.fulicenter.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import cn.ucai.fulicenter.Bean.CategoryChildBean;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.BoutiqueGoods;
import cn.ucai.fulicenter.activity.CategoryGoods;
import cn.ucai.fulicenter.activity.Goods_Details;
import cn.ucai.fulicenter.activity.Login;
import cn.ucai.fulicenter.activity.MainActivity;
import cn.ucai.fulicenter.activity.Order;


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
    public static void startActivityforResult(Activity context,Intent intent,int requestCode){
        context.startActivityForResult(intent,requestCode);
        context.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
    public static void gotoLogin(Activity context){
        Intent intent = new Intent();
        intent.setClass(context,Login.class);
        startActivityforResult(context,intent,I.REQUEST_CODE_LOGIN);
    }

    public static void gotoGoodsDetails(Activity context,int goodsId){
        Intent intent = new Intent();
        intent.setClass(context, Goods_Details.class);
        intent.putExtra(I.GoodsDetails.KEY_GOODS_ID,goodsId);
        startActivity(context,intent);
    }
    public static void gotoBoutiqueGoods(Activity context,int catid){
        Intent intent = new Intent();
        intent.setClass(context, BoutiqueGoods.class);
        intent.putExtra(I.Boutique.CAT_ID,catid);
        startActivity(context,intent);
    }
    public static void startActivity(Activity context,Intent intent){
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }

    public static void gotoCategoryGoods(Activity context, int id, String name, ArrayList<CategoryChildBean> list) {
        Intent intent = new Intent();
        intent.setClass(context, CategoryGoods.class);
        intent.putExtra(I.CategoryGroup.NAME,name);
        intent.putExtra(I.CategoryChild.CAT_ID,id);
        intent.putExtra(I.CategoryChild.ID,list);
        startActivity(context,intent);
    }

    public static void goBuy(Activity context,String cartIds){
        Intent intent = new Intent(context, Order.class).putExtra(I.Cart.ID,cartIds);
        startActivity(context,intent);
    }
}
