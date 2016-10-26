package cn.ucai.fulicenter.activity;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.Bean.CollectBean;
import cn.ucai.fulicenter.Bean.MessageBean;
import cn.ucai.fulicenter.Bean.NewGoodsBean;
import cn.ucai.fulicenter.Bean.UserAvatar;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.NewGoodsAdapter;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.FuLiCenterApplication;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.OkHttpUtils;
import cn.ucai.fulicenter.views.SpaceItemDecoration;

/**
 * Created by Administrator on 2016/10/26.
 */

public class Collection extends AppCompatActivity {

    MyAdapter mAdapter;
    ArrayList<CollectBean> mList;
    GridLayoutManager gml;
    Context mcontext;
    UserAvatar user =FuLiCenterApplication.getUser();
    int pageid=1;
    @Bind(R.id.collection_back)
    ImageView collectionBack;
    @Bind(R.id.collection_RecyclerView)
    RecyclerView RecyclerView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection);
        ButterKnife.bind(this);
        mcontext = this;
        initData();
        initView();
    }

    private void initData() {
        downloadCollection();
    }

    private void downloadCollection() {
        NetDao.downloadCollection(mcontext, user.getMuserName(), pageid, new OkHttpUtils.OnCompleteListener<CollectBean[]>() {
            @Override
            public void onSuccess(CollectBean[] result) {
                mAdapter.setMore(true);
                if (result != null) {
                    ArrayList<CollectBean> list = ConvertUtils.array2List(result);
                    mAdapter.initOrRefreshList(list);
                    if (list.size() < I.PAGE_SIZE_DEFAULT) {
                        mAdapter.setMore(false);
                    }
                } else {
                    mAdapter.setMore(false);
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void initView() {
        mList = new ArrayList<>();
        mAdapter = new MyAdapter(mcontext, mList);

        gml = new GridLayoutManager(mcontext, I.COLUM_NUM);
        gml.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            @Override
            public int getSpanSize(int position) {
                return position == mAdapter.getItemCount() - 1 ? 2 : 1;
            }
        });
        RecyclerView.setLayoutManager(gml);
        RecyclerView.setHasFixedSize(true);
        RecyclerView.setAdapter(mAdapter);
        RecyclerView.addItemDecoration(new SpaceItemDecoration(10));
    }








    class GoodsHolder extends RecyclerView.ViewHolder {
        ImageView delete;
        ImageView goods;
        TextView goodsname;
        @Bind(R.id.collection_L)
        LinearLayout Lin;
        @Bind(R.id.collection_LL)
        LinearLayout LLin;
        @OnClick({R.id.collection_iv,R.id.delete})
        public void onClick(View view){
            int cartId = (int) LLin.getTag();
            int posi = (int) Lin.getTag();
            switch (view.getId()){
                case R.id.collection_iv:
                    MFGT.gotoGoodsDetails((Activity) mcontext,cartId);
                    break;
                case R.id.delete:
                    L.i(posi+"--------"+cartId);
                    deleteCollect(cartId,posi);
                    break;
            }
        }
        public GoodsHolder(View itemView) {
            super(itemView);
            delete = (ImageView) itemView.findViewById(R.id.delete);
            goods = (ImageView) itemView.findViewById(R.id.collection_iv);
            goodsname = (TextView) itemView.findViewById(R.id.collection_goodsname);
            ButterKnife.bind(this, itemView);
        }
    }

    private void deleteCollect(int cartId, final int posi) {
        NetDao.deleteCollect(mcontext, cartId, user.getMuserName(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                if(result.isSuccess()){
                    mAdapter.delete(posi);
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    class FooterHolder extends RecyclerView.ViewHolder {
        TextView footer;

        public FooterHolder(View itemView) {
            super(itemView);
            footer = (TextView) itemView.findViewById(R.id.footer);
        }
    }
    private class MyAdapter extends RecyclerView.Adapter {
        Context context;
        ArrayList<CollectBean> mList;
        final int NEW_GOODS_TYPE = 0;
        final int FOOTER_HINT_TYPE = 1;
        boolean isMore;

        public MyAdapter(Context context, ArrayList<CollectBean> mList) {
            this.context = context;
            this.mList = mList;
        }

        public boolean isMore() {
            return isMore;
        }
        public void initOrRefreshList(ArrayList<CollectBean> list) {
            this.mList.addAll(list);
            notifyDataSetChanged();
        }
        public void setMore(boolean more) {
            isMore = more;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder holder = null;
            LayoutInflater from = LayoutInflater.from(context);
            View layout;
            switch (viewType) {
                case FOOTER_HINT_TYPE:
                    layout = from.inflate(R.layout.fooer, parent, false);
                    holder = new FooterHolder(layout);
                    break;
                case NEW_GOODS_TYPE:
                    layout = from.inflate(R.layout.collection_iteam,parent,false);
                    holder = new GoodsHolder(layout);
                    break;
            }

            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(position == mList.size()){
                FooterHolder footerholder = (FooterHolder) holder;
                if (isMore) {
                    footerholder.footer.setText("上拉加载更多");
                } else {
                    footerholder.footer.setText("没有跟多数据可以加载");
                }
                return;
            }
            CollectBean newgoods = mList.get(position);
            GoodsHolder goodsholder = (GoodsHolder) holder;
            goodsholder.goodsname.setText(newgoods.getGoodsName());
            goodsholder.delete.setImageResource(R.mipmap.delete);
            ImageLoader.downloadImg(context, goodsholder.goods, newgoods.getGoodsThumb(), true);
            goodsholder.Lin.setTag(position);
            goodsholder.LLin.setTag(newgoods.getGoodsId());
        }

        @Override
        public int getItemCount() {
            return mList == null ? 1 : mList.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == mList.size()) {
                return FOOTER_HINT_TYPE;
            }
            return NEW_GOODS_TYPE;
        }

        public void delete(int posi) {
            mList.remove(posi);
            notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }
}
