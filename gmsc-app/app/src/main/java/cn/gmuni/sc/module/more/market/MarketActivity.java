package cn.gmuni.sc.module.more.market;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseCommonActivity;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.model.MarketModel;
import cn.gmuni.sc.module.me.message.fragment.MessageFragmentAll;
import cn.gmuni.sc.module.me.message.fragment.MessageFragmentInternetFee;
import cn.gmuni.sc.module.me.message.fragment.MessageFragmentNotice;
import cn.gmuni.sc.module.more.market.fragment.MarketFragment3C;
import cn.gmuni.sc.module.more.market.fragment.MarketFragmentAll;
import cn.gmuni.sc.module.more.market.fragment.MarketFragmentBooks;
import cn.gmuni.sc.module.more.market.fragment.MarketFragmentDaily;
import cn.gmuni.sc.module.more.market.fragment.MarketFragmentOther;
import cn.gmuni.sc.module.more.market.fragment.MarketFragmentSupplier;
import cn.gmuni.sc.module.more.market.marketutil.SlideMenuCode;
import cn.gmuni.sc.module.more.market.marketutil.SlideMenuUtil;
import cn.gmuni.sc.utils.PixelUtil;
import cn.gmuni.sc.widget.CustomButton;

public class MarketActivity extends BaseCommonActivity {


    private String[] menus = {SlideMenuUtil.MARKET_ALL, SlideMenuUtil.MARKET_BOOKS, SlideMenuUtil.MARKET_SCHOOL_SUPPLIES,
            SlideMenuUtil.MARKET_DAILY_USE, SlideMenuUtil.MARKET_DIGITAL, SlideMenuUtil.MARKET_OTHER};


    private View mReView;// 子Layout要以view的形式加入到主Layo
    private LinearLayout market_tab_nav; //菜单布局

    private FragmentManager fragmentManager;
    //fragment的标记
    private static final String ALL = "market_frag_all";
    private static final String BOOKS = "market_frag_books";
    private static final String SUPPLIER = "market_frag_supplier";
    private static final String DAILY = "market_frag_daily";
    private static final String CCC = "market_frag_3c";
    private static final String OTHER = "market_frag_other";

    private String selectedTab;
    //全部
    private TextView market_tab_nav_all;
    private TextView market_tab_nav_all_underline;
    private MarketFragmentAll marketFragmentAll;
    //书籍
    private TextView market_tab_nav_books;
    private TextView market_tab_nav_books_underline;
    private MarketFragmentBooks marketFragmentBooks;
    //学习用品
    private TextView market_tab_nav_supplier;
    private TextView market_tab_nav_supplier_underline;
    private MarketFragmentSupplier marketFragmentSupplier;
    //生活用品
    private TextView market_tab_nav_daily;
    private TextView market_tab_nav_daily_underline;
    private MarketFragmentDaily marketFragmentDaily;
    //3C数码
    private TextView market_tab_nav_3C;
    private TextView market_tab_nav_3C_underline;
    private MarketFragment3C marketFragment3C;
    //其他
    private TextView market_tab_nav_other;
    private TextView market_tab_nav_other_underline;
    private MarketFragmentOther marketFragmentOther;

    @BindView(R.id.market_publish_new)
    CustomButton market_publish_new;

    public final static int RESULT_CODE = 3;

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_market);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        //管理Activity中的fragments
        fragmentManager = getBaseFragmentManager();
        //初始化菜单选项
        setSlideMenu();
        //切换fragment
        switchFragment(ALL);
        market_publish_new.setOnClickListener(new MyListener());
    }

    @Override
    public int getToolbar() {
        return R.id.market_toolbar;
    }

    //切换fragment
    private void switchFragment(String index) {
        selectedTab = index;
        //hideAllFragment(); //隐藏
        switch (index) {
            case ALL:
                /* if (marketFragmentAll == null) {*/
                marketFragmentAll = new MarketFragmentAll();
                addFragment(R.id.market_tab_content_fram, marketFragmentAll, ALL);
                /*}*/
                //showFragment(marketFragmentAll);
                break;
            case BOOKS:
                /* if (marketFragmentBooks == null) {*/
                marketFragmentBooks = new MarketFragmentBooks();
                addFragment(R.id.market_tab_content_fram, marketFragmentBooks, BOOKS);
                /*}*/
                //showFragment(marketFragmentBooks);
                break;
            case SUPPLIER:
                /* if (marketFragmentSupplier == null) {*/
                marketFragmentSupplier = new MarketFragmentSupplier();
                addFragment(R.id.market_tab_content_fram, marketFragmentSupplier, SUPPLIER);
                /*}*/
                //showFragment(marketFragmentSupplier);
                break;
            case DAILY:
                /* if (marketFragmentDaily == null) {*/
                marketFragmentDaily = new MarketFragmentDaily();
                addFragment(R.id.market_tab_content_fram, marketFragmentDaily, DAILY);
                /*}*/
                //showFragment(marketFragmentDaily);
                break;
            case CCC:
                /* if (marketFragment3C == null) {*/
                marketFragment3C = new MarketFragment3C();
                addFragment(R.id.market_tab_content_fram, marketFragment3C, CCC);
                /*}*/
                //showFragment(marketFragment3C);
                break;
            case OTHER:
                /* if (marketFragmentOther == null) {*/
                marketFragmentOther = new MarketFragmentOther();
                addFragment(R.id.market_tab_content_fram, marketFragmentOther, OTHER);
                /*}*/
                //showFragment(marketFragmentOther);
                break;
        }
    }

    /**
     * 隐藏所有的Fragment
     */
    private void hideAllFragment() {
        if (marketFragmentAll != null) {
            hideFragment(marketFragmentAll);
        }
        if (marketFragmentBooks != null) {
            hideFragment(marketFragmentBooks);
        }
        if (marketFragmentSupplier != null) {
            hideFragment(marketFragmentSupplier);
        }
        if (marketFragmentDaily != null) {
            hideFragment(marketFragmentDaily);
        }
        if (marketFragment3C != null) {
            hideFragment(marketFragment3C);
        }
        if (marketFragmentOther != null) {
            hideFragment(marketFragmentOther);
        }

    }

    private void setSlideMenu() {
        //父布局的LinearLayout
        LinearLayout menuLinerLayout = (LinearLayout) findViewById(R.id.linearLayoutMenu);

        mReView = View.inflate(getContext(), R.layout.module_market_menu_list, null);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mReView.setLayoutParams(layoutParams);
        menuLinerLayout.addView(mReView);
        //菜单布局文件
        market_tab_nav = (LinearLayout) findViewById(R.id.market_tab_nav);
        //全部
        market_tab_nav_all = (TextView) findViewById(R.id.market_tab_nav_all);
        market_tab_nav_all_underline = (TextView) findViewById(R.id.market_tab_nav_all_underline);
        //书籍
        market_tab_nav_books = (TextView) findViewById(R.id.market_tab_nav_books);
        market_tab_nav_books_underline = (TextView) findViewById(R.id.market_tab_nav_books_underline);
        //学习用品
        market_tab_nav_supplier = (TextView) findViewById(R.id.market_tab_nav_supplier);
        market_tab_nav_supplier_underline = (TextView) findViewById(R.id.market_tab_nav_supplier_underline);
        //生活用品
        market_tab_nav_daily = (TextView) findViewById(R.id.market_tab_nav_daily);
        market_tab_nav_daily_underline = (TextView) findViewById(R.id.market_tab_nav_daily_underline);
        //3C数码
        market_tab_nav_3C = (TextView) findViewById(R.id.market_tab_nav_3C);
        market_tab_nav_3C_underline = (TextView) findViewById(R.id.market_tab_nav_3C_underline);
        //其他
        market_tab_nav_other = (TextView) findViewById(R.id.market_tab_nav_other);
        market_tab_nav_other_underline = (TextView) findViewById(R.id.market_tab_nav_other_underline);
        //点击事件
        market_tab_nav_all.setOnClickListener(new MyListener());
        market_tab_nav_books.setOnClickListener(new MyListener());
        market_tab_nav_supplier.setOnClickListener(new MyListener());
        market_tab_nav_daily.setOnClickListener(new MyListener());
        market_tab_nav_3C.setOnClickListener(new MyListener());
        market_tab_nav_other.setOnClickListener(new MyListener());
        //留言
        toolbar.setOnActionListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                //TODO 我的留言板块开发
            }
        });
    }

    //自定义点击事件
    class MyListener extends OnMultiClickListener {
        @Override
        public void onMultiClick(View v) {
            switch (v.getId()) {
                case R.id.market_tab_nav_all:
                    if (!ALL.equals(selectedTab)) {
                        market_tab_nav_all.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_huge));
                        market_tab_nav_books.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
                        market_tab_nav_supplier.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
                        market_tab_nav_daily.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
                        market_tab_nav_3C.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
                        market_tab_nav_other.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));

                        market_tab_nav_all_underline.setVisibility(View.VISIBLE);
                        market_tab_nav_books_underline.setVisibility(View.INVISIBLE);
                        market_tab_nav_supplier_underline.setVisibility(View.INVISIBLE);
                        market_tab_nav_daily_underline.setVisibility(View.INVISIBLE);
                        market_tab_nav_3C_underline.setVisibility(View.INVISIBLE);
                        market_tab_nav_other_underline.setVisibility(View.INVISIBLE);

                        switchFragment(ALL);
                    }

                    break;
                case R.id.market_tab_nav_books:
                    if (!BOOKS.equals(selectedTab)) {
                        market_tab_nav_all.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
                        market_tab_nav_books.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_huge));
                        market_tab_nav_supplier.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
                        market_tab_nav_daily.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
                        market_tab_nav_3C.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
                        market_tab_nav_other.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));

                        market_tab_nav_all_underline.setVisibility(View.INVISIBLE);
                        market_tab_nav_books_underline.setVisibility(View.VISIBLE);
                        market_tab_nav_supplier_underline.setVisibility(View.INVISIBLE);
                        market_tab_nav_daily_underline.setVisibility(View.INVISIBLE);
                        market_tab_nav_3C_underline.setVisibility(View.INVISIBLE);
                        market_tab_nav_other_underline.setVisibility(View.INVISIBLE);

                        switchFragment(BOOKS);
                    }

                    break;
                case R.id.market_tab_nav_supplier:
                    if (!SUPPLIER.equals(selectedTab)) {
                        market_tab_nav_all.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
                        market_tab_nav_books.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
                        market_tab_nav_supplier.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_huge));
                        market_tab_nav_daily.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
                        market_tab_nav_3C.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
                        market_tab_nav_other.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));

                        market_tab_nav_all_underline.setVisibility(View.INVISIBLE);
                        market_tab_nav_books_underline.setVisibility(View.INVISIBLE);
                        market_tab_nav_supplier_underline.setVisibility(View.VISIBLE);
                        market_tab_nav_daily_underline.setVisibility(View.INVISIBLE);
                        market_tab_nav_3C_underline.setVisibility(View.INVISIBLE);
                        market_tab_nav_other_underline.setVisibility(View.INVISIBLE);

                        switchFragment(SUPPLIER);
                    }

                    break;
                case R.id.market_tab_nav_daily:
                    if (!DAILY.equals(selectedTab)) {
                        market_tab_nav_all.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
                        market_tab_nav_books.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
                        market_tab_nav_supplier.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
                        market_tab_nav_daily.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_huge));
                        market_tab_nav_3C.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
                        market_tab_nav_other.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));

                        market_tab_nav_all_underline.setVisibility(View.INVISIBLE);
                        market_tab_nav_books_underline.setVisibility(View.INVISIBLE);
                        market_tab_nav_supplier_underline.setVisibility(View.INVISIBLE);
                        market_tab_nav_daily_underline.setVisibility(View.VISIBLE);
                        market_tab_nav_3C_underline.setVisibility(View.INVISIBLE);
                        market_tab_nav_other_underline.setVisibility(View.INVISIBLE);

                        switchFragment(DAILY);
                    }

                    break;
                case R.id.market_tab_nav_3C:
                    if (!CCC.equals(selectedTab)) {
                        market_tab_nav_all.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
                        market_tab_nav_books.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
                        market_tab_nav_supplier.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
                        market_tab_nav_daily.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
                        market_tab_nav_3C.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_huge));
                        market_tab_nav_other.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));

                        market_tab_nav_all_underline.setVisibility(View.INVISIBLE);
                        market_tab_nav_books_underline.setVisibility(View.INVISIBLE);
                        market_tab_nav_supplier_underline.setVisibility(View.INVISIBLE);
                        market_tab_nav_daily_underline.setVisibility(View.INVISIBLE);
                        market_tab_nav_3C_underline.setVisibility(View.VISIBLE);
                        market_tab_nav_other_underline.setVisibility(View.INVISIBLE);

                        switchFragment(CCC);
                    }

                    break;
                case R.id.market_tab_nav_other:
                    if (!OTHER.equals(selectedTab)) {
                        market_tab_nav_all.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
                        market_tab_nav_books.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
                        market_tab_nav_supplier.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
                        market_tab_nav_daily.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
                        market_tab_nav_3C.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
                        market_tab_nav_other.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_huge));

                        market_tab_nav_all_underline.setVisibility(View.INVISIBLE);
                        market_tab_nav_books_underline.setVisibility(View.INVISIBLE);
                        market_tab_nav_supplier_underline.setVisibility(View.INVISIBLE);
                        market_tab_nav_daily_underline.setVisibility(View.INVISIBLE);
                        market_tab_nav_3C_underline.setVisibility(View.INVISIBLE);
                        market_tab_nav_other_underline.setVisibility(View.VISIBLE);

                        switchFragment(OTHER);
                    }

                    break;
                case R.id.market_publish_new:
                    Intent intent = new Intent(getContext(), MarketPublishActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "publish");
                    intent.putExtras(bundle);
                    startForActivityResultSecurity(intent, RESULT_CODE);
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CODE) {
            MarketModel marketModel = (MarketModel) data.getExtras().getSerializable("marketModel");
            marketFragmentAll.addListItem(marketModel);
            if (marketFragmentBooks != null && SlideMenuCode.BOOKS.equals(marketModel.getType())) {
                marketFragmentBooks.addListItem(marketModel);
            }
            if (marketFragmentSupplier != null && SlideMenuCode.SUPPLIES.equals(marketModel.getType())) {
                marketFragmentSupplier.addListItem(marketModel);
            }
            if (marketFragmentDaily != null && SlideMenuCode.DAILY.equals(marketModel.getType())) {
                marketFragmentDaily.addListItem(marketModel);
            }
            if (marketFragment3C != null && SlideMenuCode.DIGITAL.equals(marketModel.getType())) {
                marketFragment3C.addListItem(marketModel);
            }
            if (marketFragmentOther != null && SlideMenuCode.OTHER.equals(marketModel.getType())) {
                marketFragmentOther.addListItem(marketModel);
            }
        }
    }
}
