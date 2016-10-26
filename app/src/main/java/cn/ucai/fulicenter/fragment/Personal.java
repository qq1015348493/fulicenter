package cn.ucai.fulicenter.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.Bean.Result;
import cn.ucai.fulicenter.Bean.UserAvatar;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.Login;
import cn.ucai.fulicenter.activity.MainActivity;
import cn.ucai.fulicenter.activity.Personal_Data;
import cn.ucai.fulicenter.dao.UserDao;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.FuLiCenterApplication;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.OkHttpUtils;
import cn.ucai.fulicenter.utils.ResultUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class Personal extends Fragment {


    @Bind(R.id.shezhi)
    TextView shezhi;
    @Bind(R.id.personal_iv)
    ImageView personalIv;
    @Bind(R.id.personal_nick)
    TextView personalNick;
    @Bind(R.id.collection_goods)
    TextView collectionGoods;
    @Bind(R.id.collection_shops)
    TextView collectionShops;
    @Bind(R.id.footprint)
    TextView footprint;
    @Bind(R.id.all_collection_goods)
    TextView allCollectionGoods;
    @Bind(R.id.Pending_payment)
    ImageButton PendingPayment;
    @Bind(R.id.Deliver_goods)
    ImageButton DeliverGoods;
    @Bind(R.id.Goods_receipt)
    ImageButton GoodsReceipt;
    @Bind(R.id.evaluate)
    ImageButton evaluate;
    @Bind(R.id.refund)
    ImageButton refund;
    @Bind(R.id.textView)
    TextView textView;
    @Bind(R.id.Life_certificate)
    TextView LifeCertificate;
    @Bind(R.id.Shop_voucher)
    TextView ShopVoucher;
    @Bind(R.id.textView2)
    TextView textView2;
    @Bind(R.id.VIP_CD)
    TextView VIPCD;
    MainActivity mcontext;
    UserAvatar user;

    public Personal() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        ButterKnife.bind(this, view);
        mcontext = (MainActivity) getContext();
        initData();
        initView();
        return view;
    }

    private void initData() {
        user = FuLiCenterApplication.getUser();
        if (user == null) {
            MFGT.startActivity(mcontext, Login.class);
        } else {
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), mcontext, personalIv);
            personalNick.setText(user.getMuserNick());
            syncUser();
        }

    }

    private void initView() {


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.shezhi, R.id.personal_iv,R.id.personal_data_L})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shezhi:
                MFGT.startActivity(mcontext, Personal_Data.class);
                break;
            case R.id.personal_iv:
                break;
            case R.id.personal_data_L:
                MFGT.startActivity(mcontext, Personal_Data.class);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    public void syncUser(){
        NetDao.syncUser(mcontext, user.getMuserName(), new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                Result result = ResultUtils.getResultFromJson(s,UserAvatar.class);
                UserAvatar u = (UserAvatar) result.getRetData();
                if(!u.equals(user)){
                    UserDao dao = new UserDao(mcontext);
                    boolean b = dao.saveUser(u);
                    if(b){
                        FuLiCenterApplication.setUser(u);
                        user = u;
                        ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), mcontext, personalIv);
                        personalNick.setText(user.getMuserNick());
                    }else {
                        CommonUtils.showShortToast("保存数据失败");
                    }
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }
}
