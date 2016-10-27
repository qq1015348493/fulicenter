package cn.ucai.fulicenter.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.ucai.fulicenter.Bean.AlbumsBean;
import cn.ucai.fulicenter.Bean.GoodsDetailsBean;
import cn.ucai.fulicenter.Bean.MessageBean;
import cn.ucai.fulicenter.Bean.UserAvatar;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.net.DetailsDao;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.FuLiCenterApplication;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.OkHttpUtils;
import cn.ucai.fulicenter.views.FlowIndicator;
import cn.ucai.fulicenter.views.SlideAutoLoopView;

/**
 * Created by Administrator on 2016/10/18.
 */

public class Goods_Details extends AppCompatActivity {
    int goodsId;
    Context context;
    String[] mAlbumImgUrl;
    UserAvatar user = FuLiCenterApplication.getUser();
    @Bind(R.id.goods_cart)
    ImageView goodsCart;
    @Bind(R.id.goods_collect)
    ImageView goodsCollect;
    @Bind(R.id.goods_share)
    ImageView goodsShare;
    @Bind(R.id.English_name)
    TextView EnglishName;
    @Bind(R.id.China_name)
    TextView ChinaName;
    @Bind(R.id.Goods_price)
    TextView GoodsPrice;
    @Bind(R.id.slideAutoLoopView)
    SlideAutoLoopView SlideAutoLoopView;
    @Bind(R.id.flowindicator)
    FlowIndicator flowindicator;
    @Bind(R.id.Goods_explain)
    EditText GoodsExplain;
    boolean collect = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_details);
        ButterKnife.bind(this);
        context = this;
        goodsId = getIntent().getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
        L.e("details", "goodsid" + goodsId);
        initView();
        initData();
    }

    private void initView() {
    }

    private void initData() {
        DetailsDao.downloadGoodsDetails(this, goodsId, new OkHttpUtils.OnCompleteListener<GoodsDetailsBean>() {
            @Override
            public void onSuccess(GoodsDetailsBean result) {
                if (result != null) {
                    EnglishName.setText(result.getGoodsEnglishName());
                    ChinaName.setText(result.getGoodsName());
                    GoodsPrice.setText(result.getCurrencyPrice());
                    GoodsExplain.setText(result.getGoodsBrief());
                    AlbumsBean[] albums = result.getProperties()[0].getAlbums();
                    mAlbumImgUrl = new String[albums.length];
                    for (int i = 0; i < albums.length; i++) {
                        mAlbumImgUrl[i] = albums[i].getImgUrl();
                    }
                    SlideAutoLoopView.startPlayLoop(flowindicator, mAlbumImgUrl, mAlbumImgUrl.length);
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @OnClick({R.id.goods_cart, R.id.goods_collect, R.id.goods_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.goods_cart:
                final EditText ed = new EditText(this);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("请输入你要购买的数量")
                        .setView(ed)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int input = Integer.valueOf(ed.getText().toString());
                                addCart(input);
                            }
                        }).setNegativeButton("取消",null).create().show();
                break;
            case R.id.goods_collect:
                if(FuLiCenterApplication.getUser()==null){
                    MFGT.gotoLogin((Activity) context);
                }else {
                    if(!collect){
                        NetDao.CollectGoods(context, goodsId, user.getMuserName(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                            @Override
                            public void onSuccess(MessageBean result) {
                                CommonUtils.showShortToast(result.getMsg());
                                goodsCollect.setImageResource(R.mipmap.bg_collect_out);
                                collect=!collect;
                            }

                            @Override
                            public void onError(String error) {

                            }
                        });
                    }else {
                        NetDao.deleteCollect(context, goodsId, user.getMuserName(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                            @Override
                            public void onSuccess(MessageBean result) {
                                CommonUtils.showShortToast(result.getMsg());
                                goodsCollect.setImageResource(R.mipmap.bg_collect_in);
                                collect=!collect;
                            }

                            @Override
                            public void onError(String error) {

                            }
                        });
                     }

                }
                break;
            case R.id.goods_share:
                showShare();
                break;
        }
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }


    private void addCart(int input) {
        NetDao.addCart(context, goodsId, user.getMuserName(), input, new OkHttpUtils.OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                CommonUtils.showShortToast(result.getMsg());
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(user!=null){
            isCollect();
        }
    }

    public void isCollect() {
        NetDao.isCollect(context, goodsId, user.getMuserName(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                collect=result.isSuccess();
                if(result.isSuccess()){
                    goodsCollect.setImageResource(R.mipmap.bg_collect_out);
                }else {
                    goodsCollect.setImageResource(R.mipmap.bg_collect_in);
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }
}
