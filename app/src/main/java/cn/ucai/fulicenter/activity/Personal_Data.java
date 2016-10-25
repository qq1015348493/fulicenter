package cn.ucai.fulicenter.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.Bean.Result;
import cn.ucai.fulicenter.Bean.UserAvatar;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.dao.SharePrefrenceUtils;
import cn.ucai.fulicenter.dao.UserDao;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.FuLiCenterApplication;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.OkHttpUtils;

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
    UserAvatar user;
    Personal_Data mcontext;
    UserDao dao = new UserDao(Personal_Data.this);
    EditText dialogEd;

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

    @OnClick({R.id.personal_data_back, R.id.personal_data_iv_R, R.id.personal_data_nick_R, R.id.personal_data_username_R})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.personal_data_back:
                finish();
                break;
            case R.id.personal_data_iv_R:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("更改头像");
                FIndImage();
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

    private void FIndImage() {

    }

    private void updateNick(String input) {
        NetDao.updateNick(mcontext, user.getMuserName(), input, new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                if(result!=null){
                    boolean isSuccess = dao.saveUser(user);
                    if (isSuccess) {
                        SharePrefrenceUtils.getInstance(Personal_Data.this).saveUser(user.getMuserName());
                        FuLiCenterApplication.setUser(user);
                    } else {
                        CommonUtils.showShortToast(R.string.user_database_error);
                    }
                }else {
                    CommonUtils.showShortToast("更改昵称失败");
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }
}
