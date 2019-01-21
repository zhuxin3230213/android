package cn.gmuni.sc.module.more.gprs;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseCommonActivity;
import cn.gmuni.sc.database.UserInfoDao;
import cn.gmuni.sc.model.GprsModel;
import cn.gmuni.sc.model.entities.UserInfoEntity;
import cn.gmuni.sc.network.Network;
import cn.gmuni.sc.network.api.IGprsApi;
import cn.gmuni.sc.utils.DateUtils;
import cn.gmuni.sc.utils.GPSUtils;
import cn.gmuni.sc.utils.MToast;
import cn.gmuni.sc.utils.SecurityUtils;
import cn.gmuni.sc.utils.StringUtil;
import cn.gmuni.sc.widget.recyclerview.CustomRefreshRecycleView;
import cn.gmuni.sc.widget.recyclerview.adapter.MultiTypeAdapter;
import cn.gmuni.sc.widget.recyclerview.model.Visitable;


public class GprsActivity extends BaseCommonActivity {

    @BindView(R.id.gprs_userName)
    TextView gprs_userName;
    @BindView(R.id.gprs_calendar)
    TextView gprs_calendar;
    @BindView(R.id.gprs_location_date_time)
    TextView gprs_location_date_time;
    @BindView(R.id.gprs_empty)
    TextView gprs_empty;
    @BindView(R.id.gprs_location_list_view)
    CustomRefreshRecycleView listView;
    private List<Visitable> list = new ArrayList<>();
    private MultiTypeAdapter multiTypeAdapter;
    private UserInfoDao userInfoDao = new UserInfoDao();
    private String currDate;
    private Integer currentPage = 1;
    private Integer pageSize = 10;

    private static final int msgKey = 1;

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_gprs);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        gprs_userName.setText(userInfoDao.get().getName());
        currDate = DateUtils.getCurrDate(DateUtils.COMMON_DATETIME);
        gprs_calendar.setText(currDate.substring(0, 4) + "." + currDate.substring(5, 7) + "." + currDate.substring(8, 10));
        new TimeThread().start(); //动态时间显示
        //gprs_location_date_time.setText(currDate.substring(11));
        initData(); //初始化数据
        //适配数据
        multiTypeAdapter = listView.initRecyclerView(list, GprsActivity.this);
    }

    //动态时间显示
    public class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = msgKey;
                    mHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case msgKey:
                    long sysTime = System.currentTimeMillis();
                    CharSequence sysTimeStr = DateFormat
                            .format(DateUtils.LONG_TIME, sysTime);
                    currDate = DateFormat.format(DateUtils.COMMON_DATETIME, sysTime).toString();
                    gprs_location_date_time.setText(sysTimeStr);
                    break;
                default:
                    break;
            }
        }
    };

    //初始化数据
    private void initData() {
        list.clear();
        UserInfoEntity userInfo = SecurityUtils.getUserInfo();
        Map<String, String> map = new HashMap<>();
        map.put("currentPage", String.valueOf(currentPage));
        map.put("pageSize", String.valueOf(pageSize));
        map.put("userInfo", userInfo.getPhoneNumber());
        map.put("school", userInfo.getSchool());
        if (!StringUtil.isEmpty(userInfo.getStudentId())) {
            map.put("studentId", userInfo.getStudentId());
            getData(map); //获取数据
        } else {
            gprs_empty.setVisibility(View.VISIBLE);
        }
    }

    //获取当天数据
    private void getData(Map<String, String> map) {
        showLoading();
        Network.request(Network.createApi(IGprsApi.class).list(map), new Network.IResponseListener<List<Map<String, Object>>>() {
            @Override
            public void onSuccess(List<Map<String, Object>> data) {
                if (data.size() != 0) {
                    gprs_empty.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    handleData(data); //处理数据
                    multiTypeAdapter.notifyDataSetChanged();
                } else {
                    gprs_empty.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);

                }
                hideLoading();
            }

            @Override
            public void onFail(int code, String message) {
                MToast.showShortToast("初始化数据失败");
                hideLoading();
            }
        });
    }


    //处理数据
    private void handleData(List<Map<String, Object>> data) {
        for (Map<String, Object> temp : data) {
            GprsModel gprsModel = new GprsModel();
            gprsModel.setGprsAddress(String.valueOf(temp.get("address")));
            gprsModel.setGprsTime(String.valueOf(temp.get("stuSignTime")));
            list.add(gprsModel);
        }
    }

    @Override
    public int getToolbar() {
        return R.id.gprs_toolbar;
    }

    @OnClick({R.id.qiandao_location, R.id.gprs_location_title, R.id.gprs_location_date_time})
    protected void onClick(View view) {
        UserInfoEntity userInfo = SecurityUtils.getUserInfo();
        GPSUtils gpsUtils = new GPSUtils(this);
        if (GPSUtils.getLocation() != null) {
            if (!StringUtil.isEmpty(userInfo.getStudentId())) {
                Map<String, String> map = gpsUtils.getGprsMessage();
                map.put("stuSignTime", currDate);
                sendGprsMessage(map); //点击签到发送信息
            } else {
                MToast.showLongToast("未完善学籍信息");
            }

        } else {
            MToast.showLongToast("获取Location失败");
        }

    }

    //发送定位信息
    private void sendGprsMessage(Map<String, String> map) {
        showLoading();
        Network.request(Network.createApi(IGprsApi.class).sendGprsMessage(map), new Network.IResponseListener<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> data) {
                if (data != null) {
                    gprs_empty.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    GprsModel gprsModel = new GprsModel();
                    gprsModel.setGprsAddress(String.valueOf(data.get("address")));
                    gprsModel.setGprsTime(String.valueOf(data.get("stuSignTime")));
                    list.add(0, gprsModel);
                    multiTypeAdapter.notifyDataSetChanged();
                } else {
                }
                hideLoading();
            }

            @Override
            public void onFail(int code, String message) {
                MToast.showShortToast("发送信息失败");
                hideLoading();
            }
        });
    }


}
