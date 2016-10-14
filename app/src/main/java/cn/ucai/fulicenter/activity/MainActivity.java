package cn.ucai.fulicenter.activity;

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
                break;
            case R.id.boutique:
                mutual((RadioButton) v);
                break;
            case R.id.cart:
                mutual((RadioButton) v);
                break;
            case R.id.category:
                mutual((RadioButton) v);
                break;
            case R.id.personal:
                mutual((RadioButton) v);
                break;
        }
    }
}
