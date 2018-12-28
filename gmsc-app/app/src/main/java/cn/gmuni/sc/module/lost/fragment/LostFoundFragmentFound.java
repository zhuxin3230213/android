package cn.gmuni.sc.module.lost.fragment;

import android.view.View;

import cn.gmuni.sc.R;

public class LostFoundFragmentFound extends LostFoundFragment {

    /**
     * 初始化布局
     *
     * @return 布局文件的id。
     */
    @Override
    public int getLayoutRes() {
        return R.layout.fragment_lost_found_found;
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
        params.put("lfType", "1");
        return R.id.lost_found_list_view_found;
    }

    @Override
    protected void managerArguments() {

    }
    @Override
    public void showEmpty(){
        findViewById(R.id.lost_found_list_view_found_empty).setVisibility(View.VISIBLE);
    }
    @Override
    public void hideEmpty(){
        findViewById(R.id.lost_found_list_view_found_empty).setVisibility(View.INVISIBLE);
    }
}
