package cn.ucai.fulicenter.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.Bean.Result;
import cn.ucai.fulicenter.Bean.UserAvatar;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.OkHttpUtils;

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
    String name;
    String password;
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
                if(TextUtils.isEmpty(personalUsername.getText().toString())){
                    Toast.makeText(this,"用户不能为空",Toast.LENGTH_LONG).show();
                }
                else if (TextUtils.isEmpty(personalPassword.getText().toString())){
                    Toast.makeText(this,"密码不能为空",Toast.LENGTH_LONG).show();
                }
                else{
                    NetDao.Login(this, personalUsername.getText().toString(), personalPassword.getText().toString(), new OkHttpUtils.OnCompleteListener<Result>() {
                        @Override
                        public void onSuccess(Result result) {
                            String json = result.getRetData().toString();
                            Gson gson = new Gson();
                            Map map = gson.fromJson(json, Map.class);
                            json = map.get("pageData").toString();
                            UserAvatar[] users = gson.fromJson(json, UserAvatar[].class);
                            Toast.makeText(Login.this,users.toString(),Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
                }
                break;
            case R.id.personal_register:
                Intent intent = new Intent(this,Register.class);
                startActivity(intent);
                break;
        }
    }
}
