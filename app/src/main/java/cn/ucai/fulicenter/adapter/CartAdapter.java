package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import cn.ucai.fulicenter.Bean.CartBean;
import cn.ucai.fulicenter.Bean.MessageBean;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.OkHttpUtils;

/**
 * Created by Administrator on 2016/10/27.
 */

public class CartAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<CartBean> mList;
    int goodsid;
    int Count;
    public CartAdapter(Context context, ArrayList<CartBean> mList) {
        this.context = context;
        this.mList = new ArrayList<>();
        this.mList.addAll(mList);
    }

    public void initData(ArrayList<CartBean> list){
        mList.clear();
        this.mList=list;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<CartBean> list){
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        LayoutInflater from = LayoutInflater.from(context);
        View layout = from.inflate(R.layout.cart_iteam, parent, false);
        holder = new CartHolder(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final CartBean cart = mList.get(position);
        final CartHolder cartHolder = (CartHolder) holder;
        if(cart.isChecked()){
            cartHolder.cartChecked.setChecked(true);
        }
        cartHolder.cartCount.setText(cart.getCount()+"");
        cartHolder.cartName.setText(cart.getGoods().getGoodsName());
        cartHolder.cartPrice.setText(cart.getGoods().getCurrencyPrice());
        ImageLoader.downloadImg(context,cartHolder.cartIv,cart.getGoods().getGoodsThumb());
        cartHolder.cartChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cart.setChecked(isChecked);
                context.sendBroadcast(new Intent(I.BROADCAST_UPDATA_CART));
                updateCart(context,cart.getCount(),cart.getId(),cart.isChecked());
            }
        });
    }
    @Override
    public int getItemCount() {
        return mList == null? 0 : mList.size();
    }

    class CartHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.cart_checked)
        CheckBox cartChecked;
        @Bind(R.id.cart_iv)
        ImageView cartIv;
        @Bind(R.id.cart_name)
        TextView cartName;
        @Bind(R.id.addCount)
        ImageView addCount;
        @Bind(R.id.cart_Count)
        TextView cartCount;
        @Bind(R.id.cart_LinearLayout)
        LinearLayout LinearLayout;
        @Bind(R.id.delCount)
        ImageView delCount;
        @Bind(R.id.cart_price)
        TextView cartPrice;
        CartHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.addCount,R.id.delCount})
        public void onClick(View v){
            CartBean cartBean = mList.get(getPosition());
            switch (v.getId()){
                case R.id.addCount:
                    goodsid = cartBean.getId();
                    Count = cartBean.getCount();
                    Count+=1;
                    cartBean.setCount(Count);
                    cartCount.setText(Count+"");
                    context.sendBroadcast(new Intent(I.BROADCAST_UPDATA_CART));
                    updateCart(context, Count,goodsid, cartBean.isChecked());
                    notifyDataSetChanged();
                    break;
                case R.id.delCount:
                    goodsid = cartBean.getId();
                    Count = cartBean.getCount();
                    Count-=1;

                    cartBean.setCount(Count);
                    if(Count>0){
                        context.sendBroadcast(new Intent(I.BROADCAST_UPDATA_CART));
                        updateCart(context, Count,goodsid, cartBean.isChecked());
                        notifyDataSetChanged();
                    }
                    else {
                        context.sendBroadcast(new Intent(I.BROADCAST_UPDATA_CART));
                        deleteCart(getPosition(),goodsid);
                        notifyDataSetChanged();
                    }
                    break;

            }
        }

        @OnLongClick(R.id.cart_LinearLayout)
        public boolean onLongClick(View v) {
            goodsid = mList.get(getPosition()).getId();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("是否取消收藏").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteCart(getPosition(),goodsid);
                }
            }).setNegativeButton("取消", null).create().show();
            return true;
        }
    }

    private void updateCart(Context context, int count, int goodsid, boolean checked) {
        NetDao.updateCart(this.context, goodsid, count,checked,new OkHttpUtils.OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                CommonUtils.showShortToast(result.getMsg());
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void deleteCart(final int p,int goodsid) {
            NetDao.deleteCart(context, goodsid, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    CommonUtils.showShortToast(result.getMsg());
                    mList.get(p).setCount(0);
                    mList.remove(p);
                    notifyDataSetChanged();
                    context.sendBroadcast(new Intent(I.BROADCAST_UPDATA_CART));

                }

                @Override
                public void onError(String error) {

                }
            });
        }

}
