package cn.ucai.fulicenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
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


public class MainActivity extends AppCompatActivity {


    View vLine;

    RadioButton[] rbs = {};
    Fragment[] mFragment;

    int index;
    int currentIndex;

    int status;

    // NewGoodsFragment goodsFragment;
    Boutique boutiqueFragment;
    New_good goodsFragment;
    Category categoryFragment;
    Personal personFragment;
    Cart cartFragment;
    @Bind(R.id.fragment)
    LinearLayout fragment;
    @Bind(R.id.new_good)
    RadioButton newGood;
    @Bind(R.id.boutique)
    RadioButton boutique;
    @Bind(R.id.category)
    RadioButton category;
    @Bind(R.id.cart)
    RadioButton cart;
    @Bind(R.id.tvCartHint)
    TextView tvCartHint;
    @Bind(R.id.radRelative)
    RelativeLayout radRelative;
    @Bind(R.id.personal)
    RadioButton personal;
    @Bind(R.id.radGroup)
    LinearLayout radGroup;
    @Bind(R.id.radrelative)
    RelativeLayout radrelative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        L.i("MainActivity.onCreate");
        initData();
        initFragment();
    }

    private void initFragment() {
        mFragment = new Fragment[5];
        goodsFragment = new New_good();
        boutiqueFragment = new Boutique();
        categoryFragment = new Category();
        personFragment = new Personal();
        cartFragment = new Cart();
        mFragment[0] = goodsFragment;
        mFragment[1] = boutiqueFragment;
        mFragment[2] = categoryFragment;
        mFragment[3] = cartFragment;
        mFragment[4] = personFragment;

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment, boutiqueFragment)
                .add(R.id.fragment, goodsFragment)
                .add(R.id.fragment, categoryFragment)
                .add(R.id.fragment, cartFragment)
                .add(R.id.fragment, personFragment)
                .hide(boutiqueFragment)
                .hide(categoryFragment)
                .hide(cartFragment)
                .show(goodsFragment)
                .commit();

    }

    public void initData() {
        rbs = new RadioButton[5];
        rbs[0] = newGood;
        rbs[1] = boutique;
        rbs[2] = category;
        rbs[3] = cart;
        rbs[4] = personal;


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

    @OnClick({R.id.new_good, R.id.boutique, R.id.category, R.id.cart, R.id.personal})

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
                if (FuLiCenterApplication.getUser() == null) {
                    MFGT.gotoLogin(this);
                } else {
                    index = 4;
                }

                break;
        }
        setRadioButtonStatus();
        setFragment();
    }

    private void setFragment() {
        if (index != currentIndex) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.hide(mFragment[currentIndex]);
            if (!mFragment[index].isAdded()) {
                ft.add(R.id.fragment, mFragment[index]);
            }
            ft.show(mFragment[index]).commit();
        }
        setRadioButtonStatus();
        currentIndex = index;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (FuLiCenterApplication.getUser() != null) {
            index = 4;
        }else {
            index=1;
        }
        setFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == I.REQUEST_CODE_LOGIN && FuLiCenterApplication.getUser() != null) {
            index = 4;
        }
    }
}
