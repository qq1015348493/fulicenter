package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;

/**
 * Created by Administrator on 2016/10/19.
 */

public class BoutiqueGoods extends AppCompatActivity {
    @Bind(R.id.boutique_goods_jiazai)
    TextView boutiqueGoodsJiazai;
    @Bind(R.id.boutique_goods_recyclerView)
    RecyclerView boutiqueGoodsRecyclerView;
    @Bind(R.id.boutique_goods_SRL)
    SwipeRefreshLayout boutiqueGoodsSRL;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boutique_goods);
        ButterKnife.bind(this);
    }
}
