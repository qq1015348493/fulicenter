package cn.ucai.fulicenter.net;

import android.content.Context;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;

import cn.ucai.fulicenter.Bean.BoutiqueBean;
import cn.ucai.fulicenter.Bean.CategoryChildBean;
import cn.ucai.fulicenter.Bean.CategoryGroupBean;
import cn.ucai.fulicenter.Bean.CollectBean;
import cn.ucai.fulicenter.Bean.MessageBean;
import cn.ucai.fulicenter.Bean.NewGoodsBean;
import cn.ucai.fulicenter.Bean.Result;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.activity.CategoryGoods;
import cn.ucai.fulicenter.activity.MainActivity;
import cn.ucai.fulicenter.activity.Personal_Data;
import cn.ucai.fulicenter.activity.Register;
import cn.ucai.fulicenter.utils.MD5;
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
    public static void downloadBoutique(Context context, OkHttpUtils.OnCompleteListener<BoutiqueBean[]>listener){
        OkHttpUtils utils = new OkHttpUtils(context);
        utils.setRequestUrl(I.REQUEST_FIND_BOUTIQUES)
                .targetClass(BoutiqueBean[].class)
                .execute(listener);
    }
    public static void downloadBoutiqueGoods(Context context,int PageId,int CatId,OkHttpUtils.OnCompleteListener<NewGoodsBean[]>listener){
        OkHttpUtils utils = new OkHttpUtils(context);
        utils.setRequestUrl(I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam(I.NewAndBoutiqueGoods.CAT_ID,String.valueOf(CatId))
                .addParam(I.PAGE_ID,String.valueOf(PageId))
                .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(NewGoodsBean[].class)
                .execute(listener);
    }


    public static void  downloadGroupList(Context context,OkHttpUtils.OnCompleteListener<CategoryGroupBean[]>listener) {
        OkHttpUtils utils = new OkHttpUtils(context);
        utils.setRequestUrl(I.REQUEST_FIND_CATEGORY_GROUP)
                .targetClass(CategoryGroupBean[].class)
                .execute(listener);
    }

    public static void downloadChildList(Context context, int parentId, int pageid, OkHttpUtils.OnCompleteListener<CategoryChildBean[]> listener) {
        OkHttpUtils utils = new OkHttpUtils(context);
        utils.setRequestUrl(I.REQUEST_FIND_CATEGORY_CHILDREN)
                .addParam(I.CategoryChild.PARENT_ID,String.valueOf(parentId))
                .addParam(I.PAGE_ID,String.valueOf(pageid))
                .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(CategoryChildBean[].class)
                .execute(listener);
    }


    public static void downloadCategoryGoods(Context context, int pageId, int id, OkHttpUtils.OnCompleteListener<NewGoodsBean[]> listener) {
        OkHttpUtils utils = new OkHttpUtils(context);
        utils.setRequestUrl(I.REQUEST_FIND_GOODS_DETAILS)
                .addParam(I.Category.KEY_CAT_ID,String.valueOf(id))
                .addParam(I.PAGE_ID,String.valueOf(pageId))
                .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(NewGoodsBean[].class)
                .execute(listener);
   }

    public static void Login(Context context,String name, String password, OkHttpUtils.OnCompleteListener<String>listener) {
        OkHttpUtils<String> utils = new OkHttpUtils(context);
        utils.setRequestUrl(I.REQUEST_LOGIN)
                .addParam(I.User.USER_NAME,String.valueOf(name))
                .addParam(I.User.PASSWORD,MD5.getMessageDigest(password))
                .targetClass(String.class)
                .execute(listener);
    }

    public static void register(Context context, String user, String nick, String password, OkHttpUtils.OnCompleteListener<Result> listener) {
        OkHttpUtils<Result> utils = new OkHttpUtils(context);
        utils.setRequestUrl(I.REQUEST_REGISTER)
                .addParam(I.User.USER_NAME,user)
                .addParam(I.User.NICK,nick)
                .addParam(I.User.PASSWORD, MD5.getMessageDigest(password))
                .targetClass(Result.class)
                .post()
                .execute(listener);
    }

    public static void updateNick(Context context,String muserName, String input,OkHttpUtils.OnCompleteListener<String>listener) {
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_USER_NICK)
                .addParam(I.User.USER_NAME,muserName)
                .addParam(I.User.NICK,input)
                .targetClass(String.class)
                .execute(listener);
    }

    public static void updateAvatar(Context context,String username, File file,OkHttpUtils.OnCompleteListener<String>listener) {
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_AVATAR)
                .addParam(I.NAME_OR_HXID,username)
                .addParam(I.AVATAR_TYPE,"user_avatar")
                .addFile2(file)
                .targetClass(String.class)
                .post()
                .execute(listener);
    }

    public static void syncUser(Context context,String username,OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_USER)
                .addParam(I.User.USER_NAME,username)
                .targetClass(String.class)
                .execute(listener);
    }

    public static void downloadCollection(Context context, String muserName, int pageid, OkHttpUtils.OnCompleteListener<CollectBean[]> listener) {
        OkHttpUtils<CollectBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_COLLECTS)
                .addParam(I.Collect.USER_NAME,muserName)
                .addParam(I.PAGE_ID,pageid+"")
                .addParam(I.PAGE_SIZE,I.PAGE_SIZE_DEFAULT+"")
                .targetClass(CollectBean[].class)
                .execute(listener);

    }

    public static void getCollectionCount(Context context, String muserName, OkHttpUtils.OnCompleteListener<MessageBean> listener) {
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_COLLECT_COUNT)
                .addParam(I.Collect.USER_NAME,muserName)
                .targetClass(MessageBean.class)
                .execute(listener);

    }

    public static void deleteCollect(Context context, int cartID, String muserName,OkHttpUtils.OnCompleteListener<MessageBean> listener) {
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_DELETE_COLLECT)
                .addParam(I.Collect.GOODS_ID,cartID+"")
                .addParam(I.Collect.USER_NAME,muserName)
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    public static void CollectGoods(Context context, int goodsId, String muserName,OkHttpUtils.OnCompleteListener<MessageBean> listener) {
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_ADD_COLLECT)
                .addParam(I.Collect.GOODS_ID,goodsId+"")
                .addParam(I.Collect.USER_NAME,muserName)
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    public static void isCollect(Context context, int goodsId, String muserName,OkHttpUtils.OnCompleteListener<MessageBean> listener) {
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_IS_COLLECT)
                .addParam(I.Collect.GOODS_ID,goodsId+"")
                .addParam(I.Collect.USER_NAME,muserName)
                .targetClass(MessageBean.class)
                .execute(listener);

    }

    public static void addCart(Context context, int goodsId, String muserName, int input,OkHttpUtils.OnCompleteListener<MessageBean> listener) {
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_ADD_CART)
                .addParam(I.Cart.GOODS_ID,goodsId+"")
                .addParam(I.Cart.USER_NAME,muserName)
                .addParam(I.Cart.COUNT,input+"")
                .addParam(I.Cart.IS_CHECKED,"true")
                .targetClass(MessageBean.class)
                .execute(listener);
    }
}
