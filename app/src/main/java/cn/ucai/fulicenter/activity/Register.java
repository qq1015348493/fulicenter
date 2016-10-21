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

public class Register extends AppCompatActivity {
    @Bind(R.id.personal_back)
    ImageView personalBack;
    @Bind(R.id.personal_register_username)
    EditText personalRegisterUsername;
    @Bind(R.id.personal_register_nick)
    EditText personalRegisterNick;
    @Bind(R.id.personal_register_password)
    EditText personalRegisterPassword;
    @Bind(R.id.personal_register_password2)
    EditText personalRegisterPassword2;
    @Bind(R.id.personal_register_register)
    Button personalRegisterRegister;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_register);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.personal_back, R.id.personal_register_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.personal_back:
                break;
            case R.id.personal_register_register:
                break;
        }
    }
}
