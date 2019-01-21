package cn.gmuni.sc;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import cn.gmuni.sc.base.BaseActivity;
import cn.gmuni.sc.module.home.HomeFragment;
import cn.gmuni.sc.module.me.MeFragment;
import cn.gmuni.sc.module.notice.NoticeFragment;
import cn.gmuni.sc.widget.CustomConstraintLayout;

public class MainActivity extends BaseActivity {


    @BindView(R.id.nv_main)
    BottomNavigationView navigationView;

    //fragment的标记
    private static final String HOME_FLAG = "HOME_FLAG";
    private static final String NOTICE_FLAG = "NOTICE_FLAG";
    private static final String ME_FLAG = "ME_FLAG";

    private FragmentManager fragmentManager;

    //存储当前Fragment的标记
    private String mCurrentIndex;

    HomeFragment homeFragment;
    NoticeFragment noticeFragment;
    MeFragment meFragment;

    //判断是否重新打开App
    boolean isRestart = false;


    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        fragmentManager = getBaseFragmentManager();
        String startPage = HOME_FLAG;
        if (savedInstanceState != null) {
            initByRestart(savedInstanceState);
        } else {
            switchFragment(startPage);
            mCurrentIndex = startPage;
        }
        navigationView.setItemIconTintList(null); //设置icon同步变化

        navigationView.setOnNavigationItemSelectedListener(listener);
    }


    private void initByRestart(Bundle saveInstanceState) {
        mCurrentIndex = saveInstanceState.getString("mCurrentIndex");
        isRestart = true;
        homeFragment = (HomeFragment) fragmentManager.findFragmentByTag(HOME_FLAG);
        meFragment = (MeFragment) fragmentManager.findFragmentByTag(ME_FLAG);
        noticeFragment = (NoticeFragment) fragmentManager.findFragmentByTag(NOTICE_FLAG);
        switchFragment(mCurrentIndex);
    }


    /**
     * 底部菜单栏点击切换
     */
    private BottomNavigationView.OnNavigationItemSelectedListener listener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            String idx = null;
            switch (menuItem.getItemId()) {
                case R.id.navigation_home:
                    idx = HOME_FLAG;
                    break;
                case R.id.navigation_notice:
                    idx = NOTICE_FLAG;
                    break;
                case R.id.navigation_me:
                    idx = ME_FLAG;
                    break;
            }
            if (mCurrentIndex.equals(idx)) {
                return false;
            }
            switchFragment(idx);
            switchIcon(menuItem);

            return true; //设置字体颜色同步变化
        }

    };

    //设置nav按钮图标
    private boolean switchIcon(MenuItem menuItem) {
        resetToDefaultIcon();
        switch (menuItem.getItemId()) {
            case R.id.navigation_home:
                menuItem.setIcon(R.drawable.icon_zhuye);
                return true;
            case R.id.navigation_notice:
                menuItem.setIcon(R.drawable.icon_tongzhi);
                return true;
            case R.id.navigation_me:
                menuItem.setIcon(R.drawable.icon_wo);
                return true;
        }
        return false;
    }


    //切换Fragment
    private void switchFragment(String index) {
        hideAllFragment();
        switch (index) {
            case HOME_FLAG:
                showHomeFragment();
                break;
            case ME_FLAG:
                showMeFragment();
                break;
            case NOTICE_FLAG:
                showNoticeFragment();
                break;
        }
        mCurrentIndex = index;
    }

    private void showHomeFragment() {
        if (homeFragment == null) {
            homeFragment = HomeFragment.newInstance();
            addFragment(R.id.container, homeFragment, HOME_FLAG);
        } else if (isRestart) {
            getFragmentTransaction().show(homeFragment).commit();
            isRestart = false;
        } else {
            showFragment(homeFragment);
        }
    }

    private void showNoticeFragment() {
        if (noticeFragment == null) {
            noticeFragment = NoticeFragment.newInstance();
            addFragment(R.id.container, noticeFragment, NOTICE_FLAG);
        } else if (isRestart) {
            getFragmentTransaction().show(noticeFragment).commit();
            isRestart = false;
        } else {
            showFragment(noticeFragment);
        }
    }

    private void showMeFragment() {
        if (meFragment == null) {
            meFragment = MeFragment.newInstance();
            addFragment(R.id.container, meFragment, ME_FLAG);
        } else if (isRestart) {
            getFragmentTransaction().show(meFragment).commit();
            isRestart = false;
        } else {
            showFragment(meFragment);
        }
    }

    /**
     * 隐藏所有的Fragment
     */
    private void hideAllFragment() {
        if (homeFragment != null) {
            hideFragment(homeFragment);
        }
        if (meFragment != null) {
            hideFragment(meFragment);
        }
        if (noticeFragment != null) {
            hideFragment(noticeFragment);
        }
    }


    /**
     * 重置导航默认图标
     */
    private void resetToDefaultIcon() {
        MenuItem home = navigationView.getMenu().findItem(R.id.navigation_home);
        home.setIcon(R.drawable.icon_zhuye_01);
        MenuItem notice = navigationView.getMenu().findItem(R.id.navigation_notice);
        notice.setIcon(R.drawable.icon_tongzhi_01);
        MenuItem me = navigationView.getMenu().findItem(R.id.navigation_me);
        me.setIcon(R.drawable.icon_wo_01);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("mCurrentIndex", mCurrentIndex);
        Logger.i("保存状态");
    }

    @Override
    public CustomConstraintLayout getContainer() {

        return (CustomConstraintLayout) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
    }


    //    @Override
//    protected void setStatusView() {
//        Window window = this.getWindow();
//        View decorView =window.getDecorView();
//        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.setStatusBarColor(Color.TRANSPARENT);
//        decorView.setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//   }
}
