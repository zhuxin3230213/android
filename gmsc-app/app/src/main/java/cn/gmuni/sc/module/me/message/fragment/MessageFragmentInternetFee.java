package cn.gmuni.sc.module.me.message.fragment;

import android.view.View;

import cn.gmuni.sc.R;

public class MessageFragmentInternetFee extends MessageFragment {

    /**
     * 初始化布局
     *
     * @return 布局文件的id。
     */
    @Override
    public int getLayoutRes() {
        return R.layout.fragment_message_internet_fee;
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
        params.put("messageType","1");
        return R.id.message_list_view_internet_fee;
    }

    @Override
    protected void managerArguments() {

    }

    @Override
    public void showEmpty(){
        findViewById(R.id.message_list_view_internet_fee_empty).setVisibility(View.VISIBLE);
    }
    @Override
    public void hideEmpty(){
        findViewById(R.id.message_list_view_internet_fee_empty).setVisibility(View.GONE);
    }
}
