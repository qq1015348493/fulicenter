package cn.ucai.fulicenter.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.Bean.CartBean;
import cn.ucai.fulicenter.Bean.UserAvatar;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.MainActivity;
import cn.ucai.fulicenter.adapter.CartAdapter;
import cn.ucai.fulicenter.adapter.NewGoodsAdapter;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.FuLiCenterApplication;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.OkHttpUtils;
import cn.ucai.fulicenter.views.SpaceItemDecoration;

public class Cart extends Fragment {
    @Bind(R.id.cart_full)
    TextView Full;
    @Bind(R.id.heji)
    RelativeLayout Heji;
    @Bind(R.id.buy)
    TextView buy;
    @Bind(R.id.allprice)
    TextView allprice;
    @Bind(R.id.save)
    TextView save;
    @Bind(R.id.cart_RecyclerView)
    RecyclerView RecyclerView;
    @Bind(R.id.cart_SwipeRefreshLayout)
    SwipeRefreshLayout SwipeRefreshLayout;

    UserAvatar user;
    ArrayList<CartBean> mList  ;
    CartAdapter adapter;
    LinearLayoutManager manager;
    int mNewState;
    MainActivity mcontext;
    MyBroadcast broadcast;

    public Cart() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        user =  FuLiCenterApplication.getUser();
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        ButterKnife.bind(this, view);
        mcontext = (MainActivity) getContext();
        manager = new LinearLayoutManager(mcontext);
        ifFull(false);
        initView();
        initData();
        setListener();
        return view;
    }

    private void setListener() {
        setPullDownListener();
        IntentFilter filter = new IntentFilter(I.BROADCAST_UPDATA_CART);
        broadcast = new MyBroadcast();
        mcontext.registerReceiver(broadcast,filter);
    }

    private void SumPrice() {
        int SumPrice = 0;
        int savPrice = 0;
        if(mList!=null&&mList.size()>0){
            for(CartBean c : mList){
                if(c.isChecked()){
                    SumPrice+=getPrice(c.getGoods().getCurrencyPrice())*c.getCount();
                    savPrice+=getPrice(c.getGoods().getRankPrice())*c.getCount();
                }
            }
            allprice.setText("原价  ￥"+Double.valueOf(SumPrice));
            save.setText("现价  ￥"+Double.valueOf(savPrice));
        }else {
            allprice.setText("￥0");
            save.setText("￥0");
        }
    }

    private int getPrice(String price) {
        price = price.substring(price.indexOf("￥")+1);
        return Integer.valueOf(price);
    }


    private void ifFull(boolean full) {
        if (full){
            Full.setVisibility(View.GONE);
            RecyclerView.setVisibility(View.VISIBLE);
            Heji.setVisibility(View.VISIBLE);
        }else {
            Full.setVisibility(View.VISIBLE);
            RecyclerView.setVisibility(View.GONE);
            Heji.setVisibility(View.GONE);
        }
    }

    private void setPullDownListener() {
        SwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            SwipeRefreshLayout.setEnabled(true);
            SwipeRefreshLayout.setRefreshing(true);
            DownLoadCart(I.ACTION_PULL_DOWN);
        }
    });

    }


    private void initData() {
        DownLoadCart(I.ACTION_DOWNLOAD);
    }

    private void DownLoadCart(final int action) {
        NetDao.downloadcart(mcontext, user.getMuserName(),new OkHttpUtils.OnCompleteListener<CartBean[]>() {
            @Override
            public void onSuccess(CartBean[] result) {
                SwipeRefreshLayout.setRefreshing(false);
                if(result!=null&&result.length>0){
                    ArrayList<CartBean> list = ConvertUtils.array2List(result);
                    if(action==I.ACTION_DOWNLOAD||action==I.ACTION_PULL_DOWN){
                        mList.addAll(list);
                        adapter.initData(list);
                        ifFull(true);
                        mcontext.sendBroadcast(new Intent(I.BROADCAST_UPDATA_CART));
                    }
                }else {
                    ifFull(false);
                }

            }

            @Override
            public void onError(String error) {
                ifFull(false);
            }
        });
    }

    private void initView() {
        mList   = new ArrayList<>();
        adapter = new CartAdapter(mcontext,mList);
        L.i("manager:"+manager);
        RecyclerView.setLayoutManager(manager);
        RecyclerView.setAdapter(adapter);
        RecyclerView.addItemDecoration(new SpaceItemDecoration(12));
        RecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.buy)
    public void onClick() {

    }
    class MyBroadcast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            SumPrice();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(broadcast!=null){
            mcontext.unregisterReceiver(broadcast);
        }
    }
}
