package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;

/**
 * Created by Administrator on 2016/10/21.
 */

public class Login extends AppCompatActivity {
    @Bind(R.id.personal_back)
    ImageView personalBack;
    @Bind(R.id.personal_username)
    EditText personalUsername;
    @Bind(R.id.personal_password)
    EditText personalPassword;
    @Bind(R.id.personal_logn)
    Button personalLogn;
    @Bind(R.id.personal_register)
    Button personalRegister;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.personal_back, R.id.personal_logn, R.id.personal_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.personal_back:
                break;
            case R.id.personal_logn:
                break;
            case R.id.personal_register:
                break;
        }
    }
}
