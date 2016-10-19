package cn.ucai.fulicenter.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.Bean.BoutiqueBean;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.MFGT;

/**
 * Created by Administrator on 2016/10/19.
 */

public class BoutiqueAdapter extends Adapter {
    static Context context;
    ArrayList<BoutiqueBean> mList;
    boolean isMore;


    public BoutiqueAdapter(Context mcontext, ArrayList<BoutiqueBean> mList) {
        this.context=mcontext;
        this.mList=mList;
        mList.addAll(mList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if (viewType == I.TYPE_FOOTER) {
            holder = new NewGoodsAdapter.FootViewHolder(LayoutInflater.from(context).inflate(R.layout.fooer, parent, false));
        } else {
            holder = new BoutiqueViewHolder(LayoutInflater.from(context).inflate(R.layout.boutique_item, parent, false));
        }
        return holder;
    }

    public boolean isMore(){
        return isMore;
    }
    public void setMore( boolean isMore){
         isMore = isMore;
         notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == mList.size()) {
            //  mGridaLayoutManager.setSpanCount(2);
            NewGoodsAdapter.FootViewHolder footerViewHolder = (NewGoodsAdapter.FootViewHolder) holder;
            //通过isMore变量来判断是否有更多数据加载
            if (isMore) {
                footerViewHolder.foot.setText("上拉加载更多");
            } else {
                footerViewHolder.foot.setText("没有跟多数据可以加载");
            }
            return;
        }
        BoutiqueBean boutiqueBean = mList.get(position);
        BoutiqueViewHolder boutiqueAdapter = (BoutiqueViewHolder) holder;
        boutiqueAdapter.boutiqueTitle.setText(boutiqueBean.getTitle());
        boutiqueAdapter.boutiqueName.setText(boutiqueBean.getName());
        boutiqueAdapter.boutiqueDescription.setText(boutiqueBean.getDescription());
        boutiqueAdapter.boutiqueLinearlayout.setTag(boutiqueBean.getId());
        ImageLoader.downloadImg(context,boutiqueAdapter.boutiqueImage,boutiqueBean.getImageurl());
    }

    @Override
    public int getItemCount() {
        return mList!=null?mList.size()+1:1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==getItemCount()-1){
            return I.TYPE_FOOTER;
        }else {
            return I.TYPE_ITEM;
        }
    }

    public void initOrRefreshList(ArrayList<BoutiqueBean> list) {
        this.mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addList(ArrayList<BoutiqueBean> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    static class BoutiqueViewHolder extends ViewHolder{
        @Bind(R.id.boutique_Image)
        ImageView boutiqueImage;
        @Bind(R.id.boutique_title)
        TextView boutiqueTitle;
        @Bind(R.id.boutique_name)
        TextView boutiqueName;
        @Bind(R.id.boutique_description)
        TextView boutiqueDescription;
        @Bind(R.id.boutique_linearlayout)
        LinearLayout boutiqueLinearlayout;

        @OnClick(R.id.boutique_linearlayout)
        public void onClick(){
            int goodsId = (int) boutiqueLinearlayout.getTag();
            MFGT.gotoBoutiqueGoods((Activity) context,goodsId);
        }
        BoutiqueViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
