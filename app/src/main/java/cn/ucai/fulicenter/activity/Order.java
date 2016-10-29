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

import com.pingplusplus.android.PingppLog;
import com.pingplusplus.libone.PaymentHandler;
import com.pingplusplus.libone.PingppOne;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.OkHttpUtils;

/**
 * Created by Administrator on 2016/10/28.
 */

public class Order extends AppCompatActivity implements PaymentHandler{
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
    int price;
    private static String URL = "http://218.244.151.190/demo/charge";

    ArrayList<CartBean> mList = new ArrayList<>();
    UserAvatar user = FuLiCenterApplication.getUser();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        context = this;
        initView();
        initData();
        super.onCreate(savedInstanceState);
        //设置需要使用的支付方式
        PingppOne.enableChannels(new String[]{"wx", "alipay", "upacp", "bfb", "jdpay_wap"});

        // 提交数据的格式，默认格式为json
        // PingppOne.CONTENT_TYPE = "application/x-www-form-urlencoded";
        PingppOne.CONTENT_TYPE = "application/json";

        PingppLog.DEBUG = true;
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
    // 计算总金额（以分为单位）
    private void SumPrice() {
        int savPrice = 0;
        if(mList!=null&&mList.size()>0){
            for(CartBean c : mList){
                if(c.isChecked()){
                    savPrice+=getPrice(c.getGoods().getRankPrice())*c.getCount();
                }
            }
            price = savPrice;
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
                finish();
                break;
            case R.id.bt_settlement:
                if(etName.getText().toString().isEmpty()){
                    etName.requestFocus();
                    CommonUtils.showShortToast("姓名不能为空");
                    return;
                }else {
                    String name = etName.getText().toString();
                    if(etPhone.getText().toString().isEmpty()){
                        etPhone.requestFocus();
                        CommonUtils.showShortToast("手机号不能为空");
                        return;
                    }else {
                        String phone = etPhone.getText().toString();
                        if(etAddress.getText().toString().isEmpty()){
                            etAddress.requestFocus();
                            CommonUtils.showShortToast("地址不能为空");
                            return;
                        }else {
                            String address = etAddress.getText().toString();
                            L.i(name+" "+phone+" "+address);
                            gotopay();
                        }
                    }
                }
                break;
        }
    }

    private void gotopay() {
        // 产生个订单号
        String orderNo = new SimpleDateFormat("yyyyMMddhhmmss")
                .format(new Date());

        // 构建账单json对象
        JSONObject bill = new JSONObject();

        // 自定义的额外信息 选填
        JSONObject extras = new JSONObject();
        try {
            try {
                extras.put("extra1", "extra1");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            extras.put("extra2", "extra2");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            bill.put("order_no", orderNo);
            bill.put("amount", price*100);
            bill.put("extras", extras);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //壹收款: 创建支付通道的对话框
        PingppOne.showPaymentChannels(getSupportFragmentManager(), bill.toString(), URL, this);
    }

    @Override
    public void handlePaymentResult(Intent data) {
        if (data != null) {

            // result：支付结果信息
            // code：支付结果码
            //-2:用户自定义错误
            //-1：失败
            // 0：取消
            // 1：成功
            // 2:应用内快捷支付支付结果
            L.i("code"+data.getExtras().getInt("code"));
            if (data.getExtras().getInt("code") != 2) {
                PingppLog.d(data.getExtras().getString("result") + "  " + data.getExtras().getInt("code"));
            } else {
                String result = data.getStringExtra("result");
                try {
                    JSONObject resultJson = new JSONObject(result);
                    if (resultJson.has("error")) {
                        result = resultJson.optJSONObject("error").toString();
                    } else if (resultJson.has("success")) {
                        result = resultJson.optJSONObject("success").toString();
                    }
                    L.d("result::" + result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
