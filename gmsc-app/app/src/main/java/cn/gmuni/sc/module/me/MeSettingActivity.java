package cn.gmuni.sc.module.me;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import java.util.Map;

import butterknife.OnClick;
import cn.gmuni.sc.R;
import cn.gmuni.sc.app.BaseApplication;
import cn.gmuni.sc.base.BaseCommonActivity;
import cn.gmuni.sc.module.login.LoginActivity;
import cn.gmuni.sc.module.me.setting.AboutUsActivity;
import cn.gmuni.sc.module.me.setting.FeedBackActivity;
import cn.gmuni.sc.module.me.setting.SafetyActivity;
import cn.gmuni.sc.network.Network;
import cn.gmuni.sc.network.api.ILoginApi;
import cn.gmuni.sc.umpush.UmPushHelper;
import cn.gmuni.sc.utils.ClickShake;
import cn.gmuni.sc.utils.MToast;
import cn.gmuni.sc.utils.SecurityUtils;
import cn.gmuni.sc.widget.CustomButton;

public class MeSettingActivity extends BaseCommonActivity {


    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_me_setting);
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public int getToolbar() {
        return R.id.me_setting_toolbar;
    }

    /**
     * 退出登录
     *
     * @param btn
     */
    @OnClick(R.id.me_logout)
    protected void onLogout(CustomButton btn) {
        if(!ClickShake.check(btn.getId())){
            return;
        }
        showLoading();
        Network.request(Network.createApi(ILoginApi.class).logout(), new Network.IResponseListener<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> data) {
                hideLoading();
                SecurityUtils.logout();
                BaseApplication.getInstance().finishActivities();
                //用户退出登录时将所有推送标签删除
                UmPushHelper.getInstance().removeAllTags();
                startActivity(new Intent(getContext(), LoginActivity.class));
            }

            @Override
            public void onFail(int code, String message) {
                hideLoading();
                MToast.showLongToast(message);
            }
        });


    }

    @OnClick({R.id.me_check_for_update, R.id.me_feedback,
            R.id.me_help_center, R.id.me_about,R.id.me_setting_safety})
    protected void onItemClick(RelativeLayout view) {
        if(!ClickShake.check(view.getId())){
            return;
        }
        switch (view.getId()) {
            case R.id.me_check_for_update:
                 MToast.showShortToast("稍后");
                 break;
            case R.id.me_feedback:
                 startActivity(new Intent(getContext(),FeedBackActivity.class));
                 break;
            case R.id.me_help_center:
                 MToast.showShortToast("稍后");
                 break;
            case R.id.me_about:
                 startActivity(new Intent(getContext(),AboutUsActivity.class));
                 break;
            case R.id.me_setting_safety:
                startActivity(new Intent(getContext(), SafetyActivity.class));
                break;

        }

    }
}
