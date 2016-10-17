package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.Bean.NewGoodsBean;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;

/**
 * Created by Administrator on 2016/10/17.
 */

public class GoodsAdapter extends RecyclerView.Adapter {
    Context context;
    List<NewGoodsBean> mList;

    public GoodsAdapter(Context context, List<NewGoodsBean> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = null;
        if (viewType == I.TYPE_FOOTER) {
            holder = new FooterViewHolder(View.inflate(context, R.layout.fooer, null));
        } else {
            holder = new GoodsViewHolder(View.inflate(context, R.layout.new_goods, null));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position)==I.TYPE_FOOTER){
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            footerViewHolder.footer.setText("没有更多数据");
        }else {
            GoodsViewHolder viewHolder = (GoodsViewHolder) holder;
            NewGoodsBean goods = mList.get(position);
            viewHolder.goodname.setText(goods.getGoodsName());
            viewHolder.goodprice.setText(goods.getCurrencyPrice());
            viewHolder.goodiv.setImageResource(R.drawable.nopic);
        }
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() + 1 : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        } else {
            return I.TYPE_ITEM;
        }
    }

    static class FooterViewHolder extends ViewHolder {
        @Bind(R.id.footer)
        TextView footer;

        FooterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class GoodsViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.goodiv)
        ImageView goodiv;
        @Bind(R.id.goodname)
        TextView goodname;
        @Bind(R.id.goodprice)
        TextView goodprice;

        GoodsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
