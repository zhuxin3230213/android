package cn.gmuni.sc.module.me.setting;

import android.os.Bundle;

import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseCommonActivity;

public class FeedBackActivity extends BaseCommonActivity {

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_me_setting_feed_back);
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public int getToolbar() {
        return R.id.me_setting_feed_back_toolbar;
    }
}
