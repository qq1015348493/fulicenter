package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
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
    NewGoodsAdapter adapter;
    ArrayList<NewGoodsBean> mList;
    GridLayoutManager manager;
    int pageId= 1;
    int id;
    String name;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_goods);
        ButterKnife.bind(this);
        id = getIntent().getIntExtra(I.Category.KEY_ID,0);
        name = getIntent().getStringExtra(I.Category.KEY_NAME);
        initView();
        initData();
    }

    private void initData() {
        downloadCategoryGoods();
    }

    private void downloadCategoryGoods() {
        NetDao.downloadCategoryGoods(this, pageId, id, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                adapter.setMore(true);
                if(result!=null){
                    ArrayList<NewGoodsBean> list = ConvertUtils.array2List(result);
                    adapter.initOrRefreshList(list);
                    if(list.size()<I.PAGE_SIZE_DEFAULT){
                        adapter.setMore(false);
                    }
                }else {
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
        adapter = new NewGoodsAdapter(this,mList);
        manager = new GridLayoutManager(this,I.COLUM_NUM);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup(){

            @Override
            public int getSpanSize(int position) {
                return position == adapter.getItemCount()-1?2:1;
            }
        });
        categoryGoodsRecyclerView.setAdapter(adapter);
        categoryGoodsRecyclerView.setHasFixedSize(true);//自动修复
        categoryGoodsRecyclerView.setLayoutManager(manager);
        categoryGoodsRecyclerView.addItemDecoration(new SpaceItemDecoration(12));
        categoryGoodsName.setText(name);
    }
}
