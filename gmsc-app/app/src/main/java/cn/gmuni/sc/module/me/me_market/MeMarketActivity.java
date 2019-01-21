package cn.gmuni.sc.module.me.me_market;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import java.util.IllegalFormatCodePointException;

import butterknife.BindView;
import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseCommonActivity;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.module.me.me_market.fragment.MeMarketFragmentAll;
import cn.gmuni.sc.module.me.me_market.fragment.MeMarketFragmentForSale;
import cn.gmuni.sc.module.me.me_market.fragment.MeMarketFragmentIsSale;

public class MeMarketActivity extends BaseCommonActivity {


    private FragmentManager fragmentManager;
    //全部
    @BindView(R.id.market_tab_nav_all)
    TextView market_tab_nav_all;
    @BindView(R.id.market_tab_nav_all_underline)
    TextView market_tab_nav_all_underline;
    MeMarketFragmentAll meMarketFragmentAll;

    //待售
    @BindView(R.id.market_tab_nav_for_sale)
    TextView market_tab_nav_for_sale;
    @BindView(R.id.market_tab_nav_for_sale_underline)
    TextView market_tab_nav_for_sale_underline;
    MeMarketFragmentForSale meMarketFragmentForSale;

    //已出售
    @BindView(R.id.market_tab_nav_is_sale)
    TextView market_tab_nav_is_sale;
    @BindView(R.id.market_tab_nav_is_sale_underline)
    TextView market_tab_nav_is_sale_underline;
    MeMarketFragmentIsSale meMarketFragmentIsSale;

    //fragment的标记
    private static final String ALL = "market_frag_all";
    private static final String FORSALE = "market_frag_for_sale";
    private static final String ISSALE = "market_frag_is_sale";
    private String selectedTab;

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_me_market);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        //管理Activity中的fragments
        fragmentManager = getBaseFragmentManager();
        //点击事件
        market_tab_nav_all.setOnClickListener(new MyListener());
        market_tab_nav_for_sale.setOnClickListener(new MyListener());
        market_tab_nav_is_sale.setOnClickListener(new MyListener());
        //切换fragment
        switchFragment(ALL);
        //留言
        toolbar.setOnActionListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                //TODO 我的留言板块待开发
            }
        });
    }

    //切换fragment
    private void switchFragment(String index) {
        selectedTab = index;
        //hideAllFragment(); //隐藏
        switch (index) {
            case ALL:
                /* if (meMarketFragmentAll == null) {*/
                meMarketFragmentAll = new MeMarketFragmentAll();
                addFragment(R.id.me_market_tab_content_fram, meMarketFragmentAll, ALL);
                /*}*/
                //showFragment(meMarketFragmentAll);
                break;
            case FORSALE:
                /*if (meMarketFragmentForSale == null) {*/
                meMarketFragmentForSale = new MeMarketFragmentForSale();
                addFragment(R.id.me_market_tab_content_fram, meMarketFragmentForSale, FORSALE);
                /*}*/
                //showFragment(meMarketFragmentForSale);
                break;
            case ISSALE:
                /* if (messageFragmentNotice == null) {*/
                meMarketFragmentIsSale = new MeMarketFragmentIsSale();
                addFragment(R.id.me_market_tab_content_fram, meMarketFragmentIsSale, ISSALE);
                /*}*/
                //showFragment(meMarketFragmentIsSale);
                break;

        }
    }

    /**
     * 隐藏所有的Fragment
     */
    private void hideAllFragment() {
        if (meMarketFragmentAll != null) {
            hideFragment(meMarketFragmentAll);
        }
        if (meMarketFragmentForSale != null) {
            hideFragment(meMarketFragmentForSale);
        }
        if (meMarketFragmentIsSale != null) {
            hideFragment(meMarketFragmentIsSale);
        }
    }

    @Override
    public int getToolbar() {
        return R.id.me_market_toolbar;
    }

    //自定义点击事件
    class MyListener extends OnMultiClickListener {

        @Override
        public void onMultiClick(View v) {
            switch (v.getId()) {
                case R.id.market_tab_nav_all:
                    if (!ALL.equals(selectedTab)) {
                        //设置字体大小及下滑线显示隐藏
                        market_tab_nav_all.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_huge));
                        market_tab_nav_for_sale.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
                        market_tab_nav_is_sale.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
                        market_tab_nav_all_underline.setVisibility(View.VISIBLE);
                        market_tab_nav_for_sale_underline.setVisibility(View.INVISIBLE);
                        market_tab_nav_is_sale_underline.setVisibility(View.INVISIBLE);
                        switchFragment(ALL);
                    }
                    break;
                case R.id.market_tab_nav_for_sale:
                    if (!FORSALE.equals(selectedTab)){
                        market_tab_nav_all.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
                        market_tab_nav_for_sale.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_huge));
                        market_tab_nav_is_sale.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
                        market_tab_nav_all_underline.setVisibility(View.INVISIBLE);
                        market_tab_nav_for_sale_underline.setVisibility(View.VISIBLE);
                        market_tab_nav_is_sale_underline.setVisibility(View.INVISIBLE);
                        switchFragment(FORSALE);
                    }
                    break;
                case R.id.market_tab_nav_is_sale:
                    if (!ISSALE.equals(selectedTab)){
                        market_tab_nav_all.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
                        market_tab_nav_for_sale.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
                        market_tab_nav_is_sale.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_huge));
                        market_tab_nav_all_underline.setVisibility(View.INVISIBLE);
                        market_tab_nav_for_sale_underline.setVisibility(View.INVISIBLE);
                        market_tab_nav_is_sale_underline.setVisibility(View.VISIBLE);
                        switchFragment(ISSALE);
                    }
                    break;
            }
        }
    }
}
