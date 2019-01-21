package cn.gmuni.sc.module.schoolbus.fragment;

import android.view.View;

import cn.gmuni.sc.R;

/**
 * @Author:ZhuXin
 * @Description:
 * @Date:Create 2018/12/26 16:28
 * @Modified By:
 **/
public class SchoolBusFragmentGeneral extends SchoolBusFragment {
    /**
     * 初始化布局
     *
     * @return 布局文件的id。
     */
    @Override
    public int getLayoutRes() {
        return R.layout.fragment_school_bus_general;
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
        return R.id.school_bus_list_view_general_weeks;
    }

    public int getOverWeeksListId() {
        return R.id.school_bus_list_view_general_over_weeks;
    }

    @Override
    protected void managerArguments() {

    }

    /**
     * 获取季节
     *
     * @return
     */
    public int getSeason() {
        return 3;
    }

    @Override
    public void showEmpty() {
        findViewById(R.id.school_bus_list_view_general_empty).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmpty() {
        findViewById(R.id.school_bus_list_view_general_re).setVisibility(View.GONE);
    }
}
