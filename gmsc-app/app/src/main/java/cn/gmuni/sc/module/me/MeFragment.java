package cn.gmuni.sc.module.me;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseFragment;
import cn.gmuni.sc.model.entities.UserInfoEntity;
import cn.gmuni.sc.module.me.SignIn.SignInActivity;
import cn.gmuni.sc.module.me.me_market.MeMarketActivity;
import cn.gmuni.sc.module.me.message.MessageActivity;
import cn.gmuni.sc.module.me.personal_details.PersionalDetailsActivity;
import cn.gmuni.sc.module.schoolcard.SchoolCardActivity;
import cn.gmuni.sc.network.Network;
import cn.gmuni.sc.network.api.IMessageApi;
import cn.gmuni.sc.utils.BitMapUtil;
import cn.gmuni.sc.utils.ClickShake;
import cn.gmuni.sc.utils.MToast;
import cn.gmuni.sc.utils.SecurityUtils;
import cn.gmuni.sc.widget.CustomImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends BaseFragment {

    @BindView(R.id.me_setting)
    RelativeLayout settingView;

    @BindView(R.id.me_school_card)
    RelativeLayout schoolCardView;

    @BindView(R.id.me_pay_fanpiao)
    RelativeLayout fanpiaoView;

    @BindView(R.id.me_pay_detail)
    RelativeLayout payDetailView;

    @BindView(R.id.me_school_name)
    TextView schoolNameView;

    @BindView(R.id.me_user_name)
    TextView userNameView;

    @BindView(R.id.me_avatar_img)
    CustomImageView avatarView;

    @BindView(R.id.me_school_information_icon_circular_round)
    TextView me_school_information_icon_circular_round;
    public static final int UPDATE_USERINFO_CODE = 1;
    public static final int READ_MESSAGE_STATE = 2;

    public MeFragment() {
        // Required empty public constructor
    }


    @Override
    public int getLayoutRes() {
        return R.layout.fragment_me;
    }

    @Override
    public void initView() {
        UserInfoEntity entity = SecurityUtils.getUserInfo();
        if (entity != null) {
            schoolNameView.setText(entity.getSchoolName());
            userNameView.setText(entity.getName());
            if (null != entity.getAvatar() && !"".equals(entity.getAvatar())) {
                avatarView.setImageBitmap(BitMapUtil.base64ToBitmap(entity.getAvatar()));
            }

        }
        //获取消息列表有未读状态
        getMessageState();
    }

    //获取消息列表状态
    private void getMessageState() {
        Network.request(Network.createApi(IMessageApi.class).getMessageState(), new Network.IResponseListener<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> data) {
                if (data != null) {
                    if ("true".equals(String.valueOf(data.get("flag")))) {
                        //有未读消息，则显示小圆点
                        me_school_information_icon_circular_round.setVisibility(View.VISIBLE);
                    } else {
                        me_school_information_icon_circular_round.setVisibility(View.GONE);
                    }
                } else {
                    me_school_information_icon_circular_round.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFail(int code, String message) {
                MToast.showShortToast("获取消息状态失败");
            }
        });
    }


    @OnClick({R.id.me_school_message, R.id.me_school_market, R.id.me_school_card, R.id.me_pay_fanpiao, R.id.me_pay_detail, R.id.me_setting,
            R.id.me_school_xueji})
    protected void OnItemClick(RelativeLayout view) {
        if (!ClickShake.check(view.getId())) {
            return;
        }
        switch (view.getId()) {
            case R.id.me_school_message: {
                startActivityForResult(new Intent(getContext(), MessageActivity.class), READ_MESSAGE_STATE);
                break;
            }
            case R.id.me_school_market:{
                startActivity(new Intent(getContext(),MeMarketActivity.class));
                break;
            }
            case R.id.me_setting: {
                startActivity(new Intent(getContext(), MeSettingActivity.class));
                break;
            }
            case R.id.me_school_xueji: {
                startActivity(new Intent(getContext(), StudentRollActivity.class));
                break;
            }
            case R.id.me_school_card: {
                startActivitySecurity(new Intent(getContext(), SchoolCardActivity.class));
                break;
            }
            case R.id.me_pay_fanpiao: {
                MToast.showLongToast("正在开发中");
                break;
            }
            case R.id.me_pay_detail: {
                MToast.showLongToast("正在开发中");
                break;
            }
        }
    }

    @OnClick(R.id.me_edit)
    protected void onEditClick(CustomImageView edit) {
        startActivityForResult(new Intent(getContext(), PersionalDetailsActivity.class), UPDATE_USERINFO_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //如果修改了数据
        if (requestCode == UPDATE_USERINFO_CODE) {
            UserInfoEntity userInfo = SecurityUtils.getUserInfo();
            userNameView.setText(userInfo.getName());
            schoolNameView.setText(userInfo.getSchoolName());
            if (null != userInfo.getAvatar() && !"".equals(userInfo.getAvatar())) {
                avatarView.setImageBitmap(BitMapUtil.base64ToBitmap(userInfo.getAvatar()));
            }
        }
        //消息阅读状态
        if (resultCode == READ_MESSAGE_STATE) {
            String iState = data.getStringExtra("iState");
            if ("true".equals(iState)) {
                me_school_information_icon_circular_round.setVisibility(View.VISIBLE);
            } else {
                me_school_information_icon_circular_round.setVisibility(View.GONE);
            }
        }
    }

    @OnClick({R.id.me_sign_in, R.id.me_sign_in_bt})
    protected void onSignInClick(View v) {
        startActivity(new Intent(getContext(), SignInActivity.class));
    }

    @Override
    protected void managerArguments() {

    }

    public static MeFragment newInstance() {
        MeFragment fragment = new MeFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }
}
