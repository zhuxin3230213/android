package cn.gmuni.sc.module.home;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseFragment;
import cn.gmuni.sc.config.PermissionConst;
import cn.gmuni.sc.database.UserInfoDao;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.listeners.OnPermissionListener;
import cn.gmuni.sc.model.entities.UserInfoEntity;
import cn.gmuni.sc.module.home.scan.CaptureScanActivity;
import cn.gmuni.sc.module.lost.LostFoundActivity;
import cn.gmuni.sc.module.more.MoreActivity;
import cn.gmuni.sc.module.more.express.constants.RequestCode;
import cn.gmuni.sc.module.news.NewsActivity;
import cn.gmuni.sc.module.pay.PayActivity;
import cn.gmuni.sc.module.schedule.ScheduleActivity;
import cn.gmuni.sc.module.schoolbus.SchoolBusActivity;
import cn.gmuni.sc.module.schoolcard.SchoolCardActivity;
import cn.gmuni.sc.network.Network;
import cn.gmuni.sc.network.api.INetPayApi;
import cn.gmuni.sc.network.api.IUserInfoApi;
import cn.gmuni.sc.umpush.UmPushHelper;
import cn.gmuni.sc.utils.MToast;
import cn.gmuni.sc.utils.PixelUtil;
import cn.gmuni.sc.utils.SecurityUtils;
import cn.gmuni.sc.utils.StringUtil;
import cn.gmuni.sc.widget.CustomConstraintLayout;
import cn.gmuni.sc.widget.CustomDialog;
import cn.gmuni.sc.widget.CustomImageView;
import cn.gmuni.sc.widget.ImageTextButton;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends BaseFragment {

    @BindView(R.id.home_content_wrap)
    RelativeLayout wrapContent;

    @BindView(R.id.home_header_text)
    TextView homeHeaderText;
    @BindView(R.id.home_user_info_name)
    TextView home_user_info_name;
    @BindView(R.id.home_scan)
    CustomImageView scan;

    private static final List<Map<String, Object>> icons = new ArrayList<>();
    private UserInfoDao userInfoDao = new UserInfoDao();
    private boolean isGetData = false;

    static {
        Map<String, Object> map = new HashMap<>();
        map.put("text", R.string.home_card);
        map.put("imgSrc", R.drawable.icon_card);
        map.put("id", R.id.home_cell_card);
        icons.add(map);
        map = new HashMap<>();
        map.put("text", R.string.home_tuition_fees);
        map.put("imgSrc", R.drawable.icon_xuefei);
        map.put("id", R.id.home_cell_tuition_fees);
        icons.add(map);
        map = new HashMap<>();
        map.put("text", R.string.home_schedule);
        map.put("imgSrc", R.drawable.icon_timetable);
        map.put("id", R.id.home_cell_schedule);
        icons.add(map);
        map = new HashMap<>();
        map.put("text", R.string.home_score);
        map.put("imgSrc", R.drawable.icon_score);
        map.put("id", R.id.home_cell_score);
        icons.add(map);
        map = new HashMap<>();
        map.put("text", R.string.home_pay);
        map.put("imgSrc", R.drawable.icon_pay);
        map.put("id", R.id.home_cell_pay);
        icons.add(map);
        map = new HashMap<>();
        map.put("text", R.string.home_news);
        map.put("imgSrc", R.drawable.icon_news);
        map.put("id", R.id.home_cell_news);

        icons.add(map);
        map = new HashMap<>();
        map.put("text", R.string.home_car);
        map.put("imgSrc", R.drawable.car);
        map.put("id", R.id.home_cell_car);
        icons.add(map);

        map = new HashMap<>();
        map.put("text", R.string.more);
        map.put("imgSrc", R.drawable.icon_more);
        map.put("id", R.id.home_cell_more);
        icons.add(map);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView() {

//        getBaseActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        ViewGroup contentView = getBaseActivity().getWindow().getDecorView().findViewById(Window.ID_ANDROID_CONTENT);
//        contentView.getChildAt(0).setFitsSystemWindows(false);
        scan.setOnClickListener(onCellIconClick); //扫一扫点击
        renderGridItems();
        chooseCollege();
    }

    /**
     * 选择学校
     */
    private void chooseCollege() {
        if (!SecurityUtils.isSettingCollege()) {
            CollectDialog dialog = new CollectDialog(getContext(), null);
            dialog.closeable(false);
            dialog.setOnSelectedListener(d -> homeHeaderText.setText(d.get("text").toString()));
        } else {
            homeHeaderText.setText(SecurityUtils.getUserInfo().getSchoolName());
            UmPushHelper.getInstance().setTags(SecurityUtils.getUserInfo().getSchool());
        }
    }

    /**
     * 选择首页菜单项
     */
    private void renderGridItems() {
        LinearLayout linearLayout = null;
        for (int i = 0; i < icons.size(); i++) {
            Map<String, Object> mp = icons.get(i);
            if (i % 4 == 0) {
                linearLayout = new LinearLayout(getContext());
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                params.bottomMargin = PixelUtil.dp2px(36);
                if (i == 0) {
                    linearLayout.setId(R.id.home_wrap_firstline);
                } else {
                    params.addRule(RelativeLayout.BELOW, R.id.home_wrap_firstline);
                }
                wrapContent.addView(linearLayout, params);
            }
            ImageTextButton btn = new ImageTextButton(getContext());
            btn.setTitle((Integer) mp.get("text"));
            btn.setImageResource((Integer) mp.get("imgSrc"));
            btn.setId((Integer) mp.get("id"));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.weight = 1;
            btn.setOnClickListener(onCellIconClick);
            linearLayout.addView(btn, layoutParams);
        }
    }

    public View.OnClickListener onCellIconClick = new OnMultiClickListener() {

        @Override
        public void onMultiClick(View view) {
            switch (view.getId()) {
                case R.id.home_cell_card:
                    startActivitySecurity(new Intent(getContext(), SchoolCardActivity.class));
                    break;
                case R.id.home_cell_tuition_fees:
                    break;
                case R.id.home_cell_schedule:
                    startActivitySecurity(new Intent(getContext(), ScheduleActivity.class));
                    break;
                case R.id.home_cell_score:
                    MToast.showShortToast("因部分接口未购买，无法使用");
//                    startActivity(new Intent(getContext(), ScoreActivity.class));
                    break;
                case R.id.home_cell_pay:
                    getNetInfoAndStartActivity();
                    break;
                case R.id.home_cell_news:
                    startActivity(new Intent(getContext(), NewsActivity.class));
                    break;
                case R.id.home_cell_lost_found:
                    startActivity(new Intent(getContext(), LostFoundActivity.class));
                    break;
                case R.id.home_cell_car:
                    startActivity(new Intent(getContext(), SchoolBusActivity.class));
                    break;
                case R.id.home_cell_more:
                    startActivity(new Intent(getContext(), MoreActivity.class));
                    break;
                case R.id.home_scan:
                    startCaptureScanActivity(); //启动扫描
                    break;
            }
        }
    };


    //获取相机权限
    private void startCaptureScanActivity() {
        getBaseActivity().requestPermission(PermissionConst.PERMISSION_CAMERA, new OnPermissionListener() {
            @Override
            public void onReceive() {
                startActivityForResult(new Intent(getContext(), CaptureScanActivity.class), RequestCode.REQUEST_CAPTURE);
            }
        });
    }

    @Override
    protected void managerArguments() {

    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        //在我的里面修改学校名称后，在此处需更新一次 TODO 待优化
        homeHeaderText.setText(SecurityUtils.getUserInfo().getSchoolName());
    }

    @Override
    public CustomConstraintLayout getContainer() {
        return super.getContainer();
    }

    //首页刷新事件
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        //   进入当前Fragment
        if (enter && !isGetData) {
            isGetData = true;
            //   这里可以做网络请求或者需要的数据刷新操作
            //首页昵称设置
            if (userInfoDao != null) {
                home_user_info_name.setText(userInfoDao.get().getName());
            }
        } else {
            isGetData = false;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

  /*  public void onResume() {
        super.onResume();
        //首页昵称设置
        if (!isGetData) {
            if (entity != null) {
                home_user_info_name.setText(entity.getName());
            }
        }

    }*/

    public void onPause() {
        super.onPause();
        isGetData = false;
    }

    /**
     * 业务逻辑：先判断是否绑定上网账号，若未绑定提示用户输入上网账号，查询账号存在与否
     * 若已绑定，直接跳转到页面
     */
    private void getNetInfoAndStartActivity() {
        UserInfoEntity userInfo = SecurityUtils.getUserInfo();
        if (StringUtil.isEmpty(userInfo.getNetAccount())) {
            CustomDialog dialog = new CustomDialog(getContext(), "输入上网账号", R.layout.fragment_pay_net_net_account);

            dialog.setOnOkDialogListener(v -> {
                EditText text = v.findViewById(R.id.pay_net_net_account);
                String account = text.getText().toString();
                Map<String, String> params = new HashMap<>();
                params.put("account", account);
                Network.request(Network.createApi(INetPayApi.class).checkNetAccountExist(params), new Network.IResponseListener<Map<String, String>>() {
                    @Override
                    public void onSuccess(Map<String, String> data) {
                        if ("t".equalsIgnoreCase(data.get("exist"))) {
                            UserInfoDao dao = new UserInfoDao();
                            userInfo.setNetAccount(account);
                            dao.saveOrUpdate(userInfo);
                            SecurityUtils.updateUserInfo();
                            dialog.close();
                            Network.request(Network.createApi(IUserInfoApi.class).updateUser(userInfo), new Network.IResponseListener<Map<String, Object>>() {
                                @Override
                                public void onSuccess(Map<String, Object> data) {

                                }

                                @Override
                                public void onFail(int code, String message) {

                                }
                            });
                            startActivitySecurity(new Intent(getContext(), PayActivity.class));
                        } else {
                            Toast.makeText(getContext(), "账号输入有误", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFail(int code, String message) {
                    }
                });
            });

            dialog.setOnCancelDialogListener(v -> dialog.close());
            dialog.show();
        } else {
            startActivitySecurity(new Intent(getContext(), PayActivity.class));
        }
    }
}
