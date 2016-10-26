package cn.ucai.fulicenter.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;

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
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.OkHttpUtils;
import cn.ucai.fulicenter.utils.OnSetAvatarListener;
import cn.ucai.fulicenter.utils.ResultUtils;

/**
 * Created by Administrator on 2016/10/25.
 */

public class Personal_Data extends AppCompatActivity {

    @Bind(R.id.personal_data_back)
    ImageView personalDataBack;
    @Bind(R.id.personal_data_iv)
    ImageView personalDataIv;
    @Bind(R.id.personal_data_iv_R)
    RelativeLayout personalDataIvR;
    @Bind(R.id.personal_data_nick)
    TextView personalDataNick;
    @Bind(R.id.personal_data_nick_R)
    RelativeLayout personalDataNickR;
    @Bind(R.id.personal_data_username)
    TextView personalDataUsername;
    @Bind(R.id.personal_data_username_R)
    RelativeLayout personalDataUsernameR;
    @Bind(R.id.personal_data_code)
    ImageView personalDataCode;
    @Bind(R.id.personal_data_code_R)
    RelativeLayout personalDataCodeR;
    @Bind(R.id.personal_data_linearlayout)
    LinearLayout mLayout;
    UserAvatar user;
    Personal_Data mcontext;
    EditText dialogEd;
    OnSetAvatarListener setAvatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.personal_data);
        mcontext = this;
        ButterKnife.bind(this);
        initData();
        initView();
        super.onCreate(savedInstanceState);
    }

    private void initData() {
        user = FuLiCenterApplication.getUser();
        if(user!=null){
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), mcontext, personalDataIv);
            personalDataNick.setText(user.getMuserNick().toString());
            personalDataUsername.setText(user.getMuserName().toString());
        }else{
            finish();
        }

    }

    private void initView() {
    }

    @OnClick({R.id.personal_data_back, R.id.personal_data_iv_R, R.id.personal_data_nick_R,
            R.id.Exit_login,R.id.personal_data_username_R})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.personal_data_back:
                finish();
                break;
            case R.id.Exit_login:
                logout();
            case R.id.personal_data_iv_R:
                String type = "user_avatar";
                setAvatar = new OnSetAvatarListener(mcontext,R.id.personal_data_linearlayout,user.getMuserName(),type);
                break;
            case R.id.personal_data_nick_R:
                AlertDialog.Builder bd = new AlertDialog.Builder(this);
                LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialogview, null);
                dialogEd = (EditText) layout.findViewById(R.id.dialog_ed);
                bd.setTitle("请输入新昵称").setView(layout).setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String input = dialogEd.getText().toString();
                        if (input.equals("")) {
                            CommonUtils.showShortToast("昵称不能为空");
                        } else {
                            user.setMuserNick(input);
                            updateNick(input);
                            initData();
                        }
                    }
                }).setNegativeButton("取消", null).create().show();
                break;
            case R.id.personal_data_username_R:
                break;
        }
    }

    private void logout() {
        if(user!=null){
            SharePrefrenceUtils.getInstance(mcontext).removeUser();
            FuLiCenterApplication.setUser(null);
            MFGT.startActivity(mcontext,Login.class);
        }
        finish();
    }

    private void updateNick(String input) {
        NetDao.updateNick(mcontext, user.getMuserName(), input, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                Result result = ResultUtils.getResultFromJson(s, UserAvatar.class);
                if(result==null){
                   CommonUtils.showShortToast("更改昵称失败");
                }else {
                    if(result.isRetMsg()){
                        UserAvatar u = (UserAvatar) result.getRetData();
                        UserDao dao = new UserDao(Personal_Data.this);
                        boolean isSuccess = dao.updateUser(user);
                        if(isSuccess){
                            FuLiCenterApplication.setUser(u);
                            setResult(RESULT_OK);
                            MFGT.finish(mcontext);
                        }else {
                            CommonUtils.showShortToast("跟新失败");
                        }
                    }else {
                        if(result.getRetCode()==I.MSG_LOGIN_UNKNOW_USER){
                            CommonUtils.showShortToast("账号不纯在");
                        }else if(result.getRetCode()==I.MSG_LOGIN_ERROR_PASSWORD){
                            CommonUtils.showShortToast("账户密码错误");
                        }else {
                            CommonUtils.showShortToast("登录失败");
                        }
                    }

                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RESULT_OK){
            return;
        }
        setAvatar.setAvatar(requestCode,data,personalDataIv);
        if(requestCode==OnSetAvatarListener.REQUEST_CROP_PHOTO){
            updateAvatar();
        }
    }

    private void updateAvatar() {
        File file = OnSetAvatarListener.getAvatarFile(mcontext,user.getMuserName());
        NetDao.updateAvatar(mcontext, user.getMuserName(), file, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                Result result = ResultUtils.getResultFromJson(s, UserAvatar.class);
                if(result==null){
                    CommonUtils.showShortToast("更改昵称失败");
                }else {
                    if(result.isRetMsg()){
                        UserAvatar u = (UserAvatar) result.getRetData();
                        UserDao dao = new UserDao(Personal_Data.this);
                        boolean isSuccess = dao.updateUser(user);
                        if(isSuccess){
                            FuLiCenterApplication.setUser(u);
                            setResult(RESULT_OK);
                            MFGT.finish(mcontext);
                        }else {
                            CommonUtils.showShortToast("跟新失败");
                        }
                    }else {
                        if(result.getRetCode()==I.MSG_LOGIN_UNKNOW_USER){
                            CommonUtils.showShortToast("账号不纯在");
                        }else if(result.getRetCode()==I.MSG_LOGIN_ERROR_PASSWORD){
                            CommonUtils.showShortToast("账户密码错误");
                        }else {
                            CommonUtils.showShortToast("登录失败");
                        }
                    }

                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }
}
