package cn.gmuni.sc.module.schoolbus;


import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Map;

import butterknife.BindView;
import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseCommonActivity;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.module.schoolbus.fragment.SchoolBusFragmentGeneral;
import cn.gmuni.sc.module.schoolbus.fragment.SchoolBusFragmentSummer;
import cn.gmuni.sc.module.schoolbus.fragment.SchoolBusFragmentWinter;
import cn.gmuni.sc.network.Network;
import cn.gmuni.sc.network.api.ISchoolBusApi;
import cn.gmuni.sc.utils.MToast;

public class SchoolBusActivity extends BaseCommonActivity {

    @BindView(R.id.txt_main_layout)
    LinearLayout toolBarLayout;

    private View mReView;// 子Layout要以view的形式加入到主Layo
    private RelativeLayout summer; //夏季
    private RelativeLayout winter; //冬季
    private ImageView summerImg;
    private TextView summerTitle;
    private ImageView winterImg;
    private TextView winterTitle;

    private FragmentManager fragmentManager;
    //fragment标记
    private static final String SUMMER = "school_bus_frag_summer";
    private static final String WINTER = "school_bus_frag_winter";
    private static final String GENERAL = "school_bus_frag_general";
    private String selectedTab;

    //夏季
    SchoolBusFragmentSummer schoolBusFragmentSummer;
    //冬季
    SchoolBusFragmentWinter schoolBusFragmentWinter;
    //通用
    SchoolBusFragmentGeneral schoolBusFragmentGeneral;

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_school_bus);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        //管理Activity中的fragments
        fragmentManager = getBaseFragmentManager();
        //先判断 ：通用还是冬夏季,获取类型
        checkSeasonType();
    }

    //判断季节类型
    private void checkSeasonType() {
        showLoading();
        Network.request(Network.createApi(ISchoolBusApi.class).getCheckSeasonType(), new Network.IResponseListener<Map<String, String>>() {
            @Override
            public void onSuccess(Map<String, String> data) {
                if (data.size() != 0) {
                    String type = String.valueOf(data.get("type"));
                    if ("general".equals(type)) {
                        //如果是通用
                        schoolBusFragmentGeneral = new SchoolBusFragmentGeneral();
                        addFragment(R.id.school_bus_tab_content_fram, schoolBusFragmentGeneral, GENERAL);
                    } else {
                        //如果是冬夏季
                        setSeason(); //设置顶部布局及事件效果
                        //切换fragment
                        switchFragment(SUMMER);
                    }
                } else {
                    //通用
                    schoolBusFragmentGeneral = new SchoolBusFragmentGeneral();
                    addFragment(R.id.school_bus_tab_content_fram, schoolBusFragmentGeneral, GENERAL);
                }
                hideLoading();
            }

            @Override
            public void onFail(int code, String message) {
                MToast.showShortToast("获取季节状态失败");
                hideLoading();
            }
        });
    }

    //切换fragment
    private void switchFragment(String index) {
        selectedTab = index;
        //hideAllFragment(); //隐藏
        switch (index) {
            case SUMMER:
                /*  if (schoolBusFragmentSummer == null) {*/
                schoolBusFragmentSummer = new SchoolBusFragmentSummer();
                addFragment(R.id.school_bus_tab_content_fram, schoolBusFragmentSummer, SUMMER);
                /* }*/
                //showFragment(schoolBusFragmentSummer);
                break;
            case WINTER:
                /* if (schoolBusFragmentWinter == null) {*/
                schoolBusFragmentWinter = new SchoolBusFragmentWinter();
                addFragment(R.id.school_bus_tab_content_fram, schoolBusFragmentWinter, WINTER);
                /*}*/
                //showFragment(schoolBusFragmentWinter);
                break;
        }
    }

    /**
     * 隐藏所有的Fragment
     */
    private void hideAllFragment() {
        if (schoolBusFragmentSummer != null) {
            hideFragment(schoolBusFragmentSummer);
        }
        if (schoolBusFragmentWinter != null) {
            hideFragment(schoolBusFragmentWinter);
        }

    }

    @Override
    public int getToolbar() {
        return R.id.school_bus_toolbar;
    }

    //设置顶部季节布局
    private void setSeason() {
        toolBarLayout.removeAllViewsInLayout();
        mReView = View.inflate(getContext(), R.layout.module_school_bus_season, null);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mReView.setLayoutParams(layoutParams);
        toolBarLayout.addView(mReView);
        summer = (RelativeLayout) findViewById(R.id.school_bus_summer_lay);
        winter = (RelativeLayout) findViewById(R.id.school_bus_winter_lay);
        summerImg = (ImageView) findViewById(R.id.school_bus_summer);
        summerTitle = (TextView) findViewById(R.id.school_bus_summer_title);
        winterImg = (ImageView) findViewById(R.id.school_bus_winter);
        winterTitle = (TextView) findViewById(R.id.school_bus_winter_title);
        summer.setOnClickListener(new MyListener()); //夏季
        winter.setOnClickListener(new MyListener()); //冬季
    }

    //自定义点击事件
    class MyListener extends OnMultiClickListener {

        @Override
        public void onMultiClick(View v) {
            switch (v.getId()) {
                case R.id.school_bus_summer_lay:
                    //点击夏季，切换图片并改变字体颜色
                    summerChange();
                    if (!SUMMER.equals(selectedTab)) {
                        switchFragment(SUMMER);
                    }
                    break;
                case R.id.school_bus_winter_lay:
                    //点击冬季，切换图片并改变字体颜色
                    winterChange();
                    if (!WINTER.equals(selectedTab)) {
                        switchFragment(WINTER);
                    }
                    break;
            }
        }
    }

    //点击夏季效果展示
    private void summerChange() {
        Resources sResources = (Resources) summerImg.getContext().getResources();
        Drawable drawable2 = sResources.getDrawable(R.drawable.schoolbus01);
        summerImg.setBackground(drawable2);
        Resources sTitleResources = summerTitle.getContext().getResources();
        ColorStateList sumCs = (ColorStateList) sTitleResources.getColorStateList(R.color.school_bus_query);
        summerTitle.setTextColor(sumCs);

        Resources wResource = (Resources) winterImg.getContext().getResources();
        Drawable drawable3 = wResource.getDrawable(R.drawable.schoolbus02);
        winterImg.setBackground(drawable3);
        Resources wTitleresource = winterTitle.getContext().getResources();
        ColorStateList WCs = (ColorStateList) wTitleresource.getColorStateList(R.color.fontColorWhite);
        winterTitle.setTextColor(WCs);
    }

    //点击冬季效果展示
    private void winterChange() {
        Resources winImgResource = (Resources) winterImg.getContext().getResources();
        Drawable drawable = winImgResource.getDrawable(R.drawable.schoolbus04);
        winterImg.setBackground(drawable);
        Resources winTitleresource = winterTitle.getContext().getResources();
        ColorStateList WinCs = (ColorStateList) winTitleresource.getColorStateList(R.color.school_bus_query);
        winterTitle.setTextColor(WinCs);

        Resources summerImgResources = (Resources) summerImg.getContext().getResources();
        Drawable drawable1 = summerImgResources.getDrawable(R.drawable.schoolbus03);
        summerImg.setBackground(drawable1);
        Resources summerTitleResources = summerTitle.getContext().getResources();
        ColorStateList summerCs = (ColorStateList) summerTitleResources.getColorStateList(R.color.fontColorWhite);
        summerTitle.setTextColor(summerCs);
    }

}
