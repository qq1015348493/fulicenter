package cn.ucai.fulicenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.Bean.UserAvatar;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.fragment.Boutique;
import cn.ucai.fulicenter.fragment.Cart;
import cn.ucai.fulicenter.fragment.Category;
import cn.ucai.fulicenter.fragment.New_good;
import cn.ucai.fulicenter.fragment.Personal;
import cn.ucai.fulicenter.utils.FuLiCenterApplication;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = MainActivity.class.getSimpleName();

    @Bind(R.id.new_good)
    RadioButton newGoods;
    @Bind(R.id.boutique)
    RadioButton boutique;
    @Bind(R.id.category)
    RadioButton itemCategory;
    @Bind(R.id.cart)
    RadioButton itemCart;
    @Bind(R.id.personal)
    RadioButton person;

    int index;
    RadioButton[] rbs;
    MainActivity mContext;
    int currentIndex;
    UserAvatar user ;


    Fragment[] mFragment;
    New_good mNewGoodsFragment;
    Boutique mBoutiquesFragment;
    Category mCategoryFragment;
    Cart mCartFragment;
    Personal mPersonalFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        L.i("MainActivity onCreate()");
        initView();
        initFragment();
    }


    private void initView() {
        rbs = new RadioButton[5];
        rbs[0] = newGoods;
        rbs[1] = boutique;
        rbs[2] = itemCategory;
        rbs[3] = itemCart;
        rbs[4] = person;

    }

    private void initFragment() {
        mFragment = new Fragment[5];
        mNewGoodsFragment = new New_good();
        mBoutiquesFragment = new Boutique();
        mCategoryFragment = new Category();
        mCartFragment = new Cart();
        mPersonalFragment = new Personal();

        mFragment[0] = mNewGoodsFragment;
        mFragment[1] = mBoutiquesFragment;
        mFragment[2] = mCategoryFragment;
        mFragment[3] = mCartFragment;
        mFragment[4] = mPersonalFragment;

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment, mNewGoodsFragment)
                .show(mNewGoodsFragment)
                .commit();
    }


    public void onCheckedChange(View view) {

        switch (view.getId()) {
            case R.id.new_good:
                index = 0;
                break;
            case R.id.boutique:
                index = 1;
                break;
            case R.id.category:
                index = 2;
                break;
            case R.id.cart:
                index = 3;
                break;
            case R.id.personal:
                if (user == null) {
                    L.i("你妈逼");
                    MFGT.gotoLogin(mContext);
                } else {
                    index = 4;
                }
                break;
        }
        setFragment();
    }

    private void setFragment() {
        L.i("index"+index);
        L.i("currentIndex"+currentIndex);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment,mFragment[index]).show(mFragment[index]).commitAllowingStateLoss();
        setRadioButtonStatus();
        currentIndex = index;
    }

    private void setRadioButtonStatus() {
        for (int i = 0; i < rbs.length; i++) {
            if (i == index) {
                rbs[i].setChecked(true);
            } else {
                rbs[i].setChecked(false);
            }
        }
    }

    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        /*if (index==4&&FuLiCenterApplication.getUser()==null){
            index=0;
        }*/
        user = FuLiCenterApplication.getUser();
        if(user == null){
            L.i(index+"张");

            if(currentIndex==4){
                index=0;
            }else {
                index = currentIndex;
            }
        }
        updateView();
        super.onResume();
}

    private void updateView() {
        setFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.i("requestCode"+requestCode);
        L.e(TAG, "onActivityResult,requestCode=" + requestCode);
        if (requestCode == I.REQUEST_CODE_LOGIN && FuLiCenterApplication.getUser() != null) {
            index = 4;
        }else {
            if(currentIndex==4){
                index=0;
            }else {
                index = currentIndex;
            }
        }
    }

}
