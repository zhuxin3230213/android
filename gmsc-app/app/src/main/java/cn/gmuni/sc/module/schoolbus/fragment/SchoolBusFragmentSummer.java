package cn.gmuni.sc.module.schoolbus.fragment;

import android.view.View;

import cn.gmuni.sc.R;

/**
 * @Author:ZhuXin
 * @Description:
 * @Date:Create 2018/12/26 11:07
 * @Modified By:
 **/
public class SchoolBusFragmentSummer extends SchoolBusFragment {
    /**
     * 初始化布局
     *
     * @return 布局文件的id。
     */
    @Override
    public int getLayoutRes() {
        return R.layout.fragment_school_bus_summer;
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public int getWeeksListId() {
        return R.id.school_bus_list_view_summer_weeks;
    }

    public int getOverWeeksListId() {
        return R.id.school_bus_list_view_summer_over_weeks;
    }

    @Override
    protected void managerArguments() {

    }
    /**
     * 获取季节
     * @return
     */
    public int getSeason(){
        return 2;
    }
    @Override
    public void showEmpty() {
        findViewById(R.id.school_bus_list_view_summer_empty).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmpty() {
        findViewById(R.id.school_bus_list_view_summer_re).setVisibility(View.GONE);
    }
}
