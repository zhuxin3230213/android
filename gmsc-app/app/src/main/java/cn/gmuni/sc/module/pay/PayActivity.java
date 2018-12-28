package cn.gmuni.sc.module.pay;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseCommonActivity;
import cn.gmuni.sc.base.ReturnedWebViewActivity;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.model.entities.UserInfoEntity;
import cn.gmuni.sc.network.Network;
import cn.gmuni.sc.network.api.IMenuApi;
import cn.gmuni.sc.network.api.INetPayApi;
import cn.gmuni.sc.utils.BitMapUtil;
import cn.gmuni.sc.utils.PixelUtil;
import cn.gmuni.sc.utils.SecurityUtils;
import cn.gmuni.sc.widget.ImageTextButton;

public class PayActivity extends BaseCommonActivity {

    private static final int PAY_PAGE_RESULT = 0XFF;
    PayActivity self = this;
    /**
     * 菜单栏
     */
    @BindView(R.id.pay_net_main_menu_icon_layout)
    LinearLayout iconLayout;
    //账号状态标志
    @BindView(R.id.pay_net_main_status_dot)
    View statusDot;

    //账号状态提示文本
    @BindView(R.id.pay_net_main_status_txt)
    TextView statusText;
    //套餐信息提示文本
    @BindView(R.id.pay_net_main_package_txt)
    TextView packageText;
    //账号余额提示文本
    @BindView(R.id.pay_net_main_account_remain)
    TextView remainText;

    //已用流量显示文本
    @BindView(R.id.pay_net_main_yi_yong)
    TextView yiYongLiuLiang;

    //剩余流量显示文本
    @BindView(R.id.pay_net_main_sheng_yu)
    TextView shengYuLiuLiang;

    UserInfoEntity userInfo = SecurityUtils.getUserInfo();

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_pay);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        Map<String, String> params = new HashMap<>();
        params.put("module", "net");
        //请求菜单数据
        Network.request(Network.createApi(IMenuApi.class).getItems(params), new Network.IResponseListener<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> data) {
                initMenu(data);
            }

            @Override
            public void onFail(int code, String message) {
            }
        });
        getAccountInfo();
    }

    /**
     * 查询用户账号信息
     */
    private void getAccountInfo(){
        Map<String, String> params = new HashMap<>();
        params.put("account", userInfo.getNetAccount());
        Network.request(Network.createApi(INetPayApi.class).getAccountInfo(params), new Network.IResponseListener<Object>() {
            @Override
            public void onSuccess(Object data) {
                Map<String, String> m = (Map<String, String>)data;
                //可用状态
                String status = m.get("status");
                //状态备注
                String stateNote = m.get("stateNote");
                if("1".equals(status)){
                    statusDot.setBackground(getResources().getDrawable(R.drawable.normal));
                    statusText.setTextColor(getResources().getColor(R.color.fontColorPrimary));
                    statusText.setText("账号正常");
                }else{
                    statusDot.setBackground(getResources().getDrawable(R.drawable.abnormal));
                    statusText.setTextColor(getResources().getColor(R.color.fontColorMinor));
                    statusText.setText(stateNote);
                }
                //状态时间
                String statusTime = m.get("statusTime");
                //储值余额
                String storedValueBalance = m.get("storedValueBalance");
                remainText.setText("￥" + storedValueBalance);
                //本期使用费
                String thisFee = m.get("thisFee");
                //本期时长
                String currentTime = m.get("currentTime");
                //本期流量
                String currentFlow = m.get("currentFlow");
                yiYongLiuLiang.setText("已用流量：" + currentFlow + "MB");
                //起始计费日
                String initialBilling = m.get("initialBilling");
                //套餐编号
                String packageCode = m.get("packageCode");
                //套餐具体名称
                String packageName = m.get("packageName");
                packageText.setText(packageName);
                //套餐类型，包月1，包流量0
                String packageType = m.get("packageType");
                if("1".equals(packageType)){
                    shengYuLiuLiang.setText("剩余流量：" + "-MB");
                }
            }
            @Override
            public void onFail(int code, String message) {
            }
        });
    }
    @Override
    public int getToolbar() {
        return R.id.pay_net_toolbar;
    }

    /**
     * 动态渲染菜单
     */
    private void initMenu(Map<String, Object> data) {
        List<Map<String, String>> items = (List<Map<String,String>>) data.get("pay");
        items.addAll((List<Map<String,String>>) data.get("bill"));
        items.addAll((List<Map<String,String>>) data.get("service"));
        LinearLayout linearLayout = null;
        int size = items.size();
        int totalNums = (int)Math.ceil(size / 4.0) * 4;
        for (int i = 0; i < totalNums; i++) {
            if (i % 4 == 0) {
                linearLayout = new LinearLayout(getContext());
                linearLayout.setBackgroundColor(getResources().getColor(R.color.colorNormalBackground));
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setPadding(PixelUtil.dp2px(8),0,PixelUtil.dp2px(8),0);
                params.topMargin = PixelUtil.dp2px(1);
//                if (i == 0) {
//                    linearLayout.setId(R.id.home_wrap_firstline);
//                } else {
//                    params.addRule(RelativeLayout.BELOW, R.id.home_wrap_firstline);
//                }
                iconLayout.addView(linearLayout, params);
            }
            if(i < size){
                Map<String, String> mp = items.get(i);
                linearLayout.addView(getMenuItem(mp, i));
            }else{
                linearLayout.addView(getMenuItem(null, i));
            }
        }
    }

    /**
     * 渲染菜单项
     *
     * @param menuItem
     * @param idx
     * @return
     */
    private LinearLayout getMenuItem(Map<String, String> menuItem, int idx) {
        LinearLayout layout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.width = 0;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.weight = 0.25f;
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layoutParams.bottomMargin = PixelUtil.dp2px(14);
        layoutParams.topMargin = PixelUtil.dp2px(16);
//        if (idx == 0) {
//            layoutParams.setMargins(PixelUtil.dp2px(18), 0, 0, 0);
//        } else {
//            layoutParams.setMargins(PixelUtil.dp2px(33), 0, 0, 0);
//        }
        layout.setLayoutParams(layoutParams);
        if(null == menuItem){
            return layout;
        }
        ImageView iv = new ImageView(this);
        iv.setImageBitmap(BitMapUtil.base64ToBitmap(menuItem.get("icon")));

        LinearLayout.LayoutParams ivLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ivLp.setMargins(0, 0, 0, 0);
        iv.setLayoutParams(ivLp);
        layout.addView(iv);
        TextView tv = new TextView(this);
        tv.setText(menuItem.get("name"));
        tv.setTextColor(getResources().getColor(R.color.fontColorNormal));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        LinearLayout.LayoutParams tvLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tvLp.setMargins(0, PixelUtil.dp2px(10), 0, 0);
        tv.setLayoutParams(tvLp);
        layout.addView(tv);
        String url = menuItem.get("url");
        layout.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                Intent intent = new Intent(getContext(),ReturnedWebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url",url);
                intent.putExtras(bundle);
                startActivityForResult(intent, PAY_PAGE_RESULT);
            }
        });
        return layout;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == PAY_PAGE_RESULT){
            getAccountInfo();
        }
    }

    //获取屏幕的宽度
    private int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }

    //获取屏幕的高度
    private int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getHeight();
    }
}
