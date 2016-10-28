package cn.ucai.fulicenter.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.Bean.CartBean;
import cn.ucai.fulicenter.Bean.UserAvatar;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.FuLiCenterApplication;
import cn.ucai.fulicenter.utils.OkHttpUtils;

/**
 * Created by Administrator on 2016/10/28.
 */

public class Order extends AppCompatActivity {
    Context context;

    @Bind(R.id.iv_order_back)
    ImageView ivOrderBack;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.et_address)
    EditText etAddress;
    @Bind(R.id.tv_order_count_num)
    TextView tvOrderCountNum;
    @Bind(R.id.bt_settlement)
    Button btSettlement;
    String id[] = new String[]{};
    String ids;
    ArrayList<CartBean> mList = new ArrayList<>();
    UserAvatar user = FuLiCenterApplication.getUser();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        context = this;
        initView();
        initData();
    }

    private void initData() {
        ids = getIntent().getStringExtra(I.Cart.ID);
        id = ids.split(",");
        DownLoadCart();
    }

    private void DownLoadCart() {
        NetDao.downloadcart(context, user.getMuserName(), new OkHttpUtils.OnCompleteListener<CartBean[]>() {
            @Override
            public void onSuccess(CartBean[] result) {
                if (result != null && result.length > 0) {
                    ArrayList<CartBean> list = ConvertUtils.array2List(result);
                    mList.addAll(list);
                    SumPrice();
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void SumPrice() {
        int savPrice = 0;
        if(mList!=null&&mList.size()>0){
            for(CartBean c : mList){
                if(c.isChecked()){
                    savPrice+=getPrice(c.getGoods().getRankPrice())*c.getCount();
                }
            }
            tvOrderCountNum.setText("￥"+Double.valueOf(savPrice));
        }
    }

    private int getPrice(String price) {
        price = price.substring(price.indexOf("￥")+1);
        return Integer.valueOf(price);
    }

    private void initView() {
    }

    @OnClick({R.id.iv_order_back, R.id.bt_settlement})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_order_back:
                break;
            case R.id.bt_settlement:
                if(etName==null){
                    etName.requestFocus();
                    CommonUtils.showShortToast("姓名不能为空");
                }else {
                    String name = etName.getText().toString();
                    if(etPhone==null){
                        etPhone.requestFocus();
                        CommonUtils.showShortToast("手机号不能为空");
                    }else {
                        String phone = etPhone.getText().toString();
                        if(etAddress==null){
                            etAddress.requestFocus();
                            CommonUtils.showShortToast("地址不能为空");
                        }else {
                            String address = etAddress.getText().toString();
                        }
                    }
                }
                break;
        }
    }
}
