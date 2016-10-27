package cn.ucai.fulicenter.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    UserAvatar user = FuLiCenterApplication.getUser();
    ArrayList<CartBean> mList  ;
    CartAdapter adapter;
    LinearLayoutManager manager;
    int mNewState;
    int mPageId = 1;
    MainActivity mcontext;

    public Cart() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        ButterKnife.bind(this, view);
        mcontext = (MainActivity) getContext();
        manager = new LinearLayoutManager(mcontext);
        initView();
        initData();
        setListener();
        return view;
    }

    private void setListener() {
        setPullUpListener();
        setPullDownListener();
    }

    private void setPullDownListener() { SwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            SwipeRefreshLayout.setEnabled(true);
            SwipeRefreshLayout.setRefreshing(true);
            mPageId = 1;
            DownLoadCart(I.ACTION_PULL_DOWN);
        }
    });

    }

    private void setPullUpListener() {
        RecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastPosition;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mNewState = newState;
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastPosition >= adapter.getItemCount()) {
                    //滚动结且列表已到最底部且还有更多数据可加载
                    mPageId++;
                    //下载下一页的数据
                    DownLoadCart(I.ACTION_PULL_UP);
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
        DownLoadCart(I.ACTION_DOWNLOAD);
    }

    private void DownLoadCart(final int action) {
        NetDao.downloadcart(mcontext, user.getMuserName(), mPageId, new OkHttpUtils.OnCompleteListener<CartBean[]>() {
            @Override
            public void onSuccess(CartBean[] result) {
                SwipeRefreshLayout.setRefreshing(false);
                if(result!=null&&result.length>0){
                    ArrayList<CartBean> list = ConvertUtils.array2List(result);
                    if(action==I.ACTION_DOWNLOAD||action==I.ACTION_PULL_DOWN){
                        adapter.initData(list);
                    }else {
                        adapter.addData(list);
                    }
                }

            }

            @Override
            public void onError(String error) {

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
}
