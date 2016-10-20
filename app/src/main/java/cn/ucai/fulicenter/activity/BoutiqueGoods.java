package cn.ucai.fulicenter.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.Bean.NewGoodsBean;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.NewGoodsAdapter;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.OkHttpUtils;
import cn.ucai.fulicenter.views.SpaceItemDecoration;

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
    ArrayList<NewGoodsBean> mList;
    GridLayoutManager manager;
    int pageId= 1;
    NewGoodsAdapter adapter;

    int goodsId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boutique_goods);
        ButterKnife.bind(this);
        Intent intent = new Intent();
        goodsId = getIntent().getIntExtra(I.Boutique.CAT_ID,0);
        L.i(goodsId+"");
        initView();
        initData();
        setListener();
    }

    private void setListener() {
        setPullUpListener();
        setPullDownListener();
    }

    private void setPullDownListener() {
        boutiqueGoodsSRL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                boutiqueGoodsSRL.setEnabled(true);
                boutiqueGoodsSRL.setRefreshing(true);
                boutiqueGoodsJiazai.setVisibility(View.VISIBLE);
                pageId = 1;
                downloadBoutiqueGoods(I.ACTION_PULL_DOWN);
            }
        });
    }

    private void setPullUpListener() {
        boutiqueGoodsRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastPosition;
            int mNewState;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mNewState = newState;
                lastPosition = manager.findLastVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastPosition >= adapter.getItemCount() - 1
                        && adapter.isMore()) {
                    //滚动结且列表已到最底部且还有更多数据可加载
                    pageId++;
                    //下载下一页的数据
                    downloadBoutiqueGoods(I.ACTION_PULL_UP);
                }
                //停止拖拽，则通知系统回调Adapter.onBindViewHolder()，刷新RecyclerView
                if (newState != RecyclerView.SCROLL_STATE_DRAGGING) {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void initData() {
        downloadBoutiqueGoods(I.ACTION_DOWNLOAD);
    }

    private void downloadBoutiqueGoods(final int action) {
        NetDao.downloadBoutiqueGoods(this, pageId,goodsId,new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                boutiqueGoodsSRL.setRefreshing(false);
                boutiqueGoodsJiazai.setVisibility(View.GONE);
                adapter.setMore(true);
                if(result!=null&&result.length>0){
                    ArrayList<NewGoodsBean> list = ConvertUtils.array2List(result);
                    if(action==I.ACTION_DOWNLOAD||action==I.ACTION_PULL_DOWN){
                        adapter.initOrRefreshList(list);
                    }else {
                        adapter.addList(list);
                    }

                    if(list.size()<I.PAGE_SIZE_DEFAULT){
                        adapter.setMore(false);
                    }
                }else{
                    adapter.setMore(false);
                }
            }

            @Override
            public void onError(String error) {
                boutiqueGoodsSRL.setRefreshing(false);
                boutiqueGoodsJiazai.setVisibility(View.GONE);
                CommonUtils.showShortToast(error);
                L.e("error"+error);

            }
        });
    }

    private void initView() {
        mList = new ArrayList<>();
        adapter = new NewGoodsAdapter(this,mList);
        boutiqueGoodsSRL.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow)
        );
        manager = new GridLayoutManager(this,I.COLUM_NUM);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup(){

            @Override
            public int getSpanSize(int position) {
                return position == adapter.getItemCount()-1?2:1;
            }
        });
        boutiqueGoodsRecyclerView.setAdapter(adapter);
        boutiqueGoodsRecyclerView.setHasFixedSize(true);//自动修复
        boutiqueGoodsRecyclerView.setLayoutManager(manager);
        boutiqueGoodsRecyclerView.addItemDecoration(new SpaceItemDecoration(12));
    }
}
