package cn.gmuni.sc.module.schoolcard;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseCommonActivity;
import cn.gmuni.sc.base.ReturnedWebViewActivity;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.model.entities.UserInfoEntity;
import cn.gmuni.sc.network.Network;
import cn.gmuni.sc.network.api.ISchoolCardApi;
import cn.gmuni.sc.utils.ClickShake;
import cn.gmuni.sc.utils.DateUtils;
import cn.gmuni.sc.utils.DesUtils;
import cn.gmuni.sc.utils.SecurityUtils;
import cn.gmuni.sc.widget.CircleImageView;
import cn.gmuni.sc.widget.CustomDialog;


public class SchoolCardActivity extends BaseCommonActivity {
    private static final int CARD_PAY_PAGE_RESULT = 0XFF;
    //学校logo
    @BindView(R.id.school_card_school_logo)
    CircleImageView schoolLogo;
    //学校名称
    @BindView(R.id.school_card_school_name_ch)
    TextView schoolNameCh;
    //学校英文名称
    @BindView(R.id.school_card_school_name_en)
    TextView schoolNameEn;
    //卡片类型
    @BindView(R.id.school_card_card_type)
    TextView schoolCardType;
    //学生名称
    @BindView(R.id.school_card_stu_name)
    TextView stuName;
    //学号
    @BindView(R.id.school_card_stu_id)
    TextView stuCode;
    //卡片余额
    @BindView(R.id.school_card_card_balance)
    TextView cardBalance;
    //卡片陪伴时间
    @BindView(R.id.school_card_card_keep)
    TextView cardKeep;
    //开卡
    @BindView(R.id.school_card_open_card)
    TextView openCard;
    private boolean cardIsVisiable = false;
    UserInfoEntity userInfo = SecurityUtils.getUserInfo();
    /**
     * 用户开卡密码弹窗
     */
    private  CustomDialog dialog;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_school_card);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        schoolNameCh.setText(userInfo.getSchoolName());
        schoolNameEn.setText(userInfo.getSchoolName());
        schoolCardType.setText("本科生卡");
        stuName.setText("姓名：" + userInfo.getName());
        stuCode.setText("学号：" + userInfo.getStudentId());
        openCard.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                showPasswordDialog();
            }
        });
       getWalletInfo();
    }

    /**
     * 请求钱包数据
     */
    private void getWalletInfo(){
        //请求钱包数据
        Network.request(Network.createApi(ISchoolCardApi.class).getWallet(), new Network.IResponseListener<Object>() {
            @Override
            public void onSuccess(Object data) {
                if (null != data) {
                    cardIsVisiable = true;
                    openCard.setVisibility(View.INVISIBLE);
                    Map<String, Object> map = (Map<String, Object>) data;
                    cardBalance.setText("￥" + String.valueOf(map.get("amount")) + "元");
                    String describe = "这张卡已经陪你度过了0天";
                    try{
                        long diff = DateUtils.dayDiff(sdf.parse(String.valueOf(map.get("createTime"))),new Date());
                        describe = "这张卡已经陪你度过了" + diff + "天";
                    }catch (Exception e){

                    }
                    cardKeep.setText(describe);
                } else {
                    cardBalance.setText("￥--元");
                    cardKeep.setText("请开通卡片后使用校园卡各项功能");
                }
            }

            @Override
            public void onFail(int code, String message) {
            }
        });
    }

    /**
     * 显示开卡窗口
     */
    private void showPasswordDialog(){
        dialog = new CustomDialog(getContext(),"设置支付密码",R.layout.fragment_school_card_pwd, true);

        //确定
        dialog.setOnOkDialogListener(v -> {
            EditText pwd = v.findViewById(R.id.school_card_pwd);
            EditText pwdConfirm = v.findViewById(R.id.school_card_pwd_confirm);
            String password = pwd.getText().toString();
            String rePassword = pwdConfirm.getText().toString();
            if(password.equals(rePassword) && password.length() > 5 && password.length() < 17){
                String desPwd = DesUtils.encode(password, userInfo.getPhoneNumber());
                Map<String, String> param = new HashMap<>();
                param.put("pwd", desPwd);
                //请求钱包数据
                Network.request(Network.createApi(ISchoolCardApi.class).openCard(param), new Network.IResponseListener<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        if (null != data) {
                            cardIsVisiable = true;
                            openCard.setVisibility(View.INVISIBLE);
                            Map<String, Object> map = (Map<String, Object>) data;
                            cardBalance.setText("￥" + String.valueOf(map.get("amount")) + "元");
                            dialog.close();
                        } else {
                            Toast.makeText(getContext(),"开卡失败", Toast.LENGTH_LONG);
                            dialog.close();
                        }
                    }

                    @Override
                    public void onFail(int code, String message) {
                        Toast.makeText(getContext(),"开卡失败", Toast.LENGTH_LONG);
                        dialog.close();
                    }
                });
            }else{
                Toast.makeText(getContext(),"两次输入的密码不一致", Toast.LENGTH_LONG);
            }
        });
        dialog.setOnLoadComparedContentListener(v ->{
            EditText pwd = v.findViewById(R.id.school_card_pwd);
            EditText pwdConfirm = v.findViewById(R.id.school_card_pwd_confirm);
            View okBtn = dialog.findViewById(R.id.custom_dialog_single_btn);
            okBtn.setClickable(false);
            okBtn.setAlpha(0.6f);
            pwd.addTextChangedListener(new CustomTextWatcher());
            pwdConfirm.addTextChangedListener(new CustomTextWatcher());
        });
        //修改名称取消
        dialog.setOnCancelDialogListener(v -> dialog.close());
        dialog.show();
    }
    class CustomTextWatcher implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(null != dialog){
                View okBtn = dialog.findViewById(R.id.custom_dialog_single_btn);
                EditText pwd = dialog.findViewById(R.id.school_card_pwd);
                EditText pwdConfirm = dialog.findViewById(R.id.school_card_pwd_confirm);
                String password = pwd.getText().toString();
                String rePassword = pwdConfirm.getText().toString();
                if(password.equals(rePassword) && password.length() > 5 && password.length() < 17){
                    okBtn.setClickable(true);
                    okBtn.setAlpha(1f);
                }else{
                    okBtn.setClickable(false);
                    okBtn.setAlpha(0.6f);
                }
            }
        }
    }

    @OnClick({R.id.school_card_pay, R.id.school_card_pay_detail, R.id.school_card_consume})
    protected void OnClickItem(View target) {
        if (!ClickShake.check(target.getId()) || !cardIsVisiable) {
            return;
        }
        Intent intent = new Intent(getContext(), ReturnedWebViewActivity.class);
        Bundle bundle = new Bundle();
        switch (target.getId()) {
            case R.id.school_card_pay:
                bundle.putString("url", "/wallet/pay");
                break;
            case R.id.school_card_pay_detail:
                bundle.putString("url", "/wallet/pay_detail");
                break;
            case R.id.school_card_consume:
                bundle.putString("url", "/wallet/consume_detail");
                break;
        }
        intent.putExtras(bundle);
        startActivityForResult(intent, CARD_PAY_PAGE_RESULT);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == CARD_PAY_PAGE_RESULT){
            getWalletInfo();
        }
    }
    @Override
    public int getToolbar() {
        return R.id.school_card_toolbar;
    }

}
