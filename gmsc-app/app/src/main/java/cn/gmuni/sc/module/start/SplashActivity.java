package cn.gmuni.sc.module.start;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import cn.gmuni.sc.MainActivity;
import cn.gmuni.sc.R;
import cn.gmuni.sc.app.SharedPreferencesHelper;
import cn.gmuni.sc.base.BaseActivity;
import cn.gmuni.sc.config.Const;
import cn.gmuni.sc.config.SharedPreferenceConst;
import cn.gmuni.sc.module.login.LoginActivity;
import cn.gmuni.sc.umpush.UmPushHelper;
import cn.gmuni.sc.utils.SecurityUtils;
import cn.gmuni.sc.utils.StringUtil;

public class SplashActivity extends BaseActivity {

    private Handler handler = new Handler();

    @Override
    public void beforeCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.beforeCreate(savedInstanceState);
    }

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_splash);
        handler.postDelayed(() -> {
            Class cls;
            if (SecurityUtils.isLogin()) {
                //如果当前用户已选择了学校，则给推送添加学校标签
                String school = SecurityUtils.getUserInfo().getSchool();
                if (school != null) {
                    UmPushHelper.getInstance().setTags(school);
                }
                SharedPreferencesHelper.Editor build = SharedPreferencesHelper.build(SharedPreferenceConst.SHARE_LOGIN_NAMESPACE);
                int indentityType = (int) build.get(Const.LOGIN_IDENTITY_TYPE,0);
                if(indentityType == Const.IDENTITY_TYPE_PHONE){
                    String phoneNumber = SecurityUtils.getUserInfo().getPhoneNumber();
                    if (!StringUtil.isEmpty(phoneNumber)) {
                        UmPushHelper.getInstance().setTags(phoneNumber); //设置用户推送标签
                    }
                }
                cls = MainActivity.class;
            } else {
                cls = LoginActivity.class;
            }
            Intent intent = new Intent(SplashActivity.this, cls);
            startActivity(intent);
            finish();

        }, 10);
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }
}
