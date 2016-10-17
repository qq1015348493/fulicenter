package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.utils.L;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener{

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        L.i("MainActivity onCreate");
        setListenr();
    }

    private void setListenr() {
        newGood.setOnClickListener(this);
        boutique.setOnClickListener(this);
        category.setOnClickListener(this);
        cart.setOnClickListener(this);
        personal.setOnClickListener(this);
    }



    private void mutual(RadioButton v) {
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_good:
                mutual((RadioButton) v);
                New_good new_good = new New_good();
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.linearlayout, new_good);
                transaction.commit();
                break;
            case R.id.boutique:
                mutual((RadioButton) v);
                Boutique boutique = new Boutique();
                FragmentManager manager2 = getSupportFragmentManager();
                FragmentTransaction transaction2 = manager2.beginTransaction();
                transaction2.replace(R.id.linearlayout, boutique);
                transaction2.addToBackStack(null);
                transaction2.commit();
                break;
            case R.id.cart:
                mutual((RadioButton) v);
                Cart cart = new Cart();
                FragmentManager manager3 = getSupportFragmentManager();
                FragmentTransaction transaction3 = manager3.beginTransaction();
                transaction3.replace(R.id.linearlayout, cart);
                transaction3.addToBackStack(null);
                transaction3.commit();
                break;
            case R.id.category:
                mutual((RadioButton) v);
                Category category = new Category();
                FragmentManager manager4 = getSupportFragmentManager();
                FragmentTransaction transaction4 = manager4.beginTransaction();
                transaction4.replace(R.id.linearlayout, category);
                transaction4.addToBackStack(null);
                transaction4.commit();
                break;
            case R.id.personal:
                mutual((RadioButton) v);
                Personal personal = new Personal();
                FragmentManager manager5 = getSupportFragmentManager();
                FragmentTransaction transaction5 = manager5.beginTransaction();
                transaction5.replace(R.id.linearlayout, personal);
                transaction5.addToBackStack(null);
                transaction5.commit();
                break;
        }
    }
}
