package cn.ucai.fulicenter.net;

import android.content.Context;

import cn.ucai.fulicenter.Bean.NewGoodsBean;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.utils.OkHttpUtils;

/**
 * Created by Administrator on 2016/10/17.
 */

public class NetDao {
    public static void downloadNewGoods(Context context, int PageId, OkHttpUtils.OnCompleteListener<NewGoodsBean[]>listener){
        OkHttpUtils utils = new OkHttpUtils(context);
        utils.setRequestUrl(I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam(I.NewAndBoutiqueGoods.CAT_ID,String.valueOf(I.CAT_ID))
                .addParam(I.PAGE_ID,String.valueOf(PageId))
                .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(NewGoodsBean[].class)
                .execute(listener);
    }
}
