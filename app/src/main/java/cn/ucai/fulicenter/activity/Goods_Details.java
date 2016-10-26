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
                        }).setPositiveButton("取消",null).create().show();
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
                break;
        }
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
