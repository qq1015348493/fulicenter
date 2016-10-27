package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemLongClick;
import butterknife.OnLongClick;
import cn.ucai.fulicenter.Bean.CartBean;
import cn.ucai.fulicenter.Bean.MessageBean;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.OkHttpUtils;
import cn.ucai.fulicenter.utils.ResultUtils;

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
        this.mList.addAll(list);
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
        CartBean cart = mList.get(position);
        CartHolder cartHolder = (CartHolder) holder;
        cartHolder.cartCount.setText(cart.getCount()+"");
        cartHolder.cartName.setText(cart.getGoods().getGoodsName());
        cartHolder.cartPrice.setText(cart.getGoods().getCurrencyPrice());
        goodsid = cart.getGoodsId();
        ImageLoader.downloadImg(context,cartHolder.cartIv,cart.getGoods().getGoodsThumb());
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
        @Bind(R.id.cart_RelativeLayout)
        RelativeLayout RelativeLayout;
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
            switch (v.getId()){
                case R.id.addCount:
                    Count = Integer.valueOf(cartCount.getText().toString());
                    Count+=1;
                    cartCount.setText(Count+"");
                    updateCart(Count);
                    break;
                case R.id.delCount:
                    Count = Integer.valueOf(cartCount.getText().toString());
                    Count-=1;
                    cartCount.setText(Count+"");
                    updateCart(Count);
                    break;
            }
        }

        @OnLongClick(R.id.cart_RelativeLayout)
        public boolean onLongClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("是否取消收藏").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteCart(getPosition());
                }
            }).setNegativeButton("取消", null).create().show();
            return true;
        }
    }

    private void updateCart(int count) {
        NetDao.updateCart(context, goodsid, count,new OkHttpUtils.OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                CommonUtils.showShortToast(result.getMsg());
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void deleteCart(final int p) {
            NetDao.deleteCart(context, goodsid, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    CommonUtils.showShortToast(result.getMsg());
                    mList.remove(p);
                }

                @Override
                public void onError(String error) {

                }
            });
        }
}
