package cn.ucai.fulicenter.net;

import android.content.Context;

import cn.ucai.fulicenter.Bean.GoodsDetailsBean;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.utils.OkHttpUtils;

/**
 * Created by Administrator on 2016/10/18.
 */

public class DetailsDao {
    public static void downloadGoodsDetails(Context context, int goodsid, OkHttpUtils.OnCompleteListener<GoodsDetailsBean>listener){
        OkHttpUtils utils = new OkHttpUtils(context);
        utils.setRequestUrl(I.REQUEST_FIND_GOOD_DETAILS)
                .addParam(I.GoodsDetails.KEY_GOODS_ID,String.valueOf(goodsid))
                .targetClass(GoodsDetailsBean.class)
                .execute(listener);
    }
}
