package cn.ucai.fulicenter.activity;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.Bean.CategoryChildBean;
import cn.ucai.fulicenter.Bean.NewGoodsBean;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.NewGoodsAdapter;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.OkHttpUtils;
import cn.ucai.fulicenter.views.SpaceItemDecoration;

/**
 * Created by Administrator on 2016/10/20.
 */

public class CategoryGoods extends AppCompatActivity {
    NewGoodsAdapter adapter;
    ArrayList<NewGoodsBean> mList;
    GridLayoutManager manager;
    int pageId = 1;
    int id;
    String name;

    boolean priceAsc = true;
    boolean addTimeAsc = false;
    int sortBy = I.SORT_BY_ADDTIME_DESC;
    @Bind(R.id.category_goods_back)
    ImageView categoryGoodsBack;
    @Bind(R.id.backClickArea)
    LinearLayout backClickArea;
    @Bind(R.id.btnCatChildFilter)
    cn.ucai.fulicenter.views.CatChildFilterButton btnCatChildFilter;
    @Bind(R.id.category_goods_price)
    Button categoryGoodsPrice;
    @Bind(R.id.category_goods_time)
    Button categoryGoodsTime;
    @Bind(R.id.category_goods_RecyclerView)
    RecyclerView categoryGoodsRecyclerView;
    @Bind(R.id.category_goods_SRL)
    SwipeRefreshLayout categoryGoodsSRL;
    ArrayList<CategoryChildBean> mChildlist;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_goods);
        ButterKnife.bind(this);
        id = getIntent().getIntExtra(I.CategoryChild.CAT_ID, 0);
        name = getIntent().getStringExtra(I.CategoryGroup.NAME);
        mChildlist = (ArrayList<CategoryChildBean>) getIntent().getSerializableExtra(I.CategoryChild.ID);
        btnCatChildFilter.setText(name);
        initView();
        initData();
    }


    private void initData() {
        downloadCategoryGoods();
        btnCatChildFilter.setOnCatFilterClickListener(name,mChildlist);
    }

    private void downloadCategoryGoods() {
        NetDao.downloadCategoryGoods(this, pageId, id, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                adapter.setMore(true);
                if (result != null) {
                    ArrayList<NewGoodsBean> list = ConvertUtils.array2List(result);
                    adapter.initOrRefreshList(list);
                    if (list.size() < I.PAGE_SIZE_DEFAULT) {
                        adapter.setMore(false);
                    }
                } else {
                    adapter.setMore(false);
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void initView() {
        mList = new ArrayList<>();
        adapter = new NewGoodsAdapter(this, mList);
        manager = new GridLayoutManager(this, I.COLUM_NUM);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            @Override
            public int getSpanSize(int position) {
                return position == adapter.getItemCount() - 1 ? 2 : 1;
            }
        });
        categoryGoodsRecyclerView.setAdapter(adapter);
        categoryGoodsRecyclerView.setHasFixedSize(true);//自动修复
        categoryGoodsRecyclerView.setLayoutManager(manager);
        categoryGoodsRecyclerView.addItemDecoration(new SpaceItemDecoration(12));
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @OnClick({R.id.category_goods_price, R.id.category_goods_time})
    public void onClick(View view) {
        Drawable right;
        switch (view.getId()) {
            case R.id.category_goods_price:
                if (priceAsc) {
                    sortBy = I.SORT_BY_PRICE_ASC;
                    right = getResources().getDrawable(R.mipmap.arrow_order_up);
                } else {
                    sortBy = I.SORT_BY_PRICE_DESC;
                    right = getResources().getDrawable(R.mipmap.arrow_order_down);
                }
                right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicWidth());
                categoryGoodsPrice.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, right, null);
                priceAsc = !priceAsc;
                break;
            case R.id.category_goods_time:
                if (addTimeAsc) {
                    sortBy = I.SORT_BY_ADDTIME_ASC;
                    right = getResources().getDrawable(R.mipmap.arrow_order_up);
                } else {
                    sortBy = I.SORT_BY_ADDTIME_DESC;
                    right = getResources().getDrawable(R.mipmap.arrow_order_down);
                }
                right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicWidth());
                categoryGoodsTime.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, right, null);
                addTimeAsc = !addTimeAsc;
                break;
        }
        adapter.setSoryBy(sortBy);
    }
}
