package cn.ucai.fulicenter.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.Bean.Result;
import cn.ucai.fulicenter.Bean.UserAvatar;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.OkHttpUtils;

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
                String user = personalRegisterUsername.getText().toString();
                String nick = personalRegisterNick.getText().toString();
                String password = personalRegisterPassword.getText().toString();
                String password2 = personalRegisterPassword2.getText().toString();
                if (TextUtils.isEmpty(user)) {
                    CommonUtils.showShortToast(R.string.user_name_connot_be_empty);
                    personalRegisterUsername.requestFocus();
                    return;
                }else if(!user.matches("[a-zA-Z]\\w{5,15}")){
                    CommonUtils.showShortToast(R.string.illegal_user_name);
                    personalRegisterUsername.requestFocus();
                     return;
                 }else if(TextUtils.isEmpty(password)){
                    CommonUtils.showShortToast(R.string.password_connot_be_empty);
                    personalRegisterPassword.requestFocus();
                    return;
                }else if (TextUtils.isEmpty(nick)){
                    CommonUtils.showShortToast(R.string.nick_name_connot_be_empty);
                    personalRegisterNick.requestFocus();
                    return;
                }else if (TextUtils.isEmpty(password2)){
                    CommonUtils.showShortToast(R.string.confirm_password_connot_be_empty);
                    personalRegisterPassword2.requestFocus();
                    return;
                }else if(!password.equals(password2)){
                    CommonUtils.showShortToast(R.string.two_input_password);
                    personalRegisterPassword.requestFocus();
                    return;
                }
                RegisterUser(user,nick,password);
                break;
        }
    }

    private void RegisterUser(final String user, String nick, final String password) {
        final ProgressDialog bd = new ProgressDialog(this);
        bd.setMessage(getResources().getString(R.string.registering));
        bd.show();
        NetDao.register(this,user,nick,password, new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                if(result==null){
                    CommonUtils.showShortToast(R.string.register_fail);
                }else {
                    if(result.isRetMsg()){
                        CommonUtils.showShortToast(R.string.register_success);
                        MFGT.finish(Register.this);
                    }else {
                        CommonUtils.showShortToast(R.string.register_fail_exists);
                        personalRegisterUsername.requestFocus();
                    }
                }

            }

            @Override
            public void onError(String error) {
                CommonUtils.showShortToast(R.string.register_fail);
                bd.dismiss();
            }
        });
    }
}
