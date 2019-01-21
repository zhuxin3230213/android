package cn.gmuni.sc.module.me.me_market.fragment;

import android.view.View;

import cn.gmuni.sc.R;

/**
 * @Author:ZhuXin
 * @Description: 已出售
 * @Date:Create 2019/1/14 10:39
 * @Modified By:
 **/
public class MeMarketFragmentIsSale extends MeMarketFragment {
    /**
     * 初始化布局
     *
     * @return 布局文件的id。
     */
    @Override
    public int getLayoutRes() {
        return R.layout.fragment_me_market_is_sale;
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
        params.put("saleStatus", "1");
        return R.id.market_list_view_is_sale;
    }

    @Override
    protected void managerArguments() {

    }

    @Override
    public void showEmpty() {
        findViewById(R.id.market_list_view_is_sale_empty).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmpty() {
        findViewById(R.id.market_list_view_is_sale_empty).setVisibility(View.GONE);
    }
}
