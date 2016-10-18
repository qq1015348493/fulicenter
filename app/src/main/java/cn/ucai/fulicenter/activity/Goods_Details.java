package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.Bean.AlbumsBean;
import cn.ucai.fulicenter.Bean.GoodsDetailsBean;
import cn.ucai.fulicenter.Bean.PropertiesBean;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.net.DetailsDao;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.OkHttpUtils;
import cn.ucai.fulicenter.views.FlowIndicator;
import cn.ucai.fulicenter.views.SlideAutoLoopView;

/**
 * Created by Administrator on 2016/10/18.
 */

public class Goods_Details extends AppCompatActivity {
    int goodsId;
    @Bind(R.id.English_name)
    TextView EnglishName;
    @Bind(R.id.China_name)
    TextView ChinaName;
    @Bind(R.id.Goods_price)
    TextView GoodsPrice;
    @Bind(R.id.Goods_explain)
    TextView GoodsExplain;
    @Bind((R.id.flowindicator))
    FlowIndicator flowindicator;
    @Bind(R.id.slideAutoLoopView)
    SlideAutoLoopView SlideAutoLoopView;
    String[] mAlbumImgUrl;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_details);
        ButterKnife.bind(this);
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
                    for(int i=0;i<albums.length;i++){
                        mAlbumImgUrl[i]=albums[i].getImgUrl();
                    }
                    SlideAutoLoopView.startPlayLoop(flowindicator,mAlbumImgUrl,mAlbumImgUrl.length);
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }
}
