package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.Bean.UserAvatar;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.dao.SharePrefrenceUtils;
import cn.ucai.fulicenter.utils.FuLiCenterApplication;
import cn.ucai.fulicenter.utils.MFGT;

/**
 * Created by Administrator on 2016/10/25.
 */

public class Intercalate extends AppCompatActivity {
    @Bind(R.id.intercalate_back)
    ImageView intercalateBack;
    @Bind(R.id.new_message)
    TextView newMessage;
    @Bind(R.id.voice)
    TextView voice;
    @Bind(R.id.shock)
    TextView shock;
    @Bind(R.id.speaker)
    TextView speaker;
    @Bind(R.id.Blacklist)
    TextView Blacklist;
    @Bind(R.id.personal_data)
    TextView personalData;
    @Bind(R.id.diagnosis)
    TextView diagnosis;
    @Bind(R.id.Exit_login)
    Button ExitLogin;
    UserAvatar user;
    Intercalate mcontext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intercalate);
        mcontext=this;
        initView();
        initData();
        ButterKnife.bind(this);
    }

    private void initView() {
    }

    private void initData() {
        user = FuLiCenterApplication.getUser();
        if(user!=null){

        }else {
            finish();
        }
    }

    @OnClick({R.id.intercalate_back, R.id.new_message, R.id.voice, R.id.shock, R.id.speaker, R.id.Blacklist, R.id.personal_data, R.id.diagnosis, R.id.Exit_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.intercalate_back:
                finish();
                break;
            case R.id.new_message:
                break;
            case R.id.voice:
                break;
            case R.id.shock:
                break;
            case R.id.speaker:
                break;
            case R.id.Blacklist:
                break;
            case R.id.personal_data:

                break;
            case R.id.diagnosis:
                break;
            case R.id.Exit_login:
                logout();
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
}
