package cn.gmuni.sc.module.schoolbus;

import android.os.Bundle;
import android.widget.TextView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseCommonActivity;
import cn.gmuni.sc.model.SchoolBusModel;
import cn.gmuni.sc.network.Network;
import cn.gmuni.sc.network.api.ISchoolBusApi;
import cn.gmuni.sc.utils.MToast;
import retrofit2.http.POST;

public class SchoolBusDetailActivity extends BaseCommonActivity {

    @BindView(R.id.school_bus_detail_address)
    TextView school_bus_detail_address;
    @BindView(R.id.school_bus_detail_start)
    TextView school_bus_detail_start;
    @BindView(R.id.school_bus_detail_end)
    TextView school_bus_detail_end;
    @BindView(R.id.school_bus_detail_time)
    TextView school_bus_detail_time;

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_school_bus_detail);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        SchoolBusModel schoolBusModel = (SchoolBusModel) getIntent().getSerializableExtra("schoolBusModel");
        String weeksState = getIntent().getStringExtra("weeksState"); //周内与周末 0：周内 1：周末
        String startCampus = schoolBusModel.getStartCampus();
        String endCampus = schoolBusModel.getEndCampus();
        String season = String.valueOf(schoolBusModel.getSeasonState()); //季节类型 0 ：通用 1：夏季 2：冬季
        school_bus_detail_start.setText(startCampus);
        school_bus_detail_end.setText(endCampus);
        //根据季节类型、周内与周末类型、始发、终到地获取校车时刻表
        //TODO 待完善
        Map<String, String> params = new HashMap<>();
        params.put("season", season);
        params.put("week", weeksState);
        params.put("startCampus", startCampus);
        params.put("endCampus", endCampus);
        getSchoolBusTimeData(params); //获取时刻表
    }

    //获取时刻表
    private void getSchoolBusTimeData(Map<String, String> map) {
        showLoading();
        Network.request(Network.createApi(ISchoolBusApi.class).getSchoolBusTimeList(map), new Network.IResponseListener<Map<String, String>>() {
            @Override
            public void onSuccess(Map<String, String> data) {
                if (data.size() != 0) {
                    String address = data.get("address");
                    String timeList = data.get("timeList");
                    school_bus_detail_address.setText(address);
                    school_bus_detail_time.setText(timeList);
                }
                hideLoading();
            }

            @Override
            public void onFail(int code, String message) {
                MToast.showShortToast("获取时刻表失败");
                hideLoading();
            }
        });
    }

    @Override
    public int getToolbar() {
        return R.id.school_bus_detail_toolbar;
    }
}
