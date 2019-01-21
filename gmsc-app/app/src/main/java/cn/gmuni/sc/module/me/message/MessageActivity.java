package cn.gmuni.sc.module.me.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseCommonActivity;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.module.me.message.fragment.MessageFragmentAll;
import cn.gmuni.sc.module.me.message.fragment.MessageFragmentInternetFee;
import cn.gmuni.sc.module.me.message.fragment.MessageFragmentNotice;
import cn.gmuni.sc.utils.StringUtil;


public class MessageActivity extends BaseCommonActivity {

    private FragmentManager fragmentManager;
    //全部
    @BindView(R.id.message_tab_nav_all)
    TextView message_tab_nav_all;
    @BindView(R.id.message_tab_nav_all_underline)
    TextView message_tab_nav_all_underline;
    MessageFragmentAll messageFragmentAll;
    //网络
    @BindView(R.id.message_tab_nav_internet_fee)
    TextView message_tab_nav_internet_fee;
    @BindView(R.id.message_tab_nav_internet_fee_underline)
    TextView message_tab_nav_internet_fee_underline;
    MessageFragmentInternetFee messageFragmentInternetFee;
    //通知
    @BindView(R.id.message_tab_nav_notice)
    TextView message_tab_nav_notice;
    @BindView(R.id.message_tab_nav_notice_underline)
    TextView message_tab_nav_notice_underline;
    MessageFragmentNotice messageFragmentNotice;

    //fragment的标记
    private static final String ALL = "message_frag_all";
    private static final String INTENETFEE = "message_frag_intenet_fee";
    private static final String NOTICE = "message_frag_notice";
    private String selectedTab;

    public static final int AFTER_CLICK_STATE = 0;

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_message);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        //管理Activity中的fragments
        fragmentManager = getBaseFragmentManager();
        //点击事件
        message_tab_nav_all.setOnClickListener(new MyListener()); //全部
        message_tab_nav_internet_fee.setOnClickListener(new MyListener()); //网络
        message_tab_nav_notice.setOnClickListener(new MyListener()); //通知
        //切换fragment
        switchFragment(ALL);
    }

    //切换fragment
    private void switchFragment(String index) {
        selectedTab = index;
        //hideAllFragment(); //隐藏
        switch (index) {
            case ALL:
                /* if (messageFragmentAll == null) {*/
                messageFragmentAll = new MessageFragmentAll();
                addFragment(R.id.message_tab_content_fram, messageFragmentAll, ALL);
                /*}*/
                //showFragment(messageFragmentAll);
                break;
            case INTENETFEE:
                /*if (messageFragmentInternetFee == null) {*/
                messageFragmentInternetFee = new MessageFragmentInternetFee();
                addFragment(R.id.message_tab_content_fram, messageFragmentInternetFee, INTENETFEE);
                /*}*/
                //showFragment(messageFragmentInternetFee);
                break;
            case NOTICE:
                /* if (messageFragmentNotice == null) {*/
                messageFragmentNotice = new MessageFragmentNotice();
                addFragment(R.id.message_tab_content_fram, messageFragmentNotice, NOTICE);
                /*}*/
                //showFragment(messageFragmentNotice);
                break;

        }
    }

    /**
     * 隐藏所有的Fragment
     */
    private void hideAllFragment() {
        if (messageFragmentAll != null) {
            hideFragment(messageFragmentAll);
        }
        if (messageFragmentInternetFee != null) {
            hideFragment(messageFragmentInternetFee);
        }
        if (messageFragmentNotice != null) {
            hideFragment(messageFragmentNotice);
        }
    }


    //自定义点击事件
    class MyListener extends OnMultiClickListener {

        @Override
        public void onMultiClick(View v) {
            switch (v.getId()) {
                case R.id.message_tab_nav_all:
                    if (!ALL.equals(selectedTab)) {
                        //设置字体大小及下滑线显示隐藏
                        message_tab_nav_all.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_huge));
                        message_tab_nav_internet_fee.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
                        message_tab_nav_notice.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
                        message_tab_nav_all_underline.setVisibility(View.VISIBLE);
                        message_tab_nav_internet_fee_underline.setVisibility(View.INVISIBLE);
                        message_tab_nav_notice_underline.setVisibility(View.INVISIBLE);
                        switchFragment(ALL);
                    }
                    break;
                case R.id.message_tab_nav_internet_fee:
                    if (!INTENETFEE.equals(selectedTab)) {
                        message_tab_nav_all.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
                        message_tab_nav_internet_fee.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_huge));
                        message_tab_nav_notice.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
                        message_tab_nav_all_underline.setVisibility(View.INVISIBLE);
                        message_tab_nav_internet_fee_underline.setVisibility(View.VISIBLE);
                        message_tab_nav_notice_underline.setVisibility(View.INVISIBLE);
                        switchFragment(INTENETFEE);
                    }
                    break;
                case R.id.message_tab_nav_notice:
                    if (!NOTICE.equals(selectedTab)) {
                        message_tab_nav_all.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
                        message_tab_nav_internet_fee.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
                        message_tab_nav_notice.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_huge));
                        message_tab_nav_all_underline.setVisibility(View.INVISIBLE);
                        message_tab_nav_internet_fee_underline.setVisibility(View.INVISIBLE);
                        message_tab_nav_notice_underline.setVisibility(View.VISIBLE);
                        switchFragment(NOTICE);
                    }
                    break;

            }
        }
    }

    @Override
    public int getToolbar() {
        return R.id.message_toolbar;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //点击详情后消息状态处理(刷新当前fragment数据)
        if (resultCode == AFTER_CLICK_STATE) {
            String isClick = data.getStringExtra("isClick");
            if (!StringUtil.isEmpty(isClick) || "true".equals(isClick)) {
                //重新加载列表
                //管理Activity中的fragments
                fragmentManager = getBaseFragmentManager();
                if (ALL.equals(selectedTab)) {
                    messageFragmentAll.refeshFragment(); //重新刷新数据
                } else if (INTENETFEE.equals(selectedTab)) {
                    messageFragmentInternetFee.refeshFragment();
                } else if (NOTICE.equals(selectedTab)) {
                    messageFragmentNotice.refeshFragment();
                } else {
                }
            }
        }
    }

}
