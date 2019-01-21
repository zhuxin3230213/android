package cn.gmuni.sc.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import cn.gmuni.sc.model.entities.UserInfoEntity;
import cn.gmuni.sc.utils.MToast;
import cn.gmuni.sc.utils.SecurityUtils;
import cn.gmuni.sc.widget.CustomConstraintLayout;

/**
 * 基础的Fragment
 */
public abstract class BaseFragment extends Fragment implements IBaseView{
    private BaseActivity mActivity;
    private View mLayoutView;

    /**
     * 初始化布局
     * @return 布局文件的id。
     */
    public abstract int getLayoutRes();

    /**
     * 初始化视图
     */
    public abstract void initView();

    /**
     * 如果Fragment创建需要数据，在这里接收传进来的数据。
     * 如果没有这个抽象方法就空实现。
     */
    protected abstract void managerArguments();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            managerArguments();
        }
    }

    public CustomConstraintLayout getContainer(){
        return getBaseActivity().getContainer();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mLayoutView != null) {
            ViewGroup parent = (ViewGroup) mLayoutView.getParent();
            if (parent != null) {
                parent.removeView(mLayoutView);
            }
        } else {
            mLayoutView = getCreateView(inflater, container);
            ButterKnife.bind(this, mLayoutView);
            initView();     //初始化布局
        }

        return mLayoutView;
    }

    /**
     * 获取Fragment布局文件的View
     *
     * @param inflater
     * @param container
     * @return
     */
    private View getCreateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(getLayoutRes(), container, false);
    }

    /**
     * 获取当前Fragment状态
     *
     * @return true为正常 false为未加载或正在删除
     */
    private boolean getStatus() {
        return (isAdded() && !isRemoving());
    }

    /**
     * 获取Activity
     *
     * @return
     */
    public BaseActivity getBaseActivity() {
        if (mActivity == null) {
            mActivity = (BaseActivity) getActivity();
        }
        return mActivity;
    }

    /**
     * 根据id获取组件
     * @param resId
     * @return
     */
    public View findViewById(int resId){
        return mLayoutView.findViewById(resId);
    }


    public void onResume() {
        super.onResume();

    }
    public void onPause() {
        super.onPause();

    }
    /**
     * 显示加载进度
     */
    public void showLoading(){
        CustomConstraintLayout container = getContainer();
        if(container!=null){
            container.showLoading();
            return;
        }
        getBaseActivity().showLoading();
    }

    /**
     * 关闭加载进度
     */
    public void hideLoading(){
        CustomConstraintLayout container = getContainer();
        if(container!=null){
            container.hideLoading();
            return;
        }
        getBaseActivity().hideLoading();
    }

    public void startActivitySecurity(Intent intent) {
        UserInfoEntity entity = SecurityUtils.getUserInfo();
        if(entity.getStudentId()==null || "".equals(entity.getStudentId())){
            MToast.showLongToast("完善学籍信息后方可使用");
            return;
        }
        super.startActivity(intent);
    }

    /**
     * 在调用返回按钮之前处理
     * @return
     */
    public void beforeGoBack(){}
}
