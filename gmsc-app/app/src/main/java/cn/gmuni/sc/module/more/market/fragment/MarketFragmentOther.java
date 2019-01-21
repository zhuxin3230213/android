package cn.gmuni.sc.module.more.market.fragment;

import android.view.View;

import cn.gmuni.sc.R;

/**
 * @Author:ZhuXin
 * @Description: 其他
 * @Date:Create 2019/1/8 16:49
 * @Modified By:
 **/
public class MarketFragmentOther extends MarketFragment {
    /**
     * 初始化布局
     *
     * @return 布局文件的id。
     */
    @Override
    public int getLayoutRes() {
        return R.layout.fragment_market_other;
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
        params.put("type", "other");
        return R.id.market_list_view_other;
    }

    @Override
    protected void managerArguments() {

    }

    @Override
    public void showEmpty() {
        findViewById(R.id.market_list_view_other_empty).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmpty() {
        findViewById(R.id.market_list_view_other_empty).setVisibility(View.GONE);
    }
}
