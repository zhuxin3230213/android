package cn.gmuni.sc.module.me.setting;

import android.os.Bundle;

import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseCommonActivity;

public class AboutUsActivity extends BaseCommonActivity {


    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_me_setting_about_us);
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public int getToolbar() {
        return R.id.me_about_us_back_toolbar;
    }
}
