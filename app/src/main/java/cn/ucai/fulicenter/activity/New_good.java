package cn.ucai.fulicenter.activity;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.GridLayoutManager.DefaultSpanSizeLookup;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.OkHttpUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class New_good extends Fragment {
    NewGoodsAdapter mAdapter;
    ArrayList<NewGoodsBean> mList;
    GridLayoutManager gml;
    int mNewState;
    int mPageId = 1;
    int pagesize = 10;
    MainActivity mcontext;
    @Bind(R.id.jiazai)
    TextView jiazai;
    @Bind(R.id.RecyclerView)
    RecyclerView RecyclerView;

    @Bind(R.id.SwipeRefreshLayout)
    SwipeRefreshLayout SwipeRefreshLayout;

    public New_good() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mcontext = (MainActivity) getContext();
        View view = inflater.inflate(R.layout.fragment_new_good, container,false);
        ButterKnife.bind(this, view);



        L.i("New_good OncreateView");
        setListener();
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mList = new ArrayList<>();
        mAdapter = new NewGoodsAdapter(mcontext,mList);
        SwipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow)
        );
         gml = new GridLayoutManager(mcontext,I.COLUM_NUM);
        gml.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup(){

            @Override
            public int getSpanSize(int position) {
                return position == mAdapter.getItemCount()-1?2:1;
            }
        });
        RecyclerView.setLayoutManager(gml);
        RecyclerView.setHasFixedSize(true);
        RecyclerView.setAdapter(mAdapter);
    }
    private void setListener() {
        setPullUpListener();
        setPullDownListener();
    }
    //下拉
    private void setPullDownListener() {
        SwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SwipeRefreshLayout.setEnabled(true);
                SwipeRefreshLayout.setRefreshing(true);
                jiazai.setVisibility(View.VISIBLE);
                mPageId = 1;
                downloadNewGoods(I.ACTION_PULL_DOWN);
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
                lastPosition = gml.findLastVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastPosition >= mAdapter.getItemCount() - 1
                        && mAdapter.isMore()) {
                    //滚动结且列表已到最底部且还有更多数据可加载
                    mPageId++;
                    //下载下一页的数据
                    downloadNewGoods(I.ACTION_PULL_UP);
                }
                //停止拖拽，则通知系统回调Adapter.onBindViewHolder()，刷新RecyclerView
                if (newState != RecyclerView.SCROLL_STATE_DRAGGING) {
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void initData() {
        downloadNewGoods(I.ACTION_DOWNLOAD);
    }

    private void downloadNewGoods(final int action) {
        NetDao.downloadNewGoods(mcontext, mPageId, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                SwipeRefreshLayout.setRefreshing(false);
                jiazai.setVisibility(View.GONE);
                mAdapter.setMore(true);
                if(result!=null&&result.length>0){
                    ArrayList<NewGoodsBean> list = ConvertUtils.array2List(result);
                    if(action==I.ACTION_DOWNLOAD||action==I.ACTION_PULL_DOWN){
                        mAdapter.initOrRefreshList(list);
                    }else {
                        mAdapter.addList(list);
                    }

                    if(list.size()<I.PAGE_SIZE_DEFAULT){
                        mAdapter.setMore(false);
                    }
                }else{
                    mAdapter.setMore(false);
                }
            }

            @Override
            public void onError(String error) {
                SwipeRefreshLayout.setRefreshing(false);
                jiazai.setVisibility(View.GONE);
                CommonUtils.showShortToast(error);
                L.e("error"+error);

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
