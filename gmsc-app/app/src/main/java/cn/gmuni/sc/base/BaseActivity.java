package cn.gmuni.sc.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.message.PushAgent;

import butterknife.ButterKnife;
import cn.gmuni.sc.app.BaseApplication;
import cn.gmuni.sc.listeners.OnPermissionListener;
import cn.gmuni.sc.model.entities.UserInfoEntity;
import cn.gmuni.sc.utils.DrawableUtil;
import cn.gmuni.sc.utils.MToast;
import cn.gmuni.sc.utils.PixelUtil;
import cn.gmuni.sc.utils.SecurityUtils;
import cn.gmuni.sc.widget.CustomConstraintLayout;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;


public abstract class BaseActivity extends SwipeBackActivity implements IBaseView {


    //动态获取权限
    final RxPermissions rxPermissions = new RxPermissions(this);

    //记录输入法是否显示
    private boolean isInputShow = false;

    protected SwipeBackLayout swipeBackLayout;

    //初始化布局
    public abstract void initContentView(Bundle savedInstanceState);

    //初始化控件
    public abstract void initView(Bundle savedInstanceState);

    public void beforeInitContentView(Bundle savedInstanceState) {

    }

    public void beforeInitView(Bundle savedInstanceState) {

    }

    public void afterInitView(Bundle savedInstanceState) {

    }

    public void beforeCreate(Bundle savedInstanceState){

    }

    /**
     * 获取CustomConstraintLayout容器id,方便显示加载进度
     * @return
     */
    public CustomConstraintLayout getContainer(){
        return null;
    }


    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        beforeCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        //禁止横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //友盟统计应用启动数据 如果不调用此方法，不仅会导致按照”几天不活跃”条件来推送失效
        PushAgent.getInstance(getContext()).onAppStart();
        //将当前activity进行记录
        BaseApplication.getInstance().addActivity(this);
        setStatusView();
        //右滑返回
        swipeBackLayout = getSwipeBackLayout();
        swipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        swipeBackLayout.setEnableGesture(isSupportSwipeBack());

        beforeInitContentView(savedInstanceState);

        //初始化布局
        initContentView(savedInstanceState);
        setGlobalLayoutListener();
        ButterKnife.bind(this);
        beforeInitView(savedInstanceState);
        initView(savedInstanceState);
        afterInitView(savedInstanceState);
    }

    protected void setStatusView(){
        int identifier = getResources().getIdentifier("statusBarBackground", "id", "android");
        View statusBarView  = getWindow().findViewById(identifier);
        if(statusBarView !=null){
            statusBarView.setBackground(DrawableUtil.getDefaultGradientDrawable());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void scrollToFinishActivity() {
        super.scrollToFinishActivity();
    }

    /**
     * 在关闭activity时结束当前activity
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseApplication.getInstance().finishActivity(this);
    }

    /**
     * 监听全局布局变化
     */
    private void setGlobalLayoutListener(){
        View root = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                onGlobalLayoutChange();
                Rect rect = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                int height = PixelUtil.getWindowHeight() - rect.bottom;
                if(height > 100){
                    if(!isInputShow){
                        isInputShow = true;
                        onShowInput(height);
                    }
                }else{
                    if(isInputShow){
                        isInputShow = false;
                        onHideInput();
                    }
                }
            }
        });
    }

    /**
     * 如果需要监听全局布局变化，可以重写此方法
     */
    protected void onGlobalLayoutChange(){

    }

    /**
     * 在输入法显示时调用
     * @param height
     */
    protected void onShowInput(int height){
        Logger.i("输入法已打开");
    }

    /**
     * 在输入法关闭时调用
     */
    protected void onHideInput(){
        Logger.i("输入法已关闭");
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }



    public FragmentManager getBaseFragmentManager() {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        return fragmentManager;
    }


    //--------------------------Fragment相关--------------------------//

    /**
     * 获取Fragment事务管理
     *
     * @return
     */
    public FragmentTransaction getFragmentTransaction() {
        return getBaseFragmentManager().beginTransaction();
    }

    /**
     * 替换一个Fragment并设置是否加入回退栈
     *
     * @param res
     * @param fragment
     * @param isAddToBackStack
     */
    public void replaceFragment(int res, BaseFragment fragment, boolean isAddToBackStack) {
        FragmentTransaction fragmentTransaction = getFragmentTransaction();
        fragmentTransaction.replace(res, fragment);
        if (isAddToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();

    }

    /**
     * 添加一个Fragment
     *
     * @param res
     * @param fragment
     */
    public void addFragment(int res, Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentTransaction();
        fragmentTransaction.add(res, fragment);
        fragmentTransaction.commit();
    }

    public void addFragment(int res, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getFragmentTransaction();
        fragmentTransaction.add(res, fragment, tag);
        fragmentTransaction.commit();
    }

    /**
     * 移除一个Fragment
     *
     * @param fragment
     */
    public void removeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
    }

    /**
     * 显示一个Fragment
     *
     * @param fragment
     */
    public void showFragment(Fragment fragment) {
        if (fragment.isHidden()) {
            FragmentTransaction fragmentTransaction = getFragmentTransaction();
            fragmentTransaction.show(fragment);
            fragmentTransaction.commit();
        }
    }

    /**
     * 隐藏一个Fragment
     *
     * @param fragment
     */
    public void hideFragment(Fragment fragment) {
        if (!fragment.isHidden()) {
            FragmentTransaction fragmentTransaction = getFragmentTransaction();
            fragmentTransaction.hide(fragment);
            fragmentTransaction.commit();
        }
    }


    /**
     * 替换一个Fragment
     *
     * @param res
     * @param fragment
     */
    public void replaceFragment(int res, BaseFragment fragment) {
        replaceFragment(res, fragment, false);
    }


    /**
     * 显示加载进度
     */
    public void showLoading() {
        CustomConstraintLayout container = getContainer();
        if(container!=null){
            container.showLoading();
        }
    }

    /**
     * 关闭加载进度
     */
    public void hideLoading() {
        CustomConstraintLayout container = getContainer();
        if(container!=null){
            container.hideLoading();
        }
    }


    @Override
    public Context getContext() {
        return this;
    }


    /**
     * 动态获取单个权限
     * @param permission
     */
    public void requestPermission(String permission,OnPermissionListener listener){
        rxPermissions.request(permission)
                .subscribe(granted -> {
                    if(granted){
                        //如果申请成功
                        listener.onReceive();
                    }else{
                        listener.onReject(permission);
                    }
                });
    }

    /**
     * 动态申请多个权限
     * @param permission
     * @param listener
     */
    public void requestPermissions(String[] permissions,OnPermissionListener listener){
        rxPermissions.request(permissions).subscribe(granted->{
            //上次
            if (granted){
                listener.onReceive();
            }else{
                listener.onReject(permissions);
            }
        });
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
     * 是否支持滑动
     * @return
     */
    public boolean isSupportSwipeBack(){
        return false;
    }
}
