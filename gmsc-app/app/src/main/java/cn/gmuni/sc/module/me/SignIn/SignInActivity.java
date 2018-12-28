package cn.gmuni.sc.module.me.SignIn;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseCommonActivity;
import cn.gmuni.sc.database.IntegralDetailDao;
import cn.gmuni.sc.database.UserInfoDao;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.model.entities.IntegralDetailEntity;
import cn.gmuni.sc.module.home.scan.model.Scan;
import cn.gmuni.sc.network.Network;
import cn.gmuni.sc.network.api.ISignInApi;
import cn.gmuni.sc.utils.DateUtils;
import cn.gmuni.sc.utils.GPSUtils;
import cn.gmuni.sc.utils.MToast;
import cn.gmuni.sc.utils.PixelUtil;
import cn.gmuni.sc.utils.SecurityUtils;
import cn.gmuni.sc.widget.CustomCalendarView;
import cn.gmuni.sc.widget.CustomSignInDialog;

public class SignInActivity extends BaseCommonActivity {


    @BindView(R.id.sign_in_top_one_sign)
    TextView sign_in_top_one_sign;
    @BindView(R.id.sign_in_top_one_sign_hide)
    TextView sign_in_top_one_sign_hide;
    @BindView(R.id.sign_in_top_one_integral_count)
    TextView sign_in_top_one_integral_count; //累计签到积分
    @BindView(R.id.sign_in_top_two_statistics)
    TextView sign_in_top_two_statistics; //签到统计
    @BindView(R.id.sign_in_center_one_left)
    TextView sign_in_center_one_left;  //左边日期、
    @BindView(R.id.sign_in_center_one_center)
    TextView sign_in_center_one_center; //中间日期
    @BindView(R.id.sign_in_center_one_right)
    TextView sign_in_center_one_right;  //右边日期

    private LinearLayout sign_in_center_two;
    private IntegralDetailDao iIntegralDetailDao = new IntegralDetailDao();
    private UserInfoDao userInfoDao = new UserInfoDao();
    private String signInIntegral; //获取签到任务积分设置的值
    CustomCalendarView customCalendarView;

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_sign_in);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        //页面初始化
        //1.获取当天的签到状态，根究任务编码与年月日查询此积分详情对象是否存在（sqlite中查询）
        String taskCode = SignInConstant.SIGNIN_TASKCODE;
        //如果存在对象存在则隐藏签到按钮，如果不存在则不隐藏
        //查询当天签到状态
        checkSignInState(taskCode, String.valueOf(DateUtils.getCurrYear()),
                String.valueOf(DateUtils.getCurrMonth()), String.valueOf(DateUtils.getCurrDay()));
        //2.获取累计积分，签到排名，连续天数(根据任务编码、用户去积分统计表中查询此对象)
        initData(taskCode);
        //1>如果（不存在此对象）从来没有签到过，则设置累计积分、签到排名、连续天数为0
        //2>签到过则去积分统计表中去查询获取
        //3.初始化年月组件
        Date date = new Date();
        String center = currentMonth(date);
        String left = clickLeftMonth(date);
        String right = clickRightMonth(date);
        sign_in_center_one_left.setText(left);
        sign_in_center_one_center.setText(center);
        sign_in_center_one_right.setText(right);
        //*4.初始化日历组件（根据任务编码、用户、学校、年月查询本月的积分详情列表获取每日的签到状态）（后台中查询）---获取当天的日历状态
        initCalendarData(taskCode, String.valueOf(DateUtils.getCurrYear()), String.valueOf(DateUtils.getCurrMonth()));
        //*5.选择年月，根据任务编码、用户、学校、年月查询本月的积分详情列表获取每日的签到状态（sqlite中查询）
        //日历组件中，当天以前设置（已签到的设置背景图片及颜色）
        sign_in_center_one_left.setOnClickListener(new MyListener());
        sign_in_center_one_center.setOnClickListener(new MyListener());
        sign_in_center_one_right.setOnClickListener(new MyListener());
        //6.点击签到按钮
        //签到后弹窗取消事件
        sign_in_top_one_sign.setOnClickListener(new MyListener());
    }

    //去后台查询当天签到状态
    private void checkSignInState(String taskCode, String year, String month, String day) {
        Map<String, String> map = new HashMap<>();
        map.put("taskCode", taskCode);
        map.put("year", year);
        map.put("month", month);
        map.put("day", day);
        getCheckSignInStateData(map); //获取当天签到状态（对象）
    }

    //获取当天签到状态（对象）
    private void getCheckSignInStateData(Map<String, String> params) {
        showLoading();
        Network.request(Network.createApi(ISignInApi.class).listByTaskCode(params), new Network.IResponseListener<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> data) {
                if (data != null) {
                    //有此对象，则隐藏该签到按钮
                    sign_in_top_one_sign.setVisibility(View.GONE);
                    sign_in_top_one_sign_hide.setVisibility(View.VISIBLE);
                    hideLoading();
                } else {
                    //后台也没有，则显示签到按钮
                    sign_in_top_one_sign.setVisibility(View.VISIBLE);
                    sign_in_top_one_sign_hide.setVisibility(View.GONE);
                    hideLoading();
                }
            }

            @Override
            public void onFail(int code, String message) {
                MToast.showShortToast("获取当天签到状态失败");
                hideLoading();
            }
        });
    }


    //初始化统计结果
    private void initData(String taskCode) {
        Map<String, String> map = new HashMap<>();
        map.put("taskCode", taskCode);
        map.put("currentTime", "");
        getData(map); //获取数据
    }

    //获取数据
    private void getData(Map<String, String> map) {
        showLoading();
        Network.request(Network.createApi(ISignInApi.class).findIntegralStatisticsByTakcodeAndUserInfo(map), new Network.IResponseListener<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> data) {
                if (data != null) {
                    hadleInitData(data); //处理返回的数据
                    hideLoading();
                } else {
                    //从来没有签到过
                    hideLoading();
                    sign_in_top_one_integral_count.setText("0");
                    sign_in_top_two_statistics.setText("今日未签到");
                }
            }

            @Override
            public void onFail(int code, String message) {
                MToast.showShortToast("获取数据失败");
                hideLoading();
            }
        });
    }

    //处理初始化返回的数据
    private void hadleInitData(Map<String, Object> data) {
        Map<String, Object> integralStatistics = (Map<String, Object>) data.get("integralStatistics");
        String integralTotal = String.valueOf(integralStatistics.get("integralTotal")); //累计积分
        String ranking = String.valueOf(data.get("ranking")); //签到排名
        String countInte = String.valueOf(integralStatistics.get("countInte")); //连续排名
        String currentTime = String.valueOf(integralStatistics.get("currentTime")); //获取当前时间
        String today = DateUtils.date2String(new Date(), DateUtils.LONG_DATE);

        sign_in_top_one_integral_count.setText(integralTotal); //签到积分总数

        //判断今天排名，从统计表中获取的当前时间是否与今天相同，若相同则今天签到过，若不同今天没有签到过
        if (currentTime.contains(today)) {
            sign_in_top_two_statistics.setText("今日第" + ranking + "名签到者，已连续签到" + countInte + "天～");
        } else {
            sign_in_top_two_statistics.setText("今日未签到，已连续签到" + countInte + "天～");
        }

    }

    //初始化日历组件
    private void initCalendarData(String taskCode, String year, String month) {
        // 根据任务编码、用户、学校、年月查询本月的积分详情列表获取每日的签到状态
        Map<String, String> map = new HashMap<>();
        map.put("taskCode", taskCode);
        map.put("year", year);
        map.put("month", month);
        getCurrentMonthSignInStateData(map);//获取数据
    }

    // 根据任务编码、用户、学校、年月查询本月的积分详情列表获取每日的签到状态(列表)
    private void getCurrentMonthSignInStateData(Map<String, String> params) {
        showLoading();
        Network.request(Network.createApi(ISignInApi.class).listByMonth(params), new Network.IResponseListener<List<Map<String, Object>>>() {
            @Override
            public void onSuccess(List<Map<String, Object>> data) {
                if (data.size() != 0) {
                    handleCurrentMonthSignInStateData(data); //处理数据
                    hideLoading();
                } else {
                    List<IntegralDetailEntity> list = new ArrayList<>();//空集合
                    //添加日历组件至布局文件
                    sign_in_center_two = (LinearLayout) findViewById(R.id.sign_in_center_two);
                    ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    customCalendarView = new CustomCalendarView(sign_in_center_two.getContext(), new Date(), list);
                    sign_in_center_two.addView(customCalendarView, layoutParams);
                    hideLoading();
                }
            }

            @Override
            public void onFail(int code, String message) {
                MToast.showShortToast("获取当月签到列表失败");
                hideLoading();
            }
        });
    }

    // 处理数据
    private void handleCurrentMonthSignInStateData(List<Map<String, Object>> data) {
        List<IntegralDetailEntity> list = new ArrayList<>();
        for (Map<String, Object> temp : data) {
            IntegralDetailEntity integralDetailEntity = new IntegralDetailEntity();
            integralDetailEntity.setInDay(String.valueOf(temp.get("day"))); //获取天
            list.add(integralDetailEntity);
        }
        //添加日历组件至布局文件
        sign_in_center_two = (LinearLayout) findViewById(R.id.sign_in_center_two);
        ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        customCalendarView = new CustomCalendarView(sign_in_center_two.getContext(), new Date(), list);
        sign_in_center_two.addView(customCalendarView, layoutParams);

    }

    //选择日历组件
    private void checkCalendarData(String taskCode, String year, String month, Date date) {
        // 根据任务编码、用户、学校、年月查询本月的积分详情列表获取每日的签到状态
        Map<String, String> map = new HashMap<>();
        map.put("taskCode", taskCode);
        map.put("year", year);
        map.put("month", month);
        getSelectSignInStateData(map, date);//日历选择获取月份签到状态
    }

    //日历选择获取月份签到状态
    private void getSelectSignInStateData(Map<String, String> params, Date date) {
        showLoading();
        Network.request(Network.createApi(ISignInApi.class).listByMonth(params), new Network.IResponseListener<List<Map<String, Object>>>() {
            @Override
            public void onSuccess(List<Map<String, Object>> data) {
                if (data.size() != 0) {
                    List<IntegralDetailEntity> list = new ArrayList<>();
                    for (Map<String, Object> temp : data) {
                        IntegralDetailEntity integralDetailEntity = new IntegralDetailEntity();
                        integralDetailEntity.setInDay(String.valueOf(temp.get("day"))); //获取天
                        list.add(integralDetailEntity);
                    }
                    customCalendarView.refesh(date, list);
                    hideLoading();
                } else {
                    List<IntegralDetailEntity> list = new ArrayList<>();
                    customCalendarView.refesh(date, list);
                    hideLoading();
                }
            }

            @Override
            public void onFail(int code, String message) {
                MToast.showShortToast("日历选择获取月份签到状态失败");
                hideLoading();
            }
        });
    }


    //上一月
    public String clickLeftMonth(Date curDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(curDate);
        calendar.add(Calendar.MONTH, -1);
        Date recurDate = calendar.getTime();
        String s = DateUtils.date2String(recurDate, DateUtils.COMMON_DATETIME);
        return s.substring(0, 4) + "年" + s.substring(5, 7) + "月";
    }

    //当月
    public String currentMonth(Date curDate) {
        String s = DateUtils.date2String(curDate, DateUtils.COMMON_DATETIME);
        return s.substring(0, 4) + "年" + s.substring(5, 7) + "月";
    }

    //下一月
    public String clickRightMonth(Date curDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(curDate);
        calendar.add(Calendar.MONTH, 1);
        Date recurDate = calendar.getTime();
        String s = DateUtils.date2String(recurDate, DateUtils.COMMON_DATETIME);
        return s.substring(0, 4) + "年" + s.substring(5, 7) + "月";
    }

    @Override
    public int getToolbar() {
        return R.id.sign_in_toolbar;
    }

    //自定义点击事件
    class MyListener extends OnMultiClickListener {
        @Override
        public void onMultiClick(View v) {
            switch (v.getId()) {
                case R.id.sign_in_top_one_sign:
                    //根就任务编码获取任务积分integral
                    //去中间层任务表查询（根就任务编码查询返回任务积分,跳转弹窗）
                    getSignInIntegral();
                    sign_in_top_one_sign.setVisibility(View.GONE);
                    sign_in_top_one_sign_hide.setVisibility(View.VISIBLE);
                    break;
                case R.id.sign_in_center_one_left:
                    String center = sign_in_center_one_left.getText().toString();
                    String right = sign_in_center_one_center.getText().toString();
                    //根据left时间获取前一个月
                    String left = sign_in_center_one_left.getText().toString();
                    String formatLeft = left.substring(0, 4) + "-" + left.substring(5, 7);
                    Date date = DateUtils.string2Date(formatLeft, DateUtils.YEAR_MONTH);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.MONTH, -1);
                    Date time = calendar.getTime();
                    String s = DateUtils.date2String(time, DateUtils.YEAR_MONTH);
                    String reLeft = s.substring(0, 4) + "年" + s.substring(5, 7) + "月";
                    sign_in_center_one_left.setText(reLeft);
                    sign_in_center_one_center.setText(center);
                    sign_in_center_one_right.setText(right);

                    String taskCode = SignInConstant.SIGNIN_TASKCODE;
                    //去后台查询返回集合

                    checkCalendarData(taskCode, left.substring(0, 4), left.substring(5, 7), date);
                    break;

                case R.id.sign_in_center_one_center:
                    String cCenter = sign_in_center_one_center.getText().toString();
                    String formatCenter = cCenter.substring(0, 4) + "-" + cCenter.substring(5, 7);
                    Date date2 = DateUtils.string2Date(formatCenter, DateUtils.YEAR_MONTH);

                    String ctaskCode = SignInConstant.SIGNIN_TASKCODE;
                    checkCalendarData(ctaskCode, cCenter.substring(0, 4), cCenter.substring(5, 7), date2);
                    break;
                case R.id.sign_in_center_one_right:
                    String rLeft = sign_in_center_one_center.getText().toString();
                    String rCenter = sign_in_center_one_right.getText().toString();
                    //根据right时间获取下个月
                    String rRight = sign_in_center_one_right.getText().toString();
                    String formatRight = rRight.substring(0, 4) + "-" + rRight.substring(5, 7);
                    Date date1 = DateUtils.string2Date(formatRight, DateUtils.YEAR_MONTH);
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.setTime(date1);
                    calendar1.add(Calendar.MONTH, 1);
                    Date time1 = calendar1.getTime();
                    String s1 = DateUtils.date2String(time1, DateUtils.YEAR_MONTH);
                    String reRight = s1.substring(0, 4) + "年" + s1.substring(5, 7) + "月";
                    sign_in_center_one_left.setText(rLeft);
                    sign_in_center_one_center.setText(rCenter);
                    sign_in_center_one_right.setText(reRight);


                    String rtaskCode = SignInConstant.SIGNIN_TASKCODE;
                    checkCalendarData(rtaskCode, rRight.substring(0, 4), rRight.substring(5, 7), date1);

                    break;
            }

        }
    }

    //根就任务编码获取任务积分integral
    private void getSignInIntegral() {
        Map<String, String> map = new HashMap<>();
        map.put("taskCode", SignInConstant.SIGNIN_TASKCODE);
        getSignInIntegralData(map); //获取数据
    }

    //获取数据
    private void getSignInIntegralData(Map<String, String> params) {
        showLoading();
        Network.request(Network.createApi(ISignInApi.class).findTaskByCode(params), new Network.IResponseListener<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> data) {
                if (data != null) {
                    Map<String, Object> integralTask = (Map<String, Object>) data.get("integralTask");
                    signInIntegral = String.valueOf(integralTask.get("integralSet"));
                    hideLoading();
                    //点击签到按钮跳出弹窗，传参任务积分
                    final CustomSignInDialog dialog = new CustomSignInDialog(getContext(), signInIntegral);
                    dialog.show(); //显示
                    //调用这个方法时，按对话框以外的地方无法响应。按返回键还可以响应。
                    dialog.setCanceledOnTouchOutside(false);
                    //调用这个方法时，按对话框以外的地方和按返回键都无法响应。
                    dialog.setCancelable(false);
                    //设置弹窗大小、位置
                    //获取屏幕宽度
                    DisplayMetrics dm = getResources().getDisplayMetrics();
                    int screenWidth = dm.widthPixels;
                    int screenHeight = dm.heightPixels;
                    Window window = dialog.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    window.getDecorView().setPadding(PixelUtil.dp2px(75), 0, PixelUtil.dp2px(75), PixelUtil.dp2px(40));

                    final WindowManager.LayoutParams params = window.getAttributes();
                    params.gravity = Gravity.CENTER;
                  /*  params.width = screenWidth - PixelUtil.dp2px(150);
                    params.height = screenHeight- PixelUtil.dp2px(150);*/

                    dialog.getWindow().setAttributes(params);

                    //弹窗监听事件
                    dialog.setClicklistener(new CustomSignInDialog.ClickListenerInterface() {
                        @Override
                        public void doCancel() {
                            dialog.dismiss();
                            initCancleData(); //处理取消后数据渲染
                        }
                    });
                } else {
                    MToast.showLongToast("签到任务获取失败");
                }
            }

            @Override
            public void onFail(int code, String message) {
                MToast.showShortToast("获取数据失败");
                signInIntegral = "";
                hideLoading();
            }
        });
    }


    //根据任务编码获取添加积分详情及统计
    private void initCancleData() {
        //取消弹窗后
        // 1.隐藏签到按钮，当天日期加签到状态(设置背景图片)
        // 2.从新获取积分累计、签到排名、签到连续状态
        Map<String, String> map = new HashMap<>();
        map.put("taskCode", SignInConstant.SIGNIN_TASKCODE);
        getCancleData(map); //获取数据
    }

    //取消后获取数据
    private void getCancleData(Map<String, String> params) {
        showLoading();
        Network.request(Network.createApi(ISignInApi.class).signIn(params), new Network.IResponseListener<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> data) {
                if (data != null) {
                    handleData(data); //处理数据
                    hideLoading();
                } else {
                    MToast.showLongToast("签到失败");
                }
            }

            @Override
            public void onFail(int code, String message) {
                MToast.showShortToast("获取数据失败");
                hideLoading();
            }
        });
    }

    //处理数据
    private void handleData(Map<String, Object> data) {
        Map<String, Object> integralDetail = (Map<String, Object>) data.get("integralDetail");
        Map<String, Object> integralStatistics = (Map<String, Object>) data.get("integralStatistics");

        String taskCode = String.valueOf(integralStatistics.get("taskCode")); //任务编码
        String inYear = String.valueOf(integralDetail.get("year"));
        String inMonth = String.valueOf(integralDetail.get("month"));
        String inDay = String.valueOf(integralDetail.get("day"));
        String userInfo = String.valueOf(integralDetail.get("userInfo"));
        String campus = String.valueOf(integralDetail.get("campus"));
        String integral = String.valueOf(integralDetail.get("integral"));
        String state = String.valueOf(integralDetail.get("state"));


        String integralTotal = String.valueOf(integralStatistics.get("integralTotal")); //累计积分
        String ranking = String.valueOf(data.get("ranking")); //签到排名
        String countInte = String.valueOf(integralStatistics.get("countInte")); //连续签到名次


        sign_in_top_one_integral_count.setText(integralTotal); //签到积分总数
        sign_in_top_two_statistics.setText("今日第" + ranking + "名签到者，已连续签到" + countInte + "天～");

        //保存签到详情（保存到sqlite）
        //先根据任务编码与年月日查询对象是否存在
        IntegralDetailEntity integralDetailEntity = iIntegralDetailDao.listByTaskCode(taskCode, userInfo, campus, inYear, inMonth, inDay);
        if (iIntegralDetailDao != null) {
            if (integralDetailEntity == null) {
                //执行添加操作
                IntegralDetailEntity model = new IntegralDetailEntity();
                model.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                model.setInYear(inYear);
                model.setInMonth(inMonth);
                model.setInDay(inDay);
                model.setUserInfo(userInfo);
                model.setCampus(campus);
                model.setTaskCode(taskCode);
                model.setIntegral(integral);
                model.setState(state); //积分状态: 0:未获取   1:获取
                iIntegralDetailDao.saveOrUpdate(model); //保存
            } else {
                //iIntegralDetailDao.deleteAll(); //删除所有
            }
        }

        //签到成功后渲染当天签到状态
        Date today = new Date();
        Date date = DateUtils.string2Date(DateUtils.date2String(today, DateUtils.COMMON_DATETIME), DateUtils.YEAR_MONTH);
        checkCalendarData(taskCode, inYear, inMonth, date);
    }

}
