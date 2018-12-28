package cn.gmuni.sc.module.login;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import cn.gmuni.sc.MainActivity;
import cn.gmuni.sc.R;
import cn.gmuni.sc.app.SharedPreferencesHelper;
import cn.gmuni.sc.base.BaseActivity;
import cn.gmuni.sc.config.Const;
import cn.gmuni.sc.config.SharedPreferenceConst;
import cn.gmuni.sc.database.UserInfoDao;
import cn.gmuni.sc.model.entities.UserInfoEntity;
import cn.gmuni.sc.network.Network;
import cn.gmuni.sc.network.api.ILoginApi;
import cn.gmuni.sc.umpush.UmPushHelper;
import cn.gmuni.sc.utils.ClickShake;
import cn.gmuni.sc.utils.DesUtils;
import cn.gmuni.sc.utils.JsonUtils;
import cn.gmuni.sc.utils.MToast;
import cn.gmuni.sc.utils.PixelUtil;
import cn.gmuni.sc.utils.RegExUtil;
import cn.gmuni.sc.utils.SecurityUtils;
import cn.gmuni.sc.widget.CustomButton;
import cn.gmuni.sc.widget.CustomEditText;

public class LoginActivity extends BaseActivity {


    /**
     * 顶部两个背景条
     */
    @BindView(R.id.login_bg1)
    View loginBg1;

    @BindView(R.id.login_bg2)
    View loginBg2;

    //验证码按钮
    @BindView(R.id.login_verifycode_btn)
    TextView verifyBtn;

    @BindView(R.id.login_phone_number)
    CustomEditText phoneNumber;

    @BindView(R.id.login_verify_code)
    CustomEditText verifyCode;

    @BindView(R.id.login_btn)
    CustomButton loginBtn;

    //60秒定时重发验证码
    TimeCount timeCount;

    @Override
    public void beforeCreate(Bundle savedInstanceState) {
        super.beforeCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        timeCount = new TimeCount(60000,1000);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        //设置背景透明度
        loginBg1.getBackground().setAlpha(45);
        loginBg2.getBackground().setAlpha(118);
        //打开窗口先禁用登录按钮
        loginBtn.setEnabled(false);

    }

    /**
     * 手机号与验证码获取焦点事件
     * @param text
     */
    @OnFocusChange({R.id.login_phone_number,R.id.login_verify_code})
    public void onFocusChange(CustomEditText text,boolean hasFocus){
        Drawable drawable = null;
        if(hasFocus){
            if(text.getId() == R.id.login_phone_number){
                drawable = getResources().getDrawable(R.drawable.icon_phone_01,null);
            }else{
                drawable = getResources().getDrawable(R.drawable.icon_yanzhengma_01,null);


            }
        }else{
            if(text.getId() == R.id.login_phone_number){
               drawable = getResources().getDrawable(R.drawable.icon_phone,null);
            }else{
               drawable = getResources().getDrawable(R.drawable.icon_yanzhengma,null);
            }
        }
        drawable.setBounds(0,0, PixelUtil.dp2px(30),PixelUtil.dp2px(30));
        text.setCompoundDrawables(drawable,null,null,null);
    }



    /**
     * 监听验证码或手机号码输入改变
     * @param text
     */
    @OnTextChanged({R.id.login_verify_code,R.id.login_phone_number})
    public void onInputChange(CharSequence text){
        String pn = phoneNumber.getText().toString();
        String vr = verifyCode.getText().toString();
        if(RegExUtil.test(pn,RegExUtil.PHONENUBER_REGEX) && RegExUtil.test(vr,"^\\d{6}$")){
            loginBtn.setEnabled(true);
        }else {
            loginBtn.setEnabled(false);
        }
    }


    //获取验证码
    @OnClick(R.id.login_verifycode_btn)
    public void onSendVerifyCode(TextView btn){
        if(!ClickShake.check(btn.getId())){
            return;
        }
        String pn = phoneNumber.getText().toString();
        if(pn!=null && !"".equals(pn)){
            if(!RegExUtil.test(pn,RegExUtil.PHONENUBER_REGEX)){
                MToast.showLongToast("手机号码格式不正确");
                return;
            }
            verifyBtn.setEnabled(false);
            timeCount.start();
            Network.request(Network.createApi(ILoginApi.class).sendSms(pn), new Network.IResponseListener<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> data) {

                }

                @Override
                public void onFail(int code, String message) {
                    MToast.showLongToast(message);
                }
            });
        }else{
            MToast.showLongToast("请输入手机号码");
        }
    }

    @OnClick(R.id.login_btn)
    public void login(CustomButton btn){
        if(!ClickShake.check(btn.getId())){
            return;
        }
        String pn = phoneNumber.getText().toString();
        String vc = verifyCode.getText().toString();
        //获取手机唯一标识
        String deviceUniqueId = SecurityUtils.getUniqueId(getContext());
        if(pn!=null && vc!=null &&!"".equals(pn) && !"".equals(vc)){
            Network.request(Network.createApi(ILoginApi.class).loginSms(pn, DesUtils.encode(vc,pn),deviceUniqueId), new Network.IResponseListener<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> data) {
                    int identityType = (int) data.get(Const.LOGIN_IDENTITY_TYPE);
                    //登录成功之后，将登录信息保存起来
                    SharedPreferencesHelper.Editor build = SharedPreferencesHelper.build(SharedPreferenceConst.SHARE_LOGIN_NAMESPACE);
                    build.put(Const.LOGIN_IDENTITY_TYPE,identityType);
                    build.put(Const.LOGIN_TOKEN_NAME,data.get(Const.LOGIN_TOKEN_NAME));
                    build.put(Const.LOGIN_PHONE_NUMBER,data.get(Const.LOGIN_PHONE_NUMBER));
                    //将用户信息保存到数据库
                    UserInfoEntity user = JsonUtils.map2Bean((Map<String,Object>)data.get("userInfo"),UserInfoEntity.class);
                    UserInfoDao dao = new UserInfoDao();
                    //登录成功后现将本地数据删除，防止出现脏数据
                    dao.deleteAll();
                    dao.save(user);
                    SecurityUtils.updateUserInfo();
                    UmPushHelper.getInstance().setTags(pn); //设置标签
                    //跳转到首页
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    SecurityUtils.init();
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFail(int code, String message) {
                    MToast.showLongToast(message);
                    Logger.e(message);
                }
            });
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    /**
     * 重发验证码
     */
    class TimeCount extends CountDownTimer{

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            verifyBtn.setText(getResources().getString(R.string.login_re_verify)+"("+millisUntilFinished/1000+"S)");
        }

        @Override
        public void onFinish() {
            verifyBtn.setText(R.string.login_re_verify_code);
            verifyBtn.setEnabled(true);
        }
    }
}
