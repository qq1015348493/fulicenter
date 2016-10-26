package cn.ucai.fulicenter.activity;

import android.app.Activity;
import android.app.ProgressDialog;
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
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.dao.SharePrefrenceUtils;
import cn.ucai.fulicenter.dao.UserDao;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.FuLiCenterApplication;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.OkHttpUtils;
import cn.ucai.fulicenter.utils.ResultUtils;

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
        name = getIntent().getStringExtra(I.User.USER_NAME);
        password=getIntent().getStringExtra(I.User.PASSWORD);
        personalUsername.setText(name);
        personalPassword.setText(password);
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
                else {
                    if (TextUtils.isEmpty(personalPassword.getText().toString())) {
                        Toast.makeText(this, "密码不能为空", Toast.LENGTH_LONG).show();
                    } else {
                        final ProgressDialog bd = new ProgressDialog(this);
                        bd.setMessage(getResources().getString(R.string.login));
                        bd.show();
                        NetDao.Login(this, personalUsername.getText().toString(), personalPassword.getText().toString(), new OkHttpUtils.OnCompleteListener<String>() {
                            @Override
                            public void onSuccess(String s) {
                                Result result = ResultUtils.getResultFromJson(s, UserAvatar.class);
                                L.e("result" + result);
                                if (result == null) {
                                    CommonUtils.showShortToast(R.string.login_fail);
                                } else {
                                    if (result.isRetMsg()) {
                                        UserAvatar user = (UserAvatar) result.getRetData();
                                        UserDao dao = new UserDao(Login.this);
                                        boolean isSuccess = dao.saveUser(user);
                                        if (isSuccess) {
                                            SharePrefrenceUtils.getInstance(Login.this).saveUser(user.getMuserName());
                                            FuLiCenterApplication.setUser(user);
                                            setResult(I.REQUEST_CODE_LOGIN);
                                            finish();
                                        } else {
                                            CommonUtils.showShortToast(R.string.user_database_error);
                                        }
                                    } else {
                                        if (result.getRetCode() == I.MSG_LOGIN_UNKNOW_USER) {
                                            CommonUtils.showShortToast(R.string.login_fail_unknow_user);
                                        } else if (result.getRetCode() == I.MSG_LOGIN_ERROR_PASSWORD) {
                                            CommonUtils.showShortToast(R.string.login_fail_error_password);
                                        } else {
                                            CommonUtils.showShortToast(R.string.login_fail);
                                        }
                                    }
                                }
                                bd.dismiss();
                            }

                            @Override
                            public void onError(String error) {
                                bd.dismiss();
                            }
                        });
                    }
                }
                break;
            case R.id.personal_register:
                Intent intent = new Intent(this,Register.class);
                startActivity(intent);
                break;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
