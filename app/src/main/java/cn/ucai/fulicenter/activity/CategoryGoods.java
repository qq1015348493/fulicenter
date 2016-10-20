package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;

/**
 * Created by Administrator on 2016/10/20.
 */

public class CategoryGoods extends AppCompatActivity {
    @Bind(R.id.category_goods_back)
    ImageView categoryGoodsBack;
    @Bind(R.id.category_goods_name)
    TextView categoryGoodsName;
    @Bind(R.id.category_goods_arrow2)
    ImageView categoryGoodsArrow2;
    @Bind(R.id.category_goods_price)
    Button categoryGoodsPrice;
    @Bind(R.id.category_goods_time)
    Button categoryGoodsTime;
    @Bind(R.id.category_goods_RecyclerView)
    RecyclerView categoryGoodsRecyclerView;
    @Bind(R.id.category_goods_SRL)
    SwipeRefreshLayout categoryGoodsSRL;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_goods);
        ButterKnife.bind(this);
    }
}
