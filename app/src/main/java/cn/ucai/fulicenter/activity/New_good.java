package cn.ucai.fulicenter.activity;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.ucai.fulicenter.Bean.NewGoodsBean;
import cn.ucai.fulicenter.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class New_good extends Fragment {
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView jiazai;
    LinearLayoutManager manager;
    ArrayList<NewGoodsBean> newGoods = new ArrayList<>();
    MyAdapter adapter ;

    static final int ACTION_DOWN = 0;
    static final int ACTION_PULL_DOWN = 1;
    static final int ACTION_PULL_UP = 2;

    public New_good() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_good,container);
        recyclerView = (RecyclerView) view.findViewById(R.id.RecyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.SwipeRefreshLayout);
        jiazai = (TextView) view.findViewById(R.id.jiazai);
        recyclerView.setLayoutManager(manager);
        return inflater.inflate(R.layout.fragment_new_good, container, false);
    }

    @Override
    public void onAttach(Context context) {
        manager = new LinearLayoutManager(context,2,false);
        adapter = new MyAdapter(context,newGoods);
        initData();
        super.onAttach(context);
    }

    private void initData() {
    }

    private class MyAdapter extends RecyclerView.Adapter{
        Context context;
        ArrayList<NewGoodsBean> newGoods;

        public MyAdapter(Context context, ArrayList<NewGoodsBean> newGoods) {
            this.context=context;
            this.newGoods=newGoods;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
}
