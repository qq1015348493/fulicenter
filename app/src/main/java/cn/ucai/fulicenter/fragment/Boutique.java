package cn.ucai.fulicenter.fragment;


import android.app.DownloadManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.Bean.BoutiqueBean;
import cn.ucai.fulicenter.Bean.NewGoodsBean;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.MainActivity;
import cn.ucai.fulicenter.adapter.BoutiqueAdapter;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.OkHttpUtils;
import cn.ucai.fulicenter.views.SpaceItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class Boutique extends Fragment {
    LinearLayoutManager layoutManager;
    BoutiqueAdapter adapter;
    ArrayList<BoutiqueBean> mList;
    @Bind(R.id.Boutique_jiazai)
    TextView BoutiqueJiazai;
    @Bind(R.id.Boutique_recyclerView)
    RecyclerView BoutiqueRecyclerView;
    @Bind(R.id.Boutique_SRL)
    SwipeRefreshLayout BoutiqueSRL;
    MainActivity mcontext;
    public Boutique() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_boutique, container, false);
        ButterKnife.bind(this, view);
        mcontext= (MainActivity) getContext();

        initView();
        initData();
        setListener();
        return view;
    }

    private void setListener() {
        setPullUpListener();
        setPullDownListener();
    }

    private void setPullDownListener() {
        BoutiqueSRL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BoutiqueSRL.setEnabled(true);
                BoutiqueSRL.setRefreshing(true);
                BoutiqueJiazai.setVisibility(View.VISIBLE);
                DownloadBoutique(I.ACTION_PULL_DOWN);
            }
        });
    }

    private void setPullUpListener() {

        BoutiqueRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastPosition;
            int mNewState;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mNewState = newState;
                lastPosition = layoutManager.findLastVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastPosition >= adapter.getItemCount() - 1
                        && adapter.isMore()) {
                    DownloadBoutique(I.ACTION_PULL_UP);
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
        DownloadBoutique(I.ACTION_DOWNLOAD);
    }

    private void DownloadBoutique(final int action) {
        NetDao.downloadBoutique(mcontext, new OkHttpUtils.OnCompleteListener<BoutiqueBean[]>() {
            @Override
            public void onSuccess(BoutiqueBean[] result) {
                BoutiqueSRL.setRefreshing(false);
                BoutiqueJiazai.setVisibility(View.GONE);

                if(result!=null&&result.length>0){
                    ArrayList<BoutiqueBean> list = ConvertUtils.array2List(result);
                    switch (action){
                        case I.ACTION_DOWNLOAD:
                            adapter.initOrRefreshList(list);
                            adapter.setMore(true);
                            break;
                        case I.ACTION_PULL_DOWN:
                            adapter.initOrRefreshList(list);
                            adapter.setMore(true);
                            BoutiqueSRL.setRefreshing(false);
                            BoutiqueJiazai.setVisibility(View.GONE);
                            ImageLoader.release();
                            break;
                        case I.ACTION_PULL_UP:
                            adapter.addList(list);
                    }
                }else{
                    adapter.setMore(false);
                }
            }

            @Override
            public void onError(String error) {
                BoutiqueSRL.setRefreshing(false);
                CommonUtils.showShortToast(error);
                L.e("error"+error);
            }
        });
    }

    private void initView() {
        mList = new ArrayList<>();
        layoutManager = new LinearLayoutManager(mcontext);
        adapter = new BoutiqueAdapter(mcontext,mList);
        BoutiqueSRL.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow)
        );
        BoutiqueRecyclerView.addItemDecoration(new SpaceItemDecoration(12));
        BoutiqueRecyclerView.setLayoutManager(layoutManager);
        BoutiqueRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
