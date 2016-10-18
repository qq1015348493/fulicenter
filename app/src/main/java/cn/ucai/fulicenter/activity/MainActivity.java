package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.fragment.New_good;
import cn.ucai.fulicenter.utils.L;

public class MainActivity extends AppCompatActivity  {
    New_good newgoodsFragment;
    @Bind(R.id.new_good)
    RadioButton newGood;
    @Bind(R.id.boutique)
    RadioButton boutique;
    @Bind(R.id.category)
    RadioButton category;
    @Bind(R.id.personal)
    RadioButton personal;
    @Bind(R.id.cart)
    RadioButton cart;

    int index;
    RadioButton[] rbs;
    Fragment[] mfragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        L.i("MainActivity onCreate");
//        setListenr();
        initView();
        initFragment();
    }

    private void initFragment() {
        mfragment = new Fragment[5];
        newgoodsFragment = new New_good();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment,newgoodsFragment)
                .show(newgoodsFragment)
                .commit();
    }

    private void initView() {
        rbs = new RadioButton[5];
        rbs[0]=newGood;
        rbs[1]=boutique;
        rbs[2]=category;
        rbs[3]=cart;
        rbs[4]=personal;
    }

    /*private void setListenr() {
        newGood.setOnClickListener(this);
        boutique.setOnClickListener(this);
        category.setOnClickListener(this);
        cart.setOnClickListener(this);
        personal.setOnClickListener(this);
    }*/
    /*private void mutual(RadioButton v) {
        if (v != newGood) {
            newGood.setChecked(false);
        }
        if (v != boutique) {
            boutique.setChecked(false);
        }
        if (v != cart) {
            cart.setChecked(false);
        }
        if (v != category) {
            category.setChecked(false);
        }
        if (v != personal) {
            personal.setChecked(false);
        }

    }*/

    public void onCheckedChange(View v) {
        switch (v.getId()) {
            case R.id.new_good:
                L.i("new_good");
                index = 0;
//                mutual((RadioButton) v);
                /*New_good new_good = new New_good();
               FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
               transaction.add(R.id.fragment, new_good);
                transaction.commit();*/
                break;
            case R.id.boutique:
                L.i("boutique");
                index = 1;
//                mutual((RadioButton) v);
                break;
            case R.id.category:
                index = 2;
//                mutual((RadioButton) v);
                break;
            case R.id.cart:
                index =3;
//                mutual((RadioButton) v);
                break;
            case R.id.personal:
                index = 4;
//                mutual((RadioButton) v);
                break;
        }
        setB();
    }

    private void setB() {
        L.i(index+"");
        for(int i=0;i<rbs.length;i++){
            if(index == i){
                rbs[i].setChecked(true);
            }else {
                rbs[i].setChecked(false);
            }
        }
    }



}
