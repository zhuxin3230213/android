package cn.gmuni.sc.module.more.market.fragment;

import android.view.View;

import cn.gmuni.sc.R;

/**
 * @Author:ZhuXin
 * @Description:
 * 书籍
 * @Date:Create 2019/1/8 16:47
 * @Modified By:
 **/
public class MarketFragmentBooks extends MarketFragment {
    /**
     * 初始化布局
     *
     * @return 布局文件的id。
     */
    @Override
    public int getLayoutRes() {
        return R.layout.fragment_market_books;
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public int getListId() {
        params.put("type", "books");
        return R.id.market_list_view_books;
    }

    @Override
    protected void managerArguments() {

    }

    @Override
    public void showEmpty() {
        findViewById(R.id.market_list_view_books_empty).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmpty() {
        findViewById(R.id.market_list_view_books_empty).setVisibility(View.GONE);
    }
}
