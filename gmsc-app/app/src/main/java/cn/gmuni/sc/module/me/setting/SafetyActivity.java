package cn.gmuni.sc.module.me.setting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseCommonActivity;

public class SafetyActivity extends BaseCommonActivity{

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_setting_safety);
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public int getToolbar() {
        return R.id.setting_safety_toolbar;
    }
}
