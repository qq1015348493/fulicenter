package cn.ucai.fulicenter.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.utils.L;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    RadioButton NewGoods,Boutique,Category,Cart,Personal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        L.i("MainActivity onCreate");
        initView();
        setListenr();
    }

    private void setListenr() {
        NewGoods.setOnClickListener(this);
        Boutique.setOnClickListener(this);
        Category.setOnClickListener(this);
        Cart.setOnClickListener(this);
        Personal.setOnClickListener(this);
    }

    private void initView() {
        NewGoods = (RadioButton) findViewById(R.id.new_good);
        Boutique = (RadioButton) findViewById(R.id.boutique);
        Category = (RadioButton) findViewById(R.id.category);
        Cart = (RadioButton) findViewById(R.id.cart);
        Personal = (RadioButton) findViewById(R.id.personal);



    }


    private void mutual(RadioButton v) {
        if(v!=NewGoods){
            NewGoods.setChecked(false);
        }
        if(v!=Boutique){
            Boutique.setChecked(false);
        }
        if(v!=Cart){
            Cart.setChecked(false);
        }
        if(v!=Category){
            Category.setChecked(false);
        }
        if(v!=Personal){
            Personal.setChecked(false);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.new_good:
                mutual((RadioButton)v);
                New_good new_good = new New_good();
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.linearlayout,new_good);
                transaction.commit();
                break;
            case R.id.boutique:
                mutual((RadioButton) v);
                Boutique boutique = new Boutique();
                FragmentManager manager2 = getSupportFragmentManager();
                FragmentTransaction transaction2 = manager2.beginTransaction();
                transaction2.replace(R.id.linearlayout,boutique);
                transaction2.addToBackStack(null);
                transaction2.commit();
                break;
            case R.id.cart:
                mutual((RadioButton) v);
                Cart cart = new Cart();
                FragmentManager manager3 = getSupportFragmentManager();
                FragmentTransaction transaction3 = manager3.beginTransaction();
                transaction3.replace(R.id.linearlayout,cart);
                transaction3.addToBackStack(null);
                transaction3.commit();
                break;
            case R.id.category:
                mutual((RadioButton) v);
                Category category = new Category();
                FragmentManager manager4 = getSupportFragmentManager();
                FragmentTransaction transaction4 = manager4.beginTransaction();
                transaction4.replace(R.id.linearlayout,category);
                transaction4.addToBackStack(null);
                transaction4.commit();
                break;
            case R.id.personal:
                mutual((RadioButton) v);
                Personal personal = new Personal();
                FragmentManager manager5 = getSupportFragmentManager();
                FragmentTransaction transaction5 = manager5.beginTransaction();
                transaction5.replace(R.id.linearlayout,personal);
                transaction5.addToBackStack(null);
                transaction5.commit();
                break;
        }
    }
}
